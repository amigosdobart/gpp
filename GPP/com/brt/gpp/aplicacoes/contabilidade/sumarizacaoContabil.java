//Definicao do Pacote
package com.brt.gpp.aplicacoes.contabilidade;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.aplicacoes.*;

import java.text.SimpleDateFormat;
import java.sql.*;

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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try
		{
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de chamadas por hora
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_CDR_HORA");
			String query = "DELETE TBL_REL_CDR_HORA WHERE DAT_CDR = to_date(?, 'DD/MM/YYYY')";
			// Deleta TBL_REL_CDR_HORA
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());

			// Preenche tabela de chamadas por hora
			query = "insert into tbl_rel_cdr_hora "+
			"(idt_hora_cdr, dat_cdr, idt_codigo_nacional, idt_plano, num_csp, idt_modulacao,idt_sentido, tip_chamada, "+
			"ind_ff, qtd_registros, vlr_duracao, idt_roaming_origin, vlr_total, vlr_total_si, "+
			"vlr_total_principal, vlr_total_principal_si,vlr_total_bonus, vlr_total_bonus_si, vlr_total_sms, vlr_total_sms_si, "+ 
			"vlr_total_dados, vlr_total_dados_si) "+
			"select "+
			"cdrs.idt_hora, "+
			"cdrs.dat_processamento, "+
			"substr(cdrs.msisdn,3,2) as idt_ddd, "+
			"cdrs.idt_plano, "+
			"cdrs.num_csp, "+
			"cdrs.idt_modulacao, "+
			"c.idt_sentido as idt_sentido, "+
			"cdrs.tip_chamada, "+
			"cdrs.ind_ff, "+
			"count(*) as qtd_registros, "+
			"sum(cdrs.vlr_duracao), "+
			"cdrs.idt_roaming_origin, "+
			"0,0, "+
			"sum(decode( substr(cdrs.tip_chamada,0,2),'AD', "+
			            "(cdrs.vlr_total_principal/(1-aliq.vlr_aliquota))/100000, "+   //-- Se for AD, preciso pegar o interconnection const e transformar para valor bruto (ele vem líquido no cdr)
			            "decode(  sign(cdrs.vlr_total_principal - (cdrs.interconexao/(1-aliq.vlr_aliquota))),-1,0, "+
			                    "(cdrs.vlr_total_principal - (cdrs.interconexao/(1-aliq.vlr_aliquota)))/100000 "+ //--- Se não for AD, preciso descontar a eventual parcela referente ao AD desse cara (no interconnection cost)
			                  ") "+
			        ")) as vlr_total_principal, "+   
			"sum(decode( substr(cdrs.tip_chamada,0,2),'AD', "+
			            "cdrs.vlr_total_principal/100000, "+   
			            "decode(sign(cdrs.vlr_total_principal*(1-aliq.vlr_aliquota) - cdrs.interconexao),-1,0, "+
			            "(cdrs.vlr_total_principal*(1-aliq.vlr_aliquota) - cdrs.interconexao)/100000 "+
			                   ") "+
			       ")) as vlr_total_principal_si, "+
			"sum(cdrs.vlr_total_bonus/100000) as vlr_total_bonus, "+
			"sum((cdrs.vlr_total_bonus*(1-aliq.vlr_aliquota))/100000) as vlr_total_bonus_si, "+
			"sum(cdrs.vlr_total_sms/100000) as vlr_total_sms, "+
			"sum((cdrs.vlr_total_sms*(1-aliq.vlr_aliquota))/100000) as vlr_total_sms_si, "+
			"sum((cdrs.vlr_total_dados)/100000) as vlr_total_dados, "+
			"sum((cdrs.vlr_total_dados*(1-aliq.vlr_aliquota))/100000) as vlr_total_dados_si "+
			"from "+
			"( "+
			"select "+
			"sub_id as msisdn, "+
			"nvl(profile_id,0) as idt_plano, "+
			"nvl(decode(num_csp,'--','00',num_csp),'00') as num_csp, "+
			"nvl(decode(idt_modulacao,'-','F',idt_modulacao),'F') as idt_modulacao, "+
			"nvl (tip_chamada,'OUTROS') as tip_chamada, "+
			"transaction_type as tipoTransacao, "+
			"decode( substr(cell_name,4,2),null, "+
			                "substr(sub_id,3,2), "+
			                "substr(cell_name,4,2) "+
			       ") as idt_roaming_origin, "+
			"trunc(start_time/3600) as idt_Hora, "+
			"trunc(timestamp) as dat_Processamento, "+
			"decode(ff_discount,0,0,null,0,1) as ind_ff, "+
			"call_duration as vlr_duracao, "+
			"abs(interconnection_cost) as interconexao, "+
			"abs(account_balance_delta) as vlr_total_principal, "+
			"abs(bonus_balance_delta) as vlr_total_bonus, "+
			"abs(sm_balance_delta) as vlr_total_sms, "+
			"abs(data_balance_delta) as vlr_total_dados "+
			"from tbl_ger_cdr "+
			"where timestamp >= ? and timestamp < (?+1) "+	// Parametros 0 e 1
			"union all "+
			"select "+
			"sub_id as msisdn, "+
			"nvl(profile_id,0) as idt_plano, "+
			"nvl(decode(num_csp,'--','00',num_csp),'00') as num_csp, "+
			"nvl(decode(idt_modulacao,'-','F',idt_modulacao),'F') as idt_modulacao, "+
			"tip_deslocamento as tip_chamada, "+
			"transaction_type as tipoTransacao, "+
			"decode( substr(cell_name,4,2),null, "+
			                "substr(sub_id,3,2), "+
			                "substr(cell_name,4,2) "+
			       ") as idt_roaming_origin, "+
			"trunc(start_time/3600) as idt_Hora, "+
			"trunc(timestamp) as dat_Processamento, "+
			"0 as ind_ff, "+
			"call_duration as vlr_duracao, "+
			"abs(interconnection_cost) as interconexao, "+
			"abs(interconnection_cost) as vlr_total_principal, "+
			"0 as vlr_total_bonus, "+
			"0 as vlr_total_sms, "+
			"0 as vlr_total_dados "+
			"from tbl_ger_cdr "+
			"where timestamp >= ? and timestamp < (?+1) "+	// Parametros 2 e 3
			"and substr(tip_deslocamento,0,2)='AD' "+
			") cdrs, "+
			"tbl_ger_tip_transacao_tecnomen c, "+
			"tbl_ger_aliquota aliq "+
			"where c.transaction_type = cdrs.tipoTransacao "+
			"AND aliq.idt_imposto = ? "+	// Parametro 4
			"AND aliq.idt_codigo_nacional = SUBSTR(cdrs.msisdn,3,2) "+
			"AND aliq.dat_inicial_validade <= (?+1) "+	// Parametro 5
			"AND (aliq.dat_final_validade >= ? or aliq.dat_final_validade is null) "+	// Parametro 6
			"group by "+
			"idt_hora, "+
			"dat_processamento, "+
			"substr(msisdn,3,2), "+
			"idt_plano, "+
			"num_csp, "+
			"idt_modulacao, "+
			"c.idt_sentido, "+
			"tip_chamada, "+
			"ind_ff, "+
			"idt_roaming_origin"; 

			// Popula TBL_REL_CDR_HORA
			super.log(Definicoes.INFO,"sumarizaContabil","Populando dados na TBL_REL_CDR_HORA");
			java.sql.Timestamp tsDataReferencia = new java.sql.Timestamp(sdf.parse(dataReferencia).getTime());
			
			Object[] paramsCdrHora = {
					tsDataReferencia,	// Parametro 0
					tsDataReferencia,	// Parametro 1
					tsDataReferencia,	// Parametro 2
					tsDataReferencia,	// Parametro 3
					Definicoes.IMPOSTO_TLDC,	// Parametro 4
					tsDataReferencia,	// Parametro 5
					tsDataReferencia,	// Parametro 6
					};
			DBConexao.executaPreparedQuery(query,paramsCdrHora,super.getIdLog());
			
			String queryUpdate = "update tbl_rel_cdr_hora "+
					"set vlr_total = vlr_total_principal + vlr_total_bonus + vlr_total_sms + vlr_total_dados, "+
					"vlr_total_si = vlr_total_principal_si + vlr_total_bonus_si + vlr_total_sms_si + vlr_total_dados_si "+
					"where dat_cdr = ?";
			Object[] paramsUpdate = {tsDataReferencia};
			DBConexao.executaPreparedUpdate(queryUpdate, paramsUpdate, super.getIdLog());
			
			// Commit intermediário para sumarização das 
			DBConexao.commit();
			
			// Limpa tabela de chamadas por dia
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_CDR_DIA");
			query = "DELETE TBL_REL_CDR_DIA WHERE DAT_CDR = to_date(?, 'DD/MM/YYYY')";
			// Deleta TBL_REL_CDR_DIA
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
				
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
					"DAT_CDR,  " +
					"IDT_CODIGO_NACIONAL, " +
					"IDT_PLANO, " +
					"NUM_CSP, " +
					"IDT_MODULACAO, " +
					"IDT_SENTIDO, " +
					"TIP_CHAMADA,  " +
					"IDT_ROAMING_ORIGIN," +
					"sum(QTD_REGISTROS), " +
					"sum(VLR_TOTAL), " +
					"sum(VLR_DURACAO), " +
					"sum(VLR_TOTAL_SI),  " +
					"sum(vlr_total_principal), sum(vlr_total_principal_si)," +
					"sum(vlr_total_bonus), sum(vlr_total_bonus_si)," +
					"sum(vlr_total_sms), sum(vlr_total_sms_si)," +
					"sum(vlr_total_dados), sum(vlr_total_dados_si),  " +
					"IND_FF " +				
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
					"IDT_ROAMING_ORIGIN," +
					"IND_FF ";
			// Popula TBL_REL_CDR_DIA
			super.log(Definicoes.INFO,"sumarizaContabil","Populando TBL_REL_CDR_DIA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia},super.getIdLog());
			
			DBConexao.commit();
				
			return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			try
			{
				DBConexao.rollback();
			}
			catch (SQLException sqlE)
			{
				throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			}
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			
		}
		finally
		{
			try
			{
				DBConexao.setAutoCommit(true);
			}
			catch(SQLException sqlE)
			{
				super.log(Definicoes.ERRO, "Erro Banco no SetAutoCommit","Erro: "+sqlE);
			}
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
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de rel Recargas/DIA
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_RECARGAS");
			String query = "DELETE TBL_REL_RECARGAS WHERE DAT_RECARGA = to_date(?, 'dd/mm/yyyy')";
			// Deleta TBL_REL_RECARGAS
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());

			// Preenche tabela de recargas por dia
			query = "insert into TBL_REL_RECARGAS" +
				"(DAT_RECARGA,IDT_CODIGO_NACIONAL,IDT_PLANO,ID_TIPO_RECARGA,ID_TIPO_CREDITO,COD_RETORNO,ID_CANAL,ID_ORIGEM," +
				"ID_SISTEMA_ORIGEM,QTD_REGISTROS, VLR_TOTAL, VLR_TOTAL_SI, " +
				" vlr_total_principal, vlr_total_principal_si," +
				" vlr_total_bonus, vlr_total_bonus_si," +
				" vlr_total_sms, vlr_total_sms_si," +
				" vlr_total_dados, vlr_total_dados_si  "+
				")" +
				"SELECT DAT_PROCESSAMENTO,IDT_DDD,IDT_PLANO,ID_TIPO_RECARGA,ID_TIPO_CREDITO,COD_RETORNO," +
				"ID_CANAL,ID_ORIGEM,ID_SISTEMA_ORIGEM,QTD_REGISTROS, " +
				"VLR_TOTAL,nvl(VLR_TOTAL*(1-ALIQUOTA.VLR_ALIQUOTA),0), " +
				" vlr_total_principal, nvl(vlr_total_principal*(1-ALIQUOTA.VLR_ALIQUOTA),0), " +
				" vlr_total_bonus, nvl(vlr_total_bonus*(1-ALIQUOTA.VLR_ALIQUOTA),0)," +
				" vlr_total_sms, nvl(vlr_total_sms*(1-ALIQUOTA.VLR_ALIQUOTA),0)," +
				" vlr_total_dados, nvl(vlr_total_dados*(1-ALIQUOTA.VLR_ALIQUOTA),0) "+				"FROM " +
						"(select " +
							"      trunc(a.DAT_RECARGA) as DAT_PROCESSAMENTO, " +
							"      substr(a.IDT_MSISDN,3,2) as IDT_DDD, " +
							"      decode(nvl(idt_plano_preco,0),0,nvl(FNC_PLANO_ASSINANTE(A.DAT_RECARGA,A.IDT_MSISDN),0),idt_plano_preco) AS IDT_PLANO, " +
							"      a.ID_TIPO_RECARGA AS ID_TIPO_RECARGA, " +
							"      a.id_tipo_credito as ID_TIPO_CREDITO, " +
							"      0 as COD_RETORNO, " +
							"      a.ID_CANAL as ID_CANAL, " +
							"      A.ID_ORIGEM as ID_ORIGEM, " +
							"      a.ID_SISTEMA_ORIGEM as ID_SISTEMA_ORIGEM, " +
							"      count(1) as QTD_REGISTROS, " +
							"      sum(nvl(A.vlr_credito_principal,0)+nvl(a.vlr_credito_bonus,0)+nvl(a.vlr_credito_sms,0)+nvl(a.vlr_credito_gprs,0)) as VLR_TOTAL, " +
							"	   sum(nvl(A.vlr_credito_principal,0)) as vlr_total_principal, " +
							"	   sum(nvl(a.vlr_credito_bonus,0)) as vlr_total_bonus, " +
							"	   sum(nvl(a.vlr_credito_gprs,0)) as vlr_total_dados, " +
							"	   sum(nvl(a.vlr_credito_sms,0)) as vlr_total_sms " +							
							" from " +
							"  		TBL_REC_RECARGAS a " +
							" where " +
							"		a.DAT_RECARGA between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and  " +
							"   	to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') " +
							" group by " +
							"   	trunc(a.DAT_RECARGA), " +
							"  		decode(nvl(idt_plano_preco,0),0,nvl(FNC_PLANO_ASSINANTE(A.DAT_RECARGA,A.IDT_MSISDN),0),idt_plano_preco), " +
							"  		substr(a.IDT_MSISDN,3,2), " +
							"  		a.TIP_TRANSACAO, " +
							"      	a.ID_TIPO_RECARGA, " +
							"       a.id_tipo_credito, " +
							"    	a.ID_CANAL, " +
							"		A.ID_ORIGEM, " +
							"		a.ID_SISTEMA_ORIGEM, " +
							"    	nvl(a.ID_VALOR,0), " +
							"  		0  " +
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
			"  ) , " +
			"  ( " +
			"		select " +
			"			idt_codigo_nacional, " +
			"			vlr_aliquota " +
			"		from TBL_GER_ALIQUOTA ALIQUOTA" +
			"		where " +
			"			ALIQUOTA.IDT_IMPOSTO = 'ICMS' AND" +
			"			ALIQUOTA.dat_inicial_validade <= to_date(?, 'dd/mm/yyyy') AND " +
			"			(ALIQUOTA.dat_final_validade  >= to_date(?, 'dd/mm/yyyy') or ALIQUOTA.dat_final_validade is null) " +
			"  )ALIQUOTA "+
			"  WHERE  "+
			"     IDT_DDD = ALIQUOTA.idt_codigo_nacional(+) ";
				
			// Popula TBL_REL_RECARGAS
			super.log(Definicoes.INFO,"sumarizaContabil","Populando dados tabela de recargas por DIA");
			DBConexao.executaPreparedQuery(query,new Object[]{//dataReferencia,dataReferencia,
					dataReferencia,dataReferencia,dataReferencia,dataReferencia},super.getIdLog());
			
			DBConexao.commit();

				return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			try
			{
				DBConexao.rollback();
			}
			catch (SQLException sqlE)
			{
				throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			}
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			
		}
		finally
		{
			try
			{
				DBConexao.setAutoCommit(true);
			}
			catch(SQLException sqlE)
			{
				super.log(Definicoes.ERRO, "Erro Banco no SetAutoCommit","Erro: "+sqlE);
			}
			super.log(Definicoes.DEBUG,"sumarizaContabil","Fim");
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
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de rel Saldos/DIA
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_SALDOS");
			String query = "DELETE TBL_REL_SALDOS WHERE DAT_SALDO_ASSINANTE = to_date(?, 'DD/MM/YYYY')";
			// Deleta TBL_REL_saldos
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());

				// Preenche tabela de saldos por dia
				query = "insert into TBL_REL_SALDOS " +
				"(DAT_SALDO_ASSINANTE, IDT_CODIGO_NACIONAL, IDT_PLANO, IDT_STATUS_ASSINANTE,QTD_REGISTROS,VLR_ACUMULADO_SALDO, " +
				" vlr_total_principal, " +
				" vlr_total_bonus, " +
				" vlr_total_sms, " +
				" vlr_total_dados  "+
				" ) "+ 
				" select " +
				"	TRUNC(a.dat_importacao) as DAT_PROCESSAMENTO, " +
				"   nvl(substr(sub_id,3,2),00) as IDT_CODIGO_NACIONAL,  " +
				"   PROFILE_ID as IDT_PLANO,  " +
				"   ACCOUNT_STATUS as IDT_STATUS_ASSINANTE,  " +
				"   COUNT(*) as QTD_REGISTROS,  " +
				"	sum(nvl(account_balance,0)+nvl(bonus_balance,0)+nvl(sm_balance,0)+nvl(data_balance,0))/"+Definicoes.TECNOMEN_MULTIPLICADOR+" as VLR_TOTAL, " +
				"	   sum(nvl(account_balance,0)) as vlr_total_principal, " +
				"	   sum(nvl(bonus_balance,0)) as vlr_total_bonus, " +
				"	   sum(nvl(sm_balance,0)) as vlr_total_dados, " +
				"	   sum(nvl(data_balance,0)) as vlr_total_sms " +
				" from " +
				" tbl_apr_assinante_tecnomen a " +
				"where  " +
				"	a.dat_importacao = to_date(?,'DD/MM/YYYY') " +
				"group by " +
				"	 TRUNC(a.dat_importacao), " +
				"    substr(sub_id,3,2), " +
				"    PROFILE_ID, " +
				"    ACCOUNT_STATUS";
			// Popula TBL_REL_SALDOS
			super.log(Definicoes.INFO,"sumarizaContabil","Populando TBL_REL_SALDOS");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
			
			DBConexao.commit();
			return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			try
			{
				DBConexao.rollback();
			}
			catch (SQLException sqlE)
			{
				throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			}
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			
		}
		finally
		{
			try
			{
				DBConexao.setAutoCommit(true);
			}
			catch(SQLException sqlE)
			{
				super.log(Definicoes.ERRO, "Erro Banco no SetAutoCommit","Erro: "+sqlE);
			}
			super.log(Definicoes.DEBUG,"sumarizaContabil","Fim");
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
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de rel Vouchers/DIA
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_VOUCHERS");
			String query = "DELETE TBL_REL_VOUCHERS WHERE DAT_VOUCHER = to_date(?, 'dd/mm/yyyy')";
			// Deleta TBL_REL_VOUCHERS	
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());

				// Preenche tabela de vouchers por dia
				// Vai mudar para nova tabela.
				query = "insert into TBL_REL_VOUCHERS " +
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
			super.log(Definicoes.INFO,"sumarizaContabil","Populando dados tabela de vouchers por DIA");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia},super.getIdLog());
			
			DBConexao.commit();
				
			return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			try
			{
				DBConexao.rollback();
			}
			catch (SQLException sqlE)
			{
				throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			}
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			
		}
		finally
		{
			try
			{
				DBConexao.setAutoCommit(true);
			}
			catch(SQLException sqlE)
			{
				super.log(Definicoes.ERRO, "Erro Banco no SetAutoCommit","Erro: "+sqlE);
			}
			super.log(Definicoes.DEBUG,"sumarizaContabil","Fim");
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
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de rel RecargasFace/DIA
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_RECARGASFACE");
			String query = "DELETE TBL_REL_RECARGASFACE WHERE DAT_RECARGASFACE = to_date(?, 'dd/mm/yyyy')";
			// Deleta TBL_REL_RECARGASFACE	
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());

				// Preenche tabela de recargasface por dia
				// Vai mudar para nova tabela.
				query = "INSERT INTO TBL_REL_RECARGASFACE "+
				"(DAT_RECARGASFACE, IDT_CODIGO_NACIONAL, VLR_FACE, ID_CANAL, ID_ORIGEM, ID_SISTEMA_ORIGEM, IDT_PLANO_PRECO, QTD_REGISTROS) "+
				"select "+   
					"trunc(rec.dat_recarga), "+
					"substr(rec.idt_msisdn,3,2) as CN, "+ 
					"rec.id_valor as VLR_FACE, "+
					"rec.id_canal as id_canal, "+
					"rec.id_origem as id_origem, "+
					"rec.id_sistema_origem as id_sistema_origem, "+
					"rec.idt_plano_preco as idt_plano_preco, "+
					"count(*) as QTD "+ 
				"from "+ 
					"tbl_rec_valores vlr, "+ 
					"tbl_rec_recargas rec "+
				"where "+  
					"rec.id_tipo_recarga = 'R' and "+  
					"vlr.ind_vlr_face = 1 and "+  
					"vlr.id_valor = rec.id_valor and "+  
					"rec.dat_recarga between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and "+	// Parametro 0 
					"to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS') "+	// Parametro 1
				"group by "+  
					"trunc(rec.dat_recarga), "+ 
					"substr(rec.idt_msisdn,3,2), "+ 
					"rec.id_valor, "+
					"rec.id_canal, "+
					"rec.id_origem, "+
					"rec.id_sistema_origem, "+
					"rec.idt_plano_preco	" ;
			super.log(Definicoes.INFO,"sumarizaContabil","Populando TBL_REL_RECARGAS_FACE");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia},super.getIdLog());
			
			DBConexao.commit();
			return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
			retorno = -1;
			try
			{
				DBConexao.rollback();
			}
			catch (SQLException sqlE)
			{
				throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			}
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
			
		}
		finally
		{
			try
			{
				DBConexao.setAutoCommit(true);
			}
			catch(SQLException sqlE)
			{
				super.log(Definicoes.ERRO, "Erro Banco no SetAutoCommit","Erro: "+sqlE);
			}
			super.log(Definicoes.DEBUG,"sumarizaContabil","Fim");
		}
	}
}
