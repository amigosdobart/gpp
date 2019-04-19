package br.com.brasiltelcom.wigf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForaServico extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2306169549856326866L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=iso-8859-1");
		response.getOutputStream().println("<wml><card><p>Servico temporariamente indisponivel</p></card></wml>");
	}

}
