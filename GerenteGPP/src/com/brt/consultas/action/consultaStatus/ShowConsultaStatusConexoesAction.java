/*
 * Created on 18/06/2004
 *
 */
package com.brt.consultas.action.consultaStatus;

import java.net.Socket;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;
import org.omg.CORBA.ORB;

import com.brt.clientes.action.base.ShowAction;
import com.brt.clientes.interfacegpp.GerenteORB;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper;

import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

/**
 * @author André Gonçalves
 * @since 18/06/2004
 */
public class ShowConsultaStatusConexoesAction extends ShowAction {

	public static String 	ipMiddlewareSMS;
	public static String 		portaMiddlewareSMS;

	public static String 	ipServidorSocketGPP;
	public static String		portaServidorSocketGPP;
	
	public static String 	urlServidorOracleReports;
	private ArrayList dadosStatus = null;
	
	/* (non-Javadoc)
	 * @see com.brt.clientes.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
	 */
	public void updateVelocityContext(VelocityContext vctx, HttpServletRequest request, ActionForm form) 
	{
		dadosStatus = new ArrayList();
		
		// Guarda orb ativo
		int current = GerenteORB.getORBinUse();
		for (int n=0;n<GerenteORB.getOrbsCount();n++)
		{
			GerenteORB.setORBinUse(n);
			dadosStatus.add(new DadosStatus(GerenteORB.getOrbServer(n)+":"+GerenteORB.getOrbPort(n),
					consultaGPP(),consultaIDLGateway(),
					consultaPaymentEngine(),consultaVoucher(),
					consultaBancoDados(),consultaMiddlewareSMS(),
					consultaServidorSocket(),consultaOracleReports()
			));
		}
		
		//Reseta ORB ativo.
		GerenteORB.setORBinUse(current);

		vctx.put("dadosStatus", dadosStatus);
	}

	/* (non-Javadoc)
	 * @see com.brt.clientes.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroStatusConexoes.vm";
	}

	/*
	 * Faz umum PING no GPP para validar se o mesmo esta respondendo
	 */
	public String consultaGPP()
	{
		boolean retorno = false;
		gerenteGPP pPOA;
		
		// Conulta conexoes de aprovisionamento		
		
		ORB orb = GerenteORB.getOrb();

		try
		{		
			byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();
			pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);

			pPOA.ping();
			retorno = true;
		}
		catch(Exception ie)
		{
		}
		
		return (retorno==true?"OK":"Erro");
	}

	/*
	 * Faz uma consulta de assinante simples para verificar se o IDW Gateway 
	 * esta respondendo as requisicoes corba
	 */
	public String consultaIDLGateway()
	{
		boolean retorno = false;
		consulta pPOA;
		
		// Conulta conexoes de aprovisionamento		
		ORB orb = GerenteORB.getOrb();
		

		try
		{		
			byte[] managerId = "ComponenteNegociosConsulta".getBytes();
			pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			pPOA.consultaAssinanteSimples ("556184088020"); // Consulta um numero qualquer
			retorno = true;
		}
		catch(Exception ie)
		{
			
		}
		
		return (retorno==true?"OK":"Erro");

	}

	/*
	 * Faz uma consulta de voucher para verificar se o IDW Gateway (conexao agent/voucher) 
	 * esta respondendo as requisicoes corba
	 */
	public String consultaVoucher()
	{
		boolean retorno = false;
		consulta pPOA;
		
		// Conulta conexoes de voucher		
		ORB orb = GerenteORB.getOrb();
		

		try
		{		
			byte[] managerId = "ComponenteNegociosConsulta".getBytes();
			pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			pPOA.consultaVoucher("11001885*"); // Consulta um numero qualquer
			retorno = true;
		}
		catch(Exception ie)
		{

		}
		
		return (retorno==true?"OK":"Erro");

	}

	/*
	 * Faz uma consulta de assinante para verificar se o Payment Engine 
	 * esta respondendo as requisicoes corba
	 */
	public String consultaPaymentEngine()
	{
		boolean retorno = false;
		aprovisionamento pPOA;
		
		// Conulta conexoes de aprovisionamento		
		ORB orb = GerenteORB.getOrb();

		try
		{		
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();	
			pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			pPOA.consultaAssinante ("556184088020"); // Consulta um numero qualquer
			retorno = true;
		}
		catch(Exception ie)
		{

		}
		
		return (retorno==true?"OK":"Erro");
	}

	/*
	 * Faz uma atualizacao dos valores de MAP de memoria para ver se o banco  
	 * de dados esta ativo
	 */
	public String consultaBancoDados()
	{
		boolean retorno = false;
		gerenteGPP pPOA;
		
		// Conulta conexoes de aprovisionamento		
		ORB orb = GerenteORB.getOrb();

		try
		{		
			byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();
			pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			retorno = pPOA.atualizaListaConfiguracaoGPP();
		}
		catch(Exception ie)
		{
		}
		
		return (retorno==true?"OK":"Erro");
	}

	/*
	 * Abre um socket para o servidor do middleware de SMS para verificar se o mesmo
	 * encontra-se ativo
	 */
	public String consultaMiddlewareSMS()
	{
		boolean retorno = false;
		
		if (ipMiddlewareSMS.split("#").length<GerenteORB.getORBinUse()+1||
				portaMiddlewareSMS.split("#").length<GerenteORB.getORBinUse()+1) 
			return ((DadosStatus) dadosStatus.get(0)).getConexaoMiddlewareSMS();
		
		try
		{
	    	Socket sk = new Socket(
	    			ipMiddlewareSMS.split("#")[GerenteORB.getORBinUse()], 
					Integer.parseInt(portaMiddlewareSMS.split("#")[GerenteORB.getORBinUse()])
					);
	    	retorno = true;
		}
	    catch (Exception e)
		{
	    	
		}
	    
		return (retorno==true?"OK":"Erro");
	}

	/*
	 * Abre um socket para o servidor de socket do GPP para verificar se o mesmo
	 * encontra-se ativo
	 */
	public String consultaServidorSocket()
	{
		boolean retorno = false;

		if (ipServidorSocketGPP.split("#").length<GerenteORB.getORBinUse()+1||
				portaServidorSocketGPP.split("#").length<GerenteORB.getORBinUse()+1||
				ipServidorSocketGPP.split("#")[GerenteORB.getORBinUse()].length()<1||
				portaServidorSocketGPP.split("#")[GerenteORB.getORBinUse()].length()<1
		) return "-";

	    try
		{
	    	Socket sk = new Socket(
	    			ipServidorSocketGPP.split("#")[GerenteORB.getORBinUse()], 
	    			Integer.parseInt(portaServidorSocketGPP.split("#")[GerenteORB.getORBinUse()])
					);
	    	retorno = true;
		}
	    catch (Exception e)
		{

		}
	    
		return (retorno==true?"OK":"Erro");
	}

	/*
	 * Abre uma URL para o servidor de Oracle Reports para ver se o mesmo 
	 * encontra-se ativo
	 */
	public String consultaOracleReports()
	{
		boolean retorno = false;

		if (urlServidorOracleReports.split("#").length<GerenteORB.getORBinUse()+1||
				urlServidorOracleReports.split("#")[GerenteORB.getORBinUse()].length()<1		
		) return "-";

	    try
		{
	    	java.net.URL url = new java.net.URL(urlServidorOracleReports.split("#")[GerenteORB.getORBinUse()]);
   	 		(url.openConnection()).connect();
	    	retorno = true;
		}
	    catch (Exception e)
		{

		}
	    
		return (retorno==true?"OK":"Erro");
	}

}
