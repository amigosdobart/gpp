// Cliente de Consulta de Assinante
package clientes;

import com.brt.gpp.componentes.consulta.orb.*;

import java.io.*;

public class ClienteConsultaAssinanteGPP
{
	public ClienteConsultaAssinanteGPP ( )
	{
		System.out.println ("Consultanto dados de assinante...");
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
					System.out.print ("Digite o codigo do Assinante a ser consultado: ");
					String tmpMSISDN = read();
					
					String ret = null;
					
					try
					{
						ret = pPOA.consultaAssinante(tmpMSISDN);
						if (  ret != null )
						{
							System.out.println ("Metodo remoto consultaAssinante executado com sucesso...");
							System.out.println(ret);
						}
						else
						{
							System.out.println ("Metodo remoto consultaAssinante não executado com sucesso: " + ret);
						}						
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}					
					break;
				}
				case 2:
				{
					System.out.print ("Digite o codigo do Assinante a ser consultado (Simples): ");
					String tmpMSISDN = read();
					
					String ret = null;
					
					try
					{
						ret = pPOA.consultaAssinanteSimples(tmpMSISDN);
						if (  ret != null )
						{
							System.out.println ("Metodo remoto consultaAssinanteSimples executado com sucesso...");
							System.out.println(ret);
						}
						else
						{
							System.out.println ("Metodo remoto consultaAssinanteSimples executado com erro: " + ret);
						}						
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}					
					break;
				}
				case 3:
				{
					/*System.out.print ("Digite o codigo do Assinante a ser consultado: ");
					String tmpMSISDN = read();
					
					System.out.print ("Digite o valor total de recarga a ser comprada: ");
					String tmpValorTotalRecarga = read();
					
					System.out.print ("Digite o CPF do cliente: ");
					String tmpCPF = read();

					System.out.print ("Digite a categoria do cliente (0-Pos-pago, 1-Hibrido, 2-Pre-pago, 9-Null): ");
					String tmpCategoria = read();
					
					System.out.print ("Digite o Hash Code do Cartao de Credito a ser utilizado: ");
					String tmpHashCC = read();

					System.out.print ("Digite o Sistema de Origem: ");
					String tmpSO = read();
				*/
					System.out.print ("Digite o xml para consulta pré-recarga única: ");
					String aXML = read();
					
					/*String aXML = "<?xml version=\"1.0\"?>";
					aXML = aXML + "<GPPConsultaPreRecarga>";
					aXML = aXML + "<cpfCnpj>1122334455</cpfCnpj>";
					aXML = aXML + "<categoria>1</categoria>";
					aXML = aXML + "<hashCc>6677889900</hashCc>";
					aXML = aXML + "<sistemaOrigem>CLY</sistemaOrigem>";
					aXML = aXML + "<recargas>";
					aXML = aXML + "<recarga>";
					aXML = aXML + "<msisdn>551181395750</msisdn>";
					aXML = aXML + "<valor>10</valor>";
					aXML = aXML + "</recarga>";
					aXML = aXML + "</recargas>";
					aXML = aXML + "</GPPConsultaPreRecarga>";	
*/
					String ret = null;

					try
					{
						ret = pPOA.consultaAssinanteRecargaXML(aXML);
						if (  ret.startsWith("<GPPRetornoConsultaPreRecarga>") )
						{
							System.out.println ("Metodo remoto consultaAssinanteRecarga executado com sucesso...");
							System.out.println(ret);
						}
						else
						{
							System.out.println ("Metodo remoto consultaAssinante executado com erro. Retornou o erro: " + ret );
						}							
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					break;
				}
				
				case 4:
				{
					/*System.out.print ("Digite o codigo do Assinante a ser consultado: ");
					String tmpMSISDN = read();
					
					System.out.print ("Digite o valor total de recarga a ser comprada: ");
					String tmpValorTotalRecarga = read();
					
					System.out.print ("Digite o CPF do cliente: ");
					String tmpCPF = read();

					System.out.print ("Digite a categoria do cliente (0-Pos-pago, 1-Hibrido, 2-Pre-pago, 9-Null): ");
					String tmpCategoria = read();
					
					System.out.print ("Digite o Hash Code do Cartao de Credito a ser utilizado: ");
					String tmpHashCC = read();

					System.out.print ("Digite o Sistema de Origem: ");
					String tmpSO = read();

					String aXML = "<?xml version=\"1.0\"?>";
					aXML = aXML + "<GPPConsultaPreRecarga>";
					aXML = aXML + "<cpfCnpj>1122334455</cpfCnpj>";
					aXML = aXML + "<categoria>1</categoria>";
					aXML = aXML + "<hashCc>6677889900</hashCc>";
					aXML = aXML + "<sistemaOrigem>CLY</sistemaOrigem>";
					aXML = aXML + "<recargas>";
					aXML = aXML + "<recarga>";
					aXML = aXML + "<msisdn>551181395750</msisdn>";
					aXML = aXML + "<valor>10</valor>";
					aXML = aXML + "</recarga>";
					aXML = aXML + "</recargas>";
					aXML = aXML + "</GPPConsultaPreRecarga>";	
*/
					System.out.print ("Digite o xml para consulta pré-recarga múltipla: ");
					String aXML = read();

					String ret = null;

					try
					{
						ret = pPOA.consultaAssinanteRecargaMultiplaXML(aXML);
						if (  ret.startsWith("<GPPRetornoConsultaPreRecarga>") )
						{
							System.out.println ("Metodo remoto consultaAssinanteRecarga executado com sucesso...");
							System.out.println(ret);
						}
						else
						{
							System.out.println ("Metodo remoto consultaAssinante executado com erro. Retornou o erro: " + ret );
						}							
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}
					break;
				}				
				case 5:
				{
					System.out.print ("Digite o codigo do Assinante a ser consultado: ");
					String tmpMSISDN = read();
					
					System.out.print ("Digite o valor total de recarga a ser comprada: ");
					String tmpValorTotalRecarga = read();
										
					System.out.print ("Digite o CPF do cliente: ");
					String tmpCPF = read();
					
					System.out.print ("Digite a categoria do cliente (0-Pos-pago, 1-Hibrido, 2-Pre-pago, 9-Null): ");
					String tmpCategoria = read();
										
					System.out.print ("Digite o Hash Code do Cartao de Credito a ser utilizado: ");
					String tmpHashCC = read();
					
					System.out.print ("Digite o Sistema de Origem: ");
					String tmpSO = read();	
					
					String ret = null;
					
					try
					{
						if (! ( tmpCategoria.equals("0") || tmpCategoria.equals("1") || tmpCategoria.equals("2") ||
								tmpCategoria.equals("3") || tmpCategoria.equals("4") || tmpCategoria.equals("5") || 
								tmpCategoria.equals("6") || tmpCategoria.equals("7") || tmpCategoria.equals("8") ||
								tmpCategoria.equals("9") ) )
						{
							System.out.println ("ERRO: Categoria diferente de numero de 0 a 9");
						}
						ret = pPOA.consultaAssinanteRecarga(tmpMSISDN, Short.parseShort(tmpValorTotalRecarga), tmpCPF, Short.parseShort(tmpCategoria), tmpHashCC, tmpSO);
						if (  ret != null )
						{
							System.out.println ("Metodo remoto consultaAssinanteRecarga executado com sucesso...");
							System.out.println(ret);
						}
						else
						{
							System.out.println ("Metodo remoto consultaAssinante executado com sucesso. Retornou o erro: " + ret );
						}							
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}							
					break;
				}
				case 6:
				{
					System.out.print ("Digite inicio do Período (dd/mm/aaaa): ");
					String tmpInicioPeriodo = read();
	
					System.out.print ("Digite final do Período (dd/mm/aaaa): ");
					String tmpFinalPeriodo = read();
						
					System.out.print ("Digite o MSISDN: ");
					String tmpMSISDN = read();
	
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
					break;
				}
				case 7:
				{
					System.out.print ("Digite o numero do Assinante que tera aparelho consultado: ");
					String tmpMSISDN = read();
					
					String ret = null;
					
					try
					{
						ret = pPOA.consultaAparelhoAssinante(tmpMSISDN);
						if (  ret != null )
						{
							System.out.println ("Metodo remoto consultaAparelhoAssinante executado com sucesso...");
							System.out.println(ret);
						}
						else
						{
							System.out.println ("Metodo remoto consultaAparelhoAssinante não executado com sucesso: " + ret);
						}						
					}
					catch (Exception e) 
					{
						System.out.println("Erro:" + e);
					}					
					break;
				}
				case 8:
				{
					System.out.print("Digite o numero do Assinante que tera aparelho consultado: ");
					String tmpMSISDN = read();
					String ret = null;
					try
					{
						ret = pPOA.consultaAssinanteTFPP(tmpMSISDN);
						if(ret != null)
						{
							System.out.println("Metodo remoto consultaAssinanteTFPP executado com sucesso...");
							System.out.println(ret);
						}
						else
						{
							System.out.println ("Metodo remoto consultaAssinante TFPP nao executado com sucesso: " + ret);
						}
					}
					catch(Exception e)
					{
						System.out.println("Erro: " + e);
					}
					break;
				}
				case 9:
				{
					System.out.print ("Digite o codigo do Assinante: ");
					String msisdn = read();
					System.out.print ("Digite o mes de recebimento do bonus (YYYYMM): ");
					String mes = read();
					
					String result = null;
					
					try
					{
					    result = pPOA.consultaPulaPulaNoMes(msisdn, mes);
					    if(result != null)
					    {
					        System.out.println(result);
					        System.out.println("\nMetodo remoto consultaPulaPula executado com sucesso...");
					    }
					}
					catch(Exception e)
					{
					    System.out.println("Excecao: " + e);
					}
					
					break;
				}
				case 10:
				{
					System.out.print ("Digite o codigo do Assinante: ");
					String msisdn = read();
					System.out.print ("Digite a data inicial da consulta (dd/mm/yyyy): ");
					String dataInicio = read();
					System.out.print ("Digite a data final da consulta (dd/mm/yyyy): ");
					String dataFim = read();
					
					String result = null;
					
					try
					{
					    result = pPOA.consultaEstornoPulaPula(msisdn, dataInicio, dataFim);
					    if(result != null)
					    {
					        System.out.println(result);
					        System.out.println("\nMetodo remoto consultaEstornoPulaPula executado com sucesso...");
					    }
					}
					catch(Exception e)
					{
					    System.out.println("Excecao: " + e);
					}
					
					break;
				}
				default: break;
			}
		}
	}
	
	public static int menuOpcoes ( )
	{
		int userOption;
		System.out.println ("\n\n");
		System.out.println ("+----------------------------------------------+");
		System.out.println ("+  Sistema de Teste de Consulta de Assinantes  +");
		System.out.println ("+----------------------------------------------+\n");
		System.out.println ("01 - Consulta Assinante");
		System.out.println ("02 - Consulta Assinante (Simples)");
		System.out.println ("03 - Consulta Pré-Recarga Única para Assinante (XML)");
		System.out.println ("04 - Consulta Pré-Recarga Múltipla para Assinante (XML)");
		System.out.println ("05 - Consulta Recarga para Assinante");
		System.out.println ("06 - Consulta Extrato para Assinante");
		System.out.println ("07 - Consulta Aparelho Assinante");
		System.out.println ("08 - Consulta Terminal Fixo Pre-Pago");
		System.out.println ("09 - Consulta Promocao Pula-Pula do Assinante");
		System.out.println ("10 - Consulta Estornos Pula-Pula do Assinante");
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