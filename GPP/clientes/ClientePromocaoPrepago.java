// Cliente de Aprovisionamento.java
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClientePromocaoPrepago
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
		
			byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();
		
			processosBatch pPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);
				
			pPOA.promocaoPrepago(args[2]);
			
		}
		catch (Exception e)
		{
			// Finaliza o cliente GPP indicando erro no processamento
			e.printStackTrace();
			System.exit(1);		
		}
	}
}
