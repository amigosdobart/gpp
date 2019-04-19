/*
 * Created on Jul 23, 2004
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
public class ClienteConsolidacaoContabil
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
			if (pPOA.consolidarContabilidade(args[2])==0)
				System.out.println ("Metodo remoto consolidar Contabilidade executado com sucesso.");
			else 
				System.out.println ("ERRO no Metodo remoto consolidar Contabilidade.");
			System.exit(0);
		}
		catch (Exception e) 
		{
			System.out.println ("ERRO no Metodo remoto consolidar Contabilidade.");
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);			
		}					
	}
	
	public static void menuOpcoes ( )
	{
		System.out.println ("\n\n");
		System.out.println ("+----------------------------------------------------+");
		System.out.println ("+  	Sistema de Consolidacao Contabil		      +");
		System.out.println ("+----------------------------------------------------+\n");
		System.out.print ("\n");
	}	
}
