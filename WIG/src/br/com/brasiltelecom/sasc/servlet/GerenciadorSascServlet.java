package br.com.brasiltelecom.sasc.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.sasc.action.GerenciadorSasc;

/**
 * Classe responsavel por gerenciar o funcionamento do SASC, 
 * tendo a propriedade de iniciar ou terminar sua execucao.
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  18/10/2006
 * 
 */
public class GerenciadorSascServlet extends HttpServlet
{
	private Context initContext = null;
	ServletContext  sctx 		=  null;
	private Logger  logger 		= Logger.getLogger(this.getClass());
	private String  servidorWSM;
	private String  portaServidorWSM;
	private String  usuarioWSM;
	private String  senhaWSM;
	private int		numConsumidores;
	private int		qtdeMaxTentativas;
	
	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext 	  = new InitialContext();
			servidorWSM 	  = "10.44.250.62";	//(String)arg0.getServletContext().getAttribute("servidorWSM");
			portaServidorWSM  = "4040"; 		//(String)arg0.getServletContext().getAttribute("portaServidorWSM");
			usuarioWSM		  = "galvagni";		//(String)arg0.getServletContext().getAttribute("usuarioWSM");
			senhaWSM		  = "Jonnys81";		//(String)arg0.getServletContext().getAttribute("senhaWSM");
			numConsumidores   = 1;				// Integer.parseInt((String)arg0.getServletContext().getAttribute("numConsumidores"));
			qtdeMaxTentativas = 3;				// Integer.parseInt((String)arg0.getServletContext().getAttribute("qtdeMaxTentativas"));
		}
		catch (NamingException e)
		{
			logger.error("Erro: ", e);
		}
		sctx 			 = arg0.getServletContext();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		
		// Verifica a acao a ser tomada atraves do request
		String acao = (request.getParameter("acao") == null ? null : request.getParameter("acao") );
		
		try
		{
			// Cria uma unica instancia do SINGLETON GerenciadorSasc
			GerenciadorSasc gerSasc = GerenciadorSasc.getInstance();
			
			// Se a acao for start
			if ( acao != null && acao.equalsIgnoreCase("start") )
			{
				// Verifica se o SASC ja esta em execucao
				if (!gerSasc.estaExecutando())
				{
					// Caso o SASC nao esteja em execucao, um DataSource sera criado
					// para envio ao GerenciadorSasc como parametro
					String dataSourceWIG = (String)sctx.getAttribute("DataSourceWIG");
					DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/"+dataSourceWIG);
					
					// Chamada ao metodo start do GerenciadorSasc
					gerSasc.start(servidorWSM, portaServidorWSM, usuarioWSM, senhaWSM, ds, numConsumidores, qtdeMaxTentativas);
					
					// Mostra ao solicitante que o SASC foi inicializado com sucesso
					out.println("SASC inicializado com sucesso!");
				}
				else
					out.println("SASC ja esta em execucao.");
			}
			
			if ( acao != null && acao.equalsIgnoreCase("stop") )
			{
				gerSasc.stop();
				out.println("SASC finalizado com sucesso.");
			}
		}
		catch(Exception e)
		{
			logger.error("Erro: ",e);
		}
		finally
		{
			out.flush();
			out.close();
		}
	}
}

