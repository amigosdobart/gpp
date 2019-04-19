// Definicao do Pacote
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
public class consolidacaoContabil extends Aplicacoes{
	GerentePoolBancoDados gerenteBanco = null;
	
	//Data passada como parâmetro na execução desse processo
	String dataReferencia = null;
	
	//Mensagem a ser logada na TBL_GER_HISTORICO_PROC_BATCH
	String msgBatch = "";
	
	/**
	 * Metodo...: consolidacaoContabil
	 * Descricao: Construtor
	 * @param long		aIdProcesso		ID do processo
	 * @param String	aDataReferencia	Data de Referencia
	 */
	public consolidacaoContabil(long aIdProcesso, String aDataReferencia) 
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_CONSOLIDACAO_CONTABIL);

		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	

		//Registra data de início do processamento
		this.dataReferencia = aDataReferencia;
	}

	/**
	 * Metodo...: consolidarContabilidade
	 * Descricao: Consolidação Contabil, metodo principal que coordena os outros
	 * @param
	 * @return short - Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public short consolidarContabilidade() throws GPPInternalErrorException
	{
		short retorno = 1;
		String dataInicialProcesso = null;
		String dataFinalProcesso = null;
		ConexaoBancoDados DBConexao = null;

		super.log(Definicoes.INFO,"consolidarContabilidade","Inicio  DATA "+dataReferencia);

		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			//Registra Início do Processamento
			dataInicialProcesso = GPPData.dataCompletaForamtada();
			
		
			//Realiza Consolidação Contabil
			retorno = consolida(DBConexao);
			
			// Registra Data Final do Processo e loga na TBL_GER_HISTORICO_PROC_BATCH
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			super.gravaHistoricoProcessos(Definicoes.IND_REL_CONSOLIDACAO_CONTABIL,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,"Consolidações ok:"+msgBatch,dataReferencia);
		}
		catch (Exception e)
		{
			//Pega data/hora final do processo batch
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			
			//Logar Processo Batch
			super.gravaHistoricoProcessos(Definicoes.IND_REL_CONSOLIDACAO_CONTABIL,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_ERRO,e.getMessage(),dataReferencia);
			super.log(Definicoes.ERRO,"consolidarContabilidade","ERRO GPP:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
			super.log(Definicoes.INFO,"consolidarContabilidade","Fim");
		}
		return retorno;	
	}
	

	/**
	 * Metodo...: consolida
	 * Descricao: Consolida os dados Contábeis 
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @return short 						- Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public short consolida(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		short retorno = 1;
		super.log(Definicoes.DEBUG,"consolidaContabilidade","Inicio");

		try
		{
/*******************************************************************************************************************/
			// Limpa tabela de 
			String query = "DELETE FROM TBL_REL_CONTABIL WHERE IDT_PERIODO_CONTABIL = substr(?, 4,8)";
			// Deleta TBL_REL_CONTABIL
			super.log(Definicoes.DEBUG,"consolidaContabil","delecao dados tabela TBL_REL_CONTABIL");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia},super.getIdLog());
