// Cliente de Consulta de Assinante
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClienteRecargaRecorrenteGPP
{
	public ClienteRecargaRecorrenteGPP ( )
	{
		System.out.println ("Startando processo de recargas recorrentes...");
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
		
		boolean ret = false;
					
		try
		{
			ret = pPOA.executaRecargaRecorrente();
			if ( ret )
			{
				System.out.println ("Metodo remoto executaRecargaRecorrente executado com sucesso...");
				System.out.println(ret);
			}
			else
			{
				System.out.println ("Metodo remoto executaRecargaRecorrente não executado com erro ...");
				// Finaliza o cliente GPP indicando erro no processamento
				System.exit(1);
			}						
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
		}					
	}
}