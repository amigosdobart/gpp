package br.com.brasiltelecom.wig.servlet.brtVantagens;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.dao.BrtVantagensDAO;
import br.com.brasiltelecom.wig.entity.BrtVantagem;
import br.com.brasiltelecom.wig.util.ResolvedorNomeServidor;

/**
 * 
 * @author	JOÃO PAULO GALVAGNI
 * @since	12/04/2006
 */
public class BonusTodoMesConsulta extends HttpServlet 
{
	private Context initContext 	= null;
	private String  wigControl		= null;
	private int portaServidorWIG	= 0;
	private String ipEntireX		= null;
	private int portaEntireX		= 0;
	private String clarifyCadastro	= null;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public synchronized void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext		= new InitialContext();
			clarifyCadastro = (String)arg0.getServletContext().getAttribute("ClarifyCadastro");
			wigControl	  = (String)arg0.getServletContext().getAttribute("VariavelWIGControl");
			portaServidorWIG  = Integer.parseInt((String) arg0.getServletContext().getAttribute("PortaServidorWIG"));
			ipEntireX		= (String)arg0.getServletContext().getAttribute("ServidorEntireX");
			portaEntireX	= Integer.parseInt((String)arg0.getServletContext().getAttribute("PortaServidorEntireX"));
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error(e);
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{	
		String wmlNovaTentativa = "\t <do type=\"accept\">\n" +
		  						  "\t	<go href=\""+ wigControl +"?bts=2&amp;btc=7061\"/>\n" +
		  						  "\t </do>\n";
		
		String maquinaWIG = ResolvedorNomeServidor.getInstance().resolveNome(request.getServerName())+":"+portaServidorWIG;
		//String maquinaWIG = request.getServerName()+":"+portaServidorWIG;
		PrintWriter out = response.getWriter();
		HttpSession sessaoBTM = request.getSession(true);
		
		try
		{
			// Recebe o MSISDN como parametro
			String msisdn = request.getParameter("MSISDN");
			
			// Novo objeto BrtVantagem
			// Nova instancia do BrtVantagensDAO
			// Nova instancia do BrtVantagensWMLConstrutor
			BrtVantagensDAO consultaBTM = new BrtVantagensDAO();
			
			// Realizacao da consulta do cliente
			BrtVantagem brtVantagemBTM = consultaBTM.getBrtVantagensByMsisdn(msisdn, ipEntireX, portaEntireX);
			
			// Verifica se o objeto brtVantagem eh nulo
			if (brtVantagemBTM != null)
			{
				// Valida se o assinante eh valido
				if ( !brtVantagemBTM.isAtivo())
				{
					logger.debug("Assinante " + msisdn + " com pendencia no sistema Clarify.");
					// Assinante com pendencia no Sistema Clarify
					out.println(WIGWmlConstrutor.getWMLInfo("Telefone nao pode ser cadastrado. Favor entrar em contato com a Central de Relacionamento."));
				}
				else
				{
					sessaoBTM.setAttribute("brtVantagem",brtVantagemBTM);
					sessaoBTM.setAttribute("maquinaWIG", maquinaWIG);
					
					// Grava no LOG uma entrada DEBUG
					logger.debug("WML de retorno sendo enviado para o assinante " + msisdn);
					
					// Redireciona a requisicao para o ShowBonusTodoMesConsulta para visualizacao do cliente
					request.getRequestDispatcher("/ShowBonusTodoMes.jsp").forward(request,response);
				}
			}
			else
			{
				// Grava uma entrada DEBUG no log
				logger.debug("Falha na validacao do assinante " + msisdn + ". Erro do Clarify: " + brtVantagemBTM.getCodRetorno());
				
				// Mostra uma mensagem de erro ao cliente e a opcao de uma nova tentativa
				out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Clique OK e tente novamente.\n" + wmlNovaTentativa));
			}
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error(e);
			// Mostra uma mensagem de erro ao cliente e a opcao de uma nova tentativa
			out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Clique OK e tente novamente.\n" + wmlNovaTentativa));
		}
		finally
		{
			out.flush();
			out.close();
		}
	}
}
