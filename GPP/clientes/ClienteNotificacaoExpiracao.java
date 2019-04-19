// Cliente de Envio de SMS
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClienteNotificacaoExpiracao
{
	public ClienteNotificacaoExpiracao ( )
	{
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
						pPOA.gravaNotificacaoSMS();
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}					
	}		
}