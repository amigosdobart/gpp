package clientes;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

public class ClienteExtratoPulaPulaGPP 
{
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
		
		String tmpMSISDN = args[2];
		String tmpInicioPeriodo = args[3];
		String tmpFinalPeriodo = args[4];
			
		String ret = null;

		try
		{
			ret = pPOA.consultaExtratoPulaPula(tmpMSISDN, tmpInicioPeriodo, tmpFinalPeriodo);
			if (  ret != null )
			{
				System.out.println ("Metodo remoto consultaExtratoPulaPula executado com sucesso...");
				System.out.println(ret);
			}
			else
			{
				System.out.println ("Metodo remoto consultaExtratoPulaPula executado com erro.");
			}							
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
		}									
	}

}
