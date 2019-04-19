/*
 * Created on  2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

/**
 * @author Alberto Magno
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ClienteSumarizacaoContabil
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
			if(args.length == 3)
			{
				// Roda a Sumarização de todas as CNs para o dia em questão
				pPOA.sumarizarContabilidade(args[2]);
				System.out.println ("Metodo remoto sumarizar Contabil executado com sucesso");				
			}
			else
			{
				pPOA.sumarizarContabilidadeCN(args[2], args[3]);
				System.out.println ("Metodo remoto sumarizar Contabil executado com sucesso");
			}
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto sumarizar Contabil executado com erro...");
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);			
		}					
	}
	
	public static void menuOpcoes ( )
	{
		System.out.println ("\n\n");
		System.out.println ("+----------------------------------------------------+");
		System.out.println ("+  	Sistema de Sumarização Contábil			      +");
		System.out.println ("+----------------------------------------------------+\n");
		System.out.print ("\n");
	}	
}
