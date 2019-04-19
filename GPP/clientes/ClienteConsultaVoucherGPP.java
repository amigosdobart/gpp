// Cliente de Consulta de Voucher
package clientes;

import com.brt.gpp.componentes.consulta.orb.*;

public class ClienteConsultaVoucherGPP
{
	public ClienteConsultaVoucherGPP ( )
	{
		System.out.println ("Consultanto status de voucher...");
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
			String ret = pPOA.consultaVoucher (args[2]);
			if (ret != null) 
				System.out.println("Retorno = " + ret);
			else
//			    o método consultaVoucher deve lançar excecao
				System.out.println("Erro desconhecido ao consultar voucher"); 
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}
	}
}