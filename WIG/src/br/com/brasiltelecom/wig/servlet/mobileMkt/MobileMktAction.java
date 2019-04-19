package br.com.brasiltelecom.wig.servlet.mobileMkt;

import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.dao.MobileMktDAO;
import br.com.brasiltelecom.wig.entity.MobileMktQuestionario;
import br.com.brasiltelecom.wig.entity.MobileMktResultado;
import br.com.brasiltelecom.wig.entity.MobileMktResposta;

public class MobileMktAction extends HttpServlet
{
	private static final long serialVersionUID = 8270186638908398770L;
	private String wigContainer;
	private Context initContext = null;
	
	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext = new InitialContext();
			wigContainer  = (String)arg0.getServletContext().getAttribute("VariavelWIGContainer");
		} catch (NamingException e)
		{
			e.printStackTrace();
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		Logger logger = Logger.getLogger(this.getClass());
		String msisdn = request.getParameter("MSISDN");
		try
		{
			// Busca no pool de recursos qual a conexao com o banco de dados deve ser utilizada
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/WPP_WIGC");
			
			// Determina os parametros da resposta baseado na informacao
			// vinda pelo http respondido pelo assinante na pesquisa de marketing
			int idPesquisa     = Integer.parseInt(request.getParameter("pe"));
			int idQuestionario = Integer.parseInt(request.getParameter("qt"));
			int idProxQuest    = request.getParameter("pq") != null ? Integer.parseInt(request.getParameter("pq")) : 0;
			
			MobileMktResultado resultado = new MobileMktResultado(idPesquisa,idQuestionario);
			resultado.setMsisdn(msisdn);
			resultado.setDataRespostaPesquisa(Calendar.getInstance().getTime());
			
			// Busca as respostas do questionario sendo que estas possuem o formato
			// p<idpergunta>=idresposta ex: p1=1 isto indica pergunta id 1 teve a 
			// resposta de id igual a 1.
			Enumeration lista = request.getParameterNames();
			while (lista.hasMoreElements())
			{
				String nomeParametro = (String)lista.nextElement();
				// Se o nome do parametro comeca com a letra "p" entao
				// indica os parametros das perguntas enviadas para 
				// este questionario. Outra verificacao eh o tamanho
				// em caracteres deste nome de parametro, este deve ser
				// ateh 4 caracteres para evitar outro parametro que
				// comece com a mesma string possa ser tratado como uma pergunta
				if (nomeParametro.startsWith("pg") && nomeParametro.length() <= 4)
				{
					// se a resposta for vazia ou nula entao nem registra o par
					// pergunta=resposta no objeto resultado
					if (request.getParameter(nomeParametro) != null && !"".equals(request.getParameter(nomeParametro)))
					{
						// O id da pergunta eh o numero que vier apos a string "pg".
						int idPergunta = Integer.parseInt(nomeParametro.substring(2));
						int idResposta = Integer.parseInt(request.getParameter(nomeParametro));
						resultado.addResposta(idPergunta,idResposta);
					}
				}
			}
			// Insere os registros no banco de dados da pesquisa de marketing
			MobileMktDAO.getInstance().insereResultado(resultado,ds);
			
			// Verifica agora se deve ser enviado um novo questionario para 
			// resposta do assinante. Isto eh verificado atraves do parametro
			// que indica o proximo questionario. O fluxo normal eh pela numeracao
			// ou seja, apos o 1 primeiro (id=1) vem o segundo (id=2) e assim por
			// diante. Porem eh possivel que uma resposta altere este fluxo entao
			// verifica-se tambem se a ultima pegunta respondida possui uma resposta
			// que envia para outro questionario, se sim entao utiliza este questionario
			// ao inves do proximo por default.
			MobileMktQuestionario proxQuestionario = null;
			if (idProxQuest != 0)
			{
				// Realiza a pesquisa do questionario atual que foi respondido
				// para identificar o valor da resposta da ultima pergunta.
				MobileMktQuestionario questionario = MobileMktDAO.getInstance().findQuestionarioByID(idPesquisa,idQuestionario,ds);
				int idUltimaPerg  = resultado.getIdUltimaPergunta();
				int idResposta    = resultado.getIdResposta(idUltimaPerg);
				MobileMktResposta resposta = questionario.getPerguntaById(idUltimaPerg).getRespostaById(idResposta);
				
				// Se a resposta possuir um questionario resposta entao deve ser redirecionado para este
				// questionario, senao realiza a pesquisa do proximo questionario.
				proxQuestionario = resposta.getQuestionarioResposta();
				if (proxQuestionario == null)
					proxQuestionario = MobileMktDAO.getInstance().findQuestionarioByID(idPesquisa,idProxQuest,ds);
			}
			// Redireciona a montagem do WML para um JSP
			// indicando o objeto questionario no objeto Request
			// da conexao HTTP
			request.setAttribute("questionarioMkt",proxQuestionario);
			request.setAttribute("wigContainer"   ,wigContainer);
			request.getRequestDispatcher("ShowQuestionarioMkt.jsp").forward(request,response);
		}
		catch(Exception e)
		{
			logger.error("Erro ao coletar informacoes de pesquisa de marketing do assinante:"+msisdn,e);
		}
	}
}