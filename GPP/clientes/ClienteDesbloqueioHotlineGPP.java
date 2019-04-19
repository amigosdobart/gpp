//Cliente de Aprovisionamento.java
package clientes;

import com.brt.gpp.componentes.aprovisionamento.orb.*;
import java.io.*;

public class ClienteDesbloqueioHotlineGPP
{
	public ClienteDesbloqueioHotlineGPP ( )
	{
		System.out.println ("Ativando cliente de desbloquio de hotline...");
	}
	
	public static void main(String[] args) 
	{

		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

//		int userOption = 0;
//		boolean exit = true;
		long ret = 0;
		
		System.out.print ("Digite o codigo do Assinante: ");
		String tmpMSISDN = read();

		System.out.print ("Digite a categoria --> F1 - Pos-pago , F2 - Pre-pago ou F3 - Hibrido: ");
		String tmpCategoria = read();

		try
		{
			ret = pPOA.desativarHotLine(tmpMSISDN, tmpCategoria);
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
		}

		System.out.println ("Metodo remoto ativaMSISDN executado com sucesso com retorno: " + ret);
	}
					
	public static String read ( )
	{
		String msg = "";
		DataInput di = new DataInputStream(System.in);
		
		try 
		{
		  msg = di.readLine();
		} 
		catch (Exception e) 
		{
		  System.out.println("Erro lendo linha: " + e.getMessage());
		}
		return msg;
	}
}
