// Cliente de Recarga Microsiga
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClienteRecargaMicrosigaGPP
{
	public ClienteRecargaMicrosigaGPP()
	{
		System.out.println ("Inciando processo de recargas requisitadas pelo Microsiga...");
	}
	
	public static void main(String[] args) 
	{
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties(props);	
		
		//Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();
		
		processosBatch pPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		short ret = -1;
		
		// While infinito
		while (true)
		{
			try
			{
				ret = pPOA.executaRecargaMicrosiga();
				if (ret == 0)
				{
					System.out.println ("Metodo remoto executaRecargaMicrosiga executado com sucesso...");
				}
				else
				{
					System.out.println ("Metodo remoto executaRecargaMicrosiga não executado com erro ...");
					// Finaliza o cliente GPP indicando erro no processamento
					System.exit(1);
				}						
			}
			catch (Exception e) 
			{
				System.out.println("Erro:" + e);
			}
			
			// "Dormindo" 1 minuto e executad novamente
			try
			{
				Thread.sleep(15*1000);
			}
			catch (Exception e)
			{
				System.out.println("Erro:" + e);
				System.exit(2);
			}
		}
	}
}