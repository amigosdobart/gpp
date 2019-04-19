/*
 * Created on 19/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package com.brt.gppGerente;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.brt.clientes.interfacegpp.GerenteORB;
import com.brt.gpp.componentes.gerenteGPP.orb.*;

public class ConsultaHistProcBatchServlet extends HttpServlet
{
	private gerenteGPP pPOA;
	private static final String CHAR_SEPARADOR_CAMPOS="#";
	
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
		// Socilicitando o ORB
		org.omg.CORBA.ORB orb = GerenteORB.getOrb();
		byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();

		pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		int idProcBatch   = Integer.parseInt((String)req.getParameter("nomeProcesso"));
		String idtStatus  = (String)req.getParameter("statusProcesso");
		String datInicial = (String)req.getParameter("dataInicial");
		String datFinal   = (String)req.getParameter("dataFinal");
		
		System.out.println("idtStatus " + idtStatus);
		System.out.println("datInicial " + datInicial);
		System.out.println("datFinal " + datFinal);

		Collection historicoProcBatch = new Vector();
		try 
		{
			String list = pPOA.getHistProcessosBatch((short)idProcBatch,datInicial,datFinal,idtStatus);
			int posFimLinha=0;
			int posInicial=0;
			while ((posFimLinha=list.indexOf("\n",posFimLinha)) > -1)
			{
				String linha = list.substring(posInicial,posFimLinha);
				String colunas[] = linha.split(ConsultaHistProcBatchServlet.CHAR_SEPARADOR_CAMPOS);
				historicoProcBatch.add(colunas);
				posInicial = posFimLinha+1;
				posFimLinha++;
			}
		}
		catch(Exception e)
		{
			throw new ServletException(e.getMessage());
		}
		req.getSession(true).setAttribute("HistProcessosBatch",historicoProcBatch);
		req.getRequestDispatcher("resultadoPesqHistProcBatch.jsp").forward(req,res);
	}
}
