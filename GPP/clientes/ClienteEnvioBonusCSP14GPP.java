// Cliente que envia bonus por chamada sainte com CSP 14 - Bonus Toma La Da Ca
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;
 
public class ClienteEnvioBonusCSP14GPP
{
	public ClienteEnvioBonusCSP14GPP ( )
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
			pPOA.enviaBonusCSP14(args[2]);
			System.out.println ("Metodo remoto enviaBonusCSP14 executado com sucesso...");
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto enviaBonusCSP14 executado com erro...");
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}					
	}
}