package com.brt.gpp.aplicacoes.bloquear;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

//Arquivos de Import Internos
import com.brt.gpp.comum.*;
import com.brt.gpp.aplicacoes.aprovisionar.ContingenciaCRM;

//Arquivos Java
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
* Este arquivo refere-se à classe DesbloquearContingenciaContingencia, responsavel pela implementacao 
* do processo de Bloqueio dos serviços de Free Call e Identificador de Chamadas
* dos usuários que tenham passado do status First Time User para Normal 
*
* <P> Versao:			1.0
*
* @Autor: 				Daniel Abib
* Data: 				27/09/2004
*
*
*/
public class DesbloquearContingenciaContingencia extends Aplicacoes 
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	
										
	/**
	 * Metodo...: BloquearContingenciaContingencia
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public DesbloquearContingenciaContingencia (long logId)
	 {
		super(logId, Definicoes.CL_BLOQUEIO_POR_SALDO);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
		
		super.log(Definicoes.DEBUG, "BloquearContingenciaContingencia", "Classe criada");
	 }
	 
	 /**
	  * Metodo...: desbloquearHotlineContingencia
	  * Descricao: Bloqueia/Desbloqueia Serviços em Massa
	  * @param 	String	dataReferencia		Data para efeitos de históricos
	  * @return	short	0, ok; !0, nok
	  * @throws GPPInternalErrorException
	  */
	 public short desbloquearHotlineContingencia(String dataReferencia) throws GPPInternalErrorException
	 {
		super.log(Definicoes.INFO, "desbloquearHotlineContingencia", "Inicio DATA "+dataReferencia);
		PREPConexao conexaoPrep 			= null; 
		Timestamp dataHoraInicioExecucao	= null;
		Timestamp dataHoraUltimaExecucao 	= null;
		SimpleDateFormat sdf                = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		short retorno = 0;
		short numeroDesbloquiosHotline = 0;
		String status = Definicoes.PROCESSO_ERRO;

		try
		{
			// Registra Data/Hora do início do processo
			//	Pega conexão com Banco de Dados
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());

			// Verifica qual foi a data/hora da última execução
			String sqlUltimaExecucao = "SELECT MAX(DAT_FINAL_EXECUCAO) AS PERIODO_INICIAL FROM TBL_GER_HISTORICO_PROC_BATCH "+
									   "WHERE ID_PROCESSO_BATCH = ? AND IDT_STATUS_EXECUCAO = ?";
									 
			Object sqlUltimaExecucaoParam[] = {new Integer(Definicoes.IND_CONTINGENCIA_CONTINGENCIA), Definicoes.PROCESSO_SUCESSO};					 
				
			ResultSet rsUltimaExecucao = conexaoPrep.executaPreparedQuery(sqlUltimaExecucao, sqlUltimaExecucaoParam, super.getIdLog());
	
			//	Aborta execução do processo caso não haja última execução registrada na TBL_GER_HISTORICO_PROC_BATCH
			// Busca data/hora da última execução
			if (rsUltimaExecucao.next())
			{
				dataHoraUltimaExecucao = rsUltimaExecucao.getTimestamp("PERIODO_INICIAL");
				dataHoraInicioExecucao = rsUltimaExecucao.getTimestamp("PERIODO_INICIAL");
			}
			
			if(dataHoraUltimaExecucao == null)
			{
				super.log(Definicoes.WARN, "desbloquearHotlineContingencia", "Nao ha registro de Execucao Anterior!");
				
				// Aborta execução do processo
				retorno = Definicoes.RET_SEM_REGISTRO_ULTIMA_EXECUCAO; 
				
				// String no histórico apresentará não sucesso
				status = Definicoes.TIPO_OPER_ERRO;								
			}
			else
			{
				// SQLs usados pelos processos Batch para selecionar os usuários que devem ter seus serviços
				// bloqueados ou desbloqueados
				String sqlConsulta = "SELECT IDT_MSISDN,DAT_APROVISIONAMENTO FROM TBL_APR_EVENTOS A " + 
							         "WHERE A.TIP_OPERACAO = 'STATUS_NORMAL' " +  
							         "AND A.IDT_ANTIGO_CAMPO = 'STATUS_FIRST_TIME' AND A.DAT_APROVISIONAMENTO > ?";

				Object chaveElementos[] = {dataHoraUltimaExecucao};
				ResultSet elegiveis = conexaoPrep.executaPreparedQuery(sqlConsulta, chaveElementos, super.getIdLog());

				// Cria uma estrutura para envio ao ASAP
				ContingenciaCRM contCRM = new ContingenciaCRM (super.getIdLog());
				// Desbloqueia assinantes
				while(elegiveis.next())
				{
					String 		msisdn 	= elegiveis.getString("IDT_MSISDN");
					Timestamp 	dataApr = elegiveis.getTimestamp("DAT_APROVISIONAMENTO");

					// Efetua o desbloqueio contingencia HotLine para troca de status,
					// atualiza os dados criando uma OS para a contingencia e incrementa
					// o numero de desbloqueios efetivados para registro de processamento
					// somente para assinantes que ainda nao tiveram nenhum bloqueio
					// previamente cadastrado
					if ( !jaSolicitadoDesbloqueio(msisdn) )
					{
						// Desbloquia HOTLINE
						long idAtividade = contCRM.desativarHotLineContingencia(msisdn);
	
						// Atualiza a tabela de envio de dados ao CRM
						this.atualizaDadosEnvioCRM(msisdn, idAtividade);

						numeroDesbloquiosHotline++;
					}

					// Caso a data do aprovisionamento for maior que a data da ultima execucao
					// entao atualiza esta variavel com este valor.
					// O registro de historico contera o valor da data da ultima execucao
					// o valor da ultima data de aprovisionamento processado
					// A data final do ultimo registro de aprovisionamento e incrementada
					// independente se este ja foi realizado, pois os proximos processamentos
					// nao devem ler esse registro novamente
					if (dataHoraUltimaExecucao.compareTo(dataApr) < 0)
						dataHoraUltimaExecucao = dataApr;

					super.log(Definicoes.INFO, "desbloquearHotlineContingencia", "MSISDN: " + msisdn + " retirado do hotline em modo de contingencia." );
				}
				status = Definicoes.PROCESSO_SUCESSO;
			}					
		}
		catch(SQLException eSQL)
		{
			status = Definicoes.TIPO_OPER_ERRO;			 
			super.log(Definicoes.WARN, "desbloquearHotlineContingencia", "Erro SQL: "+ eSQL);
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + eSQL);					
		}
		catch(Exception eG)
		{
			status = Definicoes.TIPO_OPER_ERRO;
			super.log(Definicoes.ERRO, "desbloquearHotlineContingencia", "Erro GPP: "+eG);
			
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			// Chama a funcao para gravar no historico o Processo em questao
			String descricao = "Realizados " + numeroDesbloquiosHotline + " desbloquios de hotline em contingencia";
			String dataFinal  = dataHoraUltimaExecucao != null ? sdf.format(dataHoraUltimaExecucao) : GPPData.dataCompletaForamtada();
			String dataInicio = dataHoraInicioExecucao != null ? sdf.format(dataHoraInicioExecucao) : GPPData.dataCompletaForamtada();
			
			super.gravaHistoricoProcessos(Definicoes.IND_CONTINGENCIA_CONTINGENCIA, dataInicio, dataFinal, status, descricao, dataReferencia);
		
		}	
		super.log(Definicoes.INFO, "desbloquearHotlineContingencia", "Fim");
		
		return retorno;	 	 
	 }
	 
	 /**
	  * Metodo....:atualizaDadosEnvioCRM
	  * Descricao.:Inclui os dados do envio da solicitacao em tabela
	  * @param aMSISDN		- MSISDN que esta sendo incluido
	  * @param aIdAtividade	- Id da atividade (Chave) a ser incluida
	  */
	 public void atualizaDadosEnvioCRM ( String aMSISDN, long aIdAtividade )
	 {
		super.log(Definicoes.INFO, "atualizaDadosEnvioCRM", "Inicio MSISDN "+aMSISDN);

		PREPConexao conexaoPrep = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	 	int retorno = 0;
	 	
	 	try 
		{
			// Registra Data/Hora do início do processo
			String sDataExec = GPPData.dataCompletaForamtada();
			java.sql.Timestamp dataExec = new java.sql.Timestamp(sdf.parse(sDataExec).getTime());
			
			conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
	
			String sql = "INSERT INTO TBL_EXT_CONTINGENCIA_CRM (ID_ATIVIDADE, ID_OPERACAO, IDT_MSISDN, DAT_ATIVIDADE, IDT_ATENDENTE, IDT_STATUS_ATIVIDADE) ";
			       sql+= "VALUES (?, ?, ?, ?, ?, ?)";
			       
			Object valoresInsert[] = {new Long(aIdAtividade), Definicoes.ID_OPERACAO_DESATIVA_CONTINGENCIA, aMSISDN, dataExec, Definicoes.IDT_ATENDENTE_DESATIVA_CONTINGENCIA, Definicoes.STATUS_BLOQUEIO_SOLICITADO};
	
			retorno = conexaoPrep.executaPreparedUpdate(sql, valoresInsert, super.getIdLog());
			
			if (retorno > 0)
			{
				super.log(Definicoes.INFO, "atualizaDadosEnvioCRM", "Dados desbloquio hotline gravados com sucesso");
			}
			else
			{
				super.log(Definicoes.WARN, "atualizaDadosEnvioCRM", "Erro gravacao dados desbloqueio hotline");
			}
		} 
	 	catch (GPPInternalErrorException e) 
		{
			super.log(Definicoes.WARN, "atualizaDadosEnvioCRM", "Erro gravacao dados desbloqueio hotline:" + e);
		}
	 	catch (ParseException e )
		{
			super.log(Definicoes.WARN, "atualizaDadosEnvioCRM", "Erro (PARSER) gravacao dados desbloqueio hotline:" + e);
		}
		finally
		{
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		super.log(Definicoes.INFO, "atualizaDadosEnvioCRM", "Fim");
	}
	 
	 /**
	  * Metodo....:jaSolicitadoDesbloqueio
	  * Descricao.:Este metodo pesquisa para um determinado assinante se este ja foi solicitado
	  *            o desbloqueio Contingencia devido a mudanca de status de FirstTime-Normal
	  * @param msisdn	- Msisdn desejado para pesquisa
	  * @return boolean - Indica se o assinante pesquisado ja teve solicitacao ou nao de desbloqueio
	  * @throws GPPInternalErrorException
	  */
	 public boolean jaSolicitadoDesbloqueio(String msisdn) throws GPPInternalErrorException
	 {
 		super.log(Definicoes.INFO, "jaSolicitadoDesbloqueio", "Inicio MSISDN "+msisdn);
	 	boolean retorno=false;
	 	PREPConexao conexaoPrep = null;
	 	try
		{
	 		// Realiza a pesquisa na tabela de registros de contingencia
	 		// se o assinante ja possui registrado a operacao de contingencia
	 		// HotLine. O Id dessa operacao esta na variavel do Definicoes utilizada
	 		// na pesquisa.
	 		conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
	 		String sqlPesquisa = "SELECT IDT_MSISDN " +
			                       "FROM TBL_EXT_CONTINGENCIA_CRM " +
								  "WHERE IDT_MSISDN  = ? " +
								    "AND ID_OPERACAO = ? ";
	 		Object param[] = {msisdn,Definicoes.ID_OPERACAO_DESATIVA_CONTINGENCIA};
	 		ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPesquisa,param,super.getIdLog());
	 		// Se achou algum registro na tabela, entao indica que pelo menos
	 		// foi solicitado o desbloqueio ContingenciaHotLine.
	 		if (rs.next())
	 			retorno=true;
		}
	 	catch(Exception e)
		{
	 		super.log(Definicoes.WARN, "jaSolicitadoDesbloqueio", "Erro consulta de assinante ja desbloqueado. Erro:" + e);
		}
	 	finally
		{
	 		gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
 		super.log(Definicoes.INFO, "jaSolicitadoDesbloqueio", "Fim");
	 	
	 	return retorno;
	 }
}