/*******************************************************************************************************************/
			// Preenche tabela de 
			query =  "insert into TBL_REL_CONTABIL " +
				"(DAT_PROCESSAMENTO, IDT_PERIODO_CONTABIL, IDT_CODIGO_SERVICO_SFA, IDT_CODIGO_NACIONAL, IDT_ROAMING_ORIGIN, QTD_REGISTRO, VLR_DURACAO, VLR_TOTAL, VLR_TOTAL_SI, VLR_INDICE_BONIFICACAO, IDT_TIPO_SERVICO, IDT_PLANO_PRECO) " +
				" SELECT	" +
				"		sysdate as DAT_PROCESSAMENTO,	" +
				"		to_char(boni.dat_calculo,'MM/YYYY') as IDT_PERIODO_CONTABIL, " +
				"	    cdr_srv.idt_codigo_servico_sfa as IDT_COD_SERVICO_SFA,	" +
				"	    cdr.IDT_CODIGO_NACIONAL as IDT_CODIGO_NACIONAL,	" +
				"	    cdr.idt_roaming_origin as IDT_ROAMING_ORIGIN,	" +
				"		sum(cdr.qtd_registros) as QTD_REGISTROS,	" +
				"	    sum(cdr.vlr_duracao) as VLR_DURACAO,	" +
				"		sum(decode(	" +
				"			 instr('"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+":"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"',cdr_srv.idt_tipo_servico),0,	" +
				"			cdr.vlr_total,	" +
				"			 decode(cdr_srv.idt_tipo_servico,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"',cdr.vlr_total*(1-boni.vlr_indice_bonificacao_sp),cdr.vlr_total*boni.vlr_indice_bonificacao_sp)	" +
				"			)) as VLR_TOTAL,	" +
				"		sum(decode(	" +
				"			 instr('"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+":"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"',cdr_srv.idt_tipo_servico),0,	" +
				"			cdr.vlr_total_si,	" +
				"			 decode(cdr_srv.idt_tipo_servico,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"',cdr.vlr_total_si*(1-boni.vlr_indice_bonificacao_sp),cdr.vlr_total_si*boni.vlr_indice_bonificacao_sp)	" +
				"			)) as VLR_TOTAL_SI,	" +
				"	    decode(	" +
				"			 instr('"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+":"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"',cdr_srv.idt_tipo_servico),0,	" +
				"			0,	" +
				"			 boni.vlr_indice_bonificacao_sp)	" +
				"			as VLR_INDICE_BONIFICACAO, " +
				"			CDR_SRV.idt_tipo_servico AS TIPO_SERVICO, " +
				"			to_number(plano.idt_plano_preco) " +
				" FROM	" +
				"	    tbl_ger_cdr_servico	cdr_srv,	" +
				"		tbl_rel_cdr_dia	cdr,	" +
				"		tbl_ger_indice_bonificacao boni,	" +
				"		(select idt_plano_preco, decode(idt_categoria,0,'P','H') as pre_hibrido from tbl_ger_plano_preco where idt_categoria =1 or idt_categoria = 0) plano "+
				"where " +
				"		cdr.num_csp = cdr_srv.num_csp and	" +
				"	    cdr.idt_modulacao =	cdr_srv.idt_modulacao and	" +
				"	    cdr.tip_chamada = cdr_srv.tip_chamada and	" +
				"	    (cdr_srv.idt_tipo_servico =	'"+Definicoes.TIPO_SERVICO_CONSUMO_TERCEIROS+"' or	cdr_srv.idt_tipo_servico='"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"' or cdr_srv.idt_tipo_servico='"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"' )and			 " +
				"		to_char(boni.dat_calculo,'MM/YYYY') = substr(?,4,8) and " +
				"		boni.idt_codigo_nacional = cdr.idt_codigo_nacional and " +
				"		cdr.dat_cdr between				 " +
				"			add_months(to_date('27'||to_char(boni.dat_calculo,'/MM/YYYY'),'DD/MM/YYYY'),-1)+2 " +
				"			and to_date('28'||to_char(boni.dat_calculo,'/MM/YYYY'),'DD/MM/YYYY')	and " +
				"		trim(cdr.idt_plano) = plano.idt_plano_preco and " +
				"       cdr_srv.idt_plano_preco = plano.idt_plano_preco and " + 
				"		boni.idt_pre_hibrido = plano.pre_hibrido " +
				" group by	" +
				"		 cdr.IDT_CODIGO_NACIONAL ,	" +
				"		to_char(boni.dat_calculo,'MM/YYYY'), " +
				"	    cdr.idt_roaming_origin,	" +
				"	    cdr_srv.idt_codigo_servico_sfa,	" +
				"	    decode(instr('"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+":"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"',cdr_srv.idt_tipo_servico),0,0,boni.vlr_indice_bonificacao_sp), " +
				"	    CDR_SRV.idt_tipo_servico," +
				"		plano.idt_plano_preco "+
