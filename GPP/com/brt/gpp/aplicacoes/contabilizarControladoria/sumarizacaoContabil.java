//Definicao do Pacote
package com.brt.gpp.aplicacoes.contabilizarControladoria;

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
  * Essa classe refere-se ao processo de sumarização Contábil:
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Alberto Magno
  * Data: 				07/07/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class sumarizacaoContabil extends Aplicacoes{
	GerentePoolBancoDados gerenteBanco = null;
	
	//Data passada como parâmetro na execução desse processo
	String dataReferencia = null;
	
	//Mensagem a ser logada na TBL_GER_HISTORICO_PROC_BATCH
	String msgBatch = "";
	
	/**
	 * Metodo...: sumarizacaoContabil
	 * Descricao: Construtor
	 * @param long		aIdProcesso		ID do processo
	 * @param String	aDataReferencia	Data de Referencia
	 */
	public sumarizacaoContabil(long aIdProcesso, String aDataReferencia) 
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_SUMARIZACAO_CONTABIL);

		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	

		//Registra data de início do processamento
		this.dataReferencia = aDataReferencia;
	}

	/**
	 * Metodo...: sumarizarContabilidade
	 * Descricao: Sumarizacao Contabil, metodo principal que coordena os outros
	 * @param
	 * @return short - Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizarContabilidade() throws GPPInternalErrorException
	{
		short retorno = 1;
		String dataInicialProcesso = null;
		String dataFinalProcesso = null;
		ConexaoBancoDados DBConexao = null;

		super.log(Definicoes.DEBUG,"sumarizarContabilidade","Inicio DATA "+dataReferencia);

		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			//Registra Início do Processamento
			dataInicialProcesso = GPPData.dataCompletaForamtada();
			
		
			//Realiza Sumarização Contabil
			retorno = (short)
			(sumarizaChamadas(DBConexao)<0?-1:
			sumarizaRecargas(DBConexao)<0?-1:
			sumarizaSaldoAssinantes(DBConexao)<0?-1:
			sumarizaVoucher(DBConexao)<0?-1:
			sumarizaRecargasFace(DBConexao));
			
			// Registra Data Final do Processo e loga na TBL_GER_HISTORICO_PROC_BATCH
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			super.gravaHistoricoProcessos(Definicoes.IND_REL_CONTABIL,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,"Sumarizacoes ok:"+msgBatch,dataReferencia);
		}
		catch (Exception e)
		{
			//Pega data/hora final do processo batch
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			
			//Logar Processo Batch
			super.gravaHistoricoProcessos(Definicoes.IND_REL_CONTABIL,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_ERRO,e.getMessage(),dataReferencia);
			super.log(Definicoes.ERRO,"sumarizarContabilidade","ERRO GPP:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
			super.log(Definicoes.DEBUG,"sumarizarContabilidade","Fim");
		}
		return retorno;	
	}
	
	/**
	 * Metodo...: sumarizaChamadas
	 * Descricao: Sumariza os dados Contábeis
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @return short 						- Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaChamadas(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		short retorno = 1;
		super.log(Definicoes.DEBUG,"sumarizaContabilidade-Chamadas","Inicio");

		try
		{
/*******************************************************************************************************************/
			// Limpa tabela de chamadas por minuto
//			String query = "DELETE FROM tbl_rel_cdr_minuto WHERE dat_processamento = to_date(?, 'dd/mm/yyyy')";
			// Deleta TBL_REL_CDR_HORA
//			super.log(Definicoes.DEBUG,"sumarizaContabil","Delecao dados tabela de chamadas por MINUTO");
//			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
/*******************************************************************************************************************/
			// Preenche tabela de chamadas por hora
/*			query = "INSERT INTO tbl_rel_cdr_minuto(idt_minuto, dat_processamento, tip_cdr, idt_sentido,qtd_registros)" +
				" SELECT " +
				"            trunc(a.start_time/60), " +
				"            TRUNC(a.timestamp), " +
				"            a.tip_cdr, " +
				"            b.idt_sentido, " +
				"            count(1) " +
				" FROM " +
				"            tbl_ger_cdr a, " +
				"            tbl_ger_tip_transacao_tecnomen b " +
				" where " +
				"			 a.timestamp between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
				"   		 to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') and " +
				"            a.transaction_type = b.transaction_type " +
				" group by " +
				"            TRUNC(a.timestamp), " +
				"			 trunc(a.start_time/60), " +
				"			a.tip_cdr, b.idt_sentido";
			// Popula TBL_REL_CDR_HORA
			super.log(Definicoes.DEBUG,"sumarizaContabil","Populando dados tabela de chamadas por HORA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia},super.getIdLog());
*/

