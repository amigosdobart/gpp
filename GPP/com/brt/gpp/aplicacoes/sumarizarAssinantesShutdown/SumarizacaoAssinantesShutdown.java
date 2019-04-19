//Definicao do Pacote
package com.brt.gpp.aplicacoes.sumarizarAssinantesShutdown;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

// Arquivos de imports do java
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
  *
  * Este arquivo refere-se a classe SumarizacaoAssinantesShutdown, responsavel pela implementacao do processo batch de 
  * sumarizacao diaria de mudanca de status de assinantes para Shutdown na tabela de relatorio.
  * de bonus
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Daniel Ferreira
  * Data: 				01/08/2005
  *
  * Modificado por:     
  * Data:				
  * Razao:				
  *
  */

public final class SumarizacaoAssinantesShutdown extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; 	// Gerente de conexoes Banco Dados
	protected long idLog; 										// Armazena o ID do log
		     
	/**
	 * Metodo...: SumarizacaoAssinantesShutdown
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public SumarizacaoAssinantesShutdown(long logId)
	 {
		super(logId, Definicoes.CL_SUMARIZACAO_ASSINANTES_SHUTDOWN);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);

		this.idLog = logId;
	 }

	/**
	 * Metodo...:	sumarizarAssinantesShutdown
	 * Descricao:	Verifica os eventos diarios de mudanca de status de assinantes para shutdown e sumariza as 
	 * 				informacoes na tabela
	 * 
	 * @return	short		- RET_OPERACAO_OK se sucesso ou diferente em caso de falha
	 */
	public short sumarizaAssinantesShutdown(String dataAnalise) throws GPPInternalErrorException 
	{
		short result = -1 ;
		String status = Definicoes.PROCESSO_ERRO;
		String descricao = "";
		long subCounter = 0;
		Date dataInicioAnalise = null;
		Date dataFimAnalise = null;
		
		super.log(Definicoes.DEBUG, "sumarizaAssinantesShutdown", "INICIO");
		
		//Obtendo a data inicial do processamento
		Calendar calProcesso = Calendar.getInstance();
		Timestamp inicioProcesso = new Timestamp(calProcesso.getTimeInMillis());
		
		PREPConexao conexaoPrep = null;

		try
		{			
			//Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(this.idLog);
		
			//Obtendo as datas de inicio e fim de analise para passagem de parametro no insert
			SimpleDateFormat conversorData = new SimpleDateFormat("dd/MM/yyyy");
			Calendar calAnalise = Calendar.getInstance();
			calAnalise.setTime(conversorData.parse(dataAnalise));
			//Data de inicio da analise
			dataInicioAnalise = calAnalise.getTime();
			//Data de fim da analise
			calAnalise.add(Calendar.DAY_OF_MONTH, 1);
			dataFimAnalise = calAnalise.getTime();
			
			if(!diaProcessado(dataInicioAnalise, conexaoPrep))
			{
				//Faz a pesquisa no banco pelos eventos de mudanca de status e atualiza a tabela de relatorios  
				String sqlInsert = 
					"INSERT INTO TBL_REL_ASSINANTE_SHUTDOWN                                         	" +
					"(                                                                              	" +
					"    DAT_DESATIVACAO,                                                           	" +
					"    IDT_CODIGO_NACIONAL,                                                       	" +
					"    IDT_MSISDN,                                                                	" +
					"    DAT_ATIVACAO,                                                              	" +
					"    QTD_RECARGAS,                                                              	" +
					"    VLR_CREDITO_PRINCIPAL,                                                     	" +
					"    VLR_CREDITO_BONUS,                                                         	" +
					"    VLR_CREDITO_SMS,                                                           	" +
					"    VLR_CREDITO_GPRS                                                           	" +
					")                                                                              	" +
					"SELECT                                                                         	" +
					"    EVENTO.DAT_DESATIVACAO AS DAT_DESATIVACAO,                                 	" +
					"    EVENTO.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,                         	" +
					"    EVENTO.IDT_MSISDN AS IDT_MSISDN,                                           	" +
					"    EVENTO.DAT_ATIVACAO AS DAT_ATIVACAO,                                       	" +
					"    COUNT(RECARGA.ID_RECARGA) AS QTD_RECARGAS,                                 	" +
					"    SUM(NVL(RECARGA.VLR_CREDITO_PRINCIPAL, 0)) AS VLR_CREDITO_PRINCIPAL,       	" +
					"    SUM(NVL(RECARGA.VLR_CREDITO_BONUS, 0)) AS VLR_CREDITO_BONUS,               	" +
					"    SUM(NVL(RECARGA.VLR_CREDITO_SMS, 0)) AS VLR_CREDITO_SMS,                   	" +
					"    SUM(NVL(RECARGA.VLR_CREDITO_GPRS, 0)) AS VLR_CREDITO_GPRS                  	" +
					"FROM                                                                           	" +
					"    (                                                                          	" +
					"        SELECT                                                                 	" +
					"            TRUNC(MAX(SHUTDOWN.DAT_APROVISIONAMENTO)) AS DAT_DESATIVACAO,          " +
					"            SUBSTR(SHUTDOWN.IDT_MSISDN, 3, 2) AS IDT_CODIGO_NACIONAL,          	" +
					"            SHUTDOWN.IDT_MSISDN AS IDT_MSISDN,                                 	" +
					"            TRUNC(MAX(ATIVACAO.DAT_APROVISIONAMENTO)) AS DAT_ATIVACAO              " +
					"        FROM                                                                   	" +
					"            TBL_APR_EVENTOS ATIVACAO,                                          	" +
					"            TBL_APR_EVENTOS SHUTDOWN                                           	" +
					"        WHERE                                                                  	" +
					"            ATIVACAO.IDT_MSISDN (+) = SHUTDOWN.IDT_MSISDN AND                  	" +
					"            ATIVACAO.TIP_OPERACAO (+) = ? AND 			                        	" +
					"            ATIVACAO.DES_STATUS (+) = ? AND       	                  	        	" +
					"            SHUTDOWN.TIP_OPERACAO = ? AND  	           			            	" +
					"            SHUTDOWN.DES_STATUS = ? AND   		                                	" +
					"            ATIVACAO.DAT_APROVISIONAMENTO (+) <= SHUTDOWN.DAT_APROVISIONAMENTO AND " +
					"            SHUTDOWN.DAT_APROVISIONAMENTO >= ? AND     			   	            " +
					"            SHUTDOWN.DAT_APROVISIONAMENTO <  ?                     		  		" +
					"        GROUP BY                                                                  	" +
					"            SHUTDOWN.IDT_MSISDN				                                 	" +
					"    ) EVENTO,                                                                  	" +
					"    TBL_REC_RECARGAS RECARGA                                                   	" +
					"WHERE                                                                          	" +
					"    EVENTO.IDT_MSISDN = RECARGA.IDT_MSISDN (+) AND                             	" +
					"    RECARGA.DAT_ORIGEM (+) >= EVENTO.DAT_ATIVACAO AND                             " +
					"    RECARGA.ID_TIPO_RECARGA (+) = 'R'                                             	" +
					"GROUP BY                                                                       	" +
					"    EVENTO.DAT_DESATIVACAO,                                                    	" +
					"    EVENTO.IDT_CODIGO_NACIONAL,                                                	" +
					"    EVENTO.IDT_MSISDN,                                                         	" +
					"    EVENTO.DAT_ATIVACAO                                                        	";
			
				Object parametros[] = 
				{
					Definicoes.TIPO_APR_ATIVACAO,
					Definicoes.TIPO_OPER_SUCESSO,
					Definicoes.TIPO_APR_SHUTDOWN,
					Definicoes.TIPO_OPER_SUCESSO,
					new java.sql.Date(dataInicioAnalise.getTime()),
					new java.sql.Date(dataFimAnalise.getTime())
				};

				subCounter = conexaoPrep.executaPreparedUpdate(sqlInsert, parametros, this.getIdLog());
			
				status = Definicoes.PROCESSO_SUCESSO;
				result = 0;
				descricao = "Numero de assinantes processados: " + subCounter;
			}
			else
			{
				descricao = "Dia " + dataAnalise + " ja processado";
			}
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "sumarizaAssinantesShutdown", "Excecao Interna GPP ocorrida: "+ e);
			descricao = e.getMessage();
		}
		catch(ParseException e)
		{
			super.log(Definicoes.ERRO, "sumarizaAssinantesShutdown", "Excecao de Parse: " + e);
			descricao = e.getMessage();
		}
		finally
		{
			//Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			//Obtendo a data final do processamento
			calProcesso = Calendar.getInstance();
			Timestamp fimProcesso = new Timestamp(calProcesso.getTimeInMillis());
			
			SimpleDateFormat conversorData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			//Chama a funcao para gravar no historico o processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_SUMARIZACAO_ASSINANTES_SHUTDOWN, 
					                      conversorData.format(inicioProcesso), 
										  conversorData.format(fimProcesso), 
										  status, 
										  descricao, 
										  dataAnalise);
			
			super.log(Definicoes.INFO, "sumarizaAssinantesShutdown", "FIM");
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
				new Integer(Definicoes.IND_SUMARIZACAO_ASSINANTES_SHUTDOWN),
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