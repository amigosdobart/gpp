/*
 * Created on 26/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.servlet;

import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper;

import br.com.brasiltelecom.ppp.interfacegpp.GerenteORB;
import br.com.brasiltelecom.ppp.interfacegpp.StatusGPP;

import org.apache.log4j.Logger;

/**
 * @author Alberto Magno
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PingGPP extends Thread {

	private String GPPAddress;
	private String GPPPort;
	private long periodo = 180000L; //Período em mseg (default 3 minutos)
	
	Logger logger = Logger.getLogger(this.getClass());
	
		public void run()
	{
		String sPing = null;

		while(true)
		{
			 try
			 {
			
				 // Inicializando o ORB
				 org.omg.CORBA.ORB orb = GerenteORB.getORB(GPPAddress, GPPPort);
			
				 byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();
			
				 gerenteGPP pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);
				 sPing =  pPOA.ping();
				 StatusGPP.setStatus(sPing);
			 }
			 catch (Exception e)
			 {
				StatusGPP.setStatus(null);
				logger.error("Erro ao realizar ping no GPP("+GPPAddress+":"+GPPPort+") MSG_ERRO:" +e.getMessage());
			 }
	
		 	try 
		 	{
				sleep(periodo);
			} catch (InterruptedException e1) {}
		}
	}
	public PingGPP(String port, String address) {
			super();
			GPPAddress = address;
			GPPPort = port;
			this.setName("PingGPP");
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
