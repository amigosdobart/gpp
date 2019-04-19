package br.com.brasiltelecom.wig.servlet.optIn;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;
import br.com.brasiltelecom.wig.filtrosResposta.FiltroERB;
import br.com.brasiltelecom.wig.dao.OptInDAO;
import br.com.brasiltelecom.wig.util.Definicoes;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  12/05/2006
 */
public class DesativarOptIn extends HttpServlet
{
	private Context initContext = null;
	private String wigContainer = null;
	private String wigControl = null;
	private String dataSource = "WPP_WIGC";
	private Logger logger = Logger.getLogger(this.getClass());
	
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
			// Recebe os seguintes parametros:
			// 1 - msisdn
			// 2 - loc
			// 3 - btc (Codigo de conteudo)
			String msisdn	 = request.getParameter("MSISDN");
			String loc	 	 = request.getParameter("loc");
			String btc   	 = request.getParameter("btc");
			String operador	 = (request.getParameter("op") != null ? request.getParameter("op") : msisdn);
			
			logger.debug("Parametros recebidos com sucesso para o assinante " + msisdn + " (loc=" + loc + "coConteudo=" + 
																				btc + "operador=" + operador + ")");
			
			// Nova instancia do OptInDAO
			OptInDAO optInDAO = new OptInDAO();
			
			// Cria uma conexao com o Banco de Dados do WIG
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/"+ dataSource);
			con = ds.getConnection();
			
			// Envia uma requisicao ao DAO para consulta de OptIn ja ativo
			MensagemRetorno retorno = null;
			OptIn optIn = optInDAO.getOptOut(msisdn, con);
			
			if (optIn == null || optIn.getDataOptOut() != null)
			{
				String novaTentativa = "<do type=\"accept\">\n<go href=\""+wigControl+"?bts=2&amp;btc="+btc+"\" />\n</do>";
				logger.debug("Nao existe OptIn ativo para o assinante " + msisdn);
				retorno = new MensagemRetorno();
				retorno.setCodRetornoNum(Definicoes.WIG_RET_CADASTRADO);
				retorno.setMensagem(Definicoes.WIG_MSG_OPTIN_NAO_CADASTRADO + novaTentativa);
			}
			else
			{
				// Grava no LOG uma entrada DEBUG
				logger.debug("OptIn ja existente para o assinante " + msisdn + "\n" +
							 "Verificando se o OptIn ativo pode ser desativado...");
				
				retorno = new MensagemRetorno();
				retorno = optIn.getValidadorOptOut().validarOptOut(optIn);
				
				// Grava no LOG uma entrada DEBUG
				logger.debug("Resultado da validacao do OptIn:\nMSISDN: "+ msisdn +" Codigo: " + retorno.getCodRetorno() + "\nMensagem: " + retorno.getMensagem());
				
				if (!retorno.houveErro())
				{
					// Deleta todas categorias associadas ao assinante
					optInDAO.deletaTodasCategoriasAssinante(msisdn, con);
					
					Calendar cal = Calendar.getInstance();
					// Grava no LOG uma entrada DEBUG
					// Seta os atributos do objeto OptIn para desativacao
					optIn.setCoModelo(optInDAO.getCoModelo(msisdn, con));
					optIn.setMsisdn(msisdn);
					optIn.setLac(0);
					optIn.setCellId(0);
					optIn.setLac(FiltroERB.getLac(loc));
					optIn.setCellId(FiltroERB.getCellId(loc));
					optIn.setDataOptOut(cal.getTime());
					optIn.setDataFidelizacao(null);
					optIn.setMandatorio(false);
					optIn.setCoConteudo(btc != null ? Integer.parseInt(btc) : 0);
					optIn.setOperador(operador);
					
					// Grava no LOG uma entrada DEBUG
					logger.debug("Enviando requisicao para atualizar os dados do assinante no OptOut / Historico...");
					// Atualiza o OptIn / OptInHistorico para 'desativar' o OptIn do assinante (OptOut)
					optInDAO.atualizarOptOut(optIn, con);
				}
			}
			
			// Seta o atributo mensagemRetorno para mostrar a mensagem ao assinante
			request.setAttribute("mensagemRetorno", retorno);
			
			logger.debug("Redirecionando a requisicao para mostrar a mensagem de retorno ao assinante " + msisdn);
			// Redireciona a requisicao para o ShowMensagemOptIn
			request.getRequestDispatcher("/ShowMensagemOptIn.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error(e);
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
 
