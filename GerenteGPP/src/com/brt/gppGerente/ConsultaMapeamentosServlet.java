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

public class ConsultaMapeamentosServlet extends HttpServlet
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
		switch(tipoMapeamento)
		{
			case 1:
			{
				list = pPOA.exibeListaPlanoPreco();
				nomeMapeamento = "Plano de Preço";
				break;
			}
			case 2:
			{
				list = pPOA.exibeListaStatusAssinante();
				nomeMapeamento = "Status Assinante";
				break;
			}
			case 3:
			{
				list = pPOA.exibeListaStatusServico();
				nomeMapeamento = "Status Serviço";
				break;
			}
			case 4:
			{
				list = pPOA.exibeListaSistemaOrigem();
				nomeMapeamento = "Sistema Origem";
				break;
			}
			case 5:
			{
				list = pPOA.exibeListaTarifaTrocaMSISDN();
				nomeMapeamento = "Tarifa Troca Senha";
				break;
			}
			case 6:
			{
				list = pPOA.exibeListaConfiguracaoGPP();
				nomeMapeamento = "Configurações GPP";
				break;
			}
		}
		
		Collection valores = new Vector();
		int posFimLinha=0;
		int posInicial=0;
		while ((posFimLinha=list.indexOf("\n",posFimLinha)) > -1)
		{
			String linha = list.substring(posInicial,posFimLinha);
			String colunas[] = linha.split(ConsultaMapeamentosServlet.CHAR_SEPARADOR_CAMPOS);
			valores.add(colunas);
			posInicial = posFimLinha+1;
			posFimLinha++;
		}
		req.getSession().setAttribute("listaMapeamentos",valores);
		
		if (valores == null || valores.size() == 0)
			req.getRequestDispatcher("msgMapeamento.jsp?msg="+"Não foi possível listar valores do mapeamento " + nomeMapeamento).forward(req,res);

		req.getRequestDispatcher("resultadoPesquisaMapeamentos.jsp").forward(req,res);
	}
}
