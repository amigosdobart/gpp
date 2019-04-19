package clientes;

import com.brt.gpp.componentes.recarga.orb.*;
import java.io.*;

public class ClienteRecargaGPP
{
	public ClienteRecargaGPP ( )
	{
		System.out.println ("Cliente de recarga...");
	}
	
	public static void main(String[] args) 
	{
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

					System.out.print ("Digite tipo da transacao: ");
					String tmpTipoTransacao = read();
					
					System.out.print ("Digite o identificador da recarga: ");
					String tmpIdRecarga = read();
					
					System.out.print ("Digite tipo de credito: ");
					String tmpTipoCredito = read();
										
					System.out.print ("Digite o valor: ");
					String tmp_Valor = read();
					double tmpValor = (new Double(tmp_Valor)).doubleValue();
					
					System.out.print ("Digite a data e hora: (AAAAMMDDHHMMSS)");
					String tmpDataHora = read();
					
					System.out.print ("Digite sistema origem: ");
					String tmpSistemaOrigem = read();
					
					System.out.print ("Digite NSU instituição: ");
					String tmpNsuInstituicao = read();
					
					System.out.print ("Digite hash cartao credito: ");
					String tmpHash_CC = read();
					
					System.out.print ("Digite cpf: ");
					String tmpcpf = read();
					
					String tmpOperador = "ClienteRecargaGPP";

					short ret = -1;
					
					try
					{
						ret = pPOA.executaRecarga (	tmpMSISDN, tmpTipoTransacao, tmpIdRecarga, 
													tmpTipoCredito, tmpValor, tmpDataHora, 
													tmpSistemaOrigem, tmpOperador, tmpNsuInstituicao,
													tmpHash_CC, tmpcpf );
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto executaRecarga executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaRecarga retornou o erro: " + ret );
					}
					
					break;
				}
				
				case 2:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite tipo da transacao: ");
					String tmpTipoTransacao = read();
	
					System.out.print ("Digite tipo de credito: ");
					String tmpTipoCredito = read();
						
					System.out.print ("Digite o valor: ");
					String tmp_Valor = read();
					double tmpValor = (new Double(tmp_Valor)).doubleValue();
					
					System.out.print ("Digite o tipo (C/D): ");
					String tmpTipo = read();
	
					System.out.print ("Digite a data e hora (AAAAMMDDHHMMSS): ");
					String tmpDataHora = read();
	
					System.out.print ("Digite sistema origem: ");
					String tmpSistemaOrigem = read();
					
					System.out.print ("Digite a data de expiracao (AAAAMMDD): ");
					String tmpDataExpiracao = read();
					
					System.out.print ("Digite a descricao do ajuste: ");
					String tmpDescricao = read();
					
					String tmpOperador = "ClienteRecargaGPP";

					short ret = -1;
	
					try
					{
						ret = pPOA.executaAjusteDescrito (	tmpMSISDN, tmpTipoTransacao, tmpTipoCredito, tmpValor, 
						 							tmpTipo, tmpDataHora, tmpSistemaOrigem, tmpOperador, tmpDataExpiracao, tmpDescricao );
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto executaAjuste executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaAjuste retornou o erro: " + ret );
					}
	
					break;
				}
				
				case 3:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite o valor: ");
					String tmp_Valor = read();
					double tmpValor = (new Double(tmp_Valor)).doubleValue();
	
