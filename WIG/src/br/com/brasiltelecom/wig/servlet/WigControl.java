package br.com.brasiltelecom.wig.servlet;

import br.com.brasiltelecom.wig.entity.Servico;
import br.com.brasiltelecom.wig.entity.Conteudo;
import br.com.brasiltelecom.wig.dao.ServicoDAO;
import br.com.brasiltelecom.wig.dao.ConteudoDAO;
import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.action.Autenticador;

import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

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

/**
 * @author Joao Carlos
 * Data..: 02/06/2005
 *
 */
public class WigControl extends HttpServlet
{
	private Context initContext 	= null;
	private int		maxLengthWML	= 2000;
	private boolean	deveAutenticar	= false;
	private String	wigContainer 	= null;
	private static final long serialVersionUID = 7526471155622776147L;
	private Logger logger = Logger.getLogger(this.getClass());

	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext = new InitialContext();
			// Define as variaveis iniciais a serem utilizadas pelo WigControl
			maxLengthWML  = Integer.parseInt ((String)arg0.getServletContext().getAttribute("TamanhoMaximoWML"));
			deveAutenticar= Boolean.valueOf  ((String)arg0.getServletContext().getAttribute("DeveAutenticar")).booleanValue();
			wigContainer  = (String)arg0.getServletContext().getAttribute("VariavelWIGContainer");
			// Busca a instancia do autenticador e define o tempo maximo de logon possivel
			// atraves da propriedade definido no web.xml
			long tempoExpiracaoLogon = Long.parseLong((String)arg0.getServletContext().getAttribute("TempoExpiracaoLogon"));
			Autenticador aut = Autenticador.getInstance();
			aut.setTempoMaximoExpiracao(tempoExpiracaoLogon);
		} catch (NamingException e)
		{
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		doGet(request,response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		Connection con = null;
		PrintWriter out = response.getWriter();
		try
		{
			String msisdn = request.getParameter("MSISDN");
			String iccid = request.getHeader("X-Wig-IccId");
			int codServico  = Integer.parseInt(request.getParameter("bts"));
			int codConteudo = Integer.parseInt(request.getParameter("btc"));
			
			logger.info("Assinante:"+msisdn+" bts="+codServico+" btc="+codConteudo);
			// Busca a instancia do autenticador e verifica se o parametro do sistema
			// indica a necessidade de autenticar, se verdadeiro entao verifica se o
			// assinante estah autenticado. Se nao estiver entao retorna um WML para
			// que o usuario se autentique
			Autenticador aut = Autenticador.getInstance();
			if (deveAutenticar && !aut.estaAutenticado(msisdn))
			{
				// Prepara a URL que serah utilizada como parametro para o autenticador redirecionar
				// apos realizar a autenticacao do assinante. Para preparar a URL eh verificado
				// se ha algum parametro na queryString se verdade entao repassa esses parametros
				// senao somente a URL inicial eh repassada
				String redirect = request.getRequestURL().toString();
				if (request.getQueryString().length() > 0)
					redirect += "?" + request.getQueryString();
				// Devolve para o assinante o WML para a autenticacao. No WML apos autenticado
				// este sera redirecionado para a URL que foi como parametro
				out.println(aut.getWMLAutenticacao(msisdn,wigContainer,redirect));
			}
			else
			{
				// Busca no pool de recursos qual a conexao com o banco de dados deve ser utilizada
				DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/WPP_WIGC");
				con = ds.getConnection();
				
				// Busca os objetos contendo os valores para conteudo e servico
				Servico  servico  = ServicoDAO.getInstance().findByCodigo(codServico,con);
				Conteudo conteudo = ConteudoDAO.getInstance().findByCodigo(codConteudo,servico,con);

				// Define os parametros a serem repassados tanto para os validadores quanto para os filtros
				Map parameters = new HashMap();
				Enumeration lista = request.getParameterNames();
				while (lista.hasMoreElements())
				{
					String nomeParametro = (String)lista.nextElement();
					parameters.put(nomeParametro,request.getParameter(nomeParametro));
				}
				// Complementando os valores de parametros eh inserido o ICCID, Conteudo e Servico
				parameters.put("Conteudo",conteudo);
				parameters.put("Servico" ,servico);
				parameters.put("ICCID"  ,iccid);
				
				// Define o Header da resposta http para identificar o billing code
				// do conteudo sendo acessado
				response.setHeader("X-WAP-Payment-Info: content-value-class",String.valueOf(conteudo.getBillingCode()));
				
				// Apesar do conteudo ser comum no processo o MSISDN eh passado como parametro
				// para que possa ser executado as URL's simulando um comportamento padrao da
				// plataforma WIG que sempre envia o MSISDN
				WIGWmlConstrutor construtorWML = new WIGWmlConstrutor(msisdn,iccid);
				out.print(construtorWML.getWml(servico,conteudo,con,maxLengthWML,null,parameters));
				// Registra o acesso do assinante ao conteudo se este exijir
				if (conteudo.registraAcesso())
					ConteudoDAO.getInstance().insereAcesso(conteudo,msisdn,iccid,con);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out.println(WIGWmlConstrutor.getWMLErro("Erro 1: Servico Temporariamente Indisponivel. Tente mais tarde em alguns instantes."));
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
				out.println(WIGWmlConstrutor.getWMLErro("Erro 1: Servico Temporariamente Indisponivel. Tente mais tarde em alguns instantes."));
			}
		}
		out.flush();
		out.close();
	}
}

