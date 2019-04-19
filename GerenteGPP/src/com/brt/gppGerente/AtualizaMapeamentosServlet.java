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

public class AtualizaMapeamentosServlet extends HttpServlet
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
		/*
		 *  o valor do parametro, tipoMapeamento é um valor inteiro correspondente
		 *  ao tipo de mapeamento desejado para ser exibido, ele corresponde aos 
		 *  seguintes valores:
		 *  1 - Plano de Preço
		 *  2 - Status Assinante
		 *  3 - Status Serviço
		 *  4 - Sistema Origem
		 *  5 - Tarifa Troca Senha
		 *  6 - Configurações GPP
		 */

		// Solicitando o ORB
		org.omg.CORBA.ORB orb = GerenteORB.getOrb();
		byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();

		pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		int tipoMapeamento = Integer.parseInt((String)req.getParameter("tipoMapeamento"));
		String list="";
		String nomeMapeamento="";
		boolean atualizou=false;
		try
		{
			switch(tipoMapeamento)
			{
				case 1:
				{
					if (pPOA.atualizaListaPlanoPreco())
						atualizou=true;
					nomeMapeamento = "Plano de Preço";
					break;
				}
				case 2:
				{
					if (pPOA.atualizaListaStatusAssinante())
						atualizou=true;
					nomeMapeamento = "Status Assinante";
					break;
				}
				case 3:
				{
					if (pPOA.atualizaListaStatusServico())
						atualizou=true;
					nomeMapeamento = "Status Serviço";
					break;
				}
				case 4:
				{
					if (pPOA.atualizaListaSistemaOrigem())
						atualizou=true;
					nomeMapeamento = "Sistema Origem";
					break;
				}
				case 5:
				{
					if (pPOA.atualizaListaTarifaTrocaMSISDN())
						atualizou=true;
					nomeMapeamento = "Tarifa Troca Senha";
					break;
				}
				case 6:
				{
					if (pPOA.atualizaListaConfiguracaoGPP())
						atualizou=true;
					nomeMapeamento = "Configurações GPP";
					break;
				}
			}
		}
		catch(GPPInternalErrorException ie)
		{
			atualizou=false;
		}
		
		if (!atualizou)
			req.getRequestDispatcher("msgMapeamento.jsp?msg="+"Não foi possível atualizar lista de valores do mapeamento " + nomeMapeamento).forward(req,res);

		req.getRequestDispatcher("ConsultaMapeamentosServlet?tipoMapeamento="+tipoMapeamento).forward(req,res);
	}
}