//					String tmpOperador = "ClienteRecargaGPP";

					short ret = 0;
	
					try
					{
						ret = pPOA.podeRecarregar(tmpMSISDN, tmpValor);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto podeRecarregar executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto podeRecarregar retornou o erro:"+ret );
					}
	
					break;
				}
				
				case 4:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite tipo da transacao: ");
					String tmpTipoTransacao = read();
					
					System.out.print ("Digite o identificador da recarga: ");
					String tmpIdRecarga = read();
					
					System.out.print ("Digite tipo de credito: ");
					String tmpTipoCredito = read();
										
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
						System.out.println ("Metodo remoto executaRecargaBanco executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaRecargaBanco retornou o erro: " + ret );
					}
					
					break;
				}
				
				case 5:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite tipo da transacao: ");
					String tmpTipoTransacao = read();
	
					System.out.print ("Digite tipo de credito: ");
					String tmpTipoCredito = read();
						
					System.out.print ("Digite o valor: ");
					String tmp_Valor = read();
					double tmpValor = (new Double(tmp_Valor)).doubleValue();
					
					System.out.print ("Digite o tipo (C/D): ");
					String tmpTipo = read();
	
					System.out.print ("Digite a data e hora: (AAAAMMDDHHMMSS)");
					String tmpDataHora = read();
	
					System.out.print ("Digite sistema origem: ");
					String tmpSistemaOrigem = read();
					
					System.out.print ("Digite a data de expiracao (AAAAMMDD):");
					String tmpDataExpiracao = read();
					
					String tmpOperador = "ClienteRecargaGPP";

					short ret = -1;
	
					try
					{
						ret = pPOA.executaAjuste (	tmpMSISDN, tmpTipoTransacao, tmpTipoCredito, tmpValor, 
													tmpTipo, tmpDataHora, tmpSistemaOrigem, tmpOperador, tmpDataExpiracao);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto executaAjuste executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaAjuste retornou o erro: " + ret );
					}
	
					break;
				}	
				
				case 6:
				{
					/*
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite tipo da transacao: ");
					String tmpTipoTransacao = read();
					
					System.out.print ("Digite o identificador da recarga: ");
					String tmpIdRecarga = read();
					
					System.out.print ("Digite tipo de credito: ");
					String tmpTipoCredito = read();
										
					System.out.print ("Digite o valor: ");
					String tmp_Valor = read();
					double tmpValor = (new Double(tmp_Valor)).doubleValue();
					
					System.out.print ("Digite a datae hora: (AAAAMMDDHHMMSS)");
					String tmpDataHora = read();
					
					System.out.print ("Digite sistema origem: ");
					String tmpSistemaOrigem = read();
					
					System.out.print ("Digite o NSU da instituicao: ");
					String tmpNsuInstituicao = read();
					
					System.out.print ("Digite o hash do cartao de credito: ");
					String tmpHash_CC = read();
					
					System.out.print ("Digite o cpf: ");
					String tmpcpf = read();
					
					
					String recargaXML = "<GPPRecarga>" + 
										"  <msisdn>" + tmpMSISDN +  "</msisdn>" + 
										"  <tipoTransacao>" + tmpTipoTransacao + "</tipoTransacao>" + 
										"  <identificacaoRecarga>" + tmpIdRecarga + "</identificacaoRecarga>" + 
										"  <tipoCredito>" + tmpTipoCredito + "</tipoCredito>" + 
										"  <valor>" + tmpValor + "</valor>" + 
										"  <dataHora>" + tmpDataHora + "</dataHora>" + 
										"  <sistemaOrigem>" + tmpSistemaOrigem + "</sistemaOrigem>" + 
										"  <operador>GPP</operador>" +
										"  <nsuInstituicao>" + tmpNsuInstituicao +  "</nsuInstituicao>" +
										"  <hashCc>" + tmpHash_CC + "</hashCc>"+
										"  <cpfCnpj>" + tmpcpf + "</cpfCnpj>"+
										"</GPPRecarga>"; 
					*/
					//System.out.print ("Digite o xml de ajuste: ");
					//String recargaXML = read();
					
					String recargaXML = "<GPPRecarga><msisdn>551181111111</msisdn><tipoTransacao>04004</tipoTransacao><identificacaoRecarga>DF/OS01185543/2005051</identificacaoRecarga><nsuInstituicao></nsuInstituicao><valor>14</valor><tipoCredito>00</tipoCredito><dataHora>20050124121400</dataHora><sistemaOrigem>CRM</sistemaOrigem><operador>claradm</operador><hashCc></hashCc><cpfCnpj>37402385000123</cpfCnpj></GPPRecarga>";
					String ret = null;
					try
					{
						ret = pPOA.executaRecargaXML (recargaXML);
						System.out.println(ret);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					System.out.println ("Metodo remoto executaRecargaXML executado com sucesso...");

					break;
				}							
				case 7:
				{
					
					System.out.print ("Digite o xml de ajuste: ");
					String recargaXML = read();

					//recargaXML = "<?xml version=\"1.0\"?>" +
						//				"<GPPRecarga>" + 
							//			"<MSISDN>556111111111</MSISDN>" + 
								//		"<tipoTransacao>05004</tipoTransacao>" + 
					//					"<tipoCredito>01</tipoCredito>" + 
					//					"<valor>25.0</valor>" +
					//					"<tipo>C</tipo>"+ 
					//					"<dataHora>20040408164000</dataHora>" + 
					//					"<sistemaOrigem>CLY</sistemaOrigem>" + 
					//					"<operador>Denys</operador>" +
					//					"<dataExpiracao></dataExpiracao>"+
					//					"</GPPRecarga>";
	
					short ret = -1;
					try
					{
						ret = pPOA.executaAjusteXML (recargaXML);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto executaAjusteXML executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaAjusteXML retornou o erro: " + ret );
					}
					break;
				}
				case 8:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite tipo da transacao: ");
					String tmpTipoTransacao = read();
	
					System.out.print ("Digite o tipo (C/D): ");
					String tmpTipo = read();
	
					System.out.print ("Digite o ajuste no saldo principal: ");
					String tmp_Valor0 = read();
					double tmpPrincipal = (new Double(tmp_Valor0)).doubleValue();
					
					System.out.print ("Digite o ajuste no saldo periodico: ");
					double tmpPeriodico = Double.parseDouble(read());
					
					System.out.print ("Digite o ajuste no saldo de bonus: ");
					String tmp_Valor1 = read();
					double tmpBonus = (new Double(tmp_Valor1)).doubleValue();
					
					System.out.print ("Digite o ajuste no saldo de SMS: ");
					String tmp_Valor2 = read();
					double tmpSMS = (new Double(tmp_Valor2)).doubleValue();

					System.out.print ("Digite o ajuste no saldo de dados: ");
					String tmp_Valor3 = read();
					double tmpDados = (new Double(tmp_Valor3)).doubleValue();
					
					System.out.print ("Digite a data de expiracao do saldo Principal (dd/mm/yyyy):");
					String tmpDataExpiracaoPrincipal = read();
					
					System.out.print ("Digite a data de expiracao do saldo Periodico (dd/mm/yyyy):");
					String tmpDataExpiracaoPeriodico = read();
					
					System.out.print ("Digite a data de expiracao do saldo Bonus (dd/mm/yyyy):");
					String tmpDataExpiracaoBonus = read();
					
					System.out.print ("Digite a data de expiracao do saldo SMS (dd/mm/yyyy):");
					String tmpDataExpiracaoSms = read();
					
					System.out.print ("Digite a data de expiracao do saldo Dados (dd/mm/yyyy):");
					String tmpDataExpiracaoDados = read();
					
					System.out.print ("Digite a data e hora: (AAAAMMDDHHMMSS)");
					String tmpDataHora = read();
	
					System.out.print ("Digite sistema origem: ");
					String tmpSistemaOrigem = read();
					
					String tmpOperador = "ClienteRecargaGPP";

					short ret = -1;
	
					try
					{
						ret = pPOA.executaAjusteMultiplosSaldos (tmpMSISDN, tmpTipoTransacao, tmpPrincipal, tmpPeriodico, tmpBonus, tmpSMS, tmpDados,
								tmpDataExpiracaoPrincipal, tmpDataExpiracaoPeriodico, tmpDataExpiracaoBonus, tmpDataExpiracaoSms, tmpDataExpiracaoDados, tmpTipo, tmpDataHora, tmpSistemaOrigem, tmpOperador);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto executaAjuste executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaAjuste retornou o erro: " + ret );
					}
	
					break;
				}		
				
				case 9:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite o valor: ");
					String tmp_Valor = read();
					double tmpValor = (new Double(tmp_Valor)).doubleValue();
	
					System.out.print ("Digite o tipo da transacao: ");
					String tmpTT = read();

