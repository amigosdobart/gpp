// Cliente de Aprovisionamento.java
package clientes;

import com.brt.gpp.componentes.gerenteGPP.orb.*;

public class ClientePingGPP
{
	public static void main(String[] args) 
	{
		try
		{
			java.util.Properties props = System.getProperties();
			props.put("vbroker.agent.port", args[0]);
			props.put("vbroker.agent.addr", args[1]);
			System.setProperties ( props );	
			
			// Inicializando o ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
			
			byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();
			
			gerenteGPP pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);
	
			String sPing =  pPOA.ping();
			System.out.println (sPing);
		}
		catch (Exception e)
		{
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);		
		}
	}
}