"union "+
				" SELECT	" +
				"	    sysdate as DAT_PROCESSAMENTO,	" +
				"		to_char(boni.dat_calculo,'MM/YYYY') as IDT_PERIODO_CONTABIL, " +
				"    	rec_srv.idt_codigo_servico_sfa as IDT_COD_SERVICO_SFA,	" +
				"    	rec.IDT_CODIGO_NACIONAL as IDT_CODIGO_NACIONAL,	" +
				"    	rec.idt_codigo_nacional as IDT_ROAMING_ORIGIN,	" +
				"	    sum(rec.qtd_registros) as QTD_REGISTROS,	" +
				"    	0 as VLR_DURACAO,	" +
				"		SUM(REC.VLR_TOTAL) AS VLR_TOTAL, " +
				"		SUM(REC.VLR_TOTAL_SI) AS VLR_TOTAL_SI, " +
				/*"		sum(decode(	" +
				"		 instr('"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+":"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"',rec_srv.idt_tipo_servico),0,	" +
				"		rec.vlr_total,	" +
				"		 decode(rec_srv.idt_tipo_servico,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"',rec.vlr_total*(1-boni.vlr_indice_bonificacao_sp),rec.vlr_total*boni.vlr_indice_bonificacao_sp)			)) as VLR_TOTAL,	" +
				"		sum(decode(	" +
				"		 instr('"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+":"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"',rec_srv.idt_tipo_servico),0,	" +
				"		rec.vlr_total_si," +
				"			 decode(rec_srv.idt_tipo_servico,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"',rec.vlr_total_si*(1-boni.vlr_indice_bonificacao_sp)," +
				"			rec.vlr_total_si*boni.vlr_indice_bonificacao_sp)			" +
				"		)) as VLR_TOTAL_SI,	" +*/
				"		decode(	" +
				"		 instr('"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+":"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"',rec_srv.idt_tipo_servico),0,	" +
				"		0,	" +
				"		boni.vlr_indice_bonificacao_sp)	" +
				"		 as VLR_INDICE_BONIFICACAO,	" +
				"		REC_SRV.idt_tipo_servico AS TIPO_SERVICO, " +
				"		to_number(plano.idt_plano_preco) " +
				" FROM	" +
				"    	tbl_ger_recarga_servico rec_srv,	" +
				"	 	tbl_rel_recargas rec,	" +
				"		tbl_ger_indice_bonificacao boni,	" +
				"		(select idt_plano_preco, decode(idt_categoria,0,'P','H') as pre_hibrido from tbl_ger_plano_preco where idt_categoria =1 or idt_categoria = 0) plano "+
				" where	" +
				"		rec.COD_RETORNO = 0 and" +
				"		rec.id_canal = rec_srv.id_canal	and	" +
				"    	rec.id_origem = rec_srv.id_origem and	" +
				"    	rec.id_sistema_origem = rec_srv.id_sistema_origem and	" +
				"    	(rec_srv.idt_tipo_servico =	'"+Definicoes.TIPO_SERVICO_CONSUMO_TERCEIROS+"' or	rec_srv.idt_tipo_servico='"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"' or rec_srv.idt_tipo_servico='"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"'			 or rec_srv.idt_tipo_servico='"+Definicoes.TIPO_SERVICO_RECARGA+"' or rec_srv.idt_tipo_servico='"+Definicoes.TIPO_SERVICO_BONUS+"' ) and	" +
				"		 to_char(boni.dat_calculo,'MM/YYYY') = substr(?,4,8) and	" +
				"		boni.idt_codigo_nacional = rec.idt_codigo_nacional and	" +
				"		rec.dat_recarga	between	" +
				"				 add_months(to_date('27'||to_char(boni.dat_calculo,'/MM/YYYY'),'DD/MM/YYYY'),-1)+2	" +
				"				and to_date('28'||to_char(boni.dat_calculo,'/MM/YYYY'),'DD/MM/YYYY')	and " +
				"		trim(rec.idt_plano) = plano.idt_plano_preco and " +
				"		rec_srv.idt_plano_preco = plano.idt_plano_preco and " + 
				"		boni.idt_pre_hibrido = plano.pre_hibrido " +
				" group by	" +
				"		rec.IDT_CODIGO_NACIONAL	,	" +
				"		to_char(boni.dat_calculo,'MM/YYYY'), " +
				"    	rec_srv.idt_codigo_servico_sfa,	" +
				"    	decode(instr('"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+":"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"',rec_srv.idt_tipo_servico),0,0,boni.vlr_indice_bonificacao_sp), " +
				"   	REC_SRV.idt_tipo_servico, " +
				"   	plano.idt_plano_preco  "+
