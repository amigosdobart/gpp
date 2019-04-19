package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;
//import java.text.*;
 
public class ClienteExportacaoDWGPP
{
	public ClienteExportacaoDWGPP ( )
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
		
		short retValue=0;
		try
		{
//			SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");		
			retValue = pPOA.exportarTabelasDW();
			if (retValue == 0)
				System.out.println ("Metodo de exportacao de tabelas do DW executado com sucesso...");
			else System.out.println ("Metodo de exportacao de tabelas do DW executado com erro...");
			
			System.exit(retValue);
		}
		catch (Exception e) 
		{
			System.out.println ("Metodo remoto de exportacao de tabelas do DW executado com erro...");
			System.out.println("Erro:" + e);
			System.exit(retValue);
		}					
	}
	
	public static void menuOpcoes ( )
	{
//		int userOption;
		System.out.println ("\n\n");
		System.out.println ("+--------------------------------------------------------------------+");
		System.out.println ("+  Sistema de Teste de Envio de Bonus Toma La Da Ca - Saintes CSP14  +");
		System.out.println ("+--------------------------------------------------------------------+\n");
		System.out.println (" Procesando .....");
		System.out.print (" Fim do Processo ");		
		System.out.print ("\n");
	}
}