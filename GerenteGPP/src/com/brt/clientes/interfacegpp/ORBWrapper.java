/*
 * Created on 07/06/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.brt.clientes.interfacegpp;
import org.omg.CORBA.ORB;

/**
 * @author ex576091
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ORBWrapper 
{
	// Objeto do Orb
	private ORB orb = null;

	private String status = "Indisponível";
	
	private String servidor = null;
	private String porta = null;
	

	/**
	 * 
	 */
	public ORBWrapper(String servidor, String porta) {
		this.servidor = servidor;
		this.porta = porta;
	}

	public ORB getORB(){
		
		if(orb == null){
			initORB();
		}
		return orb;
			
	}

	private void initORB(){
			java.util.Properties props = System.getProperties();
			props.put("vbroker.agent.port", porta);
			props.put("vbroker.agent.addr", servidor);
			System.setProperties(props);
			String[] args = {porta,servidor};
			orb = ORB.init(args,props);
	}
/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		status = string;
	}

	/**
	 * @return
	 */
	public String getPorta() {
		return porta;
	}

	/**
	 * @return
	 */
	public String getServidor() {
		return servidor;
	}

	/**
	 * @param string
	 */
	public void setPorta(String string) {
		porta = string;
	}

	/**
	 * @param string
	 */
	public void setServidor(String string) {
		servidor = string;
	}

}
