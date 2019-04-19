// Definicao do Pacote
package com.brt.gpp.aplicacoes.contabilidade;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Imports de Java
import java.sql.*;

/**
  * Essa classe refere-se ao processo de consolidação Contábil:
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Alberto Magno
  * Data: 				11/05/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class ConsolidacaoContabil extends Contabilidade 
{
	/**
	 * Metodo...: consolidacaoContabil
	 * Descricao: Construtor
	 * @param long		aIdProcesso		ID do processo
	 * @param String	aDataReferencia	Data de Referencia
	 */
	public ConsolidacaoContabil(long aIdProcesso, String aDataReferencia) throws GPPInternalErrorException
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_CONSOLIDACAO_CONTABIL, aDataReferencia);
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

		super.log(Definicoes.INFO,"ConsolidarContabilidade","Inicio  DATA "+dataReferencia);

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
			super.gravaHistoricoProcessos(Definicoes.IND_REL_CONSOLIDACAO_CONTABIL,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,"Consolidações ok:",dataReferencia);
		}
		catch (Exception e)
		{
			//Pega data/hora final do processo batch
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			
			//Logar Processo Batch
			super.gravaHistoricoProcessos(Definicoes.IND_REL_CONSOLIDACAO_CONTABIL,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_ERRO,e.getMessage(),dataReferencia);
			super.log(Definicoes.ERRO,"ConsolidarContabilidade","ERRO GPP:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
			super.log(Definicoes.INFO,"ConsolidarContabilidade","Fim");
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
		short retorno = 0;
		super.log(Definicoes.DEBUG,"consolidaContabilidade","Inicio");

		try
		{
/*******************************************************************************************************************/
			// Limpa tabela de 
			String query = "DELETE FROM TBL_con_CONTABIL WHERE IDT_PERIODO_CONTABIL = ?";
			// Deleta TBL_REL_CONTABIL
			super.log(Definicoes.DEBUG,"ConsolidaContabil","delecao dados tabela TBL_CON_CONTABIL");
			DBConexao.executaPreparedQuery(query,new Object[]{super.periodoContabil},super.getIdLog());
/*******************************************************************************************************************/
			// Preenche tabela de CONSOLIDACAO MENSAL para acessos GSM
			this.rodaQueryConsolidacao("SP", DBConexao);
			this.rodaQueryConsolidacao("SB", DBConexao);
			this.rodaQueryConsolidacao("SS", DBConexao);
			this.rodaQueryConsolidacao("SD", DBConexao);
			
			// Preenche tabela de CONSOLIDAÇÃO MENSAL para acessos LigMix
			//this.rodaQueryConsolidacaoLigMix(DBConexao);
			
			// Inclui perdas de CDRs
			this.incluiPerdaCDRs(DBConexao);
			
			// Elimina zeros
			this.eliminaZeros(DBConexao);

			return retorno;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"ConsolidaContabilidade","Erro GPP:"+e);
			retorno = -1;
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			super.log(Definicoes.DEBUG,"ConsolidaContabilidade","Fim");
		}
	}
	
	 private String getCampoReferenteAoSaldo(String tipoSaldo, boolean semImposto)
	 {
		String retorno = null;
		
		if(tipoSaldo.equals("SP"))
		{
			retorno = "vlr_total_principal";
		}
		if(tipoSaldo.equals("SB"))
		{
			retorno = "vlr_total_bonus";
		}
		if(tipoSaldo.equals("SS"))
		{
			retorno = "vlr_total_sms";
		}
		if(tipoSaldo.equals("SD"))
		{
			retorno = "vlr_total_dados";
		}
		
		// Acrescenta sufixo '_si'  no nome do campo se for sem imposto
		if(retorno != null && semImposto)
		{
			retorno = retorno + "_si";
		}
		
		return retorno;
	}
	 
	/***
	 * Método...: rodaQueryConsolidacao
	 * Descricao: Roda Query Principal da consolidação contábil
	 * @param String			tipoSaldo	Saldo a ser contabilizado (SP, SB, SS, SD)
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @throws GPPInternalErrorException
	 */
	 private void rodaQueryConsolidacao(String tipoSaldo, ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		          
		String query = "INSERT into TBL_CON_CONTABIL "+
		" ( "+
		" DAT_PROCESSAMENTO, "+ 
		" IDT_PERIODO_CONTABIL, "+ 
		" IDT_TIPO_SALDO, " +
		" IDT_CODIGO_SERVICO_SFA, "+ 
		" IDT_CODIGO_NACIONAL, "+ 
		" IDT_ROAMING_ORIGIN, "+ 
		" QTD_REGISTRO, "+ 
		" VLR_DURACAO, "+ 
		" VLR_TOTAL, "+ 
		" VLR_TOTAL_SI, "+ 
		" VLR_INDICE_BONIFICACAO, "+ 
		" IDT_TIPO_SERVICO, "+ 
		" IDT_PLANO_PRECO, "+ 
		" ID_CHAVE1, "+ 
		" ID_CHAVE2, "+ 
		" ID_CHAVE3, " +
		" IDT_EOT_ORIGEM, " +
		" IDT_EOT_DESTINO "+
		" ) "+
		"SELECT "+
			 "sysdate 						as DAT_PROCESSAMENTO, "+ 	
			 "boni.idt_periodo_contabil 		as IDT_PERIODO_CONTABIL, "+  
			 "boni.idt_tipo_saldo 			as IDT_TIPO_SALDO, "+  
		     "cdr_srv.idt_codigo_servico_sfa 	as IDT_COD_SERVICO_SFA, "+ 	
		     "cdr.IDT_CODIGO_NACIONAL 		as IDT_CODIGO_NACIONAL, "+ 	
		     "cdr.idt_roaming_origin 			as IDT_ROAMING_ORIGIN, "+ 	
			 "sum(decode(	cdr_srv.idt_tipo_servico, " +
			 "				'CBB', cdr.qtd_registros * boni.vlr_indice_bonificacao_BTSA," +
			 "				'CRB', cdr.qtd_registros * (1-boni.vlr_indice_bonificacao_BTSA)," +
			 "				cdr.qtd_registros " +
			 ")) as QTD_REGISTROS, " +
	         "SUM( " +
	         "      case when cdr.vlr_total = 0 " +
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
				"decode(  "+
				  "instr('CRG:CRB:CBB:CBG',cdr_srv.idt_tipo_servico),0, "+ 	
				  "0, "+ 
				  "decode(substr(cdr_srv.idt_tipo_servico,3,1),'G', "+  
					              "boni.vlr_indice_bonificacao_GSM, "+  
					              "boni.vlr_indice_bonificacao_BTSA) "+  
				 ") as VLR_INDICE_BONIFICACAO, "+
				 "CDR_SRV.idt_tipo_servico AS TIPO_SERVICO, "+  
				 "to_number(plano.idt_plano_preco) as IDT_PLANO_PRECO, "+ 
				 "cdr_srv.num_csp as ID_CHAVE1, "+ 
				 "cdr_srv.idt_modulacao as ID_CHAVE2, "+ 
				 "cdr_srv.tip_chamada as ID_CHAVE3, "+ 
				 "nvl(cdr.idt_eot_origem,cdr.IDT_CODIGO_NACIONAL) , " + 
				 "nvl(cdr.idt_eot_destino,cdr.IDT_CODIGO_NACIONAL) " +
	  "FROM "+ 	
		     "tbl_con_cdr_servico cdr_srv, "+ 	
			 "tbl_rel_cdr_dia cdr, "+	
			 "tbl_con_indice_bonificacao boni, "+	
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
	    	 "tbl_con_recarga_servico rec_srv, "+	
		 	 "tbl_rel_recargas rec, " +
		 	 "tbl_con_indice_bonificacao boni,"+	
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
		
		Object[] pars = {	super.periodoContabil,	// Parametro 1 
							super.dataReferencia,	// Parametro 2
							tipoSaldo,				// Parametro 3 
							super.dataReferencia,	// Parametro 4 
							super.periodoContabil,	// Parametro 5 
							tipoSaldo			};	// Parametro 6

		DBConexao.executaPreparedUpdate(query, pars, super.getIdLog());
		
		super.log(Definicoes.INFO, "rodaQueryConsolidacao", "Insercao de saldo: " + tipoSaldo + " finalizada.");
	}
	 
		/***
		 * Método...: rodaQueryConsolidacaoLigMix
		 * Descricao: Roda Query para consolidação contábil das chamadas do ligmiX
		 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
		 * @throws GPPInternalErrorException
		 */
		 /*private void rodaQueryConsolidacaoLigMix(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
		{
			String query = "INSERT into TBL_CON_CONTABIL "+
			" ( "+
			" DAT_PROCESSAMENTO, "+ 
			" IDT_PERIODO_CONTABIL, "+ 
			" IDT_TIPO_SALDO, " +
			" IDT_CODIGO_SERVICO_SFA, "+ 
			" IDT_CODIGO_NACIONAL, "+ 
			" IDT_ROAMING_ORIGIN, "+ 
			" QTD_REGISTRO, "+ 
			" VLR_DURACAO, "+ 
			" VLR_TOTAL, "+ 
			" VLR_TOTAL_SI, "+ 
			" VLR_INDICE_BONIFICACAO, "+ 
			" IDT_TIPO_SERVICO, "+ 
			" IDT_PLANO_PRECO, "+ 
			" ID_CHAVE1, "+ 
			" ID_CHAVE2, "+ 
			" ID_CHAVE3, " +
			" IDT_EOT_ORIGEM, " +
			" IDT_EOT_DESTINO "+
			" ) "+
			"SELECT "+ 
			 "sysdate 		as DAT_PROCESSAMENTO, "+  	
			 "? 			as IDT_PERIODO_CONTABIL, "+   // P0 - Período contabil
			 "'SP' 			as IDT_TIPO_SALDO, "+   
		     "cdr_srv.idt_codigo_servico_sfa 	as IDT_COD_SERVICO_SFA, "+  	
		     "cdr.IDT_CODIGO_NACIONAL 		as IDT_CODIGO_NACIONAL, "+  	
		     "cdr.idt_roaming_origin 			as IDT_ROAMING_ORIGIN, "+  	
			 "sum(cdr.qtd_registros) as QTD_REGISTROS, "+  
			 "sum(cdr.vlr_duracao) as VLR_DURACAO, "+  
			 "sum(cdr.vlr_total) as VLR_TOTAL, "+  	
			 "sum(cdr.vlr_total_si) as VLR_TOTAL_SI, "+  	
			 "0 as VLR_INDICE_BONIFICACAO, "+ 
		     "CDR_SRV.idt_tipo_servico AS TIPO_SERVICO, "+   
			 "plano.idt_plano_preco as IDT_PLANO_PRECO, "+  
		     "cdr_srv.num_csp as ID_CHAVE1, "+  
			 "cdr_srv.idt_modulacao as ID_CHAVE2, "+  
		     "cdr_srv.tip_chamada as ID_CHAVE3, " +
		     "cdr.idt_eot_origem as IDT_EOT_ORIGEM, " +
		     "cdr.idt_eot_destino as IDT_EOT_DESTINO "+  
	  "FROM "+  	
		     "tbl_con_cdr_servico	cdr_srv, "+  	
			 "tbl_rel_cdr_dia	cdr, "+ 	
            "tbl_ger_plano_preco plano "+ 	
      "where "+  
			 "plano.idt_categoria = ? and "+		// P1 - Categoria de LigMix = 2
             "cdr.num_csp = cdr_srv.num_csp and "+ 	
		     "cdr.idt_modulacao = cdr_srv.idt_modulacao and "+ 	
		     "cdr.tip_chamada = cdr_srv.tip_chamada and "+ 	
		     "cdr_srv.idt_tipo_servico = ? and "+ 	// P2 - Tipo de serviço consumo ligmix = CL		 
			 "cdr.dat_cdr between  add_months(to_date('27'||?,'DD/MM/YYYY'),-1)+2 "+	// P3 - Período contábil  
			 "and to_date(?,'DD/MM/YYYY')	and "+	// P4 (data final de período - dataReferencia)
			 "trim(cdr.idt_plano) = plano.idt_plano_preco and "+  
	         "cdr_srv.idt_plano_preco = plano.idt_plano_preco "+  
	  "group by "+ 	
		     "cdr_srv.idt_codigo_servico_sfa, "+  	
		     "cdr.IDT_CODIGO_NACIONAL, "+  	
		     "cdr.idt_roaming_origin, "+  	
		     "CDR_SRV.idt_tipo_servico, "+   
			 "plano.idt_plano_preco, "+  
		     "cdr_srv.num_csp, "+  
			 "cdr_srv.idt_modulacao, "+  
		     "cdr_srv.tip_chamada, " +
		     "cdr.idt_eot_origem, " +
		     "cdr.idt_eot_destino"; 

			Object[] pars = {	super.periodoContabil,	// P0 
								new Integer(Definicoes.COD_CAT_LIGMIX),	// Parametro 1
								Definicoes.TIPO_SERVICO_CONSUMO_LIGMIX, // Parametro 2 
								super.periodoContabil, // P3
								super.dataReferencia,	// P4 
							};

			DBConexao.executaPreparedQuery(query, pars, super.getIdLog());
		}*/
	
	/***
	 * Metodo...: incluiPerdaCDRs
	 * Descricao: Determina  o valor referente à perda de CDRs
	 * @param PREPConexao	conexaoPrep		Conexão Banco de Dados
	 * @param String		periodoContabil	Período Contábil (mm/yyyy)
	 * @throws GPPInternalErrorException
	 */
	void incluiPerdaCDRs(ConexaoBancoDados conexaoPrep) throws GPPInternalErrorException
	{
		String sqlPerdaCDRs = "SELECT IDT_CODIGO_NACIONAL, IDT_PRE_HIBRIDO, IDT_TIPO_SALDO, VLR_PERDAS_CDR, VLR_INDICE_BONIFICACAO_GSM "+
								"FROM TBL_CON_INDICE_BONIFICACAO "+
								"WHERE IDT_PERIODO_CONTABIL = ? AND VLR_PERDAS_CDR <> 0";
		
		Object[] sqlPars = {super.periodoContabil};
		
		ResultSet rsPerdaCdrs = conexaoPrep.executaPreparedQuery(sqlPerdaCDRs, sqlPars, super.getIdLog());
		
		try
		{
			while(rsPerdaCdrs.next())
			{
				String idtCodigoNacional = rsPerdaCdrs.getString("idt_codigo_nacional");
				String idtPreHibrido = rsPerdaCdrs.getString("idt_pre_hibrido");
				String idtTipoSaldo = rsPerdaCdrs.getString("idt_tipo_saldo");
				double vlrPerdaCdrs = rsPerdaCdrs.getDouble("vlr_perdas_cdr");
				double indiceBonifGSM = rsPerdaCdrs.getDouble("vlr_indice_bonificacao_gsm");
				
				// Se a perda de CDRs for negativa <=> está faltando cdr's de consumo
				if(vlrPerdaCdrs < 0)
				{
					// Insere parcela de CBG referente a perda de cdr's de chamada na tbl_con_contabil
					this.inserePerdaTblContabil(idtTipoSaldo, -vlrPerdaCdrs*indiceBonifGSM, idtCodigoNacional, idtPreHibrido, "CBG", indiceBonifGSM, conexaoPrep); 
					
					// Insere parcela de CRG referente a perda de cdr's de chamada na tbl_con_contabil
					this.inserePerdaTblContabil(idtTipoSaldo, -vlrPerdaCdrs*(1-indiceBonifGSM), idtCodigoNacional, idtPreHibrido, "CRG", indiceBonifGSM, conexaoPrep); 
				}
				// Se a perda de CDRs for positiva <=> está faltando cdr's de recarga/bonus
				else
				{
					if(vlrPerdaCdrs > 0)
					{
						// Insere perda de recargas/bonus na tbl_con_contabil
						this.inserePerdaTblContabil(idtTipoSaldo, vlrPerdaCdrs, idtCodigoNacional, idtPreHibrido, "BO", 0, conexaoPrep);
					}
				}
			}			
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.ERRO,"incluiPerdaCDRs","Erro SQL: "+sqlE);
			throw new GPPInternalErrorException("Erro sql: "+sqlE);
		}
	}
	
	/**
	 * Metodo...: inserePerdaTblContabil
	 * Descricao: Insere registros de perda de cdrs na tbl_con_contabil
	 * @param String		tipoSaldo			SP, SB, SS, SD - depende do saldo que se está contabilizando
	 * @param String		periodoContabil		Período contabil (mm/yyyy)
	 * @param double		perdaCDR			Valor referente à perda de CDRs (diferença na contabilidade)
	 * @param String		cn					Código Nacional
	 * @param String		indPH				Indicador de plano de preços (H/P)
	 * @param String		tipoServicoSfa		Tipo de Serviço do sfa (BO, CRG, CBG)
	 * @param PREPConexao	conexaoPrep			Conexão com Banco de Dados
	 * @throws GPPInternalErrorException
	 */ 
	private void inserePerdaTblContabil(String tipoSaldo, double perdaCDR, String cn, String indPH, String tipoServicoSfa, double indiceBonifGSM, ConexaoBancoDados conexaoPrep) throws GPPInternalErrorException
	 {
	 	double tax = 0;
	 	
	 	// Determinar valor com imposto (bruto)
	 	String sqlTax = "SELECT VLR_ALIQUOTA FROM TBL_GER_ALIQUOTA "+
						"WHERE IDT_CODIGO_NACIONAL = ? "+	//Parametro 0 (cn)
						"AND idt_imposto = ? "+	// Parametro 1 (imposto - ICMS)
						"AND dat_inicial_validade <=  to_date(?,'DD/MM/YYYY') "+	//Parametro 2 -  Data final Periodo
						"AND (dat_final_validade >= add_months(to_date('27'||substr(?,4,7),'DD/MM/YYYY'),-1)+2  or dat_final_validade is null) ";	//Parametro 3 - Data Inicial Período
	 	
	 	Object[] parTax = {cn, Definicoes.IMPOSTO_TLDC, super.dataReferencia, super.dataReferencia };
	 	
	 	ResultSet rsTax = conexaoPrep.executaPreparedQuery1(sqlTax, parTax, super.getIdLog());
	 	
	 	try
		{
		 	if (rsTax.next())
		 	{
		 		tax = rsTax.getDouble("VLR_ALIQUOTA");
		 	}	 			 		
		}
	 	catch(SQLException sqlE)
		{
	 		super.log(Definicoes.ERRO,"inserePerdaTblContabil","Erro SQL: "+sqlE);
	 		throw new GPPInternalErrorException("Erro SQL: "+sqlE);
		}

	 	String sqlInsert = "INSERT INTO TBL_CON_CONTABIL " +
	 			"(IDT_CODIGO_NACIONAL, VLR_TOTAL, VLR_DURACAO, VLR_TOTAL_SI, IDT_ROAMING_ORIGIN, " +
	 			"IDT_CODIGO_SERVICO_SFA, VLR_INDICE_BONIFICACAO, QTD_REGISTRO, DAT_PROCESSAMENTO, IDT_PERIODO_CONTABIL," +
	 			"IDT_TIPO_SERVICO, IDT_PLANO_PRECO, IDT_TIPO_SALDO, ID_CHAVE1, ID_CHAVE2, ID_CHAVE3, IDT_EOT_ORIGEM, IDT_EOT_DESTINO) "+
				"VALUES  "+
	 			"(?, ?, 0, ?, ?, ?, ?, 0, sysdate, ?, ?, ?, ?, '-', '-', '-',?,?)";
	 	Object[] pars = {	cn,
	 						new Double(perdaCDR),
							new Double(perdaCDR*(1-tax)),
							cn,
							this.getCodigoSfaPerdaCdrs(indPH, tipoServicoSfa),
							new Double(indiceBonifGSM),
							super.periodoContabil,
							tipoServicoSfa,
							indPH.equals("H")?"4":"1",
							tipoSaldo,
							cn,
							cn
	 					};
	 	
	 	conexaoPrep.executaPreparedUpdate(sqlInsert, pars, super.getIdLog());
	 }
	 
	/***
	 * Metodo...: eliminaZeros
	 * Descricao: Elimina Registros com valores zerados (deixando apenas o do saldo principal,
	 * 				(para a informação de "existência" do evento gratuito não se perder)
	 * @param ConexaoBancoDados		conexaoPrep		Conexão com Banco de dados
	 * @param String				periodoContabil	Período Contábil
	 * @throws GPPInternalErrorException
	 */
	 private void eliminaZeros(ConexaoBancoDados conexaoPrep) throws GPPInternalErrorException
	 {
	 	String sqlZeros = "delete tbl_con_contabil "+
						"where idt_periodo_contabil = ? "+
						"AND vlr_total = 0 "+
						"and idt_tipo_saldo in ('SB', 'SS', 'SD')"; 
		
	 	Object[] parsZeros = { super.periodoContabil };
	
		conexaoPrep.executaPreparedUpdate(sqlZeros, parsZeros, super.getIdLog());
		
	 	String sqlZeros1 	= "DELETE tbl_con_contabil c                                             "
						 	+ " WHERE c.idt_periodo_contabil = ?                                     "
						 	+ "   AND c.vlr_total = 0                                                "
						 	+ "   AND c.idt_tipo_saldo = 'SP'                                        "
						 	+ "   AND c.vlr_duracao = 0                                              "
						 	+ "   AND EXISTS (                                                       "
						 	+ "          SELECT 1                                                    "
						 	+ "            FROM tbl_con_contabil ci                                  "
						 	+ "           WHERE ci.idt_periodo_contabil = ?                          "
						 	+ "             AND ci.idt_tipo_saldo IN ('SB', 'SS', 'SD')              "
						 	+ "             AND ci.qtd_registro = c.qtd_registro                     "
						 	+ "             AND ci.idt_codigo_servico_sfa = c.idt_codigo_servico_sfa "
						 	+ "             AND ci.id_chave1 = c.id_chave1                           "
						 	+ "             AND ci.id_chave2 = c.id_chave2                           "
						 	+ "             AND ci.id_chave3 = c.id_chave3                           "
						 	+ "             AND ci.idt_codigo_nacional = c.idt_codigo_nacional       "
						 	+ "             AND ci.idt_roaming_origin = c.idt_roaming_origin         "
						 	+ "             AND ci.idt_plano_preco = c.idt_plano_preco               "
						 	+ "             AND ci.idt_eot_origem = c.idt_eot_origem                 "
						 	+ "             AND ci.idt_eot_destino = c.idt_eot_destino               "
						 	+ "             AND ci.idt_tipo_servico = c.idt_tipo_servico             "
						 	+ "             AND ci.vlr_total <> 0)                                   "; 

	 	Object[] parsZeros1 = { periodoContabil, periodoContabil };

	 	conexaoPrep.executaPreparedUpdate(sqlZeros1, parsZeros1, super.getIdLog());
	 }
	 
	 /***
	  * Metodo...: getCodigoSfaPerdaCdrs
	  * Descricao: Retorna o código de serviço associado ao tipo de serviço/plano preço
	  * 			para a perda de Cdrs
	  * @param String	indPH			P: pré-pago normal; H: Híbrido
	  * @param String	tipoServicoSfa	Tipo de Serviço (BO/CRG/CBG)
	  * @return		String		Código de Serviço
	  */
	 private String getCodigoSfaPerdaCdrs(String indPH, String tipoServicoSfa)
	 {
	 	String retorno = null;
	 	
	 	// Se tipo de serviço for Consumo de Bônus GSM
	 	if(tipoServicoSfa.equals("CBG"))
	 	{
	 		if(indPH.equals("P"))
	 		{
	 			retorno = "71688";
	 		}
	 		else
	 		{
	 			retorno = "82276";
	 		}
	 	}
	 	
	 	// Se tipo de serviço for consumo de Recarga GSM
	 	if(tipoServicoSfa.equals("CRG"))
	 	{
	 		if(indPH.equals("P"))
	 		{
	 			retorno = "71687";
	 		}
	 		else
	 		{
	 			retorno = "82275";
	 		}
	 	}
	 	
	 	// Se tipo de Serviço for Bonus
	 	if(tipoServicoSfa.equals("BO"))
	 	{
	 		if(indPH.equals("P"))
	 		{
	 			retorno = "71686";
	 		}
	 		else
	 		{
	 			retorno = "82274";
	 		}
	 	}
	 	
	 	return retorno;
	 }
}
