package br.com.brasiltelecom.wig.servlet;

import br.com.brasiltelecom.wig.action.GerenciadorWIGPush;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author Joao Carlos
 * Data..: 02/06/2005
 *
 */
public class WIGPushWMLAction extends HttpServlet
{
	private static final long serialVersionUID = 7526471155622776147L;
	private Logger logger = Logger.getLogger(this.getClass());
	private String otaServerName;
	private int otaPortNumber;

	public void init(ServletConfig arg0) throws ServletException
	{
		otaPortNumber = Integer.parseInt ((String)arg0.getServletContext().getAttribute("PortaServidorPush"));
		otaServerName = (String)arg0.getServletContext().getAttribute("ServidorPush");
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		
		String msisdn = request.getParameter("destino");
		String url    = request.getParameter("origem");
		boolean isWml = request.getParameter("wml") != null ? new Boolean(request.getParameter("wml")).booleanValue() : false;
		int tariff    = request.getParameter("tariff") != null ? Integer.parseInt(request.getParameter("tariff")) : 0;
		
		GerenciadorWIGPush wigPush = new GerenciadorWIGPush(otaServerName,otaPortNumber);
		boolean enviouPush = wigPush.enviarPost(msisdn, url, isWml, tariff);
		
		out.println( "Push " + (enviouPush ? "" : "nao ") + "enviado para o assinante:"+msisdn);
		out.flush();
		out.close();
		logger.debug("Push " + (enviouPush ? "" : "nao ") + "enviado para o assinante:"+msisdn+" URL:"+url);
	}
	

}

