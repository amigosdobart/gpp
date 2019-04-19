package com.brt.gpp.aplicacoes.contabil.consolidacaoContabil;

import java.text.SimpleDateFormat;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Classe responsavel por todos os acessos ao banco de dados do GPP para a Consolidacao Contabil Diaria
 * 
 * @author Magno Batista Correa, Bernardo Vergne Dias, Daniel Ferreira
 * Criado em 23/11/2007
 */
public class ConsolidacaoContabilDiariaDAO
{
	private PREPConexao conexao;
	private long idProcesso;
    
	/**
	 * Contrutor
	 * @param conexao
	 * @param idProcesso
	 */
	public ConsolidacaoContabilDiariaDAO(PREPConexao conexao, long idProcesso)
	{
		this.conexao = conexao;
		this.idProcesso = idProcesso;
	}
	
/*************************************************************************************************************/
	
    /**
	 * Retorna o nome do campo referente ao saldo
     * 
	 * @param tipoSaldo
	 * @param semImposto
	 * @return
	 */
	private String getCampoReferenteAoSaldo(String tipoSaldo, boolean semImposto)
	 {
		String retorno = null;
		
		if(tipoSaldo.equals("SP"))
			retorno = "vlr_total_principal";
		if(tipoSaldo.equals("SB"))
			retorno = "vlr_total_bonus";
		if(tipoSaldo.equals("SS"))
			retorno = "vlr_total_sms";
		if(tipoSaldo.equals("SD"))
			retorno = "vlr_total_dados";
		if(tipoSaldo.equals("SF"))
			retorno = "VLR_TOTAL_PERIODICO";
		
		// Acrescenta sufixo '_si'  no nome do campo se for sem imposto
		if(retorno != null && semImposto)
			retorno = retorno + "_si";
		
		return retorno;
	}
	
