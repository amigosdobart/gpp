// Cliente de Envio de SMS
package clientes;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.componentes.processosBatch.orb.*;

import java.io.*;

public class ClienteEnvioSMSMenuGPP
{
	public ClienteEnvioSMSMenuGPP ( )
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

		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();

		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		int userOption = 0;
		boolean exit = true;

		while ( exit )
		{
			userOption = menuOpcoes ();
			switch ( userOption )
			{
				case 0:
				{
					exit = false;
					break;
				}
				case 1:
				{
					System.out.print ("Digite o MSISDN de Origem: ");
					String msisdnOrigem = read();

					System.out.print ("Digite o MSISDN de Destino: ");
					String msisdnDestino = read();

					System.out.print ("Digite a Mensagem: ");
					String mensagem = read();

					System.out.print ("Digite a prioridade (0 - OnLine ou 1 - Horario Comercial: ");
					String prioridade = read();

					System.out.print ("Digite o tipo: (RECARGA / AJUSTE / TROCA PLANO): ");
					String tipo = read();

					boolean ret = false;

					try
					{
						System.out.println("msisdnOrigem: "+msisdnOrigem);
						System.out.println("msisdnDestino: "+msisdnDestino);
						System.out.println("mensagem: "+mensagem);
						System.out.println("prioridade: "+prioridade);
						System.out.println("tipo: "+tipo);

						ret = pPOA.gravarMensagemSMS(msisdnOrigem, msisdnDestino, mensagem, Integer.parseInt(prioridade), tipo);
						if (ret)
						{
							System.out.println ("Metodo remoto gravaMensagemSMS executado com sucesso...");
						}
						else
						{
							System.out.println ("Metodo remoto gravaMensagemSMS executado com erro...");
						}
					}
					catch (Exception e)
					{
						System.out.println("Erro:" + e);
					}
					break;
				}
			}
		}
	}

	public static int menuOpcoes ( )
	{
		int userOption;
		System.out.println ("\n\n");
		System.out.println ("+----------------------------------------------+");
		System.out.println ("+  Sistema de Teste de Envio de SMS  +");
		System.out.println ("+----------------------------------------------+\n");
		System.out.println ("1 - Grava Mensagem de SMS");
		System.out.print   ("0 - Saida");
		System.out.println ("\n");
		System.out.print   ("Opcao:");
		userOption = Integer.parseInt ( read() );
		System.out.print   ("\n");

		return userOption;
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