//					String tmpOperador = "ClienteRecargaGPP";

					short ret = 0;
	
					try
					{
						ret = pPOA.podeRecarregarVarejo(tmpMSISDN, tmpValor, tmpTT);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto podeRecarregarVarejo executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto podeRecarregarVarejo retornou o erro:"+ret );
					}
	
					break;
				}

				case 10:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite tipo da transacao: ");
					String tmpTipoTransacao = read();
					
					System.out.print ("Digite o identificador da recarga: ");
					String tmpIdRecarga = read();
					
					System.out.print ("Digite tipo de credito: ");
					String tmpTipoCredito = read();
										
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
					
					String tmpSistemaOrigem = "VRJ";
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
						System.out.println ("Metodo remoto executaRecargaBanco executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto executaRecargaBanco retornou o erro: " + ret );
					}
					
					break;
				}
				
				case 11:
				{
					System.out.print ("Digite o codigo do assinante: ");
					String tmpMSISDN = read();

					System.out.print ("Digite o valor: ");
					String tmp_Valor = read();
					double tmpValor = (new Double(tmp_Valor)).doubleValue();
	
					System.out.print ("Digite o tipo da Transacacao: ");
					String tipTransacao = read();

					System.out.print ("Digite a origem da recarga: ");
					String origem = read();
					
					short ret = 0;
	
					try
					{
						ret = pPOA.consultaPreRecarga(tmpMSISDN, tmpValor, tipTransacao, origem);
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					if (  ret == 0 )
					{
						System.out.println ("Metodo remoto podeRecarregar executado com sucesso...");
					}
					else
					{
						System.out.println ("Metodo remoto podeRecarregar retornou o erro:"+ret );
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
		System.out.println ("+  Sistema de Teste de Recarga	     +");
		System.out.println ("+-----------------------------------+\n");
		System.out.println ("1 - Insere recarga generica");
		System.out.println ("2 - Insere ajuste com descricao");
		System.out.println ("3 - Valida recarga via banco");
		System.out.println ("4 - Insere recarga via banco");	
		System.out.println ("5 - Realiza ajuste de um unico saldo");	
		System.out.println ("6 - Insere recarga via XML");
		System.out.println ("7 - Realiza ajuste via XML");
		System.out.println ("8 - Realiza ajuste de multiplos saldos");
		System.out.println ("9 - Valida recarga via Varejo");
		System.out.println ("10- Insere recarga via Varejo");
		System.out.println ("11- Consulta pre-recarga banco(Nova)");

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
