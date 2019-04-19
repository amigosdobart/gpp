package com.brt.gpp.aplicacoes.marreta;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.*;
/*
import com.brt.gpp.comum.*;
import com.brt.gpp.aplicacoes.recarregar.*;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.conexoes.tecnomen.*;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Imports Java
import java.sql.*;
import java.util.*;
import java.text.*;

import TINC.*;
*/
public class Intervir extends Aplicacoes 
{
	/*
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; 				// Gerente de conexoes Banco Dados
	protected GerentePoolTecnomen gerenteInsercaoCreditos = null;			// Gerente de conexões com a Payment Engine da Tecnomen
	Calendar calendario;
	SimpleDateFormat sdf;
	DecimalFormat df;
	*/
	public Intervir(long idProcesso)
	{
		super(idProcesso, "Intervir");
		/*
		this.calendario = Calendar.getInstance();
		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		df = new DecimalFormat("0.00");
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(super.getIdLog());
		this.gerenteInsercaoCreditos = GerentePoolTecnomen.getInstancia(super.getIdLog());
		*/
	}
	
	/**
	 * Metodo...: migrarResiduosSegundaVersao
	 * Descricao: Migrar o que resta de pula-pula e bumerangue que ainda está 
	 * 				no saldo principal para o saldo de bônus
	 *
	 */
	/*
	public void migrarResiduosSegundaVersao()
	{
        //Inicializa variaveis do metodo
		PREPConexao conexaoPrep = null;
		TecnomenAprovisionamento ta = null;

		try
		{
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			ta = gerenteInsercaoCreditos.getTecnomenAprovisionamento(super.getIdLog());
			
			String sqlResiduos = 	
			"select idt_msisdn, recargas, bonus, saldo, transferencia from "+
				"( "+
				"select idt_msisdn, recargas, bonus, saldo, "+ 
				         "decode (sign(saldo - recargas), "+
				             "-1,0, "+ // -- Se saldo atual < recargas nao migra 
				              "0,0, "+ // -- Se saldo atual = recargas nao migra
				             // -- Se saldo atual > recargas, verifica se a diferença (X) é maior que o valor dos bonus
				              "1, decode (sign (saldo - recargas - bonus), "+ 
				                     "-1,saldo - recargas, " + //-- Deixa apenas o restante das recargas
				                     "0, saldo - recargas, "+ //-- Deixa apenas o restante das recargas 
				                     "1, bonus)) transferencia "+ //-- Transfere apenas o referente ao bonus      
				"from ( "+
				"select c.idt_msisdn, "+
				       "sum( case when id_tipo_recarga='R' "+
				             "then b.vlr_credito_principal else 0 "+
				            "end ) as recargas, "+
				       "sum( case when dat_recarga < to_date('01022005','ddmmyyyy') "+
				                "and tip_transacao in ('08003','08001') "+
				          "then b.vlr_credito_principal else 0 "+ 
				       "end ) as bonus, "+
				       "c.vlr_saldo_principal as saldo "+
				"from  tbl_apr_assinante c, "+    
				      "tbl_rec_recargas b "+ 
				"where b.idt_msisdn=c.idt_msisdn "+ 
				 "and  c.idt_status=2 "+ 
				 "and  c.idt_plano_preco not in (0,4,5) "+
				"group by  c.idt_msisdn, c.vlr_saldo_principal "+
				")) x "+
				"where transferencia >0 " +
				"and not exists(select * from hsid.tbl_rel_recargas_contabil where msisdn = x.idt_msisdn)";
			
			ResultSet rsAssinantes = conexaoPrep.executaQuery(sqlResiduos, super.getIdLog());
			
			Assinante dadosAssinante = null;
			while(rsAssinantes.next())
			{
				String msisdn = rsAssinantes.getString("idt_msisdn");
				double valorParaMigrar = rsAssinantes.getDouble("transferencia");
				
				dadosAssinante = ta.consultarAssinante(msisdn);
				
				// Verifica se o saldo do assinante é insuficiente para a migração
				if(valorParaMigrar > dadosAssinante.getCreditosPrincipal())
					valorParaMigrar = dadosAssinante.getCreditosPrincipal();
				
				// Garante que, ao menos, R$0.01 será deixado no saldo principal
				if(dadosAssinante.getCreditosPrincipal() - valorParaMigrar < 0.01)
					valorParaMigrar = valorParaMigrar - 0.01;
				
				String dataHoraMigracao = sdf.format(calendario.getTime());
				short retornoAjuste = this.migraSaldoASaldoB("00","01", msisdn, valorParaMigrar, dataHoraMigracao );
				
				if(retornoAjuste == 0)
				{
					try
					{
						// Logar Migração na tbl_rel_recargas_contabil
						String sqlInsertContabil = "insert into hsid.tbl_rel_recargas_contabil "+
								"(cn, msisdn, plano, saldo_princ_atual, saldo_princ_ajustado, saldo_bonus_atual, saldo_bonus_ajustado, total_recargas, total_bonus, dat_ult_recarga, dat_transferencia) "+
								"values "+
								"(?,?,?,?,?,?,?,?,?,?,? )";	// msisdn, plano, saldoPrincAtual, saldoPrincAjustado, saldoBonusAtual, saldoBonusAjustado, totalRecargas, totalBonus, dataUltimaRecarga, dataTransferencia
						Object[] paramsInsertContabil = { 	msisdn.substring(2,4),
															msisdn,	
															new Short(dadosAssinante.getPlanoPreco()),
															new Double(dadosAssinante.getCreditosPrincipal()),
															new Double(dadosAssinante.getCreditosPrincipal() - valorParaMigrar),
															new Double(dadosAssinante.getCreditosBonus()),
															new Double(dadosAssinante.getCreditosBonus() + valorParaMigrar), 
															null,
															null,
															null,
															new java.sql.Timestamp(sdf.parse(dataHoraMigracao).getTime())
						};
						conexaoPrep.executaPreparedUpdate(sqlInsertContabil, paramsInsertContabil, super.getIdLog());								
					}
					catch (ParseException pe)
					{
						super.log(Definicoes.ERRO, "migrarResiduoBonus","Erro de Parse: "+dataHoraMigracao);
					}
				}
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"migrarResiduosSegundaVersao","Erro do GPP: "+e);
		}
	}
	
	/**
	 * Metodo...: migrarResiduoBonus
	 * Descricao: Transferir o resíduo do saldo principal para o saldo de bonus
	 */
	/*
	public void migrarResiduoBonus()
	{
        //Inicializa variaveis do metodo
		PREPConexao conexaoPrep = null;
		TecnomenAprovisionamento ta = null;

		try
		{
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			ta = gerenteInsercaoCreditos.getTecnomenAprovisionamento(super.getIdLog());
			
			// Seleciona os assinantes da área 67
			String sqlAssinantes = "select sub_id "+
									"from tbl_apr_assinante_tecnomen "+
									"where dat_importacao = trunc(sysdate) "+
									"and account_status in (2,3,4) "+
									"and substr(sub_id,3,2) = '67'";
			
			ResultSet rsAssinantes = conexaoPrep.executaQuery(sqlAssinantes, super.getIdLog());
			
			// Para cada um dos assinantes, faz o seguinte
			while(rsAssinantes.next())
			{
				String msisdn = null;
				double saldoParaMigrar = 0;
				String dataHoraMigracao = null;
				try
				{
					// Determina o montante de recargas para esse assinante
					msisdn = rsAssinantes.getString("sub_id");
					
					String sqlSomaRecargas = "select nvl(somaRecargas.soma,0) as somaRecargas, " +
							"nvl(somaBonus.soma,0) as somaBonus, nvl(somaRecargas.datUltimaRecarga,'01-jan-1980') as datUltimaRecarga from "+
							"( "+
							"select nvl(sum(vlr_credito_principal),0) as soma,  max(dat_recarga) as datUltimaRecarga "+
							"from tbl_rec_Recargas "+ 
							"where id_tipo_recarga = 'R' "+
							"and idt_msisdn = ? "+
							") somaRecargas, "+
							"( "+
							"select sum(nvl(vlr_credito_bonus,0)+nvl(vlr_credito_principal,0)) as soma "+
							"from tbl_rec_Recargas "+ 
							"where id_tipo_recarga = 'A' "+
							"and tip_transacao in ('08001','06018','08003') "+
							"and idt_msisdn = ? "+
							") somaBonus";
					
					Object[] paramSomaRecargas = {msisdn, msisdn};
					
					ResultSet rsSomaRecargas = conexaoPrep.executaPreparedQuery1(sqlSomaRecargas, paramSomaRecargas, super.getIdLog());
					
					double somaRecargas = 0;
					double somaBonus = 0;
					java.sql.Timestamp datUltimaRecarga = null;
					
					// Se houver, ao menos, uma recarga ou um bonus haverá elementos no resultSet
					if(rsSomaRecargas.next())
					{
						somaRecargas = rsSomaRecargas.getDouble("somaRecargas");
						somaBonus = rsSomaRecargas.getDouble("somaBonus");
						datUltimaRecarga = rsSomaRecargas.getTimestamp("datUltimaRecarga");						
					}
					
					// Faz uma cosulta à tecnomen para verificar o saldo do assinante
					Assinante dadosAssinante = ta.consultarAssinante(msisdn);
					
					if(dadosAssinante != null)
					{
						// Se o saldo do Assinante for maior que a soma de suas Recargas
						// O saldo deverá ser ajustado
						if( Double.parseDouble(df.format(dadosAssinante.getCreditosPrincipal())) > somaRecargas )
						{
							saldoParaMigrar = dadosAssinante.getCreditosPrincipal() - somaRecargas;
							dataHoraMigracao = sdf.format(calendario.getTime());
							
							try
							{
								// Efetua a migração de saldos
								short retornoAjuste = this.migraSaldoASaldoB("00","01",msisdn,saldoParaMigrar,dataHoraMigracao);
								
								// Se a migração do resíduo de bonus foi bem sucedida
								if(retornoAjuste == 0)
								{
									try
									{
										// Logar Migração na tbl_rel_recargas_contabil
										String sqlInsertContabil = "insert into hsid.tbl_rel_recargas_contabil "+
												"(cn, msisdn, plano, saldo_princ_atual, saldo_princ_ajustado, saldo_bonus_atual, saldo_bonus_ajustado, total_recargas, total_bonus, dat_ult_recarga, dat_transferencia) "+
												"values "+
												"('67',?,?,?,?,?,?,?,?,?,? )";	// msisdn, plano, saldoPrincAtual, saldoPrincAjustado, saldoBonusAtual, saldoBonusAjustado, totalRecargas, totalBonus, dataUltimaRecarga, dataTransferencia
										Object[] paramsInsertContabil = { 	msisdn,	
																			new Short(dadosAssinante.getPlanoPreco()),
																			new Double(dadosAssinante.getCreditosPrincipal()),
																			new Double(dadosAssinante.getCreditosPrincipal() - saldoParaMigrar),
																			new Double(dadosAssinante.getCreditosBonus()),
																			new Double(dadosAssinante.getCreditosBonus() + saldoParaMigrar), 
																			new Double(somaRecargas),
																			new Double(somaBonus),
																			datUltimaRecarga,
																			new java.sql.Timestamp(sdf.parse(dataHoraMigracao).getTime())
										};
										conexaoPrep.executaPreparedUpdate(sqlInsertContabil, paramsInsertContabil, super.getIdLog());								
									}
									catch (ParseException pe)
									{
										super.log(Definicoes.ERRO, "migrarResiduoBonus","Erro de Parse: "+dataHoraMigracao);
									}
								}
							}
							catch (GPPInternalErrorException gppE)
							{
								super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro ao migrar saldo principal->Bonus: "+msisdn+":"+saldoParaMigrar+":"+dataHoraMigracao+":Motivo "+gppE);
							}
							catch (GPPTecnomenException tecE)
							{
								super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro ao migrar saldo principal->Bonus: "+msisdn+":"+saldoParaMigrar+":"+dataHoraMigracao+":Motivo "+tecE);
							}
						}
					}
				}
				catch(GPPInternalErrorException gppE)
				{
					super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro GPP: "+msisdn+":"+saldoParaMigrar+":"+dataHoraMigracao+":Motivo "+gppE);
				}
				catch(SQLException sqlE)
				{
					super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro SQL: "+msisdn+":"+saldoParaMigrar+":"+dataHoraMigracao+":Motivo "+sqlE);
				}
				catch(GPPTecnomenException e)
				{
					super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro Consulta Tecnomen: "+msisdn+":"+saldoParaMigrar+":"+dataHoraMigracao+":Motivo "+e);
				}
			}
		}
		catch(GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro GPP: "+gppE);
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro .next da tbl_assinantes: "+sqlE);
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			// Libera conexão com Tecnomen
			this.gerenteInsercaoCreditos.liberaConexaoAprovisionamento(ta, super.getIdLog());
		}
	}
	
	/**
	 * Metodo...: restituirCredito
	 * Descricao: Procura por assinantes que tiveram seu resíduo ajustado mas que fizeram uma
	 * 				recarga durante a execução do processo
	 */
	/*
	public void restituirCredito()
	{
        //Inicializa variaveis do metodo
		PREPConexao conexaoPrep = null;
		TecnomenRecarga tr = null;

		try
		{
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			tr = gerenteInsercaoCreditos.getTecnomenRecarga(super.getIdLog());
			
			// Seleciona os assinantes da área 67
			String sqlRestituir = "select tblSacaneados.msisdn as msisdn, rr.vlr_credito_principal as vlrRecarga, rr.dat_recarga as datUltimaRecarga "+ 
					"from tbl_rec_recargas rr, hsid.tbl_rel_recargas_contabil tblSacaneados "+
					"where rr.idt_msisdn = tblSacaneados.msisdn "+
					"and rr.dat_recarga > tblSacaneados.dat_ult_recarga " +
					"and rr.dat_recarga <= tblSacaneados.dat_transferencia "+
					"and id_tipo_recarga = 'R'";
			
			ResultSet rsRestituir = conexaoPrep.executaQuery(sqlRestituir, super.getIdLog());
			
			// Faz a restituição da transferencia indevida para cada assinante lesado
			while(rsRestituir.next())
			{
				String msisdn = null;
				double valorRecarga = 0;
				java.sql.Timestamp datUltimaRecarga = null;
				String datTransferencia = null;
				try
				{
					msisdn = rsRestituir.getString("msisdn");
					valorRecarga = rsRestituir.getDouble("vlrRecarga");
					datUltimaRecarga = rsRestituir.getTimestamp("datUltimaRecarga");
					datTransferencia = sdf.format(this.calendario.getTime());
					short retornoAjuste = this.migraSaldoASaldoB("01","00",msisdn,valorRecarga,datTransferencia);
					
					if(retornoAjuste == 0)
					{
						String sqlAtualizaTblContabil = "update hsid.tbl_rel_recargas_contabil "+
							"set saldo_princ_ajustado = saldo_princ_ajustado + ?, "+		// Valor da Recarga
							"saldo_bonus_ajustado = saldo_bonus_ajustado - ?, "+		// Valor da Recarga
							"total_recargas = total_recargas + ?, "+		// Valor da Recarga
							"dat_ult_recarga = ?, "+	// Data da Ultima Recarga
							"dat_transferencia = ? "+	// Data da Transferencia
							"where msisdn = ?";			// Msisdn
						
						Object[] paramsTblContabil = {	new Double(valorRecarga),
														new Double(valorRecarga),
														new Double(valorRecarga),
														datUltimaRecarga,
														new java.sql.Timestamp(sdf.parse(datTransferencia).getTime()),
														msisdn
														
						};
						
						conexaoPrep.executaPreparedUpdate(sqlAtualizaTblContabil, paramsTblContabil,super.getIdLog());
					}
					else
					{
						super.log(Definicoes.WARN,"restituirCredito","Erro Funcional: "+msisdn+":"+retornoAjuste);
					}
				}
				catch(GPPInternalErrorException gppE)
				{
					super.log(Definicoes.ERRO,"restituirCredito","Erro GPP: "+msisdn+":"+valorRecarga+":"+datUltimaRecarga+":Motivo "+gppE);
				}
				catch(SQLException sqlE)
				{
					super.log(Definicoes.ERRO,"restituirCredito","Erro SQL: "+msisdn+":"+valorRecarga+":"+datUltimaRecarga+":Motivo "+sqlE);
				}
				catch(GPPTecnomenException piE)
				{
					super.log(Definicoes.ERRO,"restituirCredito","Erro Consulta Tecnomen: "+msisdn+":"+valorRecarga+":"+datUltimaRecarga+":Motivo "+piE);
				}
				catch (ParseException pe)
				{
					super.log(Definicoes.ERRO, "restituirCredito","Erro de Parse: "+datTransferencia);
				}
			}
		}
		catch(GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"restituirCredito","Erro GPP: "+gppE);
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.ERRO,"restituirCredito","Erro .next da tbl_assinantes: "+sqlE);
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			// Libera conexão com Tecnomen
			this.gerenteInsercaoCreditos.liberaConexaoRecarga(tr, super.getIdLog());
		}
	}
	
	private short migraSaldoASaldoB(String saldoA, String saldoB, String msisdn, double saldoParaMigrar,
									String dataHoraMigracao) throws GPPInternalErrorException, GPPTecnomenException
	{
		Ajustar ajustador = new Ajustar(super.getIdLog());
		
		short retornoAjuste = 0;
		String tipoAjuste = "D";
		
		// Debita o resíduo do saldo principal
		retornoAjuste = ajustador.executaAjuste(msisdn, "06025",saldoA,saldoParaMigrar,
								tipoAjuste,   dataHoraMigracao,
								"GPP","GPP","",null,"",true);
		
		// Se o débito do resíduo foi bem sucedido
		if(retornoAjuste == 0)
		{
			tipoAjuste = "C";
			// Repassa o resíduo para o saldo de bônus
			retornoAjuste = ajustador.executaAjuste(msisdn, "06026",saldoB,saldoParaMigrar,
					tipoAjuste,   dataHoraMigracao,
					"GPP","GPP","",null,"",true);
			
			if(retornoAjuste !=0)
			{
				super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro ao fazer credito no saldo de bonus: "+msisdn+":"+saldoParaMigrar+":"+dataHoraMigracao+":Motivo "+retornoAjuste);
			}
		}
		else
		{
			super.log(Definicoes.ERRO,"migrarResiduoBonus","Erro ao fazer credito no saldo de bonus: "+msisdn+":"+saldoParaMigrar+":"+dataHoraMigracao+":Motivo "+retornoAjuste);
		}
		
		return retornoAjuste;
	}
	
	public void sincronizarBloqueioDEH()
	{
        //Inicializa variaveis do metodo
		PREPConexao conexaoPrep = null;
		
		try
		{
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		
			// SQL para encontrar quem precisa ser bloqueado e ainda não está
			String sql = "select msisdn from "+
					"( "+
						"select sub_id as msisdn from tbl_apr_assinante_tecnomen "+
						"where dat_importacao = trunc(sysdate) "+ 
						"and account_status = 2 "+ 
						"and account_balance/1000000 < 10 "+
					") ass "+
					//"where not exists "+ 
					//"( "+
						//"select idt_msisdn msisdn from tbl_apr_bloqueio_servico "+ 
						//"where id_servico = 'ELM_FREE_CALL' "+
						//"and idt_msisdn = ass.msisdn "+
					//") "+
					"where not exists "+ 
					"( "+	
						"select idt_msisdn msisdn from tbl_Rec_recargas rec "+
						"where rec.id_tipo_recarga = 'R' "+
						"and dat_recarga between trunc(sysdate) and sysdate "+
						"and idt_msisdn = ass.msisdn "+
					") "+
					"union all "+
					"select msisdn from "+ 
					"( "+
					"select sub_id as msisdn from tbl_apr_assinante_tecnomen "+
					"where dat_importacao = trunc(sysdate) "+
					"and account_status = 3 "+
					") ass "+
					//"where not exists "+
					//"( "+
					 //   "select idt_msisdn from tbl_apr_bloqueio_servico "+
					  //  "where idt_msisdn = ass.msisdn "+
					   // "and id_servico = 'ELM_FREE_CALL' "+
					//") "+
					"where not exists "+ 
					"( "+	
						"select idt_msisdn msisdn from tbl_Rec_recargas rec "+
						"where rec.id_tipo_recarga = 'R' "+
						"and dat_recarga between trunc(sysdate) and sysdate "+
						"and idt_msisdn = ass.msisdn "+
					")";
			
			ResultSet rsBloq = conexaoPrep.executaQuery(sql, super.getIdLog());
			
			// Abrir arquivo para mandar para HLR
			ManipuladorArquivos fileHandler = new ManipuladorArquivos("bloqueios.txt", true, super.getIdLog());
			
			// Para cada msisdn retornado acima, inseri-lo na tbl_apr_bloqueio_servico
			// E colocar no arquivo de bloqueios a ser enviado para o hlr
			while(rsBloq.next())
			{
				String msisdn = rsBloq.getString("msisdn");
				
				String sqlIns = "INSERT INTO TBL_APR_BLOQUEIO_SERVICO " +
						"(IDT_MSISDN, ID_SERVICO, IDT_USUARIO, ID_MOTIVO, ID_STATUS, DAT_ATUALIZACAO) " +
						"VALUES (?, 'ELM_FREE_CALL', 'GPP', '01', 'BC', SYSDATE)";
				
				Object[] sqlPars = {msisdn};
				
				conexaoPrep.executaPreparedUpdate(sqlIns, sqlPars, super.getIdLog());
				
				fileHandler.escreveLinha(msisdn);
			}
			
			fileHandler.fechaArquivo();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "sincronizarBloqueioDEH","Erro: "+e);
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());			
		}
	}
	*/
}
