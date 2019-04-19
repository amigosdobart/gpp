package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClienteEnvioConsolidacaoSCR 
{
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
			// Erro no caso de registros não processados na tabela de interfaces
			if(pPOA.enviarConsolidacaoSCR(args[2]) == 36)
				System.out.println ("Existem registros ainda nao processados na tabela de interface.");
			else
				System.out.println ("Metodo remoto ClienteEnvioConsolidacaoSCR executado com sucesso...");
			System.exit(0);
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto ClienteEnvioConsolidacaoSCR executado com erro...");
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}					
	}
}
