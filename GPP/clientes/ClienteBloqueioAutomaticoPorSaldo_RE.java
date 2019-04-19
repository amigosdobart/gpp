package clientes;

import com.brt.gpp.componentes.processosBatch.orb.*;

public class ClienteBloqueioAutomaticoPorSaldo_RE 
{
	public ClienteBloqueioAutomaticoPorSaldo_RE()
	{
		// Construtor Padrão
	}
	
	public static void main(String[] args) 
	{
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		short ret = 0;
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();
		
		processosBatch pPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			ret = pPOA.bloqueioAutomaticoServicoIncluindoRE(args[2]);
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
		}
		if (  ret == 0 )
		{
			System.out.println ("Metodo remoto de bloqueio automatico por saldo/RE executado com sucesso...");
		}
		else
		{
			System.out.println ("Metodo remoto de bloqueio automatico por saldo/RE retornou o erro: " + ret );
		}
	}
}
