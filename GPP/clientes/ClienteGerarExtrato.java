package clientes;

import com.brt.gpp.componentes.consulta.orb.*;

public class ClienteGerarExtrato 
{
	/*
	 * parametros de entrada: 	args[0] = porta
	 * 							args[1] = ip
	 * 							args[2] = msisdn
	 * 							args[3] = data inicial
	 * 							args[4] = data final
	 */
	public static void main(String[] args) 
	{
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		String tmpInicioPeriodo = args[3];
		String tmpFinalPeriodo = args[4];
		String tmpMSISDN = args[2];
	
		String ret = null;
	
		try
		{
			ret = pPOA.consultaExtrato(tmpMSISDN, tmpInicioPeriodo, tmpFinalPeriodo);
			if (  ret != null )
			{
				System.out.println ("Metodo remoto consultaExtrato executado com sucesso...");
				System.out.println(ret);
			}
			else
			{
				System.out.println ("Metodo remoto consultaExtrato executado com erro.");
			}							
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
		}
	}							
}
