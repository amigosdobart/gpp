/*
 * Created on May 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

/**
 * @author Denys_Oliveira
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ClienteSumarizacaoAjustes 
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
		menuOpcoes();
		
		try
		{		
			pPOA.sumarizarAjustes(args[2]);
			System.out.println ("Metodo remoto sumarizarAjustes executado com sucesso...");
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto sumarizarAjustes executado com erro...");
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);			
		}					
	}
	
	public static void menuOpcoes ( )
	{
//		int userOption;
		System.out.println ("\n\n");
		System.out.println ("+----------------------------------------------------+");
		System.out.println ("+  Sistema de Teste de Calculo Dias sem Recarga      +");
		System.out.println ("+----------------------------------------------------+\n");
		//System.out.print ("Digite a DataHora (DD/MM/AAAA): ");
		//tmpDataHora = read();
		System.out.println (" Procesando .....");
		System.out.print (" Fim do Processo ");		
		System.out.print ("\n");
	}	
}