	/**
	 * Roda Query Principal da consolidação contábil
     * 
	 * @param tipoSaldo
	 * @param vo
	 * @throws GPPInternalErrorException
	 */
	public void rodaQueryConsolidacao(String tipoSaldo, 
			 ConsolidacaoContabilDiariaVO vo) throws GPPInternalErrorException
	{
		 SimpleDateFormat sdfDataReferencia = new SimpleDateFormat("dd/MM/yyyy");
		 SimpleDateFormat sdfDataParticao = new SimpleDateFormat("yyyyMMdd");
		 SimpleDateFormat sdfPeriodoContabil = new SimpleDateFormat("MM/yyyy");
		 SimpleDateFormat sdfPeriodoContabilParticao = new SimpleDateFormat("yyyyMM");

		 String dataReferencia          = sdfDataReferencia.format(vo.getDataAProcessar());
		 String dataParticao            = sdfDataParticao.format(vo.getDataAProcessar());
		 String periodoContabil         = vo.getPeriodoContabil();
		 String periodoContabilParticao = null;
		 try{
			 periodoContabilParticao = sdfPeriodoContabilParticao.format(
					 sdfPeriodoContabil.parse(vo.getPeriodoContabil())); 
		 }
		 catch (Exception e)
		 {
			 throw new GPPInternalErrorException("Configuracao da tabela de TBL_CON_PERIODO_CONTABIL errada. Periodo contabil invalido!");
		 }
		          
		String query =
			" INSERT into TBL_CON_CONTABIL_DIA " +
            "  (                           " +
            "  DAT_PROCESSAMENTO,          " +
            "  DAT_CONTABILIZADA,          " +
            "  IDT_PERIODO_CONTABIL,       " +
            "  IDT_TIPO_SALDO,             " +
            "  IDT_CODIGO_SERVICO_SFA,     " +
            "  IDT_CODIGO_NACIONAL,        " +
            "  IDT_ROAMING_ORIGIN,         " +
            "  QTD_REGISTRO,               " +
            "  VLR_DURACAO,                " +
            "  VLR_TOTAL,                  " +
            "  VLR_TOTAL_SI,               " +
            "  VLR_INDICE_BONIFICACAO,     " +
            "  IDT_TIPO_SERVICO,           " +
            "  IDT_PLANO_PRECO,            " +
            "  ID_CHAVE1,                  " +
            "  ID_CHAVE2,                  " +
            "  ID_CHAVE3,                  " +
            "  IDT_EOT_ORIGEM,             " +
            "  IDT_EOT_DESTINO             " +
            "  )                           " +
            " SELECT                       " +
            "sysdate 						as DAT_PROCESSAMENTO,                           " +
            "TO_DATE(?,'DD/MM/YYYY')        AS DAT_CONTABILIZADA,                           " +
            "boni.idt_periodo_contabil 		as IDT_PERIODO_CONTABIL,                        " +
            "boni.idt_tipo_saldo 			as IDT_TIPO_SALDO,                              " +
            "cdr_srv.idt_codigo_servico_sfa as IDT_COD_SERVICO_SFA,                         " +
            "cdr.IDT_CODIGO_NACIONAL 		as IDT_CODIGO_NACIONAL,                         " +
            "cdr.idt_roaming_origin 		as IDT_ROAMING_ORIGIN,                          " +
            "sum(decode(	cdr_srv.idt_tipo_servico,                                       " +
            "				'CBB', cdr.qtd_registros * boni.vlr_indice_bonificacao_BTSA,    " +
            "				'CRB', cdr.qtd_registros * (1-boni.vlr_indice_bonificacao_BTSA)," +
            "				cdr.qtd_registros                                               " +
            ")) as QTD_REGISTROS,                                                           " +
            "SUM(                                                                           " +
            "      case when cdr.vlr_total = 0                                              " +
            "            then case when substr(cdr_srv.idt_tipo_servico,0,2) = 'CR' and boni.idt_tipo_saldo = 'SP' " +
            "				        then cdr.vlr_duracao " +
            "				        else 0 " +
            "                 end " +
	         "            else ( cdr.vlr_duracao * cdr."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+" / cdr.vlr_total * " +
	         "                   ( case " +
	         "                      when INSTR ('CRG:CRB:CBB:CBG', cdr_srv.idt_tipo_servico) = 0 " +
	         "                      then 1 " +
	         "                      else ( case when substr(cdr_srv.idt_tipo_servico,0,2) = 'CR' " +
	         "                                       then (1 - ( case when substr(cdr_srv.idt_tipo_servico,3,1) = 'G' " +
	         "                                                        then boni.vlr_indice_bonificacao_gsm " +
	         "                                                        else boni.vlr_indice_bonificacao_btsa " +
	         "                                                        end ) " +
	         "                                            ) " +
	         "                                       else ( case when substr(cdr_srv.idt_tipo_servico,3,1) = 'G' " +
	         "                                                   then boni.vlr_indice_bonificacao_gsm " +
	         "                                                   else boni.vlr_indice_bonificacao_btsa " +
	         "                                                   end " +
	         "                                            ) " +
	         "                                   end " +
	         "                           ) " +
	         "                       end " +
	         "                       ) " +
	         "                   ) " +
	         "            end " +
	         "       ) " +
	         "as VLR_DURACAO, " +
			 "sum(decode( "+	
				  "instr('CRG:CRB:CBB:CBG',cdr_srv.idt_tipo_servico),0, "+  // Não é Consumo GSM nem BTSA+ 
				  "cdr."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+", "+
				  "decode(substr(cdr_srv.idt_tipo_servico,0,2),'CR', "+ 
				  "cdr."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+"*(1-decode(substr(cdr_srv.idt_tipo_servico,3,1),'G', "+ //depende do saldo 
                      "boni.vlr_indice_bonificacao_GSM, "+ 
                      "boni.vlr_indice_bonificacao_BTSA)), "+ // se for consumo de Recargas
                      "cdr."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+"* decode(substr(cdr_srv.idt_tipo_servico,3,1),'G', "+   // depende do saldo
                      "boni.vlr_indice_bonificacao_GSM, "+ 
                      "boni.vlr_indice_bonificacao_BTSA)) "+  // se for consumo de Bônus
				 ")) as VLR_TOTAL, "+ 	
			 "sum(decode( "+ 	
				  "instr('CRG:CRB:CBB:CBG',cdr_srv.idt_tipo_servico),0, "+ //A mesma coisa de cima so que sem imposto	
				  "cdr."+this.getCampoReferenteAoSaldo(tipoSaldo,true)+","+  
				  "decode(substr(cdr_srv.idt_tipo_servico,0,2),'CR', "+ 
				  "cdr."+this.getCampoReferenteAoSaldo(tipoSaldo,true)+"*(1-decode(substr(cdr_srv.idt_tipo_servico,3,1),'G', "+ 
					  "boni.vlr_indice_bonificacao_GSM, "+ 
					  "boni.vlr_indice_bonificacao_BTSA)), "+ 
					  "cdr."+this.getCampoReferenteAoSaldo(tipoSaldo,true) +"*	decode(substr(cdr_srv.idt_tipo_servico,3,1),'G',"+ 
					  "boni.vlr_indice_bonificacao_GSM, "+ 
					  "boni.vlr_indice_bonificacao_BTSA)) "+ 	
				 ")) as VLR_TOTAL_SI, "+ 	
				 /*INI*/"decode(  "+
				  "instr('CRG:CRB:CBB:CBG',cdr_srv.idt_tipo_servico),0, "+ 	
				  "0, "+ 
				  "decode(substr(cdr_srv.idt_tipo_servico,3,1),'G', "+  
					              "boni.vlr_indice_bonificacao_GSM, "+  
					              "boni.vlr_indice_bonificacao_BTSA) "+  
				/*FIM*/ ") as VLR_INDICE_BONIFICACAO, "+
				/**/ "CDR_SRV.idt_tipo_servico AS TIPO_SERVICO, "+  
				/**/ "to_number(plano.idt_plano_preco) as IDT_PLANO_PRECO, "+ 
				/**/ "cdr_srv.num_csp as ID_CHAVE1, "+ 
				/**/ "cdr_srv.idt_modulacao as ID_CHAVE2, "+ 
				/**/ "cdr_srv.tip_chamada as ID_CHAVE3, "+ 
				/**/ "nvl(cdr.idt_eot_origem,cdr.IDT_CODIGO_NACIONAL) , " + 
				/**/ "nvl(cdr.idt_eot_destino,cdr.IDT_CODIGO_NACIONAL) " +
	  "FROM "+ 	
		     "tbl_con_cdr_servico cdr_srv,                                       "+ 	
			 "tbl_rel_cdr_dia PARTITION(PRCD" + dataParticao + ") cdr, "+	
			 "tbl_con_indice_bonificacao PARTITION(PCIB" + periodoContabilParticao + ") boni, "+	
			 "(select idt_plano_preco, decode(idt_categoria,0,'P','H') as pre_hibrido from tbl_ger_plano_preco where idt_categoria =1 or idt_categoria = 0) plano "+ 
	 "where "+ 
			 "cdr.num_csp = cdr_srv.num_csp and "+	
		     "cdr.idt_modulacao =	cdr_srv.idt_modulacao and "+	
		     "cdr.tip_chamada = cdr_srv.tip_chamada and "+	
		     "substr(cdr_srv.idt_tipo_servico,0,2) in	('CR','CB') and "+	// Essa linha já garante que nada de LigMix será pegado		 
			 "boni.idt_periodo_contabil = ? and "+ 				// Parametro 1 - data
			 "boni.idt_codigo_nacional = cdr.idt_codigo_nacional and " +
			 "cdr."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+" is not null and "+ 
			 "cdr.dat_cdr between "+				 
				 "add_months(to_date('27'||boni.idt_periodo_contabil,'DD/MM/YYYY'),-1)+2 "+ 
				 "and to_date(?,'DD/MM/YYYY')	and "+		// Parametro 2 (data final de período - dataReferencia)
			 "trim(cdr.idt_plano) = plano.idt_plano_preco and "+ 
	        "cdr_srv.idt_plano_preco = plano.idt_plano_preco and " +  
			 "boni.idt_pre_hibrido = plano.pre_hibrido and "+
			 "boni.idt_tipo_saldo = ? "+ 		// Parametro 3 - tipo Saldo
	  "group by "+	
			 "cdr.IDT_CODIGO_NACIONAL , "+	
			 "boni.idt_periodo_contabil, " +
			 "boni.idt_tipo_saldo, "+
		     "cdr.idt_roaming_origin, "+	
		     "cdr_srv.idt_codigo_servico_sfa, "+	
		     "decode(instr('CRG:CRB:CBB:CBG',cdr_srv.idt_tipo_servico),0,0,decode(substr(cdr_srv.idt_tipo_servico,3,1),'G',boni.vlr_indice_bonificacao_GSM,boni.vlr_indice_bonificacao_BTSA) ), "+
		     "CDR_SRV.idt_tipo_servico, "+
		     "boni.idt_tipo_saldo, "+
			 "plano.idt_plano_preco, "+
			 "cdr_srv.num_csp, "+
			 "cdr_srv.idt_modulacao, "+
			 "cdr_srv.tip_chamada, "+ 
			 "decode(  "+
					  "instr('CRG:CRB:CBB:CBG',cdr_srv.idt_tipo_servico),0, "+ 	
					  "0, "+ 
					  "decode(substr(cdr_srv.idt_tipo_servico,3,1),'G', "+  
						              "boni.vlr_indice_bonificacao_GSM, "+  
						              "boni.vlr_indice_bonificacao_BTSA) "+  
					 "), "+
			 "cdr.idt_eot_origem, " + 
			 "cdr.idt_eot_destino " +
	  "union "+ 	
	  "SELECT "+	
		     "sysdate as DAT_PROCESSAMENTO, "+	
             "TO_DATE(?,'DD/MM/YYYY')        AS DAT_CONTABILIZADA,                           " + 
			 "boni.idt_periodo_contabil as IDT_PERIODO_CONTABIL, "+ 
			 "boni.idt_tipo_saldo as IDT_TIPO_SALDO, "+  
	    	 "rec_srv.idt_codigo_servico_sfa as IDT_COD_SERVICO_SFA, "+	
	    	 "rec.IDT_CODIGO_NACIONAL as IDT_CODIGO_NACIONAL, "+	
	    	 "rec.idt_codigo_nacional as IDT_ROAMING_ORIGIN, "+	
			 "sum(decode(	rec_srv.idt_tipo_servico, " +
			 "				'CBB', rec.qtd_registros * boni.vlr_indice_bonificacao_BTSA," +
			 "				'CRB', rec.qtd_registros * (1-boni.vlr_indice_bonificacao_BTSA)," +
			 "				rec.qtd_registros " +
			 ")) as QTD_REGISTROS, " +
	    	 "0 as VLR_DURACAO, "+	
    		 "sum(decode( "+
  			 "	  instr('CRG:CRB:CBB:CBG',rec_srv.idt_tipo_servico),0, "+    //  -- Não é Consumo GSM nem BTSA+ 
  			 "	  decode(rec_srv.idt_tipo_servico,'CRO',-rec."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+", rec."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+"), "+   // -- só verifica se é um consumo CRO, para inverter seu sinal
  			 "	  decode(substr(rec_srv.idt_tipo_servico,1,2), "+
             "                     'CR', -rec."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+"*(1-decode(substr(rec_srv.idt_tipo_servico,3,1),'G', "+
                                                                                 "boni.vlr_indice_bonificacao_GSM, "+ 
                                                                                 "boni.vlr_indice_bonificacao_BTSA)),  "+ // se for consumo de Recargas
                                  "-rec."+this.getCampoReferenteAoSaldo(tipoSaldo,false)+"* decode(substr(rec_srv.idt_tipo_servico,3,1),'G', "+ 
                                                                         "boni.vlr_indice_bonificacao_GSM, "+ 
                                                                         "boni.vlr_indice_bonificacao_BTSA)) " +  //--se for consumo de Bônus
  			 "	 )) as VLR_TOTAL, "+
  			 "sum(decode( "+
  				  "instr('CRG:CRB:CBB:CBG',rec_srv.idt_tipo_servico),0, "+ // -- Não é Consumo GSM nem BTSA+ 
  				  "decode(rec_srv.idt_tipo_servico,'CRO',-rec."+this.getCampoReferenteAoSaldo(tipoSaldo,true)+", rec."+this.getCampoReferenteAoSaldo(tipoSaldo,true)+"),  "+  // -- só verifica se é um consumo CRO, para inverter seu sinal
  				  "decode(substr(rec_srv.idt_tipo_servico,1,2), "+
                                  "'CR', -rec."+this.getCampoReferenteAoSaldo(tipoSaldo,true)+"*(1-decode(substr(rec_srv.idt_tipo_servico,3,1),'G', "+
                                                                                 "boni.vlr_indice_bonificacao_GSM, "+ 
                                                                                 "boni.vlr_indice_bonificacao_BTSA)), "+ // se for consumo de Recargas
                                  "-rec."+this.getCampoReferenteAoSaldo(tipoSaldo,true)+"* decode(substr(rec_srv.idt_tipo_servico,3,1),'G', "+ 
                                                                         "boni.vlr_indice_bonificacao_GSM, "+ 
                                                                         "boni.vlr_indice_bonificacao_BTSA)) "+ //--se for consumo de Bônus
  			 "	 )) as VLR_TOTAL_SI, "+
  			"decode(substr(rec_srv.idt_tipo_servico,3,1), "+
                    "'G', boni.vlr_indice_bonificacao_GSM, "+ 
					   "'B', boni.vlr_indice_bonificacao_BTSA, "+
                     "0) as VLR_INDICE_BONIFICACAO, "+
			 "REC_SRV.idt_tipo_servico AS TIPO_SERVICO, "+ 
			 "to_number(plano.idt_plano_preco)as IDT_PLANO_PRECO , "+
			 "rec_srv.id_canal as ID_CHAVE1, "+
			 "rec_srv.id_origem as ID_CHAVE2, "+
			 "rec_srv.id_sistema_origem as ID_CHAVE3, "+
			 "to_char(rec.idt_codigo_nacional,'99'), " + 
			 "to_char(rec.idt_codigo_nacional,'99') " +
	  "FROM "+	
	    	 "tbl_con_recarga_servico rec_srv, " +
		 	 "tbl_rel_recargas PARTITION (PRR" + dataParticao + ")rec, " +
		 	 "tbl_con_indice_bonificacao PARTITION(PCIB" + periodoContabilParticao + ") boni,"+	
			 "(select idt_plano_preco, decode(idt_categoria,0,'P','H') as pre_hibrido from tbl_ger_plano_preco where idt_categoria =1 or idt_categoria = 0) plano "+ 
	  "where "+	
			 "rec.COD_RETORNO = 0 and "+ //Só recargas OK
			 "rec.id_canal = rec_srv.id_canal	and "+	
	    	 "rec.id_origem = rec_srv.id_origem and "+	
	    	 "rec.id_sistema_origem = rec_srv.id_sistema_origem and " +
	    	 "rec."+this.getCampoReferenteAoSaldo(tipoSaldo,true)+" is not null and "+	
	    	 "(substr(rec_srv.idt_tipo_servico,0,2) =	'CR' or	substr(rec_srv.idt_tipo_servico,0,2)='CB' "+			 
			 "or rec_srv.idt_tipo_servico='RE' or rec_srv.idt_tipo_servico='BO' ) and "+	
			 "rec.dat_recarga	between "+	
					  "add_months(to_date('27'||boni.idt_periodo_contabil,'DD/MM/YYYY'),-1)+2 "+
					 //"and to_date('28'||substr(?,4,7),'DD/MM/YYYY')	and "+ 		// Parametro 5 - Data
					  "and to_date(?,'DD/MM/YYYY')	and "+ 		// Parametro 4 - Data Referncia (Data final de período)
			 "trim(rec.idt_plano) = plano.idt_plano_preco and "+ 
			 "rec_srv.idt_plano_preco = plano.idt_plano_preco " +
			 "and boni.idt_periodo_contabil = ? and "+	// Parametro 5
			 "boni.idt_codigo_nacional = rec.idt_codigo_nacional and "+
    		 "boni.idt_pre_hibrido = plano.pre_hibrido and "+
			 "boni.idt_tipo_saldo = ? "+  // Parametro 6
	  "group by "+	
			 "rec.IDT_CODIGO_NACIONAL	, "+	
			 "boni.idt_periodo_contabil, " +
			 "boni.idt_tipo_saldo, "+
	    	 "rec_srv.idt_codigo_servico_sfa, "+	
	    	 "0, "+ 
		   	 "REC_SRV.idt_tipo_servico, "+ 
		   	 "plano.idt_plano_preco, "+
			 "rec_srv.id_canal , "+
			 "rec_srv.id_origem , "+
			 "rec_srv.id_sistema_origem," +
			 "decode(substr(rec_srv.idt_tipo_servico,3,1),"+
                       "'G', boni.vlr_indice_bonificacao_GSM, "+ 
					   "'B', boni.vlr_indice_bonificacao_BTSA, "+
                        "0)";
		
		Object[] pars = {	
				dataReferencia, // data_contabilizada
				periodoContabil, 
				dataReferencia,
				tipoSaldo,
				dataReferencia, // data_contabilizada
				dataReferencia, 
				periodoContabil, 
				tipoSaldo};

		this.conexao.executaPreparedUpdate(query, pars, idProcesso);
		
	}	 
}
