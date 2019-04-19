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
import com.brt.gpp.comum.gppExceptions.*;

public class RemoveConexoesServlet extends HttpServlet
{
	private gerenteGPP pPOA;

	
	/* 
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException 
	{

		// Solicitando o ORB
		org.omg.CORBA.ORB orb = GerenteORB.getOrb();
		byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();

		pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		String msg;
		String nomePool = null;
		short tipoPool;
		try
		{
			tipoPool = Short.parseShort( (String)req.getParameter("tipoPool") );
			nomePool = (String)req.getParameter("nomePool");

			if ( pPOA.removeConexao( tipoPool ) )
				msg = "Conexão no pool de conexões " + nomePool + ", foi removida com sucesso.";
			else
				msg = "Não foi possível remover conexão no pool de conexões " + nomePool + ".";
		}
		catch(GPPInternalErrorException ie)
		{
			msg = "Não foi possível remover conexão no pool de conexões " + nomePool + ". \n Erro: " + ie.getMessage();
		}
		res.sendRedirect("msgConexao.jsp?msg="+msg);
	}
	
	/* 
	 * 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException 
	{
	}
}