" union "+
				" SELECT	" +
				"	    sysdate as DAT_PROCESSAMENTO,	" +
				"		to_char(sld.dat_saldo_assinante,'MM/YYYY') as IDT_PERIODO_CONTABIL, " +
				"    	sld_srv.idt_codigo_servico_sfa as IDT_COD_SERVICO_SFA,	" +
				"    	sld.IDT_CODIGO_NACIONAL as IDT_CODIGO_NACIONAL,	" +
				"    	sld.idt_codigo_nacional as IDT_ROAMING_ORIGIN,	" +
				"    	sum(sld.qtd_registros) as QTD_REGISTROS,	" +
				"    	0 as VLR_DURACAO,	" +
				"    	sum(sld.vlr_acumulado_saldo) as VLR_TOTAL,	" +
				"    	0 as VLR_TOTAL_SI,	" +
				"    	0 as VLR_INDICE_BONIFICACAO,	" +
				"		SLD_SRV.idt_tipo_servico AS TIPO_SERVICO, " +
				"		to_number(plano.idt_plano_preco) " +
				" FROM	" +
				"    	tbl_ger_saldo_servico sld_srv,	" +
				"	 	tbl_rel_saldos	sld,	" +
				"		(select idt_plano_preco, decode(idt_categoria,0,'P','H') as pre_hibrido from tbl_ger_plano_preco where idt_categoria =1 or idt_categoria = 0) plano "+
				" where	" +
				"		sld_srv.idt_codigo_servico_sfa not in (70967,70968,70969) and " + //Supostamente provisorio
				"		sld.idt_status_assinante = sld_srv.idt_status_assinante	and	" +
				"    	sld_srv.idt_tipo_servico = '"+Definicoes.TIPO_SERVICO_VALOR_DISPONIVEL+"' and	" +
				"				to_char(sld.dat_saldo_assinante,'MM/YYYY') = substr(?,4,8) and	" +
				"    	sld.dat_saldo_assinante between		" +
				"		 add_months(to_date('27'||to_char(sld.dat_saldo_assinante,'/MM/YYYY'),'DD/MM/YYYY'),-1)+2	" +
				"			and to_date('28'||to_char(sld.dat_saldo_assinante,'/MM/YYYY'),'DD/MM/YYYY')	and " +
				"		trim(sld.idt_plano) = plano.idt_plano_preco " + 
				" group by	" +
				"	 	sld.IDT_CODIGO_NACIONAL ,	" +
				"		to_char(sld.dat_saldo_assinante,'MM/YYYY'), " +
				"    	sld_srv.idt_codigo_servico_sfa, " +
				"		SLD_SRV.idt_tipo_servico, " +
				"		plano.idt_plano_preco  "+ 
" union "+
				" SELECT	" +
				"	    sysdate as DAT_PROCESSAMENTO,	" +
				"		to_char(vch.dat_voucher,'MM/YYYY') as IDT_PERIODO_CONTABIL, " +
				"    	vch_srv.idt_codigo_servico_sfa as IDT_COD_SERVICO_SFA,	" +
				"    	vch.idt_codigo_nacional as IDT_CODIGO_NACIONAL,	" +
				"    	vch.idt_codigo_nacional as IDT_ROAMING_ORIGIN,	" +
				"    	sum(vch.qtd_registros) as QTD_REGISTROS,	" +
				"    	0 as VLR_DURACAO,	" +
				"    	sum(vch.vlr_total) as VLR_TOTAL ,	" +
				"    	0 as VLR_TOTAL_SI,	" +
				"    	0 as VLR_INDICE_BONIFICACAO,	" +
				"		VCH_SRV.idt_tipo_servico AS TIPO_SERVICO, " +
				"		0 " +
				" FROM	" +
				"    	tbl_ger_voucher_servico vch_srv,	" +
				"	 	tbl_rel_vouchers vch	" +
				" where	" +
				"		vch.id_status_voucher =	vch_srv.id_status_voucher and	" +
				"		vch.tip_cartao = vch_srv.tip_cartao and	" +
				"		vch_srv.idt_tipo_servico = '"+Definicoes.TIPO_SERVICO_VALOR_DISPONIVEL+"'	and	" +
				"		 to_char(vch.dat_voucher,'MM/YYYY') = substr(?,4,8) and	" +
				"		vch.dat_voucher	between	" +
				"				 add_months(to_date('27'||to_char(vch.dat_voucher,'/MM/YYYY'),'DD/MM/YYYY'),-1)+2	" +
				"			and to_date('28'||to_char(vch.dat_voucher,'/MM/YYYY'),'DD/MM/YYYY')	" +
				" group by	" +
				"	 	vch_srv.idt_codigo_servico_sfa,  " +
				"		to_char(vch.dat_voucher,'MM/YYYY'), " +
				"	    vch.idt_codigo_nacional," +
				"		VCH_SRV.idt_tipo_servico ";
			// Popula TBL_REL_CONTABIL
			super.log(Definicoes.DEBUG,"sumarizaContabil","Populando dados tabela TBL_REL_CONTABIL");
			DBConexao.executaPreparedQuery(query,new Object[]{dataReferencia,dataReferencia,dataReferencia,dataReferencia},super.getIdLog());

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
			super.log(Definicoes.DEBUG,"consolidaContabilidade","Fim");
		}

	}
	
}
