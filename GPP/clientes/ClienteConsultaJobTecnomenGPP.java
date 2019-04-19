// Cliente de Consulta de Assinante
package clientes;

import com.brt.gpp.componentes.consulta.orb.*;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.*;

public class ClienteConsultaJobTecnomenGPP
{
	public ClienteConsultaJobTecnomenGPP ( )
	{
		System.out.println ("Consultanto job tecnomen...");
	}
	
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
		try
		{
			InfoJobTecnomen job = pPOA.consultaJobTecnomen(Integer.parseInt(args[2]));
			int completo = job.workTotal==job.workDone ? 100 : (int)(job.workTotal/job.workDone)*100;
			System.out.println("Job Numero:"+job.numeroJob+" possui status: "+job.descStatus+" e esta "+completo+"% completado");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}