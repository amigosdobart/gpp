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
import com.brt.gpp.comum.gppExceptions.*;

public class ConsultaServicosServlet extends HttpServlet
{
	private gerenteGPP pPOA;

	public static final String STR_PRODUTOR_SMS = "Processo de Envio de SMS";
	public static final String STR_DEBUG_LOG    = "Mensagens de Debug no LOG";
	public static final String STR_GPP          = "GPP - Gateway Pre-Pago";
	
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

		Map mapServicos = new HashMap();
		
		boolean isAtivo=false;
		// Busca se o processo de Envio de SMS esta ativo
		try{
			isAtivo = pPOA.getStatusProdutorSMS();
		}
		catch(GPPInternalErrorException ie){
			isAtivo = false;
		}
		mapServicos.put(STR_PRODUTOR_SMS,new Boolean(isAtivo));

		// Busca se o processo de escrita do Debug no Log esta ativo
		try{
			isAtivo = pPOA.getStatusEscreveDebug();
		}
		catch(GPPInternalErrorException ie){
			isAtivo = false;
		}
		mapServicos.put(STR_DEBUG_LOG,new Boolean(isAtivo));

		// Busca se o GPP esta Ativo
		String ping = pPOA.ping();
		if (ping != null && !ping.equals(""))
			isAtivo = true;
		else isAtivo = false;
		mapServicos.put(STR_GPP,new Boolean(isAtivo));
		System.out.println("GPP " + isAtivo);
		
		HttpSession session = req.getSession(true);
		session.setAttribute("poolServicos",mapServicos);
		req.getRequestDispatcher("gerenteServicos.jsp").forward(req,res);
	}
}
