// Cliente de Aprovisionamento.java
package clientes;

import com.brt.gpp.componentes.gerenteGPP.orb.*;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;

import java.io.*;

public class ClienteGerenteGPP
{
	public ClienteGerenteGPP()
	{
		System.out.println ("Ativando cliente de gerenteGPP...");
	}
	
	public static void main(String[] args) 
	{
		try
		{
			java.util.Properties props = System.getProperties();
			props.put("vbroker.agent.port", args[0]);
			props.put("vbroker.agent.addr", args[1]);
			System.setProperties ( props );	
			
			// Inicializando o ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
			
			byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();
			
			gerenteGPP pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);
	
			int userOption = 0;
			boolean exit = true;
			boolean exit1 = true;
			
			int opcaoNumerodeConexao = 0;
			int opcaoNumerodeConexaoDisponiveis = 0;
			int opcaoListaProcessosComConexoesEmUso = 0;
			int opcaoCriaConexao = 0;		
			int opcaoRemoveConexao = 0;
			int opcaoPlanoPreco = 0;
			int opcaoStatusAssinante = 0;
			int opcaoStatusServico = 0;
			int opcaoSistemaOrigem = 0;
			int opcaoTarifaTrocaMSISDN = 0;
			int opcaoConfiguracaoGPP = 0;
			int opcaoValoresRecarga = 0;
			int opcaoMotivosBloqueio = 0;
			int opcaoRecOrigem = 0;
			int opcaoEnvioSMS = 0;
			int opcaoLogDebug = 0;
			int opcaoImportacaoCDR = 0;
			int opcaoMenuMapeamentos = 0;
			
			while(exit)
			{
				userOption = menuOpcoes ();			
				exit1 = true;			
				while(exit1)
				{
					switch(userOption)
					{
						// Opcao de saida do menu principal
						case 0:
						{
							exit = false;
							exit1 = false;
							break;
						}
						// opcao para menu de consulta de numero de conexoes		
						case 1:
						{
							opcaoNumerodeConexao = menuNumerodeConexao();						
							System.out.println ("");							
							switch(opcaoNumerodeConexao)
							{
								// opcao de sair do do menu de consulta de numero de conexoes
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de consulta de numero de conexoes de aprovisionamento
								case 1:
								{
									short ret = pPOA.getNumerodeConexoes( (short)Definicoes.CO_TIPO_TECNOMEN_APR );
									System.out.print ("Numero de Conexoes ativas Tecnomen Aprovisionamento: " + ret );
									break;
								}
								// opcao de consulta de numero de conexoes de recarga
								case 2:
								{
									short ret = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_TECNOMEN_REC );
									System.out.print ("Numero de Conexoes ativas Tecnomen Recarga: " + ret );
									break;
								}											
								// opcao de consulta de numero de conexoes de voucher
								case 3:
								{
									short ret = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_TECNOMEN_VOU );
									System.out.print ("Numero de Conexoes ativas Tecnomen Voucher: " + ret );
									break;
								}											
								// opcao de consulta de numero de conexoes admin
								case 4:
								{
									short ret = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_TECNOMEN_ADM );
									System.out.print ("Numero de Conexoes ativas Tecnomen Admin: " + ret );
									break;
								}											
								// opcao de consulta de numero de conexoes agent
								case 5:
								{
									short ret = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_TECNOMEN_AGE );
									System.out.print ("Numero de Conexoes ativas Tecnomen Agent: " + ret );
									break;
								}
								// opcao de consulta de numero de conexoes PREP
								case 6:
								{
									short ret = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_BANCO_DADOS_PREP );
									System.out.print ("Numero de Conexoes ativas PREPConexao: " + ret );
									break;
								}				
							}
							System.out.println ("");
							System.out.println ("");
							break;
						}
						// opcao para menu de criacao de conexoes		
						case 2:
						{
							opcaoCriaConexao = menuCriaConexao();						
							System.out.println("");							
							switch(opcaoCriaConexao)
							{
								// opcao de sair do do menu de criacao de conexoes
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de criar conexao de aprovisionamento
								case 1:
								{
									if(pPOA.criaConexao((short)Definicoes.CO_TIPO_TECNOMEN_APR))
										System.out.print("Conexao Tecnomen Aprovisionamento Criada.");
									break;
								}
								// opcao de criar conexao de recarga
								case 2:
								{
									if(pPOA.criaConexao((short) Definicoes.CO_TIPO_TECNOMEN_REC ))
										System.out.print("Conexao Tecnomen Recarga Criada.");
									break;
								}											
								// opcao de criar conexao de voucher
								case 3:
								{
									if(pPOA.criaConexao((short)Definicoes.CO_TIPO_TECNOMEN_VOU))
										System.out.print("Conexao Tecnomen Voucher Criada.");
									break;
								}											
								// opcao de criar conexao admin
								case 4:
								{
									if(pPOA.criaConexao((short) Definicoes.CO_TIPO_TECNOMEN_ADM ))
										System.out.print("Conexao Tecnomen Voucher Admin Criada.");
									break;
								}											
								// opcao de criar conexao agent
								case 5:
								{
									if(pPOA.criaConexao((short) Definicoes.CO_TIPO_TECNOMEN_AGE ))
										System.out.print("Conexao Tecnomen Voucher Agent Criada.");
									break;
								}
								// opcao de criar conexao PREP
								case 6:
								{
									if(pPOA.criaConexao((short) Definicoes.CO_TIPO_BANCO_DADOS_PREP))
										System.out.print("Conexao PREP Criada.");
									break;
								}				
							}
							System.out.println("");
							System.out.println("");
							break;
						}
						// opcao para menu de remocao de conexoes		
						case 3:
						{
							opcaoRemoveConexao = menuRemoveConexao();						
							System.out.println("");							
							switch(opcaoRemoveConexao)
							{
								// opcao de sair do do menu de remocao de conexoes
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de remover conexao de aprovisionamento
								case 1:
								{
									if(pPOA.removeConexao((short)Definicoes.CO_TIPO_TECNOMEN_APR))
										System.out.print ("Conexao Tecnomen Aprovisionamento Removida." );
									break;
								}
								// opcao de remover conexao de recarga
								case 2:
								{
									if(pPOA.removeConexao((short) Definicoes.CO_TIPO_TECNOMEN_REC))
										System.out.print("Conexao Tecnomen Recarga Removida.");
									break;
								}											
								// opcao de remover conexao de voucher
								case 3:
								{
									if(pPOA.removeConexao((short) Definicoes.CO_TIPO_TECNOMEN_VOU ))
										System.out.print("Conexao Tecnomen Voucher Removida.");
									break;
								}											
								// opcao de remover conexao admin
								case 4:
								{
									if ( pPOA.removeConexao( (short) Definicoes.CO_TIPO_TECNOMEN_ADM ) )
										System.out.print ("Conexao Tecnomen Voucher Admin Removida." );
									break;
								}											
								// opcao de remover conexao agent
								case 5:
								{
									if ( pPOA.removeConexao( (short) Definicoes.CO_TIPO_TECNOMEN_AGE ) )
										System.out.print ("Conexao Tecnomen Voucher Agent Removida." );
									break;
								}
								// opcao de remover conexao PREP
								case 6:
								{
									if ( pPOA.removeConexao( (short) Definicoes.CO_TIPO_BANCO_DADOS_PREP ) )
										System.out.print ("Conexao PREP Removida." );
									break;
								}				
							}
							System.out.println ("");
							System.out.println ("");
							break;
						}

						// opcao para menu de Atualização de Mapeamentos		
						case 4:
						{
							boolean retorno = false;
							String msgRetorno = null;
							System.out.println ("Atualizando Tabelas ...");

							retorno = pPOA.atualizaListaConfiguracaoGPP();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização das Configurações do GPP \n";
							
							retorno = pPOA.atualizaListaMotivosBloqueio();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos Motivos de Bloqueio \n";

							retorno = pPOA.atualizaListaRecOrigem();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização da Lista de Transaction Types \n";

							retorno = pPOA.atualizaListaSistemaOrigem();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos Sistemas de Origem \n";

							retorno = pPOA.atualizaListaStatusAssinante();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos Status de Assinante \n";

							retorno = pPOA.atualizaListaStatusServico();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos Status de Serviço \n";

							retorno = pPOA.atualizaListaTarifaTrocaMSISDN();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização das Tarifas de Troca de MSISDN \n";

							retorno = pPOA.atualizaListaValoresRecarga();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos Valores de Recarga \n";
								
							retorno = pPOA.atualizaListaValoresRecargaPlanoPreco();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos Valores de Recarga em Funcao dos Planos de Preco \n";
							
							retorno = pPOA.atualizaListaBonusPulaPula();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos valores de Bonus Pula-Pula \n";
							
							retorno = pPOA.atualizaListaPromocao();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização das Promocoes \n";
							
							retorno = pPOA.atualizaListaPromocaoDiaExecucao();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos dias de execucao das Promocoes \n";
							
							retorno = pPOA.atualizaListaModulacaoPlano();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização das modulacoes por plano \n";

							retorno = pPOA.atualizaListaAssinantesNaoBonificaveis();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos assinantes nao bonificaveis \n";

							retorno = pPOA.atualizaListaFeriados();
							if(!retorno)
								msgRetorno = msgRetorno + "Erro na Atualização dos feriados \n";

							if(msgRetorno == null)
								System.out.println("Mapeamentos Atualizados com Sucesso");
							else
								System.out.println("Erros: \n" + msgRetorno);

							System.out.println("");
							exit1 = false;
							break;
						}

						// opcao para menu de de mapeamento plano de preco		
						case 5:
						{
							opcaoPlanoPreco = menuPlanoPreco();
							System.out.println ("");
							switch ( opcaoPlanoPreco )
							{
								// opcao de sair do menu de mapeamento de plano de preco
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de plano de preco
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaPlanoPreco();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de plano de preco
								case 2:
								{									
									if (pPOA.atualizaListaPlanoPreco()) 
										System.out.print ("A lista de plano de preco foi atualizada.");
									else	
										System.out.print ("A lista de plano de preco nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de mapeamento de Status de Assinante		
						case 6:
						{
							opcaoStatusAssinante = menuStatusAssinante();
							System.out.println ("");
							switch ( opcaoStatusAssinante )
							{
								// opcao de sair do menu de mapeamento de Status de Assinante		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de Status de Assinante
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaStatusAssinante();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de Status de Assinante
								case 2:
								{									
									if (pPOA.atualizaListaStatusAssinante()) 
										System.out.print ("A lista de Status de Assinante foi atualizada.");
									else	
										System.out.print ("A lista de Status de Assinante nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de mapeamento de Status de Servico		
						case 7:
						{
							opcaoStatusServico = menuStatusServico();
							System.out.println ("");
							switch ( opcaoStatusServico )
							{
								// opcao de sair do menu de mapeamento de Status de Servico		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de Status de Servico
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaStatusServico();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de Status de Servico
								case 2:
								{									
									if (pPOA.atualizaListaStatusServico()) 
										System.out.print ("A lista de Status de Servico foi atualizada.");
									else	
										System.out.print ("A lista de Status de Servico nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de mapeamento de Sistema de Origem		
						case 8:
						{
							opcaoSistemaOrigem = menuSistemaOrigem();
							System.out.println ("");
							switch ( opcaoSistemaOrigem )
							{
								// opcao de sair do menu de mapeamento de Sistema de Origem		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de Sistema de Origem
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaSistemaOrigem();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de Sistema de Origem
								case 2:
								{									
									if (pPOA.atualizaListaSistemaOrigem()) 
										System.out.print ("A lista de Sistema de Origem foi atualizada.");
									else	
										System.out.print ("A lista de Sistema de Origem nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de mapeamento de Tarifa de Troca de MSISDN		
						case 9:
						{
							opcaoTarifaTrocaMSISDN = menuTarifaTrocaMSISDN();
							System.out.println ("");
							switch ( opcaoTarifaTrocaMSISDN )
							{
								// opcao de sair do menu de mapeamento de Tarifa de Troca de MSISDN		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de Tarifa de Troca de MSISDN
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaTarifaTrocaMSISDN();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de Tarifa de Troca de MSISDN
								case 2:
								{									
									if (pPOA.atualizaListaTarifaTrocaMSISDN()) 
										System.out.print ("A lista de Tarifa de Troca de MSISDN foi atualizada.");
									else	
										System.out.print ("A lista de Tarifa de Troca de MSISDN nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de mapeamento de Configuracao GPP		
						case 10:
						{
							opcaoConfiguracaoGPP = menuConfiguracaoGPP();
							System.out.println ("");
							switch ( opcaoConfiguracaoGPP )
							{
								// opcao de sair do menu de mapeamento de Configuracao GPP		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de Configuracao GPP
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaConfiguracaoGPP();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de Configuracao GPP
								case 2:
								{									
									if (pPOA.atualizaListaConfiguracaoGPP()) 
										System.out.print ("A lista de Configuracao GPP foi atualizada.");
									else	
										System.out.print ("A lista de Configuracao GPP nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de mapeamento de Valores de Recarga		
						case 11:
						{
							opcaoValoresRecarga = menuValoresRecarga();
							System.out.println ("");
							switch ( opcaoValoresRecarga )
							{
								// opcao de sair do menu de mapeamento de Valores de Recarga		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de Valores de Recarga
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaValoresRecarga();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de Valores de Recarga
								case 2:
								{									
									if (pPOA.atualizaListaValoresRecarga()) 
										System.out.print ("A lista de Valores de Recarga foi atualizada.");
									else	
										System.out.print ("A lista de Valores de Recarga nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de mapeamento dos TTs		
						case 12:
						{
							opcaoRecOrigem = menuRecOrigem();
							System.out.println ("");
							switch ( opcaoRecOrigem )
							{
								// opcao de sair do menu de mapeamento de TTs		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de TTs
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaRecOrigem();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de TTs
								case 2:
								{									
									if (pPOA.atualizaListaRecOrigem()) 
										System.out.print ("A lista de TTs foi atualizada.");
									else	
										System.out.print ("A lista de TTs nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de mapeamento dos Motivos de Bloqueio		
						case 13:
						{
							opcaoMotivosBloqueio = menuMotivosBloqueio();
							System.out.println ("");
							switch ( opcaoMotivosBloqueio )
							{
								// opcao de sair do menu de mapeamento de Motivos de bloqueio		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de exibir mapeamento de Motivos de Bloqueio
								case 1:
								{
									System.out.println ("Montando a lista...");
									System.out.println ("");
									String list =  pPOA.exibeListaMotivosBloqueio();
									System.out.println (list);
									break;
								}
								// opcao de atualizar mapeamento de Motivos Bloqueio
								case 2:
								{									
									if (pPOA.atualizaListaMotivosBloqueio()) 
										System.out.print ("A lista de Motivos Bloqueio foi atualizada.");
									else	
										System.out.print ("A lista de Motivos Bloqueio nao foi atualizada.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de envio de SMS		
						case 14:
						{
							opcaoEnvioSMS = menuEnvioSMS();
							System.out.println ("");
							switch ( opcaoEnvioSMS )
							{
								// opcao de sair do menu de envio de SMS		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de ativar envio de SMS
								case 1:
								{
									if (pPOA.processaSMS(true))
										System.out.println ("Envio de SMS ativado.");
									break;
								}
								// opcao de desativar envio de SMS
								case 2:
								{
									if (pPOA.processaSMS(false))
										System.out.println ("Envio de SMS desativado.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						// opcao para menu de log de DEBUG		
						case 15:
						{
							opcaoLogDebug = menuLogDebug();
							System.out.println ("");
							switch ( opcaoLogDebug )
							{
								// opcao de sair do menu de log de Debug		
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de ativar Debug no log
								case 1:
								{
									if (pPOA.escreveDebug(true))
										System.out.println ("DEBUG ativado.");
									break;
								}
								// opcao de desativar Debug no log
								case 2:
								{
									if (pPOA.escreveDebug(false))
										System.out.println ("DEBUG desativado.");
									break;
								}
							}
							System.out.println ("");							
							break;
						}
						//
						case 16:
						{
							opcaoNumerodeConexaoDisponiveis = menuNumerodeConexaoDisponiveis();						
							System.out.println ("");							
							switch ( opcaoNumerodeConexaoDisponiveis )
							{
								// opcao de sair do do menu de consulta de numero de conexoes disponiveis
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de consulta de numero de conexoes disponiveis de aprovisionamento
								case 1:
								{
									short ret = pPOA.getNumeroConexoesDisponiveis( (short)Definicoes.CO_TIPO_TECNOMEN_APR );
									System.out.print ("Numero de Conexoes disponiveis Tecnomen Aprovisionamento: " + ret );
									break;
								}
								// opcao de consulta de numero de conexoes disponiveis de recarga
								case 2:
								{
									short ret = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_TECNOMEN_REC );
									System.out.print ("Numero de Conexoes disponiveis Tecnomen Recarga: " + ret );
									break;
								}											
								// opcao de consulta de numero de conexoes disponiveis de voucher
								case 3:
								{
									short ret = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_TECNOMEN_VOU );
									System.out.print ("Numero de Conexoes disponiveis Tecnomen Voucher: " + ret );
									break;
								}											
								// opcao de consulta de numero de conexoes disponiveis admin
								case 4:
								{
									short ret = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_TECNOMEN_ADM );
									System.out.print ("Numero de Conexoes disponiveis Tecnomen Admin: " + ret );
									break;
								}											
								// opcao de consulta de numero de conexoes disponiveis agent
								case 5:
								{
									short ret = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_TECNOMEN_AGE );
									System.out.print ("Numero de Conexoes disponiveis Tecnomen Agent: " + ret );
									break;
								}
								// opcao de consulta de numero de conexoes disponiveis PREP
								case 6:
								{
									short ret = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_BANCO_DADOS_PREP );
									System.out.print ("Numero de Conexoes disponiveis PREPConexao: " + ret );
									break;
								}				
							}
							System.out.println ("");
							System.out.println ("");
							break;						
						}
						//Opcao para consulta de processos que possuem conexoes em uso
						case 17:
						{
							opcaoListaProcessosComConexoesEmUso = menuListaProcessosComConexoesEmUso();						
							System.out.println ("");
							IdProcessoConexao[] listaProcessos = null;							
							switch ( opcaoListaProcessosComConexoesEmUso )
							{
								// opcao de sair do do menu de consulta de numero de conexoes disponiveis
								case 0:
								{
									exit1 = false;
									break;
								}
								// opcao de consulta de numero de conexoes disponiveis de aprovisionamento
								case 1:
								{
									listaProcessos = pPOA.getListaProcessosComConexoesEmUso( (short)Definicoes.CO_TIPO_TECNOMEN_APR );
									break;
								}
								// opcao de consulta de numero de conexoes disponiveis de recarga
								case 2:
								{
									listaProcessos = pPOA.getListaProcessosComConexoesEmUso( (short) Definicoes.CO_TIPO_TECNOMEN_REC );
									break;
								}											
								// opcao de consulta de numero de conexoes disponiveis de voucher
								case 3:
								{
									listaProcessos = pPOA.getListaProcessosComConexoesEmUso( (short) Definicoes.CO_TIPO_TECNOMEN_VOU );
									break;
								}											
								// opcao de consulta de numero de conexoes disponiveis admin
								case 4:
								{
									listaProcessos = pPOA.getListaProcessosComConexoesEmUso( (short) Definicoes.CO_TIPO_TECNOMEN_ADM );
									break;
								}											
								// opcao de consulta de numero de conexoes disponiveis agent
								case 5:
								{
									listaProcessos = pPOA.getListaProcessosComConexoesEmUso( (short) Definicoes.CO_TIPO_TECNOMEN_AGE );
									break;
								}
								// opcao de consulta de numero de conexoes disponiveis PREP
								case 6:
								{
									listaProcessos = pPOA.getListaProcessosComConexoesEmUso( (short) Definicoes.CO_TIPO_BANCO_DADOS_PREP );
									break;
								}				
							}
							for (int i=0; i < listaProcessos.length; i++)
								System.out.println("IdProcesso:" + listaProcessos[i].idProcesso + " Data Inicial Uso:" + listaProcessos[i].dataInicialUso);

							System.out.println ("");
							System.out.println ("");
							break;						
						}

						// opcao para ping do GPP		
						case 18:
						{
							System.out.println ("");
							String sPing =  pPOA.ping();
							System.out.println (sPing);
							System.out.println ("");	
							exit1 = false;						
							break;
						}
						// opcao para parar o GPP		
						case 19:
						{
							System.out.println ("");
							System.out.println ("Finalizando GPP ...");
							System.out.println ("");
							exit1 = false;
							exit = false;
							pPOA.finalizaGPP();
							break;
						}
						// opcao para Importacao de CDRs	
						case 20:
						{
							opcaoImportacaoCDR = menuImportacaoCDR();
							int valorResposta = 0;
							System.out.println ("");
							switch ( opcaoImportacaoCDR )
							{
								// opcao de sair do do menu de consulta de numero de conexoes disponiveis
								case 0:
								{
									exit1 = false;
									break;
								}
								case 1:
								{
									valorResposta = pPOA.getNumThreadsImpCDRDadosVoz();
									System.out.println("Numero de Threads DadosVoz:"+valorResposta);
									break;
								}
								case 2:
								{
									valorResposta = pPOA.getNumThreadsImpCDREvtRec();
									System.out.println("Numero de Threads EvtRec:"+valorResposta);
									break;
								}
								case 3:
								{
									valorResposta = pPOA.getNumArqPendentesDadosVoz();
									System.out.println("Numero de Arquivos Pendentes DadosVoz:"+valorResposta);
									break;
								}
								case 4:
								{
									valorResposta = pPOA.getNumArqPendentesEvtRec();
									System.out.println("Numero de Arquivos Pendentes EvtRec:"+valorResposta);
									break;
								}
								case 5:
								{
									pPOA.removeThreadsDadosVoz();
									System.out.println("Pool de threads de importacao Dados-Voz foi removido");
									break;
								}
								case 6:
								{
									pPOA.removeThreadsEvtRec();
									System.out.println("Pool de threads de importacao Eventos-Recarga foi removido");
									break;
								}
								case 7:
								{
									pPOA.inicializaThreadsDadosVoz();
									System.out.println("Pool de threads de importacao Dados-Voz foi inicializado");
									break;
								}
								case 8:
								{
									pPOA.inicializaThreadsEvtRec();
									System.out.println("Pool de threads de importacao Eventos-Recarga foi inicializado");
									break;
								}
							}
							break;
						}
						case 21:
						{
						    opcaoMenuMapeamentos = menuMapeamentos();
						    
						    switch(opcaoMenuMapeamentos)
						    {
						    	case 0:
						    	{
									exit1 = false;
									break;
						    	}
						    	case 1:
						    	{
						    	    String limpar = null;
						    	    while(limpar == null)
						    	    {
						    	        System.out.print("Limpar o mapeamento antes da atualizacao (s/n)? ");
						    	        limpar = read();
						    	        if(!limpar.equalsIgnoreCase("s") && (!limpar.equalsIgnoreCase("n")))
						    	        {
						    	            limpar = null;
						    	        }
						    	    }
									System.out.println("Atualizando tabelas...");
								    int result = pPOA.atualizaMapeamentos((limpar.equalsIgnoreCase("s")));
								    if(result == 0)
								    {
										System.out.println("Mapeamentos atualizados com sucesso");
								    }
								    else
								    {
										System.out.println("Mapeamentos atualizados com erro. Codigo de retorno: " + result);
								    }
									System.out.println("");
								    break;
						    	}
						    	case 2:
						    	{
						    	    System.out.print("Nome da classe (sem pacotes): ");
						    	    String nome = read();
						    	    String limpar = null;
						    	    while(limpar == null)
						    	    {
						    	        System.out.print("Limpar o mapeamento antes da atualizacao (s/n)? ");
						    	        limpar = read();
						    	        if(!limpar.equalsIgnoreCase("s") && (!limpar.equalsIgnoreCase("n")))
						    	        {
						    	            limpar = null;
						    	        }
						    	    }
						    	    int result = pPOA.atualizaMapeamento("com.brt.gpp.comum.mapeamentos." + nome, limpar.equalsIgnoreCase("s"));
								    if(result == 0)
								    {
										System.out.println("Mapeamento " + nome + " atualizado com sucesso");
								    }
								    else
								    {
										System.out.println("Mapeamento " + nome + " atualizado com erro. Codigo de retorno: " + result);
								    }
									System.out.println("");
								    break;
						    	}
						    	case 3:
						    	{
						    	    System.out.print("Nome da classe (sem pacotes): ");
						    	    String nome = read();
						    	    String result = pPOA.exibeMapeamento("com.brt.gpp.comum.mapeamentos." + nome);
									System.out.println(result);
									System.out.println("");
								    break;
						    	}
						    }
						    break;
						}
						case 22:
						{
							long idProcesso = menuLiberarConexoesEmUso();
							pPOA.liberarConexoesEmUso(idProcesso);
							System.out.println("\nConexoes liberadas com sucesso para o processo " + idProcesso);
							exit1 = false;
							break;
						}
						case 23:
						{
							String result = pPOA.exibirNumeroStatementsPorConexao();
							System.out.print(result);
							exit1 = false;
							break;
						}
					}
				}					
			}
		}
		catch (GPPInternalErrorException e)
		{
			System.out.println("Excecao interna GPP ocorrida: "+ e);							 
		}
		catch (Exception e1)
		{
			if ( (e1.toString()).compareTo("org.omg.CORBA.TRANSIENT:   vmcid: 0x0  minor code: 0  completed: No") != 0)
				System.out.println("Excecao ocorrida: "+ e1);							 
		}
	}
	
	public static int menuOpcoes()
	{
		int userOption;
		System.out.println ("");
		System.out.println ("+----------------------------------------+");
		System.out.println ("+  Sistema de Teste de Gerente GPP       +");
		System.out.println ("+----------------------------------------+\n");
		System.out.println ("01 - Numero de Conexoes");
		System.out.println ("02 - Cria Conexao");
		System.out.println ("03 - Remove Conexao");
		System.out.println ("04 - Atualiza Mapeamentos");
		System.out.println ("05 - Menu Plano de Preco");
		System.out.println ("06 - Menu Status Assinante");
		System.out.println ("07 - Menu Status Servico");
		System.out.println ("08 - Menu Sistema de Origem");
		System.out.println ("09 - Menu Tarifa Troca de Senha");
		System.out.println ("10 - Menu Configuracoes GPP");
		System.out.println ("11 - Menu Valores de Recarga");
		System.out.println ("12 - Menu Transaction Types");
		System.out.println ("13 - Menu Motivos de Bloqueio");
		System.out.println ("14 - Menu Envio SMS");
		System.out.println ("15 - Menu de Debug");
		System.out.println ("16 - Numero de Conexoes Disponiveis");
		System.out.println ("17 - Lista de Processos com Conexoes Em Uso");
		System.out.println ("18 - Ping GPP");
		System.out.println ("19 - Finaliza GPP");
		System.out.println ("20 - Importacao de CDRs");
		System.out.println ("21 - Menu Mapeamentos");
		System.out.println ("22 - Liberar Conexoes em Uso");
		System.out.println ("23 - Statements Abertos por Conexao com BD");
		System.out.println ("");
		System.out.println ("0 - Saida");
		System.out.println ("");
		System.out.print ("Opcao: ");
		userOption = Integer.parseInt ( read() );
		
		return userOption;
	}
	
	public static int menuImportacaoCDR ( )
	{
		int opcaoImpCDR;
		System.out.println ("");
		System.out.println ("01 - Numero de threads de importacao Dados-Voz \t");
		System.out.println ("02 - Numero de threads de importacao Eventos-Recarga \t");
		System.out.println ("03 - Numero de arquivos pendentes Dados-Voz \t");
		System.out.println ("04 - Numero de arquivos pendentes Eventos-Recarga \t");
		System.out.println ("05 - Remove threads importacao Dados-Voz \t");
		System.out.println ("06 - Remove threads importacao Eventos-Recarga \t");
		System.out.println ("07 - Inicializa pool de threads importacao Dados-Voz \t");
		System.out.println ("08 - Inicializa pool de threads importacao Eventos-Recarga \t");
		System.out.println ("");		
		System.out.println ("0 - Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");		
		opcaoImpCDR = Integer.parseInt ( read() );
		
		return opcaoImpCDR;
	}
	
	public static int menuNumerodeConexao ( )
	{
		int opcaoNumerodeConexao;
		System.out.println ("");
		System.out.println ("01 - Numero de Conexoes Tecnomen Aprovisionamento \t");
		System.out.println ("02 - Numero de Conexoes Tecnomen Recarga \t");
		System.out.println ("03 - Numero de Conexoes Tecnomen Voucher \t");
		System.out.println ("04 - Numero de Conexoes Tecnomen Admin \t");
		System.out.println ("05 - Numero de Conexoes Tecnomen Agent \t");
		System.out.println ("06 - Numero de Conexoes PREP \t");
		System.out.println ("");		
		System.out.println ("0 - Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");		
		opcaoNumerodeConexao = Integer.parseInt ( read() );
		
		return opcaoNumerodeConexao;
	}
	
	public static int menuCriaConexao ( )
	{
		int opcaoCriaConexao;
		System.out.println ("");
		System.out.println ("01 - Cria Conexao Tecnomen Aprovisionamento \t");
		System.out.println ("02 - Cria Conexao Tecnomen Recarga \t");
		System.out.println ("03 - Cria Conexao Tecnomen Voucher \t");
		System.out.println ("04 - Cria Conexao Conexoes Tecnomen Admin \t");
		System.out.println ("05 - Cria Conexao Conexoes Tecnomen Agent \t");
		System.out.println ("06 - Cria Conexao Conexoes PREP \t");
		System.out.println ("");		
		System.out.println ("0 - Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");		
		opcaoCriaConexao = Integer.parseInt ( read() );
			
		return opcaoCriaConexao;
	}
	
	public static int menuRemoveConexao ( )
	{
		int opcaoRemoveConexao;
		System.out.println ("");
		System.out.println ("01 - Remove Conexao Tecnomen Aprovisionamento \t");
		System.out.println ("02 - Remove Conexao Tecnomen Recarga \t");
		System.out.println ("03 - Remove Conexao Tecnomen Voucher \t");
		System.out.println ("04 - Remove Conexao Conexoes Tecnomen Admin \t");
		System.out.println ("05 - Remove Conexao Conexoes Tecnomen Agent \t");
		System.out.println ("06 - Remove Conexao Conexoes PREP \t");
		System.out.println ("");		
		System.out.println ("0 - Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");		
		opcaoRemoveConexao = Integer.parseInt ( read() );
			
		return opcaoRemoveConexao;
	}
	
	public static int menuPlanoPreco ( )
	{
		int opcaoPlanoPreco;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de Planos de Preco em memoria \t");
		System.out.println ("02 - Atualizar Planos de Preco em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoPlanoPreco = Integer.parseInt ( read() );
		return opcaoPlanoPreco;
	}
	
	public static int menuStatusAssinante( )
	{
		int opcaoStatusAssinante;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de Status de Assinante em memoria \t");
		System.out.println ("02 - Atualizar Status de Assinante em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoStatusAssinante = Integer.parseInt ( read() );
		return opcaoStatusAssinante;
	}
	
	public static int menuStatusServico( )
	{
		int opcaoStatusServico;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de Status de Servico em memoria \t");
		System.out.println ("02 - Atualizar Status de Servico em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoStatusServico = Integer.parseInt ( read() );
		return opcaoStatusServico;
	}
	
	public static int menuSistemaOrigem( )
	{
		int opcaoSistemaOrigem;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de Sistemas de Origem em memoria \t");
		System.out.println ("02 - Atualizar Sistemas de Origem em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoSistemaOrigem = Integer.parseInt ( read() );
		return opcaoSistemaOrigem;
	}
	
	public static int menuTarifaTrocaMSISDN()
	{
		int opcaoTarifaTrocaMSISDN;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de Tarifa de Troca de MSISDN em memoria \t");
		System.out.println ("02 - Atualizar Tarifa de Troca de MSISDN em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoTarifaTrocaMSISDN = Integer.parseInt(read());
		return opcaoTarifaTrocaMSISDN;
	}
	
	public static int menuConfiguracaoGPP()
	{
		int opcaoConfiguracaoGPP;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de Configuracao GPP em memoria \t");
		System.out.println ("02 - Atualizar Configuracao GPP em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoConfiguracaoGPP = Integer.parseInt(read());
		return opcaoConfiguracaoGPP;
	}	
	
	public static int menuValoresRecarga()
	{
		int opcaoValoresRecarga;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de Valores de Recarga em memoria \t");
		System.out.println ("02 - Atualizar Valores de Recarga em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoValoresRecarga = Integer.parseInt(read());
		return opcaoValoresRecarga;
	}	

	public static int menuRecOrigem()
	{
		int opcaoRecOrigem;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de TTs em memoria \t");
		System.out.println ("02 - Atualizar TTs em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoRecOrigem = Integer.parseInt(read());
		return opcaoRecOrigem;
	}	

	public static int menuMotivosBloqueio()
	{
		int opcaoMotivosBloqueio;
		System.out.println ("");
		System.out.println ("01 - Exbibir lista de Motivos de bloqueio em memoria \t");
		System.out.println ("02 - Atualizar Motivos de Bloqueio em memoria \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoMotivosBloqueio = Integer.parseInt(read());
		return opcaoMotivosBloqueio;
	}	

	public static int menuEnvioSMS()
	{
		int opcaoEnvioSMS;
		System.out.println ("");
		System.out.println ("01 - Ativa processo de envio de SMS \t");
		System.out.println ("02 - Desativa processo de envio de SMS \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoEnvioSMS = Integer.parseInt (read());
		return opcaoEnvioSMS;
	}	
	
	public static int menuLogDebug()
	{
		int opcaoLogDebug;
		System.out.println ("");
		System.out.println ("01 - Ativa mensagens de DEBUG no LOG \t");
		System.out.println ("02 - Desativa mensagens de DEBUG no LOG \t");		
		System.out.println ("");
		System.out.println ("0- Menu Principal \t");
		System.out.println ("");
		System.out.print ("Opcao:");
		opcaoLogDebug = Integer.parseInt(read());
		return opcaoLogDebug;
	}

	public static int menuNumerodeConexaoDisponiveis()
		{
			int opcaoNumerodeConexaoDisponiveis;
			System.out.println ("");
			System.out.println ("01 - Numero de Conexoes Tecnomen Aprovisionamento Disponiveis \t");
			System.out.println ("02 - Numero de Conexoes Tecnomen Recarga Disponiveis \t");
			System.out.println ("03 - Numero de Conexoes Tecnomen Voucher Disponiveis \t");
			System.out.println ("04 - Numero de Conexoes Tecnomen Admin Disponiveis \t");
			System.out.println ("05 - Numero de Conexoes Tecnomen Agent Disponiveis \t");
			System.out.println ("06 - Numero de Conexoes PREP \t");
			System.out.println ("");		
			System.out.println ("0 - Menu Principal \t");
			System.out.println ("");
			System.out.print ("Opcao:");		
			opcaoNumerodeConexaoDisponiveis = Integer.parseInt(read());
		
			return opcaoNumerodeConexaoDisponiveis;
		}

	public static int menuListaProcessosComConexoesEmUso()
		{
			int opcaoListaProcessosComConexoesEmUso;
			System.out.println ("");
			System.out.println ("01 - Processos com Conexoes Tecnomen Aprovisionamento Em Uso \t");
			System.out.println ("02 - Processos com Conexoes Tecnomen Recarga Em Uso \t");
			System.out.println ("03 - Processos com Conexoes Tecnomen Voucher Em Uso \t");
			System.out.println ("04 - Processos com Conexoes Tecnomen Admin Em Uso \t");
			System.out.println ("05 - Processos com Conexoes Tecnomen Agent Em Uso \t");
			System.out.println ("06 - Processos com Conexoes PREP Em Uso \t");
			System.out.println ("");		
			System.out.println ("0 - Menu Principal \t");
			System.out.println ("");
			System.out.print ("Opcao:");		
			opcaoListaProcessosComConexoesEmUso = Integer.parseInt ( read() );
		
			return opcaoListaProcessosComConexoesEmUso;
		}
	
	public static int menuMapeamentos()
	{
		System.out.println ("");
		System.out.println ("01 - Atualiza todos os mapeamentos");
		System.out.println ("02 - Atualiza um mapeamento");
		System.out.println ("03 - Exibe um mapeamento");
		System.out.println ("");		
		System.out.println ("0 - Menu Principal");
		System.out.println ("");
		System.out.print ("Opcao: ");		
	
		return Integer.parseInt(read());
	}

	public static long menuLiberarConexoesEmUso()
	{
		long idProcesso = 0;
		do
		{
			System.out.println("\nDigite o id do processo que a ter sua conexoes liberadas: ");
			idProcesso = Long.parseLong(read());
			if(idProcesso < 1)
				System.out.println("Opção Inválida");
		}
		while(idProcesso < 1);	
		return idProcesso;
	}

	public static String read()
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
