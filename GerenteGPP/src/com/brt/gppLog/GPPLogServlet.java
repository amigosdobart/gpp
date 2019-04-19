/*
 * Created on 15/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.brt.gppLog;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.text.*;
import java.util.*;

public class GPPLogServlet extends HttpServlet{


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */

	protected void doGet(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException 
	{
		doPost(req,res);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		Collection resConsulta = null; 

		try
		{
			HttpSession session = req.getSession(true);

			Map parametros = new HashMap();
			//parametros.put("logFile",		session.getAttribute("logFile"));
			parametros.put("idProcesso",	req.getParameter("idProcesso"));
			parametros.put("componente",	req.getParameter("componente"));
			parametros.put("nomeClasse",	req.getParameter("nomeClasse"));
			parametros.put("severidade",	req.getParameter("severidade"));
			parametros.put("dataInicial",	req.getParameter("dataInicial"));
			parametros.put("dataFinal",		req.getParameter("dataFinal"));
			parametros.put("mensagem",		req.getParameter("mensagem"));
			
			int 	window = 20;
			int 	page = 1;
			
			if(req.getParameter("pageSize")!=null)
				window 	= Integer.parseInt((String)req.getParameter("pageSize"));
				
			if (req.getParameter("page")!=null)
				page 	= Integer.parseInt((String)req.getParameter("page"));

			GPPLogCache logCache = null;//LogCache
			boolean firstTime = false;	//Se eh primeira vez.
			if ((req.getParameter("firstTime")!=null)&&req.getParameter("firstTime").equals("YES"))
			{
				logCache = GPPLogCache.buscaRegistros(parametros,new java.io.File(req.getParameter("fileName")),window,page);
				//logCache = GPPLogCache.getCache(req.getParameter("fileName"));
				logCache.setQueryHash(parametros.hashCode());
			}
			else
				logCache = (GPPLogCache)session.getAttribute("logFile");
			
			boolean subConsulta = logCache.getFile().equals(logCache.getParentFile());
			// Primeira Consulta?
			if(!logCache.sameSearch(parametros)) //Não é continuação da mesma pesquisa e nao eh primeira pesquisa.
				logCache	=  GPPLogCache.buscaRegistros(parametros,logCache.getParentFile(),window,page);
			else 								// É continuação da mesma pesquisa.
				logCache.nextPage(parametros, window, page);
			
			resConsulta	= logCache.getUltimaConsulta();
			
			session.removeAttribute("total");	
			session.setAttribute("logFile",logCache);	
			session.setAttribute("total",new Integer(logCache.getTotal()));
			session.setAttribute("listaLogs",resConsulta);

			req.setAttribute("page",		new Integer(req.getParameter("page")));
			req.setAttribute("pageSize",	new Integer(req.getParameter("pageSize")));
			req.setAttribute("idProcesso",	new String(req.getParameter("idProcesso")));
			req.setAttribute("componente",	new String(req.getParameter("componente")));
			req.setAttribute("nomeClasse",	new String(req.getParameter("nomeClasse")));
			req.setAttribute("severidade",	new String(req.getParameter("severidade")));
			req.setAttribute("dataInicial",	new String(req.getParameter("dataInicial")));
			req.setAttribute("dataFinal",	new String(req.getParameter("dataFinal")));
			req.setAttribute("mensagem",	new String(req.getParameter("mensagem")));

			req.getRequestDispatcher("resultadoPesquisaLog.jsp").forward(req,res);
		}
		catch(ParseException e)
		{
			throw new ServletException("Parametros inválidos na consulta. " + e.getMessage());
		}
		catch (NumberFormatException e1) 
		{
			throw new ServletException("Parametros inválidos na consulta. " + e1.getMessage());
		}
	}

}
