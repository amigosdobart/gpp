// Cliente de Envia Informacoes de Recaraga para Irmao14
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClienteEnvioInfosRecargaGPP
{
	public ClienteEnvioInfosRecargaGPP ( )
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
			pPOA.enviaInfosRecarga(args[2]);
			System.out.println ("Metodo remoto enviaInfosRecarga executado com sucesso...");
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto enviaInfosRecarga executado com erro...");
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}					
	}
	
	public static void menuOpcoes ( )
	{
//		int userOption;
		System.out.println ("\n\n");
		System.out.println ("+--------------------------------------------------------------------+");
		System.out.println ("+  Sistema de Teste de Envio de Informacoes de Recarga para Irmao14  +");
		System.out.println ("+--------------------------------------------------------------------+\n");
		System.out.println (" Procesando .....");
		System.out.print (" Fim do Processo ");		
		System.out.print ("\n");
	}
}