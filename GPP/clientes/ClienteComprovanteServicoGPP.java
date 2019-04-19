// Cliente de Processamento de Comprovante de Servico
package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClienteComprovanteServicoGPP
{
	public ClienteComprovanteServicoGPP ( )
	{
		System.out.println ("Startando processo de envio de Comprovante de Servico...");
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
		
		boolean ret = false;
					
		try
		{
			ret = pPOA.envioComprovanteServico();
			if ( ret )
			{
				System.out.println ("Metodo remoto envioComprovanteServico executado com sucesso...");
				System.out.println(ret);
			}
			else
			{
				System.out.println ("Metodo remoto envioComprovanteServico não executado com erro ...");
			}						
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}					
	}
}