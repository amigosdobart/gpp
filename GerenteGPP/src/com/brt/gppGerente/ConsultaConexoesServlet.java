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
import com.brt.gpp.comum.*;
import com.brt.gpp.componentes.gerenteGPP.orb.*;
import com.brt.gpp.comum.gppExceptions.*;
import org.omg.CORBA.ORB;

public class ConsultaConexoesServlet extends HttpServlet
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
		ORB orb = GerenteORB.getOrb();
		byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();

		pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		Map poolConexoes    = new HashMap();
		Map poolTipos       = new HashMap();
		Map poolDisponiveis = new HashMap();

		try
		{		
			poolTipos.put(Definicoes.CO_TECNOMEN_APR,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_APR));
			poolTipos.put(Definicoes.CO_TECNOMEN_REC,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_REC));
			poolTipos.put(Definicoes.CO_TECNOMEN_VOU,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_VOU));
			poolTipos.put(Definicoes.CO_TECNOMEN_ADM,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_ADM));
			poolTipos.put(Definicoes.CO_TECNOMEN_AGE,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_AGE));
			poolTipos.put(Definicoes.CO_BANCO_DADOS_PREP, new Short((short)Definicoes.CO_TIPO_BANCO_DADOS_PREP));
	
			poolConexoes.put(Definicoes.CO_TECNOMEN_APR,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_APR)));
			poolConexoes.put(Definicoes.CO_TECNOMEN_REC,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_REC)));
			poolConexoes.put(Definicoes.CO_TECNOMEN_VOU,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_VOU)));
			poolConexoes.put(Definicoes.CO_TECNOMEN_ADM,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_ADM)));
			poolConexoes.put(Definicoes.CO_TECNOMEN_AGE,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_AGE)));
			poolConexoes.put(Definicoes.CO_BANCO_DADOS_PREP, new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_BANCO_DADOS_PREP)));

			poolDisponiveis.put(Definicoes.CO_TECNOMEN_APR,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_APR)));
			poolDisponiveis.put(Definicoes.CO_TECNOMEN_REC,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_REC)));
			poolDisponiveis.put(Definicoes.CO_TECNOMEN_VOU,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_VOU)));
			poolDisponiveis.put(Definicoes.CO_TECNOMEN_ADM,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_ADM)));
			poolDisponiveis.put(Definicoes.CO_TECNOMEN_AGE,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_AGE)));
			poolDisponiveis.put(Definicoes.CO_BANCO_DADOS_PREP, new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_BANCO_DADOS_PREP)));

		}
		catch(GPPInternalErrorException ie)
		{
			throw new ServletException(ie.getMessage());		
		}

		HttpSession session = req.getSession(true);
		session.setAttribute("poolConexoes",poolConexoes);
		session.setAttribute("poolTipos"   ,poolTipos);
		session.setAttribute("poolDisponiveis",poolDisponiveis);

		req.getRequestDispatcher("gerentePoolConexoes.jsp").forward(req,res);
	}
}