/*******************************************************************************************************************/
			// Limpa tabela de chamadas por hora
			String query = "DELETE FROM TBL_REL_CDR_HORA WHERE DAT_CDR = to_date(?, 'DD/MM/YYYY')";
			// Deleta TBL_REL_CDR_HORA
			super.log(Definicoes.DEBUG,"sumarizaContabil","Delecao dados tabela de chamadas por HORA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
/*******************************************************************************************************************/
			// Preenche tabela de chamadas por hora
			query = "insert into TBL_REL_CDR_HORA" +
				"(IDT_HORA_CDR,DAT_CDR,IDT_CODIGO_NACIONAL,IDT_PLANO,NUM_CSP,IDT_MODULACAO," +
				"IDT_SENTIDO,TIP_CHAMADA,QTD_REGISTROS,VLR_TOTAL,VLR_DURACAO,VLR_TOTAL_SI,IDT_ROAMING_ORIGIN, " +
				" vlr_total_principal, vlr_total_principal_si," +
				" vlr_total_bonus, vlr_total_bonus_si," +
				" vlr_total_sms, vlr_total_sms_si," +
				" vlr_total_dados, vlr_total_dados_si, ind_ff  "+
				") " +
				"SELECT "+
			" IDT_HORA, DAT_PROCESSAMENTO, IDT_DDD, IDT_PLANO,NUM_CSP,IDT_MODULACAO, "+
			" IDT_SENTIDO,TIP_CHAMADA, " +
			" SUM(QTD_REGISTROS)," +
			" SUM(VLR_CHAMADAS)," +
			" SUM(VLR_DURACAO)," +
			" nvl(SUM(VLR_CHAMADAS*(1-ALIQUOTA.VLR_ALIQUOTA)),0)," +
			" DECODE(IDT_ROAMING_ORIGIN,NULL,IDT_DDD,IDT_ROAMING_ORIGIN), " +
			" SUM(vlr_total_principal), nvl(SUM(vlr_total_principal*(1-ALIQUOTA.VLR_ALIQUOTA)),0), " +
			" SUM(vlr_total_bonus), nvl(SUM(vlr_total_bonus*(1-ALIQUOTA.VLR_ALIQUOTA)),0)," +
			" SUM(vlr_total_sms), nvl(SUM(vlr_total_sms*(1-ALIQUOTA.VLR_ALIQUOTA)),0)," +
			" SUM(vlr_total_dados), nvl(SUM(vlr_total_dados*(1-ALIQUOTA.VLR_ALIQUOTA)),0), 2 "+	// ind_ff = 2
			" FROM ( "+
			" select trunc(A.START_TIME/3600)                                 AS IDT_HORA, "+ 
			"                 TRUNC(A.TIMESTAMP)                   			  AS DAT_PROCESSAMENTO, "+ 
			"                 substr(A.SUB_ID,3,2)                            AS IDT_DDD, "+
			"                 DECODE(nvl(a.profile_id,0),0,nvl(FNC_PLANO_ASSINANTE(A.TIMESTAMP,A.SUB_ID),0),A.PROFILE_ID) AS IDT_PLANO,  "+
			"                 nvl(decode(A.NUM_CSP,'"+Definicoes.IND_SEM_PLANO+"','00',A.NUM_CSP),'00') AS NUM_CSP, "+
			"                 nvl(decode(A.IDT_MODULACAO,'"+Definicoes.IND_MODULACAO_X+"','"+Definicoes.IND_MODULACAO_FLAT+"',A.IDT_MODULACAO),'"+Definicoes.IND_MODULACAO_FLAT+"') AS IDT_MODULACAO, "+
			"                 C.IDT_SENTIDO                                   AS IDT_SENTIDO, "+
			"                 nvl (A.TIP_CHAMADA,'OUTROS')                    AS TIP_CHAMADA, "+
			"                 count(1)                                        AS QTD_REGISTROS,  "+
			"                 sum(abs((nvl(account_balance_delta,0)+nvl(bonus_balance_delta,0)+nvl(sm_balance_delta,0)+nvl(data_balance_delta,0))-decode(substr(tip_deslocamento,0,2),'"+Definicoes.IND_PREFIXO_DESLOCAMENTO+"',abs(A.interconnection_cost),0))/"+Definicoes.TECNOMEN_MULTIPLICADOR+") AS VLR_CHAMADAS, "+
			"                 sum(A.CALL_DURATION)                            AS VLR_DURACAO, " +
			"				  SUBSTR(CELL_NAME,4,2) 						  AS IDT_ROAMING_ORIGIN, " +
			"				  sum(nvl(A.account_balance_delta,0)/"+Definicoes.TECNOMEN_MULTIPLICADOR+") as vlr_total_principal, " +
			"				  sum(nvl(bonus_balance_delta,0)/"+Definicoes.TECNOMEN_MULTIPLICADOR+") as vlr_total_bonus, " +
			"				  sum(nvl(data_balance_delta,0)/"+Definicoes.TECNOMEN_MULTIPLICADOR+") as vlr_total_dados, " +  
			"				  sum(nvl(sm_balance_delta,0)/"+Definicoes.TECNOMEN_MULTIPLICADOR+") as vlr_total_sms " +			
			"           from  "+
			"                 TBL_GER_CDR A, "+
			"                 TBL_GER_TIP_TRANSACAO_TECNOMEN C "+
			"           where  "+
			"				  a.timestamp between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
			"   			  to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') and " +
			"                 A.TRANSACTION_TYPE = C.TRANSACTION_TYPE  "+
			"         group by  "+
			"                 trunc(A.START_TIME/3600), "+ 
			"                 TRUNC(A.TIMESTAMP),  "+
			"                 substr(A.SUB_ID,3,2), "+
			"                 DECODE(nvl(a.profile_id,0),0,nvl(FNC_PLANO_ASSINANTE(A.TIMESTAMP,A.SUB_ID),0),A.PROFILE_ID),  "+
			"                 nvl(decode(A.NUM_CSP,'"+Definicoes.IND_SEM_PLANO+"','00',A.NUM_CSP),'00'), "+
			"                 nvl(decode(A.IDT_MODULACAO,'"+Definicoes.IND_MODULACAO_X+"','"+Definicoes.IND_MODULACAO_FLAT+"',A.IDT_MODULACAO),'"+Definicoes.IND_MODULACAO_FLAT+"'), "+
			"                 C.IDT_SENTIDO, "+
			"                 nvl (A.TIP_CHAMADA,'OUTROS'), " +
			"				  SUBSTR(CELL_NAME,4,2) "+     
			" union "+
			" select trunc(A.START_TIME/3600)                                 AS IDT_HORA, "+ 
			"                 TRUNC(A.TIMESTAMP)				                      AS DAT_PROCESSAMENTO, "+ 
			"                 substr(A.SUB_ID,3,2)                            AS IDT_DDD, "+
			"                 DECODE(nvl(a.profile_id,0),0,nvl(FNC_PLANO_ASSINANTE(A.TIMESTAMP,A.SUB_ID),0),A.PROFILE_ID) AS IDT_PLANO, "+ 
			"                 nvl(decode(A.NUM_CSP,'"+Definicoes.IND_SEM_PLANO+"','00',A.NUM_CSP),'00') AS NUM_CSP, "+
			"                 nvl(decode(A.IDT_MODULACAO,'"+Definicoes.IND_MODULACAO_X+"','"+Definicoes.IND_MODULACAO_FLAT+"',A.IDT_MODULACAO),'"+Definicoes.IND_MODULACAO_FLAT+"') AS IDT_MODULACAO, "+
			"                 C.IDT_SENTIDO                                   AS IDT_SENTIDO, "+
			"                 A.tip_deslocamento       						  AS TIP_CHAMADA, "+
			"                 count(1)                                        AS QTD_REGISTROS, "+ 
			"                 sum(abs(A.interconnection_cost)/"+Definicoes.TECNOMEN_MULTIPLICADOR+") AS VLR_CHAMADAS, "+
			"                 sum(A.CALL_DURATION)                            AS VLR_DURACAO, "+
			"				  SUBSTR(CELL_NAME,4,2) 						  AS IDT_ROAMING_ORIGIN, " +
			"					0,0,0,0 "+
			"           from "+ 
			"                 TBL_GER_CDR A, "+
			"                 TBL_GER_TIP_TRANSACAO_TECNOMEN C "+
			"           where  "+
			"				  a.timestamp between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
			"   			  to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') and " +
			"                 A.TRANSACTION_TYPE = C.TRANSACTION_TYPE and "+
			"                 substr(tip_deslocamento,0,2)='"+Definicoes.IND_PREFIXO_DESLOCAMENTO+"' "+
			"         group by  "+
			"                 trunc(A.START_TIME/3600), "+ 
			"                 TRUNC(A.TIMESTAMP),  "+
			"                 substr(A.SUB_ID,3,2), "+
			"                 DECODE(nvl(a.profile_id,0),0,nvl(FNC_PLANO_ASSINANTE(A.TIMESTAMP,A.SUB_ID),0),A.PROFILE_ID),  "+
			"                 nvl(decode(A.NUM_CSP,'"+Definicoes.IND_SEM_PLANO+"','00',A.NUM_CSP),'00'), "+
			"                 nvl(decode(A.IDT_MODULACAO,'"+Definicoes.IND_MODULACAO_X+"','"+Definicoes.IND_MODULACAO_FLAT+"',A.IDT_MODULACAO),'"+Definicoes.IND_MODULACAO_FLAT+"'), "+
			"                 C.IDT_SENTIDO, "+
			"                 A.tip_deslocamento, " +
			"				  SUBSTR(CELL_NAME,4,2) "+
			"  ) , " +
			"  ( " +
			"		select " +
			"			idt_codigo_nacional, " +
			"			vlr_aliquota " +
			"		from TBL_GER_ALIQUOTA ALIQUOTA " +
			"		where " +
			"			ALIQUOTA.IDT_IMPOSTO = 'ICMS' AND" +
			"			ALIQUOTA.dat_inicial_validade <= to_date(?, 'dd/mm/yyyy') AND " +
			"			(ALIQUOTA.dat_final_validade  >= to_date(?, 'dd/mm/yyyy') or ALIQUOTA.dat_final_validade is null) " +
			"  )ALIQUOTA "+
			"  WHERE  "+
			"      IDT_DDD = ALIQUOTA.idt_codigo_nacional (+) " +
			"	GROUP BY " +
			"	IDT_HORA, DAT_PROCESSAMENTO, IDT_DDD, IDT_PLANO,NUM_CSP,IDT_MODULACAO, "+
			" IDT_SENTIDO,TIP_CHAMADA," +
			" DECODE(IDT_ROAMING_ORIGIN,NULL,IDT_DDD,IDT_ROAMING_ORIGIN)"; 

			// Popula TBL_REL_CDR_HORA
			super.log(Definicoes.DEBUG,"sumarizaContabil","Populando dados tabela de chamadas por HORA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia,dataReferencia,dataReferencia,dataReferencia,dataReferencia},super.getIdLog());
/*******************************************************************************************************************/				
			// Limpa tabela de chamadas por dia
			query = "DELETE FROM TBL_REL_CDR_DIA WHERE DAT_CDR = to_date(?, 'DD/MM/YYYY')";
			// Deleta TBL_REL_CDR_DIA
			super.log(Definicoes.DEBUG,"sumarizaContabil","Delecao dados tabela de chamadas por DIA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
/*******************************************************************************************************************/				
			// Preenche tabela de chamadas por dia
			query = "insert into TBL_REL_CDR_DIA" +
				"(DAT_CDR,IDT_CODIGO_NACIONAL,IDT_PLANO,NUM_CSP,IDT_MODULACAO," +
				"IDT_SENTIDO,TIP_CHAMADA,IDT_ROAMING_ORIGIN,QTD_REGISTROS,VLR_TOTAL,VLR_DURACAO,VLR_TOTAL_SI, " +
				" vlr_total_principal, vlr_total_principal_si," +
				" vlr_total_bonus, vlr_total_bonus_si," +
				" vlr_total_sms, vlr_total_sms_si," +
				" vlr_total_dados, vlr_total_dados_si, ind_ff  "+
				") "+
				"select " +
					"DAT_CDR AS DAT_CDR,  " +
					"IDT_CODIGO_NACIONAL, " +
					"IDT_PLANO, " +
					"NUM_CSP, " +
					"IDT_MODULACAO, " +
					"IDT_SENTIDO, " +
					"TIP_CHAMADA,  " +
					"IDT_ROAMING_ORIGIN, " +
					"sum(QTD_REGISTROS), " +
					"sum(VLR_TOTAL), " +
					"sum(VLR_DURACAO), " +
					"sum(VLR_TOTAL_SI),  " +
					"sum(vlr_total_principal), sum(vlr_total_principal_si)," +
					"sum(vlr_total_bonus), sum(vlr_total_bonus_si)," +
					"sum(vlr_total_sms), sum(vlr_total_sms_si)," +
					"sum(vlr_total_dados), sum(vlr_total_dados_si), ind_ff  "+				
					"from  TBL_REL_CDR_HORA " +
				"where    " +
				"  DAT_CDR between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
				"  to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') " +
				"group by  " +
					"DAT_CDR, " +
					"IDT_CODIGO_NACIONAL, " +
					"IDT_PLANO, " +
					"NUM_CSP, " +
					"IDT_MODULACAO, " +
					"IDT_SENTIDO,  " +
					"TIP_CHAMADA,  " +
					"IDT_ROAMING_ORIGIN, ind_ff ";
			// Popula TBL_REL_CDR_DIA
			super.log(Definicoes.DEBUG,"sumarizaContabil","Populando dados tabela de chamadas por DIA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia},super.getIdLog());
/*******************************************************************************************************************/				
			return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			
		}
		finally
		{
			super.log(Definicoes.DEBUG,"sumarizaContabil","Fim");
		}

	}
	
	/**
	 * Metodo...: sumarizaRecargas
	 * Descricao: Sumariza os dados Contábeis de Recargas 
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @return short 						- Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaRecargas(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		short retorno = 1;
		super.log(Definicoes.DEBUG,"sumarizaContabilidade-Recargas","Inicio");

		try
		{
/*******************************************************************************************************************/
			// Limpa tabela de rel Recargas/DIA
//			String query = "DELETE FROM TBL_REL_RECARGAS WHERE DAT_RECARGA = to_date(?, 'dd/mm/yyyy')";
			// Deleta TBL_REL_RECARGAS
			super.log(Definicoes.DEBUG,"sumarizaContabil","Delecao dados tabela de recargas por dia");
//			TODO_OLD:Comentar				DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
/*******************************************************************************************************************/
			// Preenche tabela de recargas por dia
//			query = "insert into TBL_REL_RECARGAS" +
//				"(DAT_RECARGA,IDT_CODIGO_NACIONAL,IDT_PLANO,ID_TIPO_RECARGA,ID_TIPO_CREDITO,COD_RETORNO,ID_CANAL,ID_ORIGEM," +
//				"ID_SISTEMA_ORIGEM,QTD_REGISTROS, VLR_TOTAL, VLR_TOTAL_SI, " +
//				" vlr_total_principal, vlr_total_principal_si," +
//				" vlr_total_bonus, vlr_total_bonus_si," +
//				" vlr_total_sms, vlr_total_sms_si," +
//				" vlr_total_dados, vlr_total_dados_si  "+
//				")" +
//				"SELECT DAT_PROCESSAMENTO,IDT_DDD,IDT_PLANO,ID_TIPO_RECARGA,ID_TIPO_CREDITO,COD_RETORNO," +
//				"ID_CANAL,ID_ORIGEM,ID_SISTEMA_ORIGEM,QTD_REGISTROS, " +
//				"VLR_TOTAL,nvl(VLR_TOTAL*(1-ALIQUOTA.VLR_ALIQUOTA),0), " +
//				" vlr_total_principal, nvl(vlr_total_principal*(1-ALIQUOTA.VLR_ALIQUOTA),0), " +
//				" vlr_total_bonus, nvl(vlr_total_bonus*(1-ALIQUOTA.VLR_ALIQUOTA),0)," +
//				" vlr_total_sms, nvl(vlr_total_sms*(1-ALIQUOTA.VLR_ALIQUOTA),0)," +
//				" vlr_total_dados, nvl(vlr_total_dados*(1-ALIQUOTA.VLR_ALIQUOTA),0) "+				"FROM " +
//						"(select " +
//							"      trunc(a.DAT_RECARGA) as DAT_PROCESSAMENTO, " +
//							"      substr(a.IDT_MSISDN,3,2) as IDT_DDD, " +
//							"      decode(nvl(idt_plano_preco,0),0,nvl(FNC_PLANO_ASSINANTE(A.DAT_RECARGA,A.IDT_MSISDN),0),idt_plano_preco) AS IDT_PLANO, " +
//							"      a.ID_TIPO_RECARGA AS ID_TIPO_RECARGA, " +
//							"      a.id_tipo_credito as ID_TIPO_CREDITO, " +
//							"      0 as COD_RETORNO, " +
//							"      a.ID_CANAL as ID_CANAL, " +
//							"      A.ID_ORIGEM as ID_ORIGEM, " +
//							"      a.ID_SISTEMA_ORIGEM as ID_SISTEMA_ORIGEM, " +
//							"      count(1) as QTD_REGISTROS, " +
//							"      sum(nvl(A.vlr_credito_principal,0)+nvl(a.vlr_credito_bonus,0)+nvl(a.vlr_credito_sms,0)+nvl(a.vlr_credito_gprs,0)) as VLR_TOTAL, " +
//							"	   sum(nvl(A.vlr_credito_principal,0)) as vlr_total_principal, " +
//							"	   sum(nvl(a.vlr_credito_bonus,0)) as vlr_total_bonus, " +
//							"	   sum(nvl(a.vlr_credito_gprs,0)) as vlr_total_dados, " +
//							"	   sum(nvl(a.vlr_credito_sms,0)) as vlr_total_sms " +							
//							" from " +
//							"  		TBL_REC_RECARGAS a " +
//							" where " +
//							"		a.DAT_RECARGA between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
//							"   	to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') " +
//							" group by " +
//							"   	trunc(a.DAT_RECARGA), " +
//							"  		decode(nvl(idt_plano_preco,0),0,nvl(FNC_PLANO_ASSINANTE(A.DAT_RECARGA,A.IDT_MSISDN),0),idt_plano_preco), " +
//							"  		substr(a.IDT_MSISDN,3,2), " +
//							"  		a.TIP_TRANSACAO, " +
//							"      	a.ID_TIPO_RECARGA, " +
//							"       a.id_tipo_credito, " +
//							"    	a.ID_CANAL, " +
//							"		A.ID_ORIGEM, " +
//							"		a.ID_SISTEMA_ORIGEM, " +
//							"    	nvl(a.ID_VALOR,0), " +
//							"  		0  " +
/* ************************* Desativado sumarização de NOK a pedido de MArcos Rezende/Acc 
							"union " +
						" select " +
										"      trunc(a.DAT_RECARGA) as DAT_PROCESSAMENTO, " +
										"      substr(a.IDT_MSISDN,3,2) as IDT_DDD, " +
										"      decode(nvl(idt_plano_preco,0),0,FNC_PLANO_ASSINANTE(A.DAT_RECARGA,A.IDT_MSISDN),idt_plano_preco)  AS IDT_PLANO, " +
										"      a.ID_TIPO_RECARGA AS ID_TIPO_RECARGA, " +
										"      a.id_tipo_credito as ID_TIPO_CREDITO, " +
										"      A.IDT_ERRO as COD_RETORNO, " +
										"      a.ID_CANAL as ID_CANAL, " +
										"      A.ID_ORIGEM as ID_ORIGEM, " +
										"      a.ID_SISTEMA_ORIGEM as ID_SISTEMA_ORIGEM, " +
										"      count(1) as QTD_REGISTROS, " +
										"      sum(nvl(A.vlr_credito_principal,0)+nvl(a.vlr_credito_bonus,0)+nvl(a.vlr_credito_sms,0)+nvl(a.vlr_credito_gprs,0)) as VLR_TOTAL, " +
							"	   sum(nvl(A.vlr_credito_principal,0)) as vlr_total_principal, " +
							"	   sum(nvl(a.vlr_credito_bonus,0)) as vlr_total_bonus, " +
							"	   sum(nvl(a.vlr_credito_gprs,0)) as vlr_total_dados, " +
							"	   sum(nvl(a.vlr_credito_sms,0)) as vlr_total_sms " +							" from " +
										" from " +
										"  		TBL_REC_RECARGAS_NOK a " +
										" where " +
										"		a.DAT_RECARGA between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
										"   	to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') " +
										" group by " +
										"   	trunc(a.DAT_RECARGA), " +
										"  		decode(nvl(idt_plano_preco,0),0,FNC_PLANO_ASSINANTE(A.DAT_RECARGA,A.IDT_MSISDN),idt_plano_preco)  , " +
										"  		substr(a.IDT_MSISDN,3,2), " +
										"  		a.TIP_TRANSACAO, " +
										"      	a.ID_TIPO_RECARGA, " +
										"       a.id_tipo_credito, " +
										"    	a.ID_CANAL, " +
										"		A.ID_ORIGEM, " +
										"		a.ID_SISTEMA_ORIGEM, " +
										"    	nvl(a.ID_VALOR,0), " +
										"  		A.IDT_ERRO  " +
*******************************************************************************/										
//			"  ) , " +
//			"  ( " +
//			"		select " +
//			"			idt_codigo_nacional, " +
//			"			vlr_aliquota " +
//			"		from TBL_GER_ALIQUOTA ALIQUOTA" +
//			"		where " +
//			"			ALIQUOTA.IDT_IMPOSTO = 'ICMS' AND" +
//			"			ALIQUOTA.dat_inicial_validade <= to_date(?, 'dd/mm/yyyy') AND " +
//			"			(ALIQUOTA.dat_final_validade  >= to_date(?, 'dd/mm/yyyy') or ALIQUOTA.dat_final_validade is null) " +
//			"  )ALIQUOTA "+
//			"  WHERE  "+
//			"     IDT_DDD = ALIQUOTA.idt_codigo_nacional(+) ";
				
			// Popula TBL_REL_RECARGAS
			super.log(Definicoes.DEBUG,"sumarizaContabil","Populando dados tabela de recargas por DIA");
//			TODO_OLD:Comentar				DBConexao.executaPreparedQuery(query,new Object[]{//dataReferencia,dataReferencia,
//			TODO_OLD:Comentar						dataReferencia,dataReferencia,dataReferencia,dataReferencia},super.getIdLog());

				return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			super.log(Definicoes.DEBUG,"sumarizaContabilidade","Fim");
		}
	}
	
	/**
	 * Metodo...: sumarizaSaldoAssinantes
	 * Descricao: Sumariza os dados Contábeis dos Assinantes
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @return short 						- Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaSaldoAssinantes(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		short retorno = 1;
		super.log(Definicoes.DEBUG,"sumarizaContabilidade-SaldoAssinantes","Inicio");

		try
		{
/*******************************************************************************************************************/
				// Limpa tabela de rel Saldos/DIA
