package br.com.brasiltelecom.wig.servlet.optIn;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Map;

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

import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.dao.OptInDAO;
import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;
import br.com.brasiltelecom.wig.filtrosResposta.FiltroERB;
import br.com.brasiltelecom.wig.util.Definicoes;

public class AtivarOptIn extends HttpServlet
{
	private String wigContainer = null;
	private String wigControl = null;
	private Context initContext = null;
	private Logger logger = Logger.getLogger(this.getClass());
	private String dataSource = "WPP_WIGC";
	
	public void init(ServletConfig arg0)
	{
		try
		{
			initContext		= new InitialContext();
			wigContainer	= (String)arg0.getServletContext().getAttribute("VariavelWIGContainer");
			wigControl    	= (String)arg0.getServletContext().getAttribute("VariavelWIGControl");
			dataSource		= (String)arg0.getServletContext().getAttribute("DataSourceWIG");
		}
		catch (NamingException e)
		{
			logger.error(e);
		}
	}
	 
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		Connection con  = null;
		
		try
		{
			// Recebe os seguintes parametros
			// 1 - msisdn
			// 2 - co_conteudo
			// 3 - loc
			// 4 - mandatorio
			// 5 - fidelizacao
			String msisdn 	   = request.getParameter("MSISDN");
			int coConteudo	   = (request.getParameter("btc") == null ? 0 : Integer.parseInt(request.getParameter("btc")) );
			String loc 		   = request.getParameter("loc");
			boolean mandatorio = (request.getParameter("mt") == null ? false : true);
			int fidelizacao    = (request.getParameter("fid") != null ? Integer.parseInt(request.getParameter("fid")) : 0);
			Calendar cal = Calendar.getInstance();
			
			logger.debug("Parametros recebidos com sucesso para o assinante " + msisdn + " (loc=" + loc +";mandatorio=" + mandatorio + ";fidelizacao=" + fidelizacao + ")");
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/"+dataSource);
			con = ds.getConnection();
			
			// Cria um objeto MensagemRetorno para envio ao showMensagemOptIn.jsp
			// e o objeto mensagemSMS para envio do SMS de confirmacao
			MensagemRetorno retorno = null;
			OptInDAO optInDAO = new OptInDAO();
			logger.debug("Solicitando o OptIn ativo.");
			
			// Solicita o OptIn ativo, caso haja
			OptIn optIn = optInDAO.getOptIn(msisdn, con);
			
			// Caso o objeto optIn seja nulo, significa que nao ha OptIn ativo
			// e o novo OptIn pode ser inserido
			if (optIn == null)
			{
				logger.debug("Nao existe OptIn ativo para o assinante " + msisdn);
				
				// Cria um novo objeto OptIn, pois o anterior era nulo
				optIn = new OptIn();
				
				if (fidelizacao > 0)
				{
					// Adiciona a data de atual os dias de fidelizacao do OptIn
					cal.add(Calendar.DAY_OF_MONTH, fidelizacao);
					optIn.setDataFidelizacao(cal.getTime());
				}
				// Seta o coModelo do objeto optIn de acordo com o msisdn
				optIn.setCoModelo(optInDAO.getCoModelo(msisdn, con));
				// Seta o restante dos atributos do objeto optIn
				optIn.setMsisdn(msisdn);
				optIn.setLac(0);
				optIn.setCellId(0);
				optIn.setLac(FiltroERB.getLac(loc));
				optIn.setCellId(FiltroERB.getCellId(loc));
				optIn.setMandatorio(mandatorio);
				optIn.setCoConteudo(coConteudo);
				
				logger.debug("Enviando requisicao para inserir os dados do assinante no OptIn / Historico...");
				// Insere o assinante no OptIn / OptInHistorico
				optInDAO.inserirOptIn(optIn, con);
				// Associa ao assinante todas as categorias
				optInDAO.insereTodasCategoriasAssinante(optIn.getMsisdn(), con);
				
				retorno = new MensagemRetorno();
				// Construcao do SMS a ser enviado junto com a requisicao
				retorno.setTagSMS(msisdn, Definicoes.WIG_SMS_OPTIN_SUCESSO);
			}
			else
			{
				logger.debug("Optin ja existente para o assinante " + msisdn + ". \n" +
							 "Verificando o tipo de OptIn ativo para o assinante...");
				retorno = optIn.getValidadorOptIn().validarOptIn(optIn, fidelizacao);
				
				logger.debug("Resultado da validacao do OptIn:\nCodigo: " + retorno.getCodRetorno() + "\nMensagem: " + retorno.getMensagem());
				// Caso o resultado da validacao do OptIn seja positivo,  
				// o optIn podera ser atualizado
				if (!retorno.houveErro())
				{
					if (fidelizacao > 0)
					{
						// Adiciona a data de atual os dias de fidelizacao do OptIn
						cal.add(Calendar.DAY_OF_MONTH, fidelizacao);
						optIn.setDataFidelizacao(cal.getTime());
					}
					
					// Seta o coModelo do objeto optIn de acordo com o msisdn
					optIn.setCoModelo(optInDAO.getCoModelo(msisdn, con));
					// Seta o restante dos atributos do objeto optIn
					optIn.setMsisdn(msisdn);
					optIn.setLac(0);
					optIn.setCellId(0);
					if (loc != null && loc.length() == 7)
					{
						optIn.setLac(FiltroERB.getLac(loc));
						optIn.setCellId(FiltroERB.getCellId(loc));
					}
					optIn.setMandatorio(mandatorio);
					optIn.setCoConteudo(coConteudo);
					// Caso a data de opt-out estiver preenchida entao marca
					// para nulo afim de indicar o novo optin
					optIn.setDataOptOut(null);
					
					logger.debug("Enviando requisicao para atualizar os dados do assinante no OptIn / Historico...");
					
					// Atualiza o assinante no OptIn / OptInHistorico
					optInDAO.atualizarOptIn(optIn, con);
					
					// Construcao do SMS a ser enviado junto com a requisicao
					retorno.setTagSMS(msisdn, Definicoes.WIG_SMS_OPTIN_SUCESSO);
				}
			}
			
			// Seta o atributo mensagemRetorno para mostrar a mensagem ao assinante
			request.setAttribute("mensagemRetorno", retorno);
			logger.debug("Redirecionando a requisicao para mostrar a mensagem ao assinante " + msisdn);
			// Redireciona a requisicao para o ShowMensagemOptIn
			request.getRequestDispatcher("/ShowMensagemOptIn.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro na ativacao do OptIn.", e);
			// Mostra uma mensagem de erro ao cliente			
			out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Tente novamente em alguns instantes."));
		}
		finally
		{
			out.flush();
			out.close();
			
			try
			{
				if (con != null)
					con.close();
			}
			catch(Exception e)
			{
				logger.error(e);
			}
		}
	}
}
