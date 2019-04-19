package br.com.brasiltelecom.ppp.portal.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.model.Assinante;

/**
 * 
 * @author Joao Paulo Galvagni
 * Data..: 08/03/2006
 * 
 * Classe que valida se o cliente possui saldo para efetuar 
 * alguma transacao com a devida necessidade
 * 
 */
public class ValidaPrePago extends HttpServlet 
{
	public void init(ServletConfig scfg) throws ServletException 
	{
		servletContext = scfg.getServletContext();		
	}
	
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private ServletContext servletContext = null;

	protected void service(HttpServletRequest request, HttpServletResponse response)
	{
		try 
		{
			// Recepcao do parametro URL, tendo sido utilizado o URLEncoder.encode para manter formatacao
			//String url2 = request.getParameter("url");
			//String teste = URLEncoder.encode("http://10.61.176.115:7778/wig/nwigcontrol?msisdn="+request.getParameter("MSISDN")+"&mes="+"03","UTF-8");

			// Recepcao dos parametros, de forma a manter a URL enviada intacta
			String query = request.getQueryString().replaceAll("\\?", "&");
			Pattern p = Pattern.compile("(\\w*)=([\\w|\\/|:|\\.]*)&?");
			Matcher m = p.matcher(query);

			String parametros = "";
			String url	= "";
			String msisdn 	= request.getParameter("MSISDN");

			while(m.find())
			{
				if (m.group(1).equalsIgnoreCase("url"))
					url = m.group(2);
				else
					parametros += m.group(1)+"="+m.group(2)+"&";
			}
			url += "?" + parametros;
			
			// Realiza a busca nas configuracoes do servidor de aplicacao para identificar
			// os valores a serem utilizados para a conexao com o sistema GPP
			String servidor  = (String) servletContext.getAttribute(Constantes.GPP_NOME_SERVIDOR);
			String porta     = (String) servletContext.getAttribute(Constantes.GPP_PORTA_SERVIDOR);

			// Realiza a consulta dos dados do cliente
			Assinante assinante = ConsultaStatusAssinanteGPP.getAssinante(msisdn,servidor,porta);

			// Encaminha a requisicao para a URL especificada no parametro
			encaminhaRequisicao(request,response,assinante,url);
		}		
		catch (Exception e)
		{
			logger.error("Erro na consulta do saldo Pula Pula",e);
			if (request.getAttribute(Constantes.MENSAGEM)!=null)
				mostraErro(request,response,(String)request.getAttribute(Constantes.MENSAGEM));
			else
				mostraErro(request,response,"Erro ao validar o cliente. "+e.getMessage());
		}	
	}
	
	/**
	 * Metodo   : encaminhaRequisicao
	 * Descricao: Recebe um objeto assinante ja contendo todas as informacoes e 
	 * 			  encaminha a requisicao para a URL especificada, caso satisfaca
	 * 			  as seguinte condicao: 
	 * 				1) Eh cliente pos-pago
	 * 		      ou as condicoes abaixo:
	 * 				3) Possui saldo superior a R$ 0,50
	 * 				2) Possui status Normal 				
	 * 
	 * @param request
	 * @param response
	 * @param assinante
	 * @param url
	 */
	private void encaminhaRequisicao(HttpServletRequest request, HttpServletResponse response, Assinante assinante, String url)
	{
		try 
		{
			if (assinante.getRetorno().equals("0002") || (assinante.getStatusAssinante().equals("2") && assinante.getSaldoPrincipalDouble() > 0.50) )
				response.sendRedirect(url);
			else
			{
				ServletOutputStream out = response.getOutputStream();
				out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" +
						"<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\"" +
						"\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">" +
						"<wml>" +
						"<card>" +
						"    <p>" +
						"    Saldo insuficiente. Clique OK para sair." +
						"    </p>" +
						"</card>" +
						"</wml>");
				out.flush();
			}	
		} catch (IOException e) {
			logger.error("Erro na escrita do resultado", e);
			new Exception("Erro de Comunicação");
		}
	}
	
	/**
	 * Metodo   : mostraErro
	 * Descricao: Mostra uma mensagem amigavel ao cliente, contendo o erro
	 * 			  ocorrido na tentativa da validacao
	 * 
	 * @param request
	 * @param response
	 * @param erro
	 */
	private void mostraErro(HttpServletRequest request, HttpServletResponse response, String erro)
	{
		try
		{
			ServletOutputStream out = response.getOutputStream();
			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" +
					"<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\"" +
					"\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">" +
					"<wml>" +
					"<card>" +
					"    <p>" +
					"    Erro Interno. " +
					"    </p>" +
					"</card>" +
					"</wml>");
		}
		catch (IOException e)
		{
			logger.error("Erro na escrita do resultado", e);
			new Exception("Erro de Comunicação");
		}
	}
}
