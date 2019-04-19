package br.com.brasiltelecom.ppp.interfacegpp;
import org.omg.CORBA.ORB;


public class GerenteORB{
	private static ORB orb = null;
	
	private GerenteORB(){}
	
	public static ORB getORB(String servidor, String porta){
		
		if(orb == null){
			initORB(servidor, porta);
		}
		return orb;
			
	}
	
	private synchronized static void initORB(String servidor, String porta){
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", porta);
		props.put("vbroker.agent.addr", servidor);
		props.put("org.omg.CORBA.ORBSingletonClass", "com.inprise.vbroker.orb.ORB");
		props.put("org.omg.CORBA.ORBClass", "com.inprise.vbroker.orb.ORB");
		String[] args = {porta,servidor};

		orb = ORB.init(args,props);
		
		
	}
	
}