//				String query = "DELETE FROM TBL_REL_SALDOS WHERE DAT_SALDO_ASSINANTE = to_date(?, 'DD/MM/YYYY')";
				// Deleta TBL_REL_saldos
				super.log(Definicoes.DEBUG,"sumarizaContabil","Delecao dados tabela de saldos por dia");
//				TODO_OLD:Comentar					DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
/*******************************************************************************************************************/
				// Preenche tabela de saldos por dia
//				query = "insert into TBL_REL_SALDOS " +
//				"(DAT_SALDO_ASSINANTE, IDT_CODIGO_NACIONAL, IDT_PLANO, IDT_STATUS_ASSINANTE,QTD_REGISTROS,VLR_ACUMULADO_SALDO, " +
//				" vlr_total_principal, " +
//				" vlr_total_bonus, " +
//				" vlr_total_sms, " +
//				" vlr_total_dados  "+
//				" ) "+ 
//				" select " +
//				"	TRUNC(a.dat_importacao) as DAT_PROCESSAMENTO, " +
//				"   nvl(substr(sub_id,3,2),00) as IDT_CODIGO_NACIONAL,  " +
//				"   PROFILE_ID as IDT_PLANO,  " +
//				"   ACCOUNT_STATUS as IDT_STATUS_ASSINANTE,  " +
//				"   COUNT(*) as QTD_REGISTROS,  " +
//				"	sum(nvl(account_balance,0)+nvl(bonus_balance,0)+nvl(sm_balance,0)+nvl(data_balance,0))/"+Definicoes.TECNOMEN_MULTIPLICADOR+" as VLR_TOTAL, " +
//				"	   sum(nvl(account_balance,0)) as vlr_total_principal, " +
//				"	   sum(nvl(bonus_balance,0)) as vlr_total_bonus, " +
//				"	   sum(nvl(sm_balance,0)) as vlr_total_dados, " +
//				"	   sum(nvl(data_balance,0)) as vlr_total_sms " +
//				" from " +
//				" tbl_apr_assinante_tecnomen a " +
//				"where  " +
//				"	a.dat_importacao = to_date(?,'DD/MM/YYYY') " +
//				"group by " +
//				"	 TRUNC(a.dat_importacao), " +
//				"    substr(sub_id,3,2), " +
//				"    PROFILE_ID, " +
//				"    ACCOUNT_STATUS";
			// Popula TBL_REL_SALDOS
			super.log(Definicoes.DEBUG,"sumarizaContabil","Populando dados tabela de saldos por DIA");
