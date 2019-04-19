package br.com.brasiltelcom.wigf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmBranco extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2306169549856326866L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=iso-8859-1");
		response.getOutputStream().println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> " + 
				"<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\" " + 
				"  \"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\"> " + 
				"<wml> " + 
				" <wigplugin name=\"noresponse\"/> " + 
				"  <card>  " + 
				"   <p> " + 
				"   </p> " + 
				"  </card> " + 
				"</wml>");
	}

}
