// Cliente que envia bonus por chamada sainte com CSP 14 - Bonus Toma La Da Ca
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;
 
public class ClienteEstornoBonusSobreBonusGPP
{
	public ClienteEstornoBonusSobreBonusGPP()
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
		
		short retorno = -1;
		
		try
		{		
			retorno = pPOA.estornaBonusSobreBonus();
			if(retorno == 0)
			  System.out.println ("Metodo remoto estornaBonusSobreBonus executado com sucesso...");
			else
		      System.out.println ("Metodo remoto estornaBonusSobreBonus executado com erro...");
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto estornaBonusSobreBonus executado com erro. Problema durante a gravacao do historico.");
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
		System.out.println ("+  Sistema de Teste de Estorno de Bonus sobre Bonus  +");
		System.out.println ("+----------------------------------------------------+\n");
		System.out.println (" Procesando .....");
		System.out.print (" Fim do Processo ");		
		System.out.print ("\n");
	}
}