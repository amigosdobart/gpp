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

public class GPPLogParseArquivoServlet extends HttpServlet{

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException 
	{
		HttpSession session = req.getSession(true);

		req.setAttribute("fileName",(String)req.getParameter("nomeArquivo"));
		req.setAttribute("firstTime","YES");
		
		req.getRequestDispatcher("pesquisaLog.jsp").forward(req,res);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
	}
}
