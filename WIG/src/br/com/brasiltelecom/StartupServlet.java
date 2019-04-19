package br.com.brasiltelecom;

import java.util.Enumeration;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

public class StartupServlet extends HttpServlet
{
	private ServletContext ctx = null;
	private static final long serialVersionUID = 7526471155622776147L;
	
	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			ctx  = arg0.getServletContext();
			PropertyConfigurator.configureAndWatch(ctx.getRealPath("/WEB-INF/log4j.properties"));
			Enumeration listaParam = arg0.getInitParameterNames();
			while (listaParam.hasMoreElements())
			{
				String name = (String)listaParam.nextElement();
				ctx.setAttribute(name,arg0.getInitParameter(name));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
	}
}