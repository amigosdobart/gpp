//Definicao do Pacote
package com.brt.gpp.aplicacoes.calcularSumarizacaoRelatorio;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.aplicacoes.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

/**
  * Essa classe refere-se ao processo de sumarização dos seguintes tipo de ajuste:
  * a) Acordo Judicial
  * b) Carry Over de Plano Híbrido
  * c) Concatenação de Chamadas
  * d) Constestação
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				21/05/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class sumarizacaoAjustes extends Aplicacoes{
	GerentePoolBancoDados gerenteBanco = null;
	
	//Data passada como parâmetro na execução desse processo
	String dataReferencia = null;
	
	//Mensagem a ser logada na TBL_GER_HISTORICO_PROC_BATCH
	String msgBatch = "";
	
	/**
	 * Metodo...: sumarizacaoAjustes
	 * Descricao: Construtor
	 * @param long		aIdProcesso		ID do processo
	 * @param String	aDataReferencia	Data de Referencia
	 */
	public sumarizacaoAjustes(long aIdProcesso, String aDataReferencia) 
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_SUMARIZACAO_AJUSTES);

		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	

		//Registra data de início do processamento
		this.dataReferencia = aDataReferencia;
	}

	/**
	 * Metodo...: sumarizarAjustes
	 * Descricao: Sumarizacao dos ajustes, metodo principal que coordena os outros
	 * @param
	 * @return short - Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizarAjustes() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"sumarizarAjustes","Inicio");
		
		short retorno = 0;
		String dataInicialProcesso = null;
		String dataFinalProcesso = null;
		ConexaoBancoDados DBConexao = null;


		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			//Registra Início do Processamento
			dataInicialProcesso = GPPData.dataCompletaForamtada();
			
			//Limpa Tabela TBL_REL_AJUSTES
			retorno = this.limpaTabela(DBConexao);
			if (retorno==0)
				this.msgBatch = this.msgBatch + "Limpar Tabela";
		
			//Realiza Sumarização de Ajustes por Acordo Judicial
			retorno = this.sumarizaAcordoJudicial(DBConexao);
			if (retorno==0)
				this.msgBatch = this.msgBatch + "Judicial;";
			
			//Realiza Sumarização de Ajustes por Carry Over de Plano Híbrido 
			retorno = this.sumarizaCarryOver(DBConexao);
			if (retorno==0)
				this.msgBatch = this.msgBatch + "Carry Over;";
				
			//Realiza Sumarização de Ajustes por Concatenação de Chamadas
			this.sumarizaConcatenacao(DBConexao);
			if(retorno==0)
				this.msgBatch = this.msgBatch + "Concatenacao;";
				
			//Realiza Sumarização de Ajustes por Contestação
			this.sumarizaContestacao(DBConexao);
			if(retorno==0)
				this.msgBatch = this.msgBatch + "Contestacao;";
				
			// Registra Data Final do Processo e loga na TBL_GER_HISTORICO_PROC_BATCH
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			super.gravaHistoricoProcessos(Definicoes.IND_REL_AJUSTES,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,"Sumarizacoes ok:"+msgBatch,dataReferencia);
		}
		catch (Exception e)
		{
			//Pega data/hora final do processo batch
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			
			//Logar Processo Batch
			super.gravaHistoricoProcessos(Definicoes.IND_REL_AJUSTES,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_ERRO,e.getMessage(),dataReferencia);
			super.log(Definicoes.ERRO,"sumarizarAjustes","Erro Interno GPP: "+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
		}
		super.log(Definicoes.INFO,"sumarizarAjustes","Fim");
		return retorno;	
	}
	
	/**
	 * Metodo...: sumarizaAcordoJudicial
	 * Descricao: Sumariza os Ajustes por Acordo Judicial
	 * @param ConexaoBancoDados	DBConexao	Coenxão com Banco de dados
	 * @return short 						- Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaAcordoJudicial(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"sumarizaAcordoJudicial","Inicio");

		short retorno = -1;
				
		try
		{
			// Query para inserir novos usuários
			String queryInsert = "INSERT INTO TBL_REL_AJUSTES "+
				"(IDT_FILIAL,TIP_AJUSTE,VLR_AJUSTE_POS,VLR_AJUSTE_NEG,NUM_CHAMADAS,DAT_SUMARIO) "+
				"SELECT "+ 
				"SUBSTR(IDT_MSISDN,3,2),"+
				"?,"+
				"SUM(DECODE(TIP_TRANSACAO,?,VLR_CREDITO,0)) AS VALOR_POSITIVO,"+
				"SUM(DECODE(TIP_TRANSACAO,?,VLR_CREDITO,0)) AS VALOR_NEGATIVO,"+
				"NULL,"+
				"TRUNC(TO_DATE(?,'DD/MM/YYYY')) "+     
				"FROM TBL_REC_RECARGAS "+ 
				"WHERE TIP_TRANSACAO IN (?,?) "+ 		
				"AND DAT_RECARGA BETWEEN TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') "+
				"GROUP BY SUBSTR(IDT_MSISDN,3,2)";
			
				Object paramRecarga[] = {Definicoes.IND_REL_AJUSTE_JUDICIAL,Definicoes.TRANSACTION_TYPE_AJUSTE_JUDICIAL,
					Definicoes.TRANSACTION_TYPE_AJUSTE_JUDICIAL_DEBITO,dataReferencia,
					Definicoes.TRANSACTION_TYPE_AJUSTE_JUDICIAL,Definicoes.TRANSACTION_TYPE_AJUSTE_JUDICIAL_DEBITO,
					// Se a data de referencia for dd/mm/yyyy, a linha abaixo gera 29/mm-1/yyyy 00:00:00
					GPPData.formataDataHora("29/"+(new Integer(dataReferencia.substring(3,5)).intValue()-1 + dataReferencia.substring(5))+" "+Definicoes.HORA_INICIO_DIA),
					// Se a data de referencia for dd/mm/yyyy, a linha abaixo gera 28/mm/yyyy 23:59:59
					"28/"+dataReferencia.substring(3)+" "+Definicoes.HORA_FINAL_DIA};

				// Popula a tabela TBL_REL_AJUSTES com totalizações
				DBConexao.executaPreparedQuery(queryInsert,paramRecarga,super.getIdLog());
				
				retorno=0;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaAcordoJudicial","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		super.log(Definicoes.INFO,"sumarizaAcordoJudicial","Fim");
		return retorno;

		
	}
	
	/**
	 * Metodo...: sumarizaCarryOver
	 * Descricao: Sumariza os Ajustes por Carry Over
	 * @param	ConexaoBancoDados	DBConexao	Conexão com Banco de Dados
	 * @return short 							- Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaCarryOver(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"sumarizaCarryOver","Inicio");

		short retorno = -1;

		try
		{
			// Query para inserir novos usuários
			String queryInsert = "INSERT INTO TBL_REL_AJUSTES "+
				"(IDT_FILIAL,TIP_AJUSTE,VLR_AJUSTE_POS,VLR_AJUSTE_NEG,NUM_CHAMADAS,DAT_SUMARIO) "+
				"SELECT "+ 
				"SUBSTR(IDT_MSISDN,3,2) AS FILIAL,"+
				"?,"+
				"NULL,"+
				"SUM(VLR_CREDITO),"+
				"NULL,"+
				"TRUNC(TO_DATE(?,'DD/MM/YYYY')) "  +  
				"FROM TBL_REC_RECARGAS " +
				"WHERE "+
				"TIP_TRANSACAO=? "+
				"AND DAT_RECARGA BETWEEN TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') "+
				"GROUP BY SUBSTR(IDT_MSISDN,3,2)";
			
				Object paramRecarga[] = {Definicoes.IND_REL_AJUSTE_CARRYOVER,dataReferencia,
					Definicoes.TRANSACTION_TYPE_AJUSTE_CARRYOVER,
					// Se a data de referencia for dd/mm/yyyy, a linha abaixo gera 29/mm-1/yyyy 00:00:00
					GPPData.formataDataHora("29/"+(new Integer(dataReferencia.substring(3,5)).intValue()-1 + dataReferencia.substring(5))+" "+Definicoes.HORA_INICIO_DIA),
					// Se a data de referencia for dd/mm/yyyy, a linha abaixo gera 28/mm/yyyy 23:59:59
					"28/"+dataReferencia.substring(3)+" "+Definicoes.HORA_FINAL_DIA};

				// Popula a tabela TBL_REL_AJUSTES com totalizações
				DBConexao.executaPreparedQuery(queryInsert,paramRecarga,super.getIdLog());
				
				retorno = 0;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaCarryOver","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		super.log(Definicoes.INFO,"sumarizaCarryOver","Fim");
		return retorno;
	}
	
	/**
	 * Metodo...: sumarizaConcatenacao
	 * Descricao: Sumariza os Ajustes por Concatenação de Chamadas
	 * @param	ConexaoBancoDados	DBConexao	Conexão com Banco de Dados
	 * @return short 							- Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaConcatenacao(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"sumarizaConcatenacao","Inicio");

		short retorno = -1;
	
		try
		{
			// Query para inserir novos usuários
			String queryInsert = "INSERT INTO TBL_REL_AJUSTES "+
				"(IDT_FILIAL,TIP_AJUSTE,VLR_AJUSTE_POS,VLR_AJUSTE_NEG,NUM_CHAMADAS,DAT_SUMARIO) "+
				"SELECT "+ 
				"SUBSTR(IDT_MSISDN,3,2),"+
				"?,"+
				"SUM(VLR_CREDITO),"+
				"NULL,"+
				"COUNT(*) AS NUM_CHAMADAS,"+
				"TRUNC(TO_DATE(?,'DD/MM/YYYY')) "+     
				"FROM TBL_REC_RECARGAS "+
				"WHERE TIP_TRANSACAO=? "+
				"AND DAT_RECARGA BETWEEN TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') "+
				"GROUP BY SUBSTR(IDT_MSISDN,3,2)";
			
				Object paramRecarga[] = {Definicoes.IND_REL_AJUSTE_CONCATENACAO,dataReferencia,
					Definicoes.TRANSACTION_TYPE_AJUSTE_CONCATENACAO,
					// Se a data de referencia for dd/mm/yyyy, a linha abaixo gera 29/mm-1/yyyy 00:00:00
					GPPData.formataDataHora("29/"+(new Integer(dataReferencia.substring(3,5)).intValue()-1 + dataReferencia.substring(5))+" "+Definicoes.HORA_INICIO_DIA),
					// Se a data de referencia for dd/mm/yyyy, a linha abaixo gera 28/mm/yyyy 23:59:59
					"28/"+dataReferencia.substring(3)+" "+Definicoes.HORA_FINAL_DIA};

				// Popula a tabela TBL_REL_AJUSTES com totalizações
				DBConexao.executaPreparedQuery(queryInsert,paramRecarga,super.getIdLog());
				
				retorno = 0;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaConcatenacao","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		super.log(Definicoes.INFO,"sumarizaConcatenacao","Fim");
		return retorno;
	}
	
	/**
	 * Metodo...: sumarizaContestacao
	 * Descricao: Sumariza os Ajustes por Contestação
	 * @param	ConexaoBancoDados	DBConexao	Conexão com Banco de Dados
	 * @return short 							- Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaContestacao(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"sumarizaContestacao","Inicio");

		short retorno = -1;


		try
		{
			// Query para inserir novos usuários
			String queryInsert = "INSERT INTO TBL_REL_AJUSTES "+
			"(IDT_FILIAL,TIP_AJUSTE,VLR_AJUSTE_POS,VLR_AJUSTE_NEG,NUM_CHAMADAS,DAT_SUMARIO) "+
			"SELECT "+ 
			"SUBSTR(IDT_MSISDN,3,2),"+
			"?,"+
			"SUM(VLR_CREDITO),"+
			"NULL,NULL,"+
			"TRUNC(TO_DATE(?,'dd/mm/yyyy')) AS DAT_SUMARIO "+
			"FROM TBL_REC_RECARGAS "+
			"WHERE TIP_TRANSACAO=? "+
			"AND DAT_RECARGA BETWEEN TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') "+
			"GROUP BY SUBSTR(IDT_MSISDN,3,2)";
			
			Object paramRecarga[] = {Definicoes.IND_REL_AJUSTE_CONTESTACAO,dataReferencia,
					Definicoes.TRANSACTION_TYPE_AJUSTE_CONTESTACAO,
				// Se a data de referencia for dd/mm/yyyy, a linha abaixo gera 29/mm-1/yyyy 00:00:00
				GPPData.formataDataHora("29/"+(new Integer(dataReferencia.substring(3,5)).intValue()-1 + dataReferencia.substring(5))+" "+Definicoes.HORA_INICIO_DIA),
				// Se a data de referencia for dd/mm/yyyy, a linha abaixo gera 28/mm/yyyy 23:59:59
				"28/"+dataReferencia.substring(3)+" "+Definicoes.HORA_FINAL_DIA};

				// Popula a tabela TBL_REL_AJUSTES com totalizações
				DBConexao.executaPreparedQuery(queryInsert,paramRecarga,super.getIdLog());
				
				retorno = 0;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContestação","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		super.log(Definicoes.INFO,"sumarizaContestacao","Fim");
		return retorno;
	}	
	
	/***
	 * Metodo...: limpaTabela
	 * Descricao: Limpa Registros da TBL_REL_AJUSTES
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de Dados
	 * @return	short						0, ok; !0, nok
	 * @throws GPPInternalErrorException
	 */
	private short limpaTabela(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"limpaTabela","Inicio");

		short retorno = -1;

		try
		{
			// Query para limpar tabela
			String queryInsert = "DELETE TBL_REL_AJUSTES";
			
			// Popula a tabela TBL_REL_AJUSTES com totalizações
			DBConexao.executaQuery(queryInsert,super.getIdLog());
				
			retorno = 0;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"limpaTabela","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		super.log(Definicoes.INFO,"limpaTabela","Fim do limpaTabela");
		return retorno;
	}
}
