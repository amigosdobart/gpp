//Cliente que faz a manutenção dos créditos dos Gerentes Felizes
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;
import com.brt.gpp.comum.Definicoes;
 
public class ClienteGerarArquivoCobilling
{
	public ClienteGerarArquivoCobilling ( )
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
		    // Consulta para todos os estados cobertos pela BrT
		    if( args.length == 5)
		        pPOA.gerarArquivoCobilling(args[2],args[3],args[4],Definicoes.TODAS_UF);
		    // Consulta para um estado específico
		    else
		        pPOA.gerarArquivoCobilling(args[2],args[3],args[4],args[5]);
		    
			System.out.println ("Metodo remoto gerarArquivoCobilling executado com sucesso...");
			
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto gerarArquivoCobilling executado com erro...");
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}					
	}
}