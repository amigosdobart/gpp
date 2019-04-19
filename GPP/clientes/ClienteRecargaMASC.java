//Cliente que realiza a recarga e consulta na plataforma da Alcatel via MASC
package clientes;

import com.brt.gpp.componentes.recarga.orb.*;
import java.io.*;

public class ClienteRecargaMASC
{
	public ClienteRecargaMASC ( )
	{
		System.out.println ("Cliente de recarga via MASC...");
	}
	
	public static void main(String[] args) 
	{
		//parametros do cliente: porta e endereço do host
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosRecarga".getBytes();
		
		recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

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
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();
					
//					String tmpOperador = "ClienteRecargaMASC";

					short ret = -1;
					
					try
					{
						ret = pPOA.podeRecarregar(tmpMSISDN,0);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto executaConsultaMASC executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaConsultaMASC retornou o erro: " + ret );
					}
					
					break;
				}
				
				case 2:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite tipo da transacao: ");
					String tmpTipoTransacao = read();
					
					System.out.print ("Digite o identificador da recarga: ");
					String tmpIdRecarga = read();
					
					String tmpTipoCredito = "00";
										
					System.out.print ("Digite o valor: ");
					String tmp_Valor = read();
					double tmpValor = (new Double(tmp_Valor)).doubleValue();
					
					System.out.print ("Digite a data e hora: (AAAAMMDDHHMMSS)");
					String tmpDataHora = read();
					
					System.out.print ("Digite NSU instituicao: ");
					String tmpNsuInstituicao = read();
					
					System.out.print ("Digite codigo loja: ");
					String tmpCodLoja = read();
					
					System.out.print ("Digite data e hora do banco: (AAAAMMDDHHMMSS)");
					String tmpDataHoraBanco = read();
					
					System.out.print ("Digite data contabil: (MMDD)");
					String tmpDataContabil = read();
					
					String tmpSistemaOrigem = "BCO";
					String tmpTerminal = "Terminal X";
					String tmpTipoTerminal = "ZZ";
					String tmpOperador = "ClienteRecargaGPP";

					short ret = -1;
					
					try
					{
						ret = pPOA.executaRecargaBanco (tmpMSISDN, tmpTipoTransacao, tmpIdRecarga, tmpNsuInstituicao,
														tmpCodLoja, tmpTipoCredito, tmpValor, tmpDataHora, 
														tmpDataHoraBanco, tmpDataContabil,tmpTerminal,tmpTipoTerminal,
														tmpSistemaOrigem, tmpOperador);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto executaRecargaMASC executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaRecargaMASC retornou o erro:"+ret );
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
		System.out.println ("+-----------------------------------+");
		System.out.println ("+ Sistema de Teste de Conexao MASC  +");
		System.out.println ("+-----------------------------------+\n");
		System.out.println ("1 - Consulta pre-recarga via MASC");
		System.out.println ("2 - Recarga via MASC");
		System.out.println ("0 - Saida");
		System.out.print ("Opcao:");
		userOption = Integer.parseInt ( read() );
		
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
