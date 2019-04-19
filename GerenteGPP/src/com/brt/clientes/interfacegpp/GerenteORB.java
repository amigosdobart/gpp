package com.brt.clientes.interfacegpp;


import org.omg.CORBA.ORB;

import com.brt.clientes.servlet.PingGPPs;
import org.apache.log4j.Logger;


public class GerenteORB{

	static Logger logger = Logger.getLogger(GerenteORB.class);

	private static ORBWrapper[] orbs = null;
	// Thread para consulta status GPPs;
	private static PingGPPs pingGPPs = null;
	private static String[] listaGPPs = null;
	private static String servidores = null;
	private static long periodoPingGPP = 180000L; //default de 3 minutos
	
	public static int ORBinUse = -1;
	
	public static void setPossibleORBinUse()
	{
		int tem = -1;
		for(int i=0;i<orbs.length;i++)
			if (getOrbStatus(i).equals("Disponível"))
			{
				tem = i;
				break;				
			}
		ORBinUse = tem;			
	}
	
	public static String getOrbStatus(int o)
	{
		return o>-1&&o<orbs.length?orbs[o].getStatus():"Indisponível"; 
	}
	
	public static int getOrbsCount()
	{
		return orbs.length; 
	}

	public static String getOrbServer(int o)
	{
		return o>-1&&o<orbs.length?orbs[o].getServidor():"Indisponível"; 
	}
	
	public static String getOrbPort(int o)
	{
		return o>-1&&o<orbs.length?orbs[o].getPorta():"Indisponível"; 
	}

	public static String getCurrentOrb()
	{
		return "Servidor:"+getOrbServer(ORBinUse)+" Porta:"+getOrbPort(ORBinUse);
	}
	public static ORB getOrb()
	{
		return orbs[ORBinUse].getORB();
	}
	
	public static void startThread()
	{

		try {
			pingGPPs = new PingGPPs(orbs);
			pingGPPs.setName("PingORBs");
			pingGPPs.setDaemon(true);
			pingGPPs.setPeriodo(periodoPingGPP);
			pingGPPs.start();
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error("Erro ao iniciar PingThread MSG_ERRO:" +e.getMessage());
		}
	}

	private GerenteORB(){}
	
	
	
	/**
	 * @return
	 */
	public static int getORBinUse() {
		return ORBinUse;
	}

	/**
	 * @param i
	 */
	public static void setORBinUse(int i) {
		if (i>=0&&i<orbs.length)
			ORBinUse = i;
	}

	/**
	 * @return
	 */
	public static String[] getOrbs() {
		String[] lista = null;
		for (int i=0; i<orbs.length;i++)
			lista[i] = orbs[i].getServidor()+":"+orbs[i].getPorta();
		return lista;
	}

	/**
	 * @param strings
	 */
	public static void setListaGPPs(String[] strings) {
		listaGPPs = strings;
	}


	/**
	 * @param string
	 */
	public static void setServidores(String servs) {
			
		String[] GPPs = servs.split("#");
		orbs = new ORBWrapper[GPPs.length];
		for (int g=0;g<GPPs.length;g++)
		{
			String[] socket = GPPs[g].split(":");
			orbs[g]=new ORBWrapper(socket[0],socket[1]);
		}
		
	}

	/**
	 * @return
	 */
	public static long getPeriodoPingGPP() {
		return periodoPingGPP;
	}

	/**
	 * @param l
	 */
	public static void setPeriodoPingGPP(long l) {
		periodoPingGPP = l;
	}

}