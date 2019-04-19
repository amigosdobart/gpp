package com.brt.gpp.aplicacoes.controle.analiseInconsistencia;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivo Internos GPP
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.ManipuladorArquivos;

// Arquivos Java
import java.text.SimpleDateFormat;
import java.sql.*;
import java.text.ParseException;

/**
* Este arquivo refere-se a classe AnalisarInconsistencias, responsavel por encontrar
* inconsistências no extrato
*
* <P> Versao:			1.0
*
* @Autor: 			Denys Oliveira
* Data: 				29/12/2004
*
*/
public class AnalisarInconsistencias extends Aplicacoes
{
	GerentePoolBancoDados gerenteBanco;
	
	public AnalisarInconsistencias(long aIdProcesso)
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_ANALISAR_INCONSISTENCIAS);
		
		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	
	}
	
	/**
	 * Metodo...: procurarGaps
	 * Descricao: Procura por inconsistências de Saldo no Extrato
	 * @param String		dataHoraInicio				Início do período a ser analisado (dd/mm/yyyy HH24:MI:SS)
	 * @param String		dataHoraFim					Fim do período a ser analisado (dd/mm/yyyy HH24:MI:SS)
	 * @param String		complementoClausulaWhere	Cláusula where que identificará os msisdns a serem analisados
	 * 													ex: = '556184077766'
	 * 														LIKE '5561%'
	 * @return	Valor Total das inconsistências
	 */
	public double procurarGaps(String dataHoraInicio, String dataHoraFim, String whereMsisdn, String pathNomeArquivoGaps)
	{
		double gapTotal = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ManipuladorArquivos arquivoInconsistencias = null;
		
		ConexaoBancoDados DBConexao = null;
		
		try
		{
			arquivoInconsistencias = new ManipuladorArquivos(pathNomeArquivoGaps, true, super.getIdLog());
			
			// Pega conexão com banco de dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());
			
			// Busca quais são os msisdns a terem suas contas analisadas
			//String sqlMsisdns = "SELECT DISTINCT MSISDN FROM (" +
			//					"SELECT DISTINCT IDT_MSISDN AS MSISDN FROM TBL_REC_RECARGAS WHERE IDT_MSISDN "+whereMsisdn +
			//					"AND DAT_RECARGA BETWEEN ? AND ? "+
			//					"UNION ALL "+
			//					"SELECT DISTINCT SUB_ID AS MSISDN FROM TBL_GER_CDR WHERE SUB_ID "+whereMsisdn +
			//					"AND TIMESTAMP BETWEEN ? AND ? )";
			
			//Object[] prmMsisdn = {	new java.sql.Timestamp(sdf.parse(dataHoraInicio).getTime()),
			//						new java.sql.Timestamp(sdf.parse(dataHoraFim).getTime()),
			//						new java.sql.Timestamp(sdf.parse(dataHoraInicio).getTime()),
			//						new java.sql.Timestamp(sdf.parse(dataHoraFim).getTime())
			//					};

			if (whereMsisdn.indexOf("#")>0)
				whereMsisdn = "LIKE '"+whereMsisdn.replace('#','%')+"'";
			else whereMsisdn = "='"+whereMsisdn+"'";
			
			String sqlMsisdns = "SELECT SUB_ID AS MSISDN " +
			                      "FROM TBL_APR_ASSINANTE_TECNOMEN " +
								 "WHERE DAT_IMPORTACAO = trunc(to_date('"+dataHoraFim+"','DD/MM/YYYY HH24:MI:SS')) " +
								   "AND SUB_ID " + whereMsisdn;
			
			ResultSet rsMsisdn = DBConexao.executaQuery(sqlMsisdns,super.getIdLog());
			
			// Faz análise de saldo para cada msisdn que tenha lançamentos de recarga/chamada no período em questão
			while(rsMsisdn.next())
			{
				String msisdn = null;
				try
				{
					double gapAssinante = 0;
					msisdn = rsMsisdn.getString("MSISDN");
					
					// Gera um extrato simplificado para o assinante
					String sqlExtrato = "SELECT SUB_ID AS MSISDN, A.TIMESTAMP AS DATAHORA, 'C' AS IND_RECARGA, A.CALL_VALUE/100000 AS VALOR, A.FINAL_ACCOUNT_BALANCE/100000 AS SALDOFINAL "+
										"FROM TBL_GER_CDR A, TBL_GER_TIP_TRANSACAO_TECNOMEN TTT "+
										"WHERE A.TIMESTAMP BETWEEN ? AND ? "+
										"AND TTT.TRANSACTION_TYPE = A.TRANSACTION_TYPE "+
										"AND TTT.IND_IMPRESSAO_COMP_CHAMADAS = ? "+ 
										"AND NOT (TTT.IDT_SENTIDO=? AND A.CALL_VALUE=0) "+
										"AND A.CALL_VALUE<>0 "+
										"AND SUB_ID = ? "+
										"UNION ALL "+
										"SELECT A.IDT_MSISDN AS MSISDN, A.DAT_RECARGA AS DATAHORA,'R' AS IND_RECARGA,A.VLR_CREDITO AS VALOR, A.VLR_SALDO_FINAL AS SALDOFINAL "+
										"FROM TBL_REC_RECARGAS A WHERE "+ 
										"DAT_RECARGA BETWEEN ? AND ? "+
										"AND A.VLR_CREDITO <> 0 "+
										"AND IDT_MSISDN = ? "+
										"ORDER BY DATAHORA";

					Object prmExtrato[] = 	{	new java.sql.Timestamp(sdf.parse(dataHoraInicio).getTime()),
												new java.sql.Timestamp(sdf.parse(dataHoraFim).getTime()),
												Definicoes.TT_INCLUIR_EXTRATO, Definicoes.IND_CHAMADA_RECEBIDA,
												msisdn,
												new java.sql.Timestamp(sdf.parse(dataHoraInicio).getTime()),
												new java.sql.Timestamp(sdf.parse(dataHoraFim).getTime()),
												msisdn
											};
					ResultSet rsExtrato = DBConexao.executaPreparedQuery1(sqlExtrato,prmExtrato,super.getIdLog());
					
					// Faz a análise de consistência das linhas do extrato desse assinante nesse período
					MovimentacaoCredito estadoInicial = null;
					MovimentacaoCredito estadoFinal = null;
					while(rsExtrato.next())
					{
						try
						{
							// Pega estado inicial do saldo do assinante
							if(estadoFinal == null)
							{
								estadoInicial = new MovimentacaoCredito( 	rsExtrato.getString("DATAHORA"),
																			rsExtrato.getString("IND_RECARGA").equalsIgnoreCase("R"),
																			rsExtrato.getDouble("VALOR"),
																			rsExtrato.getDouble("SALDOFINAL")
																		);
							}
							else
							{
								// O estado inicial, agora, recebe todos os parâmetros do estado final
								estadoInicial.copiaDe(estadoFinal);
							}

							// Pega estado final do saldo do assinante
							// Se não houver mais nenhum próximo estado, não precisa fazer check de consistencia
							if(estadoFinal == null)
							{
								rsExtrato.next();
							}
							estadoFinal = new MovimentacaoCredito( 	rsExtrato.getString("DATAHORA"),
																	rsExtrato.getString("IND_RECARGA").equalsIgnoreCase("R"),
																	rsExtrato.getDouble("VALOR"),
																	rsExtrato.getDouble("SALDOFINAL")
																);

							// Verifica consistencia dos dois estados adjacentes
							double gapSaldo = estadoFinal.checarConsistencia(estadoInicial);
							if(gapSaldo > Definicoes.GAP_MINIMO_ACEITAVEL)
							{
								arquivoInconsistencias.escreveLinha(msisdn,estadoInicial, estadoFinal, gapSaldo);
								gapTotal += gapSaldo;
								gapAssinante +=gapSaldo;
							}
						}
						catch(SQLException sqlE)
						{
							super.log(Definicoes.ERRO,"procurarGaps","Erro ao ler Estado do Saldo do Assinante: "+sqlE);
						}
					}
					super.log(Definicoes.INFO,"procurarGaps","Assinante "+msisdn+" apresentou gap de R$ "+gapAssinante);
				}
				catch (SQLException sqlE)
				{
					super.log(Definicoes.ERRO, "procurarGaps","Erro ao puxar extrato do assinante "+msisdn+": "+sqlE);
				}
			}
		}
		catch (ParseException parseE)
		{
			super.log(Definicoes.ERRO,"procurarGaps","Erro no formato dos parametros data/hora inicial/final: "+parseE);
		}
		catch (GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"procurarGaps","Erro Interno GPP: "+gppE);
		}
		catch (SQLException sqlE)
		{
			super.log(Definicoes.ERRO,"procurarGaps","Erro de banco ao selecionar os Msisdns: "+sqlE);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao,super.getIdLog());
		}
		
		arquivoInconsistencias.fechaArquivo();
		return gapTotal;
	}
}
