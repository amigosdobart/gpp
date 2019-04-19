//Definicao do Pacote
package com.brt.gpp.aplicacoes.contabilidade;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;

import java.sql.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

/**
  * Essa classe refere-se ao processo de sumarização Contábil:
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				28/07/2005
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class ProcessadorSumarizacaoChamadas extends Contabilidade implements Runnable
{
	// CN que deve ter suas chamadas sumarizadas
	String cn = null;
	
	/**
	 * Metodo...: sumarizacaoContabil
	 * Descricao: Construtor
	 * @param long		aIdProcesso		ID do processo
	 * @param String	aDataReferencia	Data de Referencia
	 */
	public ProcessadorSumarizacaoChamadas(long aIdProcesso, String aDataReferencia, String aCN) throws GPPInternalErrorException  
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_SUMARIZACAO_CONTABIL, aDataReferencia);

		//Registra CN
		this.cn = aCN;
	}

	/**
	 *  (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		try
		{
			this.sumarizaChamadas();
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "run","Erro Sumarizacao Chamadas: "+e);
		}
	}
	
	/**
	 * Metodo...: sumarizaChamadas
	 * Descricao: Sumariza os dados Contábeis
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @return short 						- Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public void sumarizaChamadas( ) throws GPPInternalErrorException
	{
		ConexaoBancoDados DBConexao = null;
		
		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = super.gerenteBanco.getConexaoPREP(super.getIdLog());
			
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de chamadas por hora
			super.log(Definicoes.INFO,"sumarizaChamadas","Limpando TBL_REL_CDR_DIA;Data: "+super.dataReferencia+", CN: "+this.cn);
			String query = "DELETE TBL_REL_CDR_DIA WHERE DAT_CDR = to_date(?, 'DD/MM/YYYY') and idt_codigo_nacional = ?";
			// Deleta TBL_REL_CDR_HORA
			DBConexao.executaPreparedQuery(query,new Object[]{super.dataReferencia, this.cn},super.getIdLog());

			query = " insert into tbl_rel_cdr_dia "+
			" (dat_cdr, idt_codigo_nacional, idt_plano, num_csp, idt_modulacao,idt_sentido, tip_chamada, "+ 
			"  ind_ff, qtd_registros, vlr_duracao, idt_roaming_origin,"+ 
			"  vlr_total_principal,vlr_total_principal_si,vlr_total_periodico,vlr_total_periodico_si," +
			"  vlr_total_bonus, vlr_total_bonus_si, vlr_total_sms, vlr_total_sms_si, "+  
			"  vlr_total_dados, vlr_total_dados_si,idt_eot_origem, idt_eot_destino, idt_cn_call_id) "+ 
			"	select  "+
			"	cdrs.dat_processamento,  "+
			"	? as idt_ddd, "+	// Parametro 0 (CN)
			"	cdrs.idt_plano,  "+
			"	cdrs.num_csp,  "+
			"	cdrs.idt_modulacao,  "+
			"	c.idt_sentido as idt_sentido,  "+
			"	cdrs.tip_chamada,  "+
			"	cdrs.ind_ff,  "+
			"	count(*) as qtd_registros,  "+
			"	sum(cdrs.vlr_duracao),  "+
			"	cdrs.idt_roaming_origin,  "+
			"	sum(decode( substr(cdrs.tip_chamada,0,2),'AD',  "+
			"	          (cdrs.vlr_total_principal/(1-aliq.vlr_aliquota))/100000, "+  //-- Se for AD, preciso pegar o interconnection cost e transformar para valor bruto (ele vem líquido no cdr)
			"	          decode(  sign(cdrs.vlr_total_principal - (cdrs.interconexao/(1-aliq.vlr_aliquota))),-1,0,  "+
			"	          (cdrs.vlr_total_principal - (cdrs.interconexao/(1-aliq.vlr_aliquota)))/100000 "+  //--- Se não for AD, preciso descontar a eventual parcela referente ao AD desse cara (no interconnection cost)
			"	                  )  "+
			"	       )) as vlr_total_principal,     "+
			"	sum(decode( substr(cdrs.tip_chamada,0,2),'AD',  "+
			"	            cdrs.vlr_total_principal/100000,     "+
			"	            decode(sign(cdrs.vlr_total_principal*(1-aliq.vlr_aliquota) - cdrs.interconexao),-1,0, "+
			"	            (cdrs.vlr_total_principal*(1-aliq.vlr_aliquota) - cdrs.interconexao)/100000  "+
			"	                   )  "+
			"	       )) as vlr_total_principal_si,  "+
			"	sum(cdrs.vlr_total_periodico/100000) as vlr_total_periodico,  "+
			"	sum((cdrs.vlr_total_periodico*(1-aliq.vlr_aliquota))/100000) as vlr_total_periodico_si,  "+
			"	sum(cdrs.vlr_total_bonus/100000) as vlr_total_bonus,  "+
			"	sum((cdrs.vlr_total_bonus*(1-aliq.vlr_aliquota))/100000) as vlr_total_bonus_si,  "+
			"	sum(cdrs.vlr_total_sms/100000) as vlr_total_sms,  "+
			"	sum((cdrs.vlr_total_sms*(1-aliq.vlr_aliquota))/100000) as vlr_total_sms_si,  "+
			"	sum((cdrs.vlr_total_dados)/100000) as vlr_total_dados,  "+
			"	sum((cdrs.vlr_total_dados*(1-aliq.vlr_aliquota))/100000) as vlr_total_dados_si, "+
			"	cdrs.idt_eot_origem as idt_eot_origem, "+
			"	cdrs.idt_eot_destino as idt_eot_destino, "+
			"	nvl (fnc_get_codigo_nacional(cdrs.msisdn,cdrs.call_id),00) "+
			"	from  "+
			"	(  "+
			"	select  "+
			"	cdr.sub_id as msisdn,  "+
			"	decode (nvl(cdr.profile_id,0), 0, nvl (ass.idt_plano_preco,0), nvl(cdr.profile_id,0)) as idt_plano,  "+
			"	nvl(decode(num_csp,'--','00',num_csp),'00') as num_csp,  "+
			"	nvl(decode(idt_modulacao,'-','F',idt_modulacao),'F') as idt_modulacao,  "+
			"	nvl (tip_chamada,'OUTROS') as tip_chamada,  "+
			"	transaction_type as tipoTransacao,  "+
			"	decode( substr(cell_name,4,2), "+
			"                null, ? , 	"+	//Parametro 1 (CN)
			"	        '_N', ? ,       "+	//Parametro 2 (CN)
			"                substr(cell_name,4,2)  "+
			"	       ) as idt_roaming_origin,  "+
			"	trunc(timestamp) as dat_Processamento,  "+
			"	decode(ff_discount,0,0,null,0,1) as ind_ff,  "+
			"	call_duration as vlr_duracao,  "+
			"	abs(interconnection_cost) as interconexao,  "+
			"	abs(account_balance_delta) as vlr_total_principal,  "+
			"	abs(periodic_balance_delta) as vlr_total_periodico,  "+
			"	abs(bonus_balance_delta) as vlr_total_bonus,  "+
			"	abs(sm_balance_delta) as vlr_total_sms,  "+
			"	abs(data_balance_delta) as vlr_total_dados, "+
			"	decode(length(idt_operadora_origem),2,idt_operadora_origem, 3, idt_operadora_origem,SUBSTR(cdr.sub_id,3,2))  AS idt_eot_origem, "+
			"	decode(length(idt_operadora_destino),2,idt_operadora_destino, 3, idt_operadora_destino,SUBSTR(cdr.sub_id,3,2)) AS idt_eot_destino,  "+
			"	call_id  "+
			"	from tbl_ger_cdr cdr, tbl_apr_assinante ass  "+
			"	WHERE ass.idt_msisdn(+) = cdr.sub_id "+
			"        and timestamp >= to_date('"+super.dataReferencia+"','dd/mm/yyyy') and timestamp < to_date('"+super.dataReferencia+"','dd/mm/yyyy')+1 "+	// Parametros 3 e 4 (Data Referencia)
			"        and cdr.sub_id like '55"+this.cn+"%' 		"+
			"	union all  "+
			"	select  "+
			"	cdr.sub_id as msisdn,  "+
			"	decode (nvl(cdr.profile_id,0), 0, nvl (ass.idt_plano_preco,0), nvl(cdr.profile_id,0)) as idt_plano,  "+
			"	nvl(decode(num_csp,'--','00',num_csp),'00') as num_csp,  "+
			"	nvl(decode(idt_modulacao,'-','F',idt_modulacao),'F') as idt_modulacao,  "+
			"	tip_deslocamento as tip_chamada,  "+
			"	transaction_type as tipoTransacao,  "+
			"	decode( substr(cell_name,4,2), "+
			"                null, ? , 	"+		// Parametro 6 (CN)
			"	        '_N', ? , 	    "+  	// Parametro 7 (CN)
			"                substr(cell_name,4,2)  "+
			"	       ) as idt_roaming_origin,  "+
			"	trunc(timestamp) as dat_Processamento,  "+
			"	0 as ind_ff,  "+
			"	call_duration as vlr_duracao,  "+
			"	abs(interconnection_cost) as interconexao,  "+
			"	abs(interconnection_cost) as vlr_total_principal,  "+
			"	0 as vlr_total_periodico,  "+
			"	0 as vlr_total_bonus,  "+
			"	0 as vlr_total_sms,  "+
			"	0 as vlr_total_dados,  "+
			"	decode(length(idt_operadora_origem),2,idt_operadora_origem, 3, idt_operadora_origem,SUBSTR(cdr.sub_id,3,2))  AS idt_eot_origem, "+
			"	decode(length(idt_operadora_destino),2,idt_operadora_destino, 3, idt_operadora_destino,SUBSTR(cdr.sub_id,3,2)) AS idt_eot_destino, "+
			"	call_id"+
			"	from tbl_ger_cdr cdr, tbl_apr_assinante ass  "+
			"	WHERE  ass.idt_msisdn(+) = cdr.sub_id "+
			"        and timestamp >=  to_date('"+super.dataReferencia+"','dd/mm/yyyy') and timestamp <  to_date('"+super.dataReferencia+"','dd/mm/yyyy')+1 "+	// Parametros 8 e 9 (data referencia)
			"	and substr(tip_deslocamento,0,2)='AD'  "+
			"        and cdr.sub_id like '55"+this.cn+"%' 		"+
			"	) cdrs,  "+
			"	tbl_ger_tip_transacao_tecnomen c,  "+
			"	tbl_ger_aliquota aliq"+
			"	where c.transaction_type = cdrs.tipoTransacao  "+
			"	AND aliq.idt_imposto =  'ICMS' "+
			"	AND aliq.idt_codigo_nacional = ? 	"+	// Parametro 11 (CN) 
			"	AND aliq.dat_inicial_validade <=  to_date('"+super.dataReferencia+"','dd/mm/yyyy')+1 "+	// Parametro 12 (data referencia)
			"	AND (aliq.dat_final_validade >=  to_date('"+super.dataReferencia+"','dd/mm/yyyy') or aliq.dat_final_validade is null) "+	// Parametro 13 (Data referencia)
			"	group by  "+
			"	dat_processamento,  "+
			"	idt_plano,  "+
			"	num_csp,  "+
			"	idt_modulacao,  "+
			"	c.idt_sentido,  "+
			"	tip_chamada,  "+
			"	ind_ff,  "+
			"	idt_roaming_origin, "+
			"	idt_eot_origem, "+
			"	idt_eot_destino, "+
			"	nvl (fnc_get_codigo_nacional(cdrs.msisdn,cdrs.call_id),00) "; 
			
			// Popula TBL_REL_CDR_HORA
			super.log(Definicoes.INFO,"sumarizaChamadas","Populando dados na TBL_REL_CDR_DIA;Data: "+super.dataReferencia+", CN: "+this.cn);
			//java.sql.Timestamp tsDataReferencia = new java.sql.Timestamp(sdf.parse(dataReferencia).getTime());
			
			Object[] paramsCdrHora = {
					this.cn,	// Parametro 0
					this.cn,	// Parametro 1
					this.cn,	// Parametro 2
					//dataReferencia,	// Parametro 3
					//dataReferencia,	// Parametro 4
					//"55"+this.cn+"%",	// Parametro 5
					this.cn,	// Parametro 6
					this.cn,	// Parametro 7
					//dataReferencia,	// Parametro 8
					//dataReferencia,	// Parametro 9
					//"55"+this.cn+"%",	// Parametro 10
					this.cn,	// Parametro 11
					//dataReferencia,	// Parametro 12
					//dataReferencia,	// Parametro 13
					};
			
			DBConexao.executaPreparedQuery(query,paramsCdrHora,super.getIdLog());
			DBConexao.commit();
			super.log(Definicoes.INFO, "sumarizaChamadas", "TBL_REL_CDR_DIA populada;Data: "+super.dataReferencia+", CN: "+this.cn);
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaChamadas","Data: "+super.dataReferencia+", CN: "+this.cn+" Erro GPP:"+e);
			
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
				gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
			}
			catch(SQLException sqlE)
			{
				super.log(Definicoes.ERRO, "sumarizaChamadas","Data: "+super.dataReferencia+", CN: "+this.cn+"Erro GPP:"+sqlE);
			}
		}
	}	
}
