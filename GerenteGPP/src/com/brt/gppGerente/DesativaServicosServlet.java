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

public class DesativaServicosServlet extends HttpServlet
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
		boolean desativou=false;

		if (nomeServico.equals(ConsultaServicosServlet.STR_PRODUTOR_SMS))
			desativou = pPOA.processaSMS(false);

		if (nomeServico.equals(ConsultaServicosServlet.STR_DEBUG_LOG))
			desativou = pPOA.escreveDebug(false);
				
		if (nomeServico.equals(ConsultaServicosServlet.STR_GPP))
		{
			// A finalizacao do GPP implica em uma excecao CORBA.
			// Portanto ao encontrar uma excecao ao finalizar o GPP
			// e esta possuir uma mensagem especifica para identifica-la
			// o sistema simplesmente a desconsidera
			try
			{
				pPOA.finalizaGPP();
			}
			catch(Exception e)
			{
				// Entao caso a excecao seja devido a finalizacao normal do 
				// sistema passa-se o controle para a pagina inicial
				if (e.toString().indexOf("org.omg.CORBA.TRANSIENT") > -1)
				{
					// Forca o servidor de ORBs a realizar o ping em todos
					// os GPPs para mostrar que foi desativado
					GerenteORB.startThread();
					req.getRequestDispatcher("escolheArquivo.jsp").forward(req,res);
				}
				else
					desativou=false;
			}
		}
		
		if (desativou)
			req.getRequestDispatcher("ConsultaServicosServlet").forward(req,res);
			//msg = "Servico " + nomeServico + " foi desativado com sucesso.";
		else
		{
			msg = "Nao foi possivel desativar servico: " + nomeServico;
			req.getRequestDispatcher("msgServico.jsp?msg="+msg).forward(req,res);
		}
	}
}
