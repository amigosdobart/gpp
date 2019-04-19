package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClienteBloqueioAutomatico 
{
	public ClienteBloqueioAutomatico ( )
	{
		// Construtor
	}
	
	public static void main(String[] args) 
	{
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();
		
		processosBatch pPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			pPOA.bloqueioAutomaticoServicoPorSaldo(args[2]);
		}
		catch(Exception e)
		{
			System.out.println("Erro Interno GPP: "+ e);
		}
	}
}
