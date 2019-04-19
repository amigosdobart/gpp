// Definicao do Pacote
package com.brt.gpp.aplicacoes;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivos de Import Internos
import com.brt.gpp.comum.*;

// Imports de Java
import java.sql.*;

/**
  *
  * Este arquivo refere-se a classe abstrata Aplicacoes
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Camile Cardoso Couto
  * Data: 				17/03/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class Aplicacoes 
{
	//	Variaveis Membro
	protected GerentePoolLog 		log 			= null; // Gerente de LOG
	protected long					logId			= 0;
	protected String				nomeClasse 		= null;
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	
	/**
	 * Metodo...: Aplicacoes
	 * Descricao: Construtor 
	 * @param	aLogId		- Identificador do processo
	 * @param	aNomeClasse	- Nome da classe
	 * @return	
	 */
	public Aplicacoes (long aLogId, String aNomeClasse)
	{
		this.logId			= aLogId;
		this.nomeClasse 	= aNomeClasse;
		
		// Obtem referencia ao gerente de LOG
		this.log = GerentePoolLog.getInstancia(this.getClass());
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(this.logId);
	}
	
	/**
	  * Metodo...: log
	  * Descricao: Chama o metodo log da classe GerentePoolLog
	  * @param	aTipo 		- Tipo da severidade do log	  
	  * @param	aMetodo 	- Metodo que chamou o log	  
	  * @param	aMensagem 	- Mensagem que deve ser escrita no log	
	  * @return
	  * @throws  
	  */
	public void log (int aTipo, String aMetodo, String aMensagem)
	{
		log.log(this.logId, aTipo, this.nomeClasse, aMetodo, aMensagem);
	}
	
	/**
	  * Metodo...: getLogId
	  * Descricao: Retorna o ID do log 
	  * @param
	  * @return		LodID 	- Id do log 	  
	  * @throws  
	  */
	public long getIdLog ( )
	{
		return this.logId;
	}
	
	/**
	  * Metodo...: gravaHistoricoProcessos
	  * Descricao: Fornece um historico dos processos batch
	  * @param	conexaoPrep		- instancia da conexao do banco	  
	  * @param	idProcessoBatch	- Identificador do Processo Batch	  
	  * @param	dataInicio 		- Data de Inicio do Processo Batch	  
	  * @param	dataFim 		- Data de Fim do Processo Batch  
	  * @param	statusExecucao 	- Status do Processo Batch	  
	  * @param	descObs 		- Descricao do Processo Batch	  
	  * @param	dataProcessamento- Data do processamento do Processo Batch 
	  * @return
	  * @throws GPPInternalErrorException
	  */
	public void gravaHistoricoProcessos (int idProcessoBatch, String dataInicio, String dataFim, String statusExecucao, String descObs, String dataProcessamento)
										throws GPPInternalErrorException
	{
		PREPConexao conexaoPREP = null;
		
		try
		{
			// Busca uma conexao de banco de dados		
			conexaoPREP = this.gerenteBancoDados.getConexaoPREP(this.getIdLog());

			// Insere na tabela o historico dos processos batch
			String sql = "INSERT INTO TBL_GER_HISTORICO_PROC_BATCH " +
						" (ID_PROCESSO_BATCH, DAT_INICIAL_EXECUCAO, DAT_FINAL_EXECUCAO, " +
						" IDT_STATUS_EXECUCAO, DES_OBSERVACAO, DAT_PROCESSAMENTO) VALUES " +
						" (? ,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), " +
						" TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, ?," +
						" TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
			
			Object parametros[] = {new Integer(idProcessoBatch),  dataInicio, dataFim, statusExecucao, descObs, dataProcessamento};
	
			if (conexaoPREP.executaPreparedUpdate(sql, parametros, this.logId) > 0)
			{
				this.log(Definicoes.DEBUG, "gravaHistoricoProcessos", "Processo Batch inserido com sucesso.");
			}
			else
			{
				this.log(Definicoes.ERRO, "gravaHistoricoProcessos", "Erro: Nao foi possivel inserir o Processo Batch.");
			}
		}
		catch (GPPInternalErrorException e)
		{
			this.log(Definicoes.ERRO, "gravaHistoricoProcessos", "Erro no processo de gravaHistoricoProcessos: Nao conseguiu pegar conexao com banco de dados.");
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, this.getIdLog());
		}
	}
	
	/***
	 * Metodo...: foiExecutado
	 * Descricao: Verifica se consta uma execução do Processo Batch na tbl_Ger_historico_proc_Bath
	 * 				na data em questão (referencia: dat_processamento)
	 * @param String		aData		Data que será procurada na tbl_ger_historico_proc_batch
	 * @param PREPConexao 	conexaoPrep	Conexão com Banco de Dados
	 * @return	boolean		true - já existe uma execuçao com sucesso para esse processo nessa data
	 * 						false - caso contrário
	 */
	protected boolean foiExecutado(String aData, int idProcessoBatch, PREPConexao conexaoPrep)
	{
		boolean retorno = false;
	
		// Procura por uma execução desse processo no dia em questão
		String sqlHistory = "SELECT * FROM TBL_GER_HISTORICO_PROC_BATCH WHERE ID_PROCESSO_BATCH = ? AND " +
				"DAT_PROCESSAMENTO BETWEEN TO_DATE(?||' 00:00:00','DD/MM/YYYY hh24:mi:ss') AND TO_DATE(?||' 23:59:59','DD/MM/YYYY hh24:mi:ss')";
		
		Object[] parHistory = { new Integer(idProcessoBatch), aData, aData };
		
		try
		{
			ResultSet rsHistory = conexaoPrep.executaPreparedQuery(sqlHistory, parHistory, this.logId);
		
			// Verifica se há registro de execução de Processo hoje
			if(rsHistory.next())
			{
				retorno = true;
			}			
		}
		catch(Exception sqlE)
		{
			this.log(Definicoes.ERRO, "foiExecutado", "Erro: Problemas Banco de Dados");
		}

		return retorno;
	}
}
