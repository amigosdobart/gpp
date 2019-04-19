/*
 * Created on 19/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package com.brt.gppGerente;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.brt.clientes.interfacegpp.GerenteORB;
import com.brt.gpp.componentes.gerenteGPP.orb.*;

public class AtivaServicosServlet extends HttpServlet
{
	private gerenteGPP pPOA;

	
	/* 
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException 
	{
		doPost(req,res);
	}
	
	/* 
	 * 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException 
	{
		// Solicitando o ORB
		org.omg.CORBA.ORB orb = GerenteORB.getOrb();
		
		byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();

		pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		String msg="";
		String nomeServico = (String)req.getParameter("nomeServico");
		boolean ativou=false;

		if (nomeServico.equals(ConsultaServicosServlet.STR_PRODUTOR_SMS))
			ativou = pPOA.processaSMS(true);

		if (nomeServico.equals(ConsultaServicosServlet.STR_DEBUG_LOG))
			ativou = pPOA.escreveDebug(true);
				
		if (ativou)
			req.getRequestDispatcher("ConsultaServicosServlet").forward(req,res);
			//msg = "Servico " + nomeServico + " foi ativado com sucesso.";
		else
		{
			msg = "Nao foi possivel ativar servico: " + nomeServico;
			req.getRequestDispatcher("msgServico.jsp?msg="+msg).forward(req,res);
		}
	}
}