//			TODO_OLD:Comentar				DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
				
				return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			super.log(Definicoes.DEBUG,"sumarizaContabilidade","Fim");
		}
	}
		
	/**
	 * Metodo...: sumarizaVoucher
	 * Descricao: Sumariza os dados Contábeis dos Vouchers
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @return short 						- Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaVoucher(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		short retorno = 1;
		super.log(Definicoes.DEBUG,"sumarizaContabilidade-Voucher","Inicio");

		try
		{
/*******************************************************************************************************************/
				// Limpa tabela de rel Vouchers/DIA
//				String query = "DELETE FROM TBL_REL_VOUCHERS WHERE DAT_VOUCHER = to_date(?, 'dd/mm/yyyy')";
				// Deleta TBL_REL_VOUCHERS	
//				super.log(Definicoes.DEBUG,"sumarizaContabil","Delecao dados tabela de vouchers por dia");
//				DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
/*******************************************************************************************************************/
				// Preenche tabela de vouchers por dia
				// Vai mudar para nova tabela.
/*				query = "insert into TBL_REL_VOUCHERS " +
				"(DAT_VOUCHER, IDT_CODIGO_NACIONAL, ID_STATUS_VOUCHER, TIP_CARTAO, VLR_FACE, QTD_REGISTROS, VLR_TOTAL) "+ 
				" select " +
				"    trunc(vouchers.dat_atualizacao) as DAT_PROCESSAMENTO, " +
				"    NVL(SUBSTR(IDT_MSISDN_UTILIZADOR,3,2),00) AS IDT_CODIGO_NACIONAL, " +
				"	 id_status_voucher as ID_STATUS_VOUCHER,  " +
				"    tip_cartao as TIP_CARTAO,  " +
				"    vlr_face as VLR_FACE,  " +
				"    count (*) as QTD_REGISTROS,  " +
				"    sum(vlr_face) as VLR_TOTAL " +
				"from  " +
				"    tbl_rec_voucher vouchers  " +
				"where " +
				"	 vouchers.dat_atualizacao between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
				"    to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') and " +
				"	 (id_status_voucher = "+Definicoes.STATUS_VOUCHER_ATIVADO+" or id_status_voucher = "+Definicoes.STATUS_VOUCHER_UTILIZADO+" or id_status_voucher = "+Definicoes.STATUS_VOUCHER_CANCELADO+") " +
				"group by " +
				"   trunc(vouchers.dat_atualizacao), " +
				"	NVL(SUBSTR(IDT_MSISDN_UTILIZADOR,3,2),00), " +
				"	id_status_voucher, " +
				"   tip_cartao, " +
				"   vlr_face ";
			// Popula TBL_REL_SALDOS
			super.log(Definicoes.DEBUG,"sumarizaContabil","Populando dados tabela de vouchers por DIA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia},super.getIdLog());
	*/			
				return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			super.log(Definicoes.DEBUG,"sumarizaContabilidade","Fim");
		}
	}



	/**
	 * Metodo...: sumarizaRecargasFace
	 * Descricao: Sumariza os dados de recargas por FACE
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @return short 						- Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public short sumarizaRecargasFace(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		short retorno = 1;
		super.log(Definicoes.DEBUG,"sumarizaContabilidade-RecargasFace","Inicio");

		try
		{
/*
				// Limpa tabela de rel RecargasFace/DIA
				String query = "DELETE FROM TBL_REL_RECARGASFACE WHERE DAT_RECARGASFACE = to_date(?, 'dd/mm/yyyy')";
				// Deleta TBL_REL_RECARGASFACE	
				super.log(Definicoes.DEBUG,"sumarizaContabil","Delecao dados tabela de RecargasFace por dia");
				DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());

				// Preenche tabela de recargasface por dia
				// Vai mudar para nova tabela.
				query = "INSERT INTO TBL_REL_RECARGASFACE  " +
					"(DAT_RECARGASFACE, IDT_CODIGO_NACIONAL, VLR_FACE, QTD_REGISTROS) " +
					"select   " +
					"	trunc(rec.dat_recarga), " +
					"	substr(rec.idt_msisdn,3,2) as CN, " +
					"	rec.id_valor as VLR_FACE,  " +
					"	count(*) as QTD " +
					"from " +
					"	tbl_rec_valores vlr, " +
					"	tbl_rec_recargas rec, " +
					"   tbl_ger_transacao_recargasface trans  " +
					"where  " +
					"	rec.id_tipo_recarga = 'R' and  " +
					"	vlr.ind_vlr_face = 1 and  " +
					"   trans.tip_transacao = rec.tip_transacao and  " +
					"	vlr.id_valor = rec.id_valor and  " +
					"	rec.dat_recarga between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
					"   to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') " +
					"group by  " +
					"	trunc(rec.dat_recarga), " +
					"	substr(rec.idt_msisdn,3,2), " +
					"	rec.id_valor";
			// Popula TBL_REL_RECARGASFACE
			super.log(Definicoes.DEBUG,"sumarizaContabil","Populando dados tabela de RecargasFace por DIA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia},super.getIdLog());
				*/
				return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			super.log(Definicoes.DEBUG,"sumarizaContabilidade","Fim");
		}
	}

	
}
