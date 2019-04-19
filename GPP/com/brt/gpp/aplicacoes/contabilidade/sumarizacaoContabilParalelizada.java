//Definicao do Pacote
package com.brt.gpp.aplicacoes.contabilidade;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

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
public class sumarizacaoContabilParalelizada extends Contabilidade 
{
	
	//Mensagem a ser logada na TBL_GER_HISTORICO_PROC_BATCH
	String msgBatch = "";
	
	/**
	 * Metodo...: sumarizacaoContabil
	 * Descricao: Construtor
	 * @param long		aIdProcesso		ID do processo
	 * @param String	aDataReferencia	Data de Referencia
	 */
	public sumarizacaoContabilParalelizada(long aIdProcesso, String aDataReferencia) throws GPPInternalErrorException
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_SUMARIZACAO_CONTABIL, aDataReferencia);
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
		short retorno = Definicoes.RET_OPERACAO_OK;
		ConexaoBancoDados DBConexao = null;

		super.log(Definicoes.INFO,"sumarizarContabilidade","Inicio DATA "+super.dataReferencia);

		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = super.gerenteBanco.getConexaoPREP(super.getIdLog());

			//Registra Início do Processamento
			super.dataInicialProcesso = GPPData.dataCompletaForamtada();
			
			//Realiza Sumarização Contabil
			String sqlCN = "select idt_codigo_nacional from tbl_ger_codigo_nacional where ind_regiao_Brt = 1";
			ResultSet rsCN = DBConexao.executaQuery(sqlCN, super.getIdLog());
			
			// Paraleliza Sumarização das CNs
			ThreadGroup thg = new ThreadGroup("SumarizacaoChamadas");
			
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia();
			int numThreads  = Integer.parseInt(map.getMapValorConfiguracaoGPP("NUM_THREADS_SUMARIZACAO"));
			int tempoEspera = Integer.parseInt(map.getMapValorConfiguracaoGPP("TEMPO_ESPERA_SUMARIZACAO"));

			while(rsCN.next())
			{
				while (thg.activeCount() >= numThreads)
					Thread.sleep(tempoEspera*1000);
				
				// Inicia a thread de processamento
				Thread th = new Thread(thg, new ProcessadorSumarizacaoChamadas(super.getIdLog(), super.dataReferencia, rsCN.getString("idt_codigo_nacional")));
				th.start();
			}
			
			// Espera ateh que todas as threads tenham sido terminadas
			while (thg.activeCount() > 0)
				Thread.sleep(tempoEspera*1000);
			
			// Popula os campos vlr_total e vlr_total_si a partir dos saldos parciais
			String queryUpdate = "update tbl_rel_cdr_dia "+
			"set vlr_total = vlr_total_principal+vlr_total_periodico+vlr_total_bonus+vlr_total_sms+vlr_total_dados, "+
			"    vlr_total_si = vlr_total_principal_si+vlr_total_periodico_si+vlr_total_bonus_si+vlr_total_sms_si+vlr_total_dados_si "+
			"where dat_cdr = to_date(?,'dd/mm/yyyy')";
			
			Object[] paramsUpdate = {super.dataReferencia};
			DBConexao.executaPreparedUpdate(queryUpdate, paramsUpdate, super.getIdLog());
	
			// Realiza as demais sumarizações
			sumarizaRecargas(DBConexao);
			sumarizaSaldoAssinantes(DBConexao);
			sumarizaVoucher(DBConexao);
			sumarizaRecargasFace(DBConexao);

			// Registra Data Final do Processo e loga na TBL_GER_HISTORICO_PROC_BATCH
			super.dataFinalProcesso = GPPData.dataCompletaForamtada();
			super.gravaHistoricoProcessos(Definicoes.IND_REL_CONTABIL,super.dataInicialProcesso,super.dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,"Sumarizacoes ok:"+this.msgBatch,super.dataReferencia);
		}
		catch (Exception e)
		{
			retorno = Definicoes.RET_ERRO_GENERICO_GPP;
			//Pega data/hora final do processo batch
			super.dataFinalProcesso = GPPData.dataCompletaForamtada();
			
			//Logar Processo Batch
			super.gravaHistoricoProcessos(Definicoes.IND_REL_CONTABIL,super.dataInicialProcesso,super.dataFinalProcesso,Definicoes.PROCESSO_ERRO,e.getMessage(),super.dataReferencia);
			super.log(Definicoes.ERRO,"sumarizarContabilidade","ERRO GPP:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			super.gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
			super.log(Definicoes.DEBUG,"sumarizarContabilidade","Fim");
		}
		return retorno;	
	}
	
	/**
	 * Metodo...: sumarizaRecargas
	 * Descricao: Sumariza os dados Contábeis de Recargas 
	 * @param ConexaoBancoDados	DBConexao	Conexão com Banco de dados
	 * @return short 						- Se for sucesso retorna um (1), caso contrario, menos um (-1)
	 * @throws GPPInternalErrorException
	 */	
	public void sumarizaRecargas(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"sumarizaContabilidade-Recargas","Inicio");

		try
		{
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de rel Recargas/DIA
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_RECARGAS");
			String query = "DELETE TBL_REL_RECARGAS WHERE DAT_RECARGA = to_date(?, 'dd/mm/yyyy')";
			// Deleta TBL_REL_RECARGAS
			DBConexao.executaPreparedQuery(query,new Object[]{super.dataReferencia},super.getIdLog());

			// Preenche tabela de recargas por dia
			super.log(Definicoes.INFO,"sumarizaContabil","Populando dados tabela de recargas por DIA");

			query = "insert into tbl_rel_recargas "+
				"(dat_recarga, id_origem, id_tipo_recarga, cod_retorno, qtd_registros, "+
				" idt_codigo_nacional, id_sistema_origem, id_canal, id_tipo_credito, idt_plano, "+
				" vlr_total_principal, vlr_total_principal_si, vlr_total_periodico, vlr_total_periodico_si, " +
				" vlr_total_bonus, vlr_total_bonus_si, "+
				" vlr_total_sms, vlr_total_sms_si, vlr_total_dados, vlr_total_dados_si) "+
				"select  trunc(rec.DAT_ORIGEM) as DAT_PROCESSAMENTO, "+  
				        "rec.ID_ORIGEM as ID_ORIGEM, "+
				        "rec.ID_TIPO_RECARGA AS ID_TIPO_RECARGA, "+ 
				        "0 as cod_retorno, "+
				        "count(1) as QTD_REGISTROS, "+
						"substr(rec.IDT_MSISDN,3,2) as IDT_CODIGO_NACIONAL, "+
				        "rec.ID_SISTEMA_ORIGEM as ID_SISTEMA_ORIGEM, "+
				        "rec.ID_CANAL as ID_CANAL, "+
				        "'00' as ID_TIPO_CREDITO, "+
				        "nvl(rec.idt_plano_preco,0) as IDT_PLANO, "+  
						"sum(nvl(rec.vlr_credito_principal,0)) as vlr_total_principal, "+
				  		"sum(nvl(rec.vlr_credito_principal,0)*(1-ALIQUOTA.VLR_ALIQUOTA)) as vlr_total_principal_si, "+
						"sum(nvl(rec.vlr_credito_periodico,0)) as vlr_total_periodico, "+
				  		"sum(nvl(rec.vlr_credito_periodico,0)*(1-ALIQUOTA.VLR_ALIQUOTA)) as vlr_total_periodico_si, "+
						"sum(nvl(rec.vlr_credito_bonus,0)) as vlr_total_bonus, "+  
				  		"sum(nvl(rec.vlr_credito_bonus,0)*(1-ALIQUOTA.VLR_ALIQUOTA)) as vlr_total_bonus_si, "+  
						"sum(nvl(rec.vlr_credito_sms,0)) as vlr_total_sms, "+
				        "sum(nvl(rec.vlr_credito_sms,0)*(1-ALIQUOTA.VLR_ALIQUOTA)) as vlr_total_sms_si, "+
				        "sum(nvl(rec.vlr_credito_gprs,0)) as vlr_total_dados, "+
				        "sum(nvl(rec.vlr_credito_gprs,0)*(1-ALIQUOTA.VLR_ALIQUOTA)) as vlr_total_dados_si "+
				"from TBL_REC_RECARGAS rec, TBL_GER_ALIQUOTA ALIQUOTA "+
				"where   rec.DAT_ORIGEM between to_date('"+super.dataReferencia+"'||' 00:00:00','DD/MM/YYYY HH24:MI:SS') "+ 
				        "and to_date('"+super.dataReferencia+"'||' 23:59:59','DD/MM/YYYY HH24:MI:SS') "+
				        "and ALIQUOTA.dat_inicial_validade <= to_date('"+super.dataReferencia+"', 'dd/mm/yyyy') "+ 
				        "and (ALIQUOTA.dat_final_validade  >= to_date('"+super.dataReferencia+"', 'dd/mm/yyyy') or ALIQUOTA.dat_final_validade is null) "+
				        "and ALIQUOTA.IDT_IMPOSTO = 'ICMS' "+
				        "and substr(rec.IDT_MSISDN,3,2) = ALIQUOTA.idt_codigo_nacional "+ 
				"group by "+  
				        "trunc(rec.DAT_ORIGEM), "+  
						"substr(rec.IDT_MSISDN,3,2), "+
				        "nvl(rec.idt_plano_preco,0), "+  
						"rec.ID_TIPO_RECARGA, "+  
				    	"rec.ID_CANAL, "+  
						"rec.ID_ORIGEM, "+  
						"rec.ID_SISTEMA_ORIGEM  ";

			DBConexao.executaQuery(query,super.getIdLog());
			
			// Popula dos campos de total na tbl_rel_recargas
			query = "update tbl_rel_recargas "+
				"set vlr_total = vlr_total_principal+vlr_total_periodico+vlr_total_bonus+vlr_total_sms+vlr_total_dados, "+
				"    vlr_total_si = vlr_total_principal_si+vlr_total_periodico_si+vlr_total_bonus_si+vlr_total_sms_si+vlr_total_dados_si "+
				"where dat_recarga = to_date('"+super.dataReferencia+"','dd/mm/yyyy') ";
			DBConexao.executaQuery(query,super.getIdLog());
			
			DBConexao.commit();
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
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
	public void sumarizaSaldoAssinantes(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"sumarizaContabilidade-SaldoAssinantes","Inicio");

		try
		{
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de rel Saldos/DIA
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_SALDOS");
			String query = "DELETE TBL_REL_SALDOS WHERE DAT_SALDO_ASSINANTE = to_date(?, 'DD/MM/YYYY')";
			// Deleta TBL_REL_saldos
			DBConexao.executaPreparedQuery(query,new Object[]{super.dataReferencia},super.getIdLog());

			// Preenche tabela de saldos por dia
			query = 
				"INSERT INTO tbl_rel_saldos(dat_saldo_assinante, " +
				"							idt_codigo_nacional, " +
				"							idt_plano," +
				"							idt_status_assinante," +
				"							qtd_registros," +
				"							vlr_acumulado_saldo, " +
				"							vlr_total_principal, " +
				"							vlr_total_periodico, " +
				"							vlr_total_bonus, " +
				"							vlr_total_sms, " +
				"							vlr_total_dados) " + 
				"SELECT to_date(?,'dd/mm/yyyy'), " +
				"       cn, " +
				"       plano, " +
				"       status, " +
				"       count(1), " +
				"       (sum(saldo_principal) + " +
				"        sum(saldo_periodico) + " +
				"        sum(saldo_bonus) + " +
				"        sum(saldo_torpedos) + " +
				"        sum(saldo_dados))/" + Definicoes.TECNOMEN_MULTIPLICADOR + ", " +
				"        sum(saldo_principal), " +
				"       sum(saldo_periodico), " +
				"       sum(saldo_bonus), " +
				"       sum(saldo_torpedos), " +
				"       sum(saldo_dados) " +
				"  FROM (SELECT substr(a.sub_id,3,2) AS cn, " +
				"               a.account_status AS status, " +
				"               a.profile_id AS plano, " +
				"               nvl(a.account_balance,0) AS saldo_principal, " +
				"               nvl(a.periodic_balance,0) AS saldo_periodico, " +
				"               nvl(a.bonus_balance,0) AS saldo_bonus, " +
				"               nvl((SELECT balance_amount_0 " +
				"                      FROM tbl_apr_saldo_tecnomen s " +
				"                     WHERE s.dat_importacao = a.dat_importacao " +
				"                       AND s.user_id = a.id " +
				"                       AND s.account_type = " + TipoSaldo.TORPEDOS + "),0) AS saldo_torpedos, " +
				"               nvl((SELECT balance_amount_0 " +
				"                      FROM tbl_apr_saldo_tecnomen s " +
				"                     WHERE s.dat_importacao = a.dat_importacao " +
				"                       AND s.user_id = a.id " +
				"                       AND s.account_type = " + TipoSaldo.DADOS + "),0) AS saldo_dados " +
				"          FROM tbl_apr_assinante_tecnomen a " +
				"         WHERE a.dat_importacao = to_date(?,'dd/mm/yyyy')) " +
				" GROUP BY cn, " +
				"          status, " +
				"          plano ";
				
			// Popula TBL_REL_SALDOS
			super.log(Definicoes.INFO,"sumarizaContabil","Populando TBL_REL_SALDOS");
			DBConexao.executaPreparedQuery(query, 
										   new Object[]{super.dataReferencia, super.dataReferencia}, 
										   super.getIdLog());
			
			DBConexao.commit();
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
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
	public void sumarizaVoucher(ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"sumarizaContabilidade-Voucher","Inicio");

		try
		{
			DBConexao.setAutoCommit(false);
			
			// Limpa tabela de rel Vouchers/DIA
			super.log(Definicoes.INFO,"sumarizaContabil","Limpando TBL_REL_VOUCHERS");
			String query = "DELETE TBL_REL_VOUCHERS WHERE DAT_VOUCHER = to_date(?, 'dd/mm/yyyy')";
			// Deleta TBL_REL_VOUCHERS	
			DBConexao.executaPreparedQuery(query,new Object[]{super.dataReferencia},super.getIdLog());

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
				
			// Popula TBL_REL_VOUCHERS
			super.log(Definicoes.INFO,"sumarizaContabil","Populando dados tabela de vouchers por DIA");
			DBConexao.executaPreparedQuery(query,new Object[]{super.dataReferencia,super.dataReferencia},super.getIdLog());
			
			DBConexao.commit();
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"sumarizaContabilidade","Erro GPP:"+e);
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
			DBConexao.executaPreparedQuery(query,new Object[]{super.dataReferencia},super.getIdLog());

				// Preenche tabela de recargasface por dia
				// Vai mudar para nova tabela.
				query = "INSERT INTO TBL_REL_RECARGASFACE "+
				"(DAT_RECARGASFACE, IDT_CODIGO_NACIONAL, VLR_FACE, ID_CANAL, ID_ORIGEM, ID_SISTEMA_ORIGEM, IDT_PLANO_PRECO, QTD_REGISTROS) "+
				" select trunc(rec.dat_origem),"+
				"        substr(rec.idt_msisdn,3,2) as CN,"+
				"        rec.vlr_pago as VLR_FACE,"+
				"        rec.id_canal as id_canal,"+
				"        rec.id_origem as id_origem,"+
				"        rec.id_sistema_origem as id_sistema_origem,"+
				"        rec.idt_plano_preco as idt_plano_preco,"+
				"        count(*) as QTD"+
				"   from tbl_rec_valor vlr,"+
				"        tbl_rec_recargas rec,"+
				"        tbl_ger_plano_preco pln"+
				"  where pln.idt_plano_preco = rec.idt_plano_preco and"+
				"        rec.id_tipo_recarga = 'R' and"+
				"        vlr.ind_valor_face = 1 and"+
				"        vlr.id_valor = rec.vlr_pago and"+
				"		 vlr.idt_tipo_saldo = 0 and"+
				"        vlr.idt_categoria = pln.idt_categoria and"+
				"        vlr.dat_ini_vigencia <= rec.dat_origem and"+
				"        (vlr.dat_fim_vigencia >= rec.dat_origem or vlr.dat_fim_vigencia is null) and"+
				"        rec.dat_origem between to_date(?||' 00:00:00','DD/MM/YYYY HH24:MI:SS') and"+
				"        to_date(?||' 23:59:59','DD/MM/YYYY HH24:MI:SS')"+
				" group by"+
				"        trunc(rec.dat_origem),"+
				"        substr(rec.idt_msisdn,3,2),"+
				"        rec.vlr_pago,"+
				"        rec.id_canal,"+
				"        rec.id_origem,"+
				"        rec.id_sistema_origem,"+
				"        rec.idt_plano_preco";
				
			super.log(Definicoes.INFO,"sumarizaContabil","Populando TBL_REL_RECARGAS_FACE");
			DBConexao.executaPreparedQuery(query,new Object[]{super.dataReferencia,super.dataReferencia},super.getIdLog());
			
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
