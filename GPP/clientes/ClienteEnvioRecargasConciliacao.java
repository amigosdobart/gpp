package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;
//import java.io.*;

/**
 * @author Denys_Oliveira
 *
 */
public class ClienteEnvioRecargasConciliacao {

	public ClienteEnvioRecargasConciliacao ( )
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
			pPOA.enviarRecargasConciliacao(args[2]);
			System.out.println ("Metodo remoto enviarRecargasConciliacao executado com sucesso...");
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto enviarRecargasConciliacao executado com erro...");
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}					
	}
}
