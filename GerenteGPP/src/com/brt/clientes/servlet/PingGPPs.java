/*
 * Created on 07/06/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.brt.clientes.servlet;

import com.brt.clientes.interfacegpp.GerenteORB;
import com.brt.clientes.interfacegpp.ORBWrapper;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper;
/**
 * @author Alberto Magno
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PingGPPs extends Thread {

	private ORBWrapper[] listaORBs = null;
	private long periodo = 180000L; //Período em mseg (default 3 minutos)
	
	public void run()
	{
		String sPing = null;
		while(true)
		{
			 // Percorre todos os ORBs agendados
			 for (int nORB=listaORBs.length-1; nORB>-1; nORB--)
			 {
				 try
				 {
				
					 // Inicializando o ORB
					 org.omg.CORBA.ORB orb = listaORBs[nORB].getORB();
				
					 byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();
				
					 gerenteGPP pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);
					 sPing =  pPOA.ping();
					 listaORBs[nORB].setStatus((sPing!=null)&&(!sPing.equals(""))?"Disponível":"Indisponível");
				 }
				 catch (Exception e)
				 {
					listaORBs[nORB].setStatus("Indisponível");
					//e.printStackTrace();
			
				 }
			 }

			GerenteORB.setPossibleORBinUse();
		 	try {
				sleep(periodo);
				
			} catch (InterruptedException e1) {
				//e1.printStackTrace();
			}
		}
	}
	
	public PingGPPs(ORBWrapper[] listaORBs) {
			super();
			this.listaORBs = listaORBs;
			this.setName("PingGPPs");
		}

	/**
	 * @return
	 */
	public long getPeriodo() {
		return periodo;
	}

	/**
	 * @param l
	 */
	public void setPeriodo(long l) {
		periodo = l;
	}

}
