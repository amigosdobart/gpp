// Cliente que invoca o Processo Batch de sumarizacao de mudanca de status de assinantes para Shutdown 
// para a tabela de relatorio
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;
 
public class ClientePulaPulaGPP
{
	
	public static void main(String[] args) 
	{
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties(props);	
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();
		
		processosBatch pPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		short retorno = -1;
		
		try
		{
		    String	tipoExecucao		= args[2];
		    String	dataReferencia		= args[3];
		    int		promocao			= (args.length >= 5) ? Integer.parseInt(args[4]) : -1;
		    
			retorno = pPOA.executaConcessaoPulaPula(tipoExecucao, dataReferencia, promocao);
			
			if(retorno == 0)
			{
				System.out.println("Metodo remoto executaConcessaoPulaPula executado com sucesso...");
			}
			else
			{
				System.out.println ("Metodo remoto executaConcessaoPulaPula retornou o erro: " + retorno);
			}
			
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto executaConcessaoPulaPula executado com erro...");
			System.out.println("Erro: " + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(99);
		}
		
		System.exit(retorno);
	}
	
}