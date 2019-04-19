//Definicao do Pacote
package com.brt.gpp.aplicacoes.enviarInfosCartaoUnico;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

// Arquivos de imports do java
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
  *
  * Este arquivo refere-se a classe EnvioInfoCartaoUnico, responsavel pela implementacao do processo batch de envio 
  * de informacoes referentes a ligacoes utilizando Cartao Unico para a tabela de relatorio.
  * de bonus
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Daniel Ferreira
  * Data: 				06/05/2005
  *
  * Modificado por:     
  * Data:				
  * Razao:				
  *
  */

public final class EnvioInfosCartaoUnico extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; 	// Gerente de conexoes Banco Dados
	protected long idLog; 										// Armazena o ID do log
		     
	/**
	 * Metodo...: EnvioInfosCartaoUnico
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public EnvioInfosCartaoUnico(long logId)
	 {
		super(logId, Definicoes.CL_ENVIO_INFOS_CARTAO_UNICO);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);

		this.idLog = logId;
	 }

	/**
	 * Metodo...: enviaInfosCartaoUnico
	 * Descricao: Verifica os CDRs referentes ao cartao unico e sumariza na tabela de relatorios
	 * 
	 * @return	short		- RET_OPERACAO_OK se sucesso ou diferente em caso de falha
	 */
	public short enviaInfosCartaoUnico() throws GPPInternalErrorException 
	{
		short result = -1 ;
		String status = Definicoes.PROCESSO_ERRO;
		String descricao = "";
		long subCounter = 0;
		Date dataProcesso = null;
		Date dataInicioAnalise = null;
		Date dataFimAnalise = null;
		
		super.log(Definicoes.DEBUG, "enviaInfosCartaoUnico", "INICIO");
		
		//Obtendo a data inicial do processamento
		Calendar calProcesso = Calendar.getInstance();
		Timestamp inicioProcesso = new Timestamp(calProcesso.getTimeInMillis());
		
		PREPConexao conexaoPrep = null;

		try
		{			
			//Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(this.idLog);
		
			//Obtendo as datas de inicio e fim de analise. Devido ao atraso de CDRs a analise deve corresponder ao
			//dia correspondente a execucao atual menos o numero de dias definido. A analise corresponde a 1 dia
			//fechado (dd/mm/yyyy 00:00:00 a dd/mm/yyyy 23:59:59).
			dataProcesso = new Date(inicioProcesso.getTime());
			int numDiasAtraso = this.getNumDiasAtraso();
			
			Calendar calAnalise = Calendar.getInstance();
			calAnalise.setTime(dataProcesso);
			calAnalise.set(calAnalise.get(Calendar.YEAR), calAnalise.get(Calendar.MONTH), 
						   calAnalise.get(Calendar.DAY_OF_MONTH), 0, 0, 0);			
			calAnalise.add(Calendar.DAY_OF_MONTH, -numDiasAtraso);

			//Data de processamento
			dataProcesso = calAnalise.getTime();
			
			//Data de inicio da analise
			dataInicioAnalise = calAnalise.getTime();
			
			calAnalise.add(Calendar.DAY_OF_MONTH, 1);
			
			//Data de fim da analise
			dataFimAnalise = calAnalise.getTime();
			
			SimpleDateFormat conversorData = new SimpleDateFormat("yyyyMM");
			
			if(!diaProcessado(dataInicioAnalise, conexaoPrep))
			{
				//Faz a pesquisa no banco pelos CDRs e atualiza a tabela de relatorios  
				String sqlMerge = 
					"MERGE INTO TBL_REL_CARTAO_UNICO TABELA                                                    " + 
					"USING                                                                                     " + 
					"(                                                                                         " + 
					"  SELECT /*+ INDEX(TBL_GER_CDR XIE1TBL_GER_CDR) */                                        " + 
					"    SUB_ID                            AS IDT_MSISDN,                                      " + 
					"    ?                                 AS DAT_MES,                                         " + 
					"    SUM(CALL_DURATION)                AS VLR_DURACAO,                                     " + 
					"    SUM(ABS(ACCOUNT_BALANCE_DELTA))/? AS VLR_TOTAL,                                       " + 
					"    COUNT(1)                          AS QTD_REGISTROS                                    " + 
					"  FROM                                                                                    " + 
					"    TBL_GER_CDR PARTITION(PC" + conversorData.format(dataInicioAnalise) + ")              " + 
					"  WHERE                                                                                   " + 
					"    TRANSACTION_TYPE = ? AND                                                              " + 
					"    TIMESTAMP >= ?       AND                                                              " + 
					"    TIMESTAMP <  ?                                                                        " + 
					"  GROUP BY                                                                                " + 
					"    SUB_ID                                                                                " + 
					") CONSULTA                                                                                " + 
					"ON                                                                                        " + 
					"(                                                                                         " + 
					"  TABELA.IDT_MSISDN = CONSULTA.IDT_MSISDN AND                                             " + 
					"  TABELA.DAT_MES    = CONSULTA.DAT_MES                                                    " + 
					")                                                                                         " + 
					"WHEN MATCHED THEN                                                                         " + 
					"  UPDATE                                                                                  " + 
					"  SET                                                                                     " + 
					"    TABELA.VLR_DURACAO   = NVL(TABELA.VLR_DURACAO,   0) + NVL(CONSULTA.VLR_DURACAO,   0), " + 
					"    TABELA.VLR_TOTAL     = NVL(TABELA.VLR_TOTAL,     0) + NVL(CONSULTA.VLR_TOTAL,     0), " + 
					"    TABELA.QTD_REGISTROS = NVL(TABELA.QTD_REGISTROS, 0) + NVL(CONSULTA.QTD_REGISTROS, 0)  " + 
					"WHEN NOT MATCHED THEN                                                                     " + 
					"  INSERT                                                                                  " + 
					"  (                                                                                       " + 
					"    IDT_MSISDN,                                                                           " + 
					"    DAT_MES,                                                                              " + 
					"    VLR_DURACAO,                                                                          " + 
					"    VLR_TOTAL,                                                                            " + 
					"    QTD_REGISTROS                                                                         " + 
					"  )                                                                                       " + 
					"  VALUES                                                                                  " + 
					"  (                                                                                       " + 
					"    CONSULTA.IDT_MSISDN,                                                                  " + 
					"    CONSULTA.DAT_MES,                                                                     " + 
					"    NVL(CONSULTA.VLR_DURACAO,   0),                                                       " + 
					"    NVL(CONSULTA.VLR_TOTAL,     0),                                                       " + 
					"    NVL(CONSULTA.QTD_REGISTROS, 0)                                                        " + 
					"  )                                                                                       "; 
			
				Object parametros[] = 
				{
					conversorData.format(dataInicioAnalise),
					new Integer(Definicoes.TECNOMEN_MULTIPLICADOR),
					new Integer(this.getTransactionTypeCartaoUnico()),
					new java.sql.Date(dataInicioAnalise.getTime()),
					new java.sql.Date(dataFimAnalise.getTime())
				};

				subCounter = conexaoPrep.executaPreparedUpdate(sqlMerge, parametros, this.getIdLog());
			
				status = Definicoes.PROCESSO_SUCESSO;
				result = 0;
				descricao = "Numero de assinantes processados: " + subCounter;
			}
			else
			{
				descricao = "Dia " + new SimpleDateFormat("dd/MM/yyyy").format(dataProcesso) + " ja processado";
			}
		}
		catch(GPPInternalErrorException e1)
		{
			super.log(Definicoes.ERRO, "enviaInfosCartaoUnico", "Excecao Interna GPP ocorrida: "+ e1);
			descricao = "" + e1;
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			calProcesso = Calendar.getInstance();
			Timestamp fimProcesso = new Timestamp(calProcesso.getTimeInMillis());
			
			SimpleDateFormat conversorData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			//chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_ENVIO_INFOS_CARTAO_UNICO, 
					                      conversorData.format(inicioProcesso), 
										  conversorData.format(fimProcesso), 
										  status, 
										  descricao, 
										  (dataProcesso != null) ? conversorData.format(dataProcesso) : null);
			
			super.log(Definicoes.INFO, "enviaInfosCartaoUnico", "FIM");
		}
		
		return result;
	}
	
	/**
	 * Metodo....: getInicioUltimaExecucao
	 * Descricao.: Retorna a data e hora do inicio da ultima execucao do processo batch
	 * 
	 * @param 	PREPConexao		conexaoPrep 	Conexao com o banco de dados
	 * @return 	Date 							Data da ultima execucao do processo
	 */
//	private Date getInicioUltimaExecucao(PREPConexao conexaoPrep) throws GPPInternalErrorException
//	{
//		Date result = null;
//		
//		super.log(Definicoes.DEBUG, "getInicioUltimaExecucao", "INICIO");
//		
//		// Busca a ultima data de execucao encontrada para este processo batch
//		// caso o registro seja nulo (primeira execucao) entao utiliza-se a data
//		// do dia sem as informacoes de hora
//		String sql = "SELECT NVL(MAX(DAT_INICIAL_EXECUCAO), TRUNC(SYSDATE)) AS DAT_INICIAL_EXECUCAO " + 
//		             "FROM TBL_GER_HISTORICO_PROC_BATCH " +
//		             "WHERE " +
//		             "  ID_PROCESSO_BATCH = ? AND " + 
//					 "  IDT_STATUS_EXECUCAO = ?";
//		
//        Object parametros[] = {new Integer(Definicoes.IND_ENVIO_INFOS_CARTAO_UNICO), Definicoes.PROCESSO_SUCESSO};
//        try
//		{
//	        ResultSet resultData = conexaoPrep.executaPreparedQuery(sql, parametros, this.idLog);
//	        
//			if (resultData.next())
//			{
//				// Retorna a data da ultima execucao cadastrada
//				result = (Date)resultData.getTimestamp("DAT_INICIAL_EXECUCAO");
//			}
//			
//			resultData.close();
//		}
//		catch(SQLException e)
//		{
//			super.log(Definicoes.DEBUG, "getInicioUltimaExecucao", "Erro na consulta no Banco de Dados");
//			throw new GPPInternalErrorException("Erro na consulta do Banco de Dados: " + e);
//		}
//		finally
//		{
//			super.log(Definicoes.DEBUG, "getInicioUltimaExecucao", "FIM");
//		}
//
//		return result;
//	}
	
	/**
	 * Metodo....:	getNumDiasAtraso
	 * Descricao.:	Retorna o numero de dias de atraso para a analise do processo de envio de informacoes de 
	 * 				Cartao Unico de acordo com a configuracao do GPP.
	 * 
	 * @return	int 										Numero de dias de atraso
	 * @throws	GPPInternalErrorException					Caso a configuracao nao seja encontrada, o processo nao
	 * 														pode continuar
	 */
	private int getNumDiasAtraso() throws GPPInternalErrorException
	{
		int result = -1;
		
		super.log(Definicoes.DEBUG, "getNumDiasAtraso", "INICIO");
		
		try
		{
			//Procurando pelo valor no mapeamento de configuracoes do GPP
			MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstancia();
			String numDiasAtraso = (mapConfiguracao != null) ? 
				mapConfiguracao.getMapValorConfiguracaoGPP("ENVIO_CARTAO_UNICO_NUM_DIAS_ATRASO") : null;
		
			//Verificando se o valor existe no mapeamento
			if(numDiasAtraso != null)
			{
				result = Integer.parseInt(numDiasAtraso);
			}
			else
			{
				//Valor nao foi encontrado. Processo nao pode continuar
				throw new GPPInternalErrorException("Valor de configuracao ENVIO_CARTAO_UNICO_NUM_DIAS_ATRASO nao encontrado");
			}
		}
		finally
		{
			super.log(Definicoes.DEBUG, "getNumDiasAtraso", "FIM");
		}
		
		return result;
	}
	
	/**
	 * Metodo....:	getTransactionTypeCartaoUnico
	 * Descricao.:	Retorna o Transaction Type da Tecnomen referente ao Cartao Unico de acordo com a configuracao do GPP. 
	 * 
	 * @return	int 										Transaction Type da Tecnomen
	 * @throws	GPPInternalErrorException					Caso a configuracao nao seja encontrada, o processo nao
	 * 														pode continuar
	 */
	private int getTransactionTypeCartaoUnico() throws GPPInternalErrorException
	{
		int result = -1;
		
		super.log(Definicoes.DEBUG, "getTransactionTypeCartaoUnico", "INICIO");
		
		try
		{
			//Procurando pelo valor no mapeamento de configuracoes do GPP
			MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstancia();
			String transactionType = (mapConfiguracao != null) ? 
				mapConfiguracao.getMapValorConfiguracaoGPP("ENVIO_CARTAO_UNICO_TRANSACTION_TYPE") : null;
		
			//Verificando se o valor existe no mapeamento
			if(transactionType != null)
			{
				result = Integer.parseInt(transactionType);
			}
			else
			{
				//Valor nao foi encontrado. Processo nao pode continuar
				throw new GPPInternalErrorException("Valor de configuracao ENVIO_CARTAO_UNICO_TRANSACTION_TYPE nao encontrado");
			}
		}
		finally
		{
			super.log(Definicoes.DEBUG, "getTransactionTypeCartaoUnico", "FIM");
		}
		
		return result;
	}
	
	/**
	 * Metodo....:	diaProcessado
	 * Descricao.:	Verifica se o dia passado por parametro ja foi processado 
	 * 
	 * @return	boolean 						true se ja foi processado e false caso contrario
	 * @throws	GPPInternalErrorException		Caso algum erro interno ocorra, o processo nao pode continuar
	 * @throws  SQLException					Caso uma excecao SQL ocorra, o processo nao pode continuar		
	 */
	private boolean diaProcessado(Date diaAnalise, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		boolean result = false;
		
		try
		{
			String sqlQuery = "SELECT 1 FROM TBL_GER_HISTORICO_PROC_BATCH " +
							  "WHERE ID_PROCESSO_BATCH = ? AND IDT_STATUS_EXECUCAO = ? AND " +
							  "      TRUNC(DAT_PROCESSAMENTO) = ?";
			Object[] parametros =
			{
				new Integer(Definicoes.IND_ENVIO_INFOS_CARTAO_UNICO),
				Definicoes.PROCESSO_SUCESSO,
				new java.sql.Date(diaAnalise.getTime())
			};
			ResultSet resultDia = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, this.getIdLog());
			
			if(resultDia.next())
			{
				result = true;
			}
			
			resultDia.close();
		}
		catch(SQLException e)
		{
			super.log(Definicoes.DEBUG, "diaProcessado", "Erro na consulta no Banco de Dados");
			throw new GPPInternalErrorException("Erro na consulta do Banco de Dados: " + e);
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.DEBUG, "diaProcessado", "Erro interno do GPP");
			throw new GPPInternalErrorException("Erro interno do GPP: " + e);
		}
		
		return result;
	}
	
}