package com.brt.gpp.aplicacoes.recarregar;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.recarregar.ParametrosRecarga;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.entidade.RevendaVarejo;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 *	Classe responsavel pelas operacoes de acesso a banco de dados referentes a recargas e ajustes.
 *
 *	@version		1.0		04/05/2007		Primeira versao.
 *	@author			Daniel Ferreira
 *
 *  Classe alterada para adição do método fezRecarga() que verifica para um determinado msisdn se 
 *  o mesmo realizou alguma recarga desde sua ativação.
 *  @author     Jorge Abreu
 *  @since      28/08/2007 
 */
public abstract class RecargaDAO 
{
	
	/**
	 *	Insert de registros na TBL_REC_RECARGAS.
	 */
	private static final String INS_RECARGA_GSM = 
		"INSERT INTO tbl_rec_recargas(id_recarga, " +
        "                             tip_transacao, " +
        "                             idt_msisdn, " +
        "                             dat_origem, " +
        "                             dat_recarga, " +
        "                             dat_inclusao, " +
        "                             id_valor, " +
        "                             id_tipo_credito, " +
        "                             id_tipo_recarga, " +
        "                             idt_plano_preco, " +
        "                             id_sistema_origem, " +
        "                             nom_operador, " +
        "                             idt_cpf, " +
        "                             num_hash_cc, " +
        "                             dat_contabil, " +
        "                             idt_loja, " +
        "                             idt_terminal, " +
        "                             tip_terminal, " +
        "                             idt_nsu_instituicao, " +
        "                             id_canal, " +
        "                             id_origem, " +
        "                             vlr_pago, " +
        "                             vlr_credito_principal, " +
        "                             vlr_credito_periodico, " +
        "                             vlr_credito_bonus, " +
        "                             vlr_credito_sms, " +
        "                             vlr_credito_gprs, " +
        "                             vlr_saldo_final_principal, " +
        "                             vlr_saldo_final_periodico, " +
        "                             vlr_saldo_final_bonus, " +
        "                             vlr_saldo_final_sms, " +
        "                             vlr_saldo_final_gprs, " +
        "                             num_dias_exp_principal, " +
        "                             num_dias_exp_periodico, " +
        "                             num_dias_exp_bonus, " +
        "                             num_dias_exp_sms, " +
        "                             num_dias_exp_gprs, " +
        "                             des_observacao) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 *	Insert de registros na TBL_REC_RECARGAS_NOK.
	 */
	private static final String INS_RECARGA_NOK_GSM = 
		"INSERT INTO tbl_rec_recargas_nok(id_recarga, " +
        "                                 tip_transacao, " +
        "                                 idt_msisdn, " +
        "                                 dat_origem, " +
        "                                 dat_recarga, " +
        "                                 dat_inclusao, " +
        "                                 id_valor, " +
        "                                 id_tipo_credito, " +
        "                                 id_tipo_recarga, " +
        "                                 idt_plano_preco, " +
        "                                 id_sistema_origem, " +
        "                                 nom_operador, " +
        "                                 idt_cpf, " +
        "                                 num_hash_cc, " +
        "                                 dat_contabil, " +
        "                                 idt_loja, " +
        "                                 idt_terminal, " +
        "                                 tip_terminal, " +
        "                                 idt_nsu_instituicao, " +
        "                                 id_canal, " +
        "                                 id_origem, " +
        "                                 vlr_pago, " +
        "                                 vlr_credito_principal, " +
        "                                 vlr_credito_periodico, " +
        "                                 vlr_credito_bonus, " +
        "                                 vlr_credito_sms, " +
        "                                 vlr_credito_gprs, " +
        "                                 vlr_saldo_final_principal, " +
        "                                 vlr_saldo_final_periodico, " +
        "                                 vlr_saldo_final_bonus, " +
        "                                 vlr_saldo_final_sms, " +
        "                                 vlr_saldo_final_gprs, " +
        "                                 num_dias_exp_principal, " +
        "                                 num_dias_exp_periodico, " +
        "                                 num_dias_exp_bonus, " +
        "                                 num_dias_exp_sms, " +
        "                                 num_dias_exp_gprs, " +
        "                                 idt_erro, " +
        "                                 dat_timestamp, " +
        "                                 num_tentativas, " +
        "                                 des_observacao) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 *	Update de atualizacao dos saldos finais na TBL_REC_RECARGAS.
	 */
	private static final String UPD_ATUALIZAR_SALDO_FINAL = 
		"UPDATE tbl_rec_recargas " +
		"   SET vlr_saldo_final_principal = ?, " +
		"       vlr_saldo_final_periodico = ?, " +
		"       vlr_saldo_final_bonus = ?, " +
		"       vlr_saldo_final_sms = ?, " +
		"       vlr_saldo_final_gprs = ? " +
		" WHERE id_recarga = ? " +
		"   AND tip_transacao = ? " +
		"   AND dat_origem = to_date(?, 'dd/mm/yyyy hh24:mi:ss') ";
	
	/**
	 *	Update de contador de tentativas da TBL_REC_RECARGAS_NOK.
	 */
	private static final String UPD_INCREMENTAR_NOK_GSM = 
		"UPDATE tbl_rec_recargas_nok " +
		"   SET num_tentativas = num_tentativas + 1," +
		"       dat_timestamp = ? " +
		" WHERE id_recarga = ? " +
		"   AND tip_transacao = ? " +
		"   AND dat_origem >= to_date(?, 'dd/mm/yyyy') " +
		"   AND dat_origem <  to_date(?, 'dd/mm/yyyy') + 1 ";
	
	/**
	 *	Insert de registros na TBL_REC_RECARGAS_TFPP.
	 */
	private static final String INS_RECARGA_TFPP = 
		"INSERT INTO tbl_rec_recargas_tfpp(id_recarga, " +
        "                                  tip_transacao, " +
        "                                  idt_msisdn, " +
        "                                  dat_origem, " +
        "                                  dat_recarga, " +
        "                                  dat_inclusao, " +
        "                                  id_valor, " +
        "                                  id_tipo_credito, " +
        "                                  id_tipo_recarga, " +
        "                                  idt_plano_preco, " +
        "                                  id_sistema_origem, " +
        "                                  nom_operador, " +
        "                                  idt_cpf, " +
        "                                  num_hash_cc, " +
        "                                  dat_contabil, " +
        "                                  idt_loja, " +
        "                                  idt_terminal, " +
        "                                  tip_terminal, " +
        "                                  idt_nsu_instituicao, " +
        "                                  id_canal, " +
        "                                  id_origem, " +
        "                                  vlr_pago, " +
        "                                  vlr_credito_principal, " +
        "                                  vlr_credito_bonus, " +
        "                                  vlr_credito_sms, " +
        "                                  vlr_credito_gprs, " +
        "                                  vlr_saldo_final_principal, " +
        "                                  vlr_saldo_final_bonus, " +
        "                                  vlr_saldo_final_sms, " +
        "                                  vlr_saldo_final_gprs, " +
        "                                  num_dias_exp_principal, " +
        "                                  num_dias_exp_bonus, " +
        "                                  num_dias_exp_sms, " +
        "                                  num_dias_exp_gprs, " +
        "                                  des_observacao) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 *	Insert de registros na TBL_REC_RECARGAS_NOK_TFPP.
	 */
	private static final String INS_RECARGA_NOK_TFPP = 
		"INSERT INTO tbl_rec_recargas_tfpp_nok(id_recarga, " +
        "                                      tip_transacao, " +
        "                                      idt_msisdn, " +
        "                                      dat_origem, " +
        "                                      dat_recarga, " +
        "                                      dat_inclusao, " +
        "                                      id_valor, " +
        "                                      id_tipo_credito, " +
        "                                      id_tipo_recarga, " +
        "                                      idt_plano_preco, " +
        "                                      id_sistema_origem, " +
        "                                      nom_operador, " +
        "                                      idt_cpf, " +
        "                                      num_hash_cc, " +
        "                                      dat_contabil, " +
        "                                      idt_loja, " +
        "                                      idt_terminal, " +
        "                                      tip_terminal, " +
        "                                      idt_nsu_instituicao, " +
        "                                      id_canal, " +
        "                                      id_origem, " +
        "                                      vlr_pago, " +
        "                                      vlr_credito_principal, " +
        "                                      vlr_credito_bonus, " +
        "                                      vlr_credito_sms, " +
        "                                      vlr_credito_gprs, " +
        "                                      vlr_saldo_final_principal, " +
        "                                      vlr_saldo_final_bonus, " +
        "                                      vlr_saldo_final_sms, " +
        "                                      vlr_saldo_final_gprs, " +
        "                                      num_dias_exp_principal, " +
        "                                      num_dias_exp_bonus, " +
        "                                      num_dias_exp_sms, " +
        "                                      num_dias_exp_gprs, " +
        "                                      idt_erro, " +
        "                                      dat_timestamp, " +
        "                                      num_tentativas, " +
        "                                      des_observacao) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 *	Update de contador de tentativas da TBL_REC_RECARGAS_NOK_TFPP.
	 */
	private static final String UPD_INCREMENTAR_NOK_TFPP = 
		"UPDATE tbl_rec_recargas_tfpp_nok " +
		"   SET num_tentativas = num_tentativas + 1," +
		"       dat_timestamp = ? " +
		" WHERE id_recarga = ? " +
		"   AND tip_transacao = ? " +
		"   AND dat_origem >= to_date(?, 'dd/mm/yyyy') " +
		"   AND dat_origem <  to_date(?, 'dd/mm/yyyy') + 1 ";
	
	/**
	 *	Update de valor de credito utilizado por agentes de revenda varejo.
	 */
	private static final String UPD_CREDITO_VAREJO =
		"UPDATE tbl_rec_limite_credito " +
		"   SET vlr_utilizado = vlr_utilizado + ? " +
		" WHERE cod_revenda_servcel = ? " +
		"   AND vlr_utilizado + ? <= vlr_limite_credito ";
	
	/**
	 *	Select de identificadores de recarga.
	 */
	private static final String QRY_ID_RECARGA = 
		"SELECT seq_recarga_id.NEXTVAL AS id_recarga " +
		"  FROM dual ";

	/**
	 *	Select de verificacao de nova recarga GSM.
	 */
	private static final String QRY_NOVA_RECARGA_GSM = 
		"SELECT 1 " +
		"  FROM tbl_rec_recargas " +
		" WHERE id_recarga = ? " +
		"   AND tip_transacao = ? " +
		"   AND dat_origem >= to_date(?, 'dd/mm/yyyy') " +
		"   AND dat_origem <= to_date(?, 'dd/mm/yyyy hh24:mi:ss')";
	
	/**
	 *	Select de verificacao de nova recarga TFPP.
	 */
	private static final String QRY_NOVA_RECARGA_TFPP = 
		"SELECT 1 " +
		"  FROM tbl_rec_recargas_tfpp " +
		" WHERE id_recarga = ? " +
		"   AND tip_transacao = ? " +
		"   AND dat_origem >= to_date(?, 'dd/mm/yyyy') " +
		"   AND dat_origem <= to_date(?, 'dd/mm/yyyy hh24:mi:ss')";
	
	/**
	 *	Select de obtencao de informacoes de revenda de varejo.
	 */
	private static final String QRY_REVENDA_VAREJO = 
		"SELECT vlr_limite_credito, " +
		"       dat_atualizacao_mcr, " +
		"       ind_bloqueio, " +
		"       vlr_utilizado " +
		"  FROM tbl_rec_limite_credito " +
		" WHERE cod_revenda_servcel = ?";
	
	private static final String QRY_FEZ_RECARGA = 
		" SELECT tptr.idt_msisdn                  AS msisdn,                             "+
		"        tptr.qtd_recargas                AS qtd                                 "+
        "   FROM tbl_pro_totalizacao_recargas     tptr                                   "+                
        "  WHERE tptr.idt_msisdn = ?                                                     "+
        "    AND tptr.dat_mes >= ?                                                       "+
        "    AND tptr.dat_mes <= ?                                                       ";
	
	private static final String QRY_FEZ_RECARGA_SE_MUDOU_MSISDN =
		"SELECT tae.idt_antigo_campo AS MSISDN,                                          "+
	    "       tae.dat_aprovisionamento AS DATA                                         "+ 
	    "  FROM tbl_apr_eventos tae                                                      "+
	    " WHERE tae.tip_operacao = 'TROCA_MSISDN'                                        "+
	    "   AND tae.idt_msisdn = ?                                                       "+
	    "   AND tae.idt_msisdn <> idt_antigo_campo                                       "+
	    "   AND tae.dat_aprovisionamento =  (SELECT max(dat_aprovisionamento)            "+
	    "                                      FROM tbl_apr_eventos ev                   "+
	    "                                     WHERE ev.idt_msisdn = tae.idt_msisdn       "+
	    "                                       AND ev.dat_aprovisionamento >= ?         "+
	    "                                       AND ev.dat_aprovisionamento <= ?         "+
	    "                                       AND ev.tip_operacao = 'TROCA_MSISDN'     "+
	    "                                       AND ev.idt_msisdn <> ev.idt_antigo_campo)  ";
	
	private static final SimpleDateFormat formataDataContabil = new SimpleDateFormat(Definicoes.MASCARA_MES_DIA);
	
	/**
	 *	Insere o registro de recarga na TBL_REC_RECARGAS.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	public static void inserirRecargaGSM(ParametrosRecarga recarga, PREPConexao conexaoPrep) throws Exception
	{
		Object[] parametros = 
		{
			(recarga.getIdentificacaoRecarga() != null) ? recarga.getIdentificacaoRecarga() : RecargaDAO.newIdRecarga(conexaoPrep),
			recarga.getTipoTransacao(),
			recarga.getMSISDN(),
			new Timestamp(recarga.getDatOrigem().getTime()),
			new Timestamp(recarga.getDatRecarga().getTime()),
			new Timestamp(Calendar.getInstance().getTimeInMillis()),
			new Double(recarga.getIdValor()),
			recarga.getTipoCredito(),
			recarga.getIdOperacao(),
			new Short(recarga.getAssinante().getPlanoPreco()),
			recarga.getSistemaOrigem(),
			recarga.getOperador(),
			recarga.getCpf(),
			recarga.getHash_cc(),
			(recarga.getDataContabil() != null) ? formataDataContabil.format(recarga.getDataContabil()) : null,
			recarga.getCodLoja(),
			recarga.getIdTerminal(),
			recarga.getTipoTerminal(),
			recarga.getNsuInstituicao(),
			recarga.getTipoTransacao().substring(0, 2),
			recarga.getTipoTransacao().substring(2),
			new Double(recarga.getValores().getValorEfetivoPago()),
			new Double(recarga.getValores().getSaldoPrincipal()),
			new Double(recarga.getValores().getSaldoPeriodico()),
			new Double(recarga.getValores().getSaldoBonus()),
			new Double(recarga.getValores().getSaldoSMS()),
			new Double(recarga.getValores().getSaldoGPRS()),
			new Double(recarga.getAssinante().getCreditosPrincipal()),
			new Double(recarga.getAssinante().getCreditosPeriodico()),
			new Double(recarga.getAssinante().getCreditosBonus()),
			new Double(recarga.getAssinante().getCreditosSms()),
			new Double(recarga.getAssinante().getCreditosDados()),
			new Short(recarga.getValores().getNumDiasExpPrincipal()),
			new Short(recarga.getValores().getNumDiasExpPeriodico()),
			new Short(recarga.getValores().getNumDiasExpBonus()),
			new Short(recarga.getValores().getNumDiasExpSMS()),
			new Short(recarga.getValores().getNumDiasExpGPRS()),
			recarga.getDescricao()
		};
		
		conexaoPrep.executaPreparedUpdate(RecargaDAO.INS_RECARGA_GSM, parametros, conexaoPrep.getIdProcesso());
	}
	
	/**
	 *	Insere o registro de recarga com erro na TBL_REC_RECARGAS_NOK.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		codigoRetorno			Codigo de retorno da operacao de recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	public static void inserirRecargaNokGSM(ParametrosRecarga recarga, short codigoRetorno, PREPConexao conexaoPrep) throws Exception
	{
		Object[] parametros = 
		{
			(recarga.getIdentificacaoRecarga() != null) ? recarga.getIdentificacaoRecarga() : RecargaDAO.newIdRecarga(conexaoPrep),
			recarga.getTipoTransacao(),
			recarga.getMSISDN(),
			new Timestamp(recarga.getDatOrigem().getTime()),
			new Timestamp(recarga.getDatRecarga().getTime()),
			new Timestamp(Calendar.getInstance().getTimeInMillis()),
			new Double(recarga.getIdValor()),
			recarga.getTipoCredito(),
			recarga.getIdOperacao(),
			(recarga.getAssinante() != null) ? new Short(recarga.getAssinante().getPlanoPreco()) : null,
			recarga.getSistemaOrigem(),
			recarga.getOperador(),
			recarga.getCpf(),
			recarga.getHash_cc(),
			(recarga.getDataContabil() != null) ? formataDataContabil.format(recarga.getDataContabil()) : null,
			recarga.getCodLoja(),
			recarga.getIdTerminal(),
			recarga.getTipoTerminal(),
			recarga.getNsuInstituicao(),
			recarga.getTipoTransacao().substring(0, 2),
			recarga.getTipoTransacao().substring(2),
			(recarga.getValores() != null) ? new Double(recarga.getValores().getValorEfetivoPago()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoPrincipal  ()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoPeriodico  ()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoBonus      ()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoSMS        ()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoGPRS       ()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosPrincipal()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosPeriodico()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosBonus    ()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosSms      ()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosDados    ()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpPrincipal()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpPeriodico()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpBonus    ()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpSMS      ()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpGPRS     ()) : null,
			new Short(codigoRetorno),
			new Timestamp(recarga.getDatOrigem().getTime()),
			new Integer(1),
			recarga.getObservacao()
		};
		
		conexaoPrep.executaPreparedUpdate(RecargaDAO.INS_RECARGA_NOK_GSM, parametros, conexaoPrep.getIdProcesso());
	}
	
	/**
	 *	Insere o registro de recarga na TBL_REC_RECARGAS.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	public static void inserirRecargaTFPP(ParametrosRecarga recarga, PREPConexao conexaoPrep) throws Exception
	{
		Object[] parametros = 
		{
			(recarga.getIdentificacaoRecarga() != null) ? recarga.getIdentificacaoRecarga() : RecargaDAO.newIdRecarga(conexaoPrep),
			recarga.getTipoTransacao(),
			recarga.getMSISDN(),
			new Timestamp(recarga.getDatOrigem().getTime()),
			new Timestamp(recarga.getDatRecarga().getTime()),
			new Timestamp(Calendar.getInstance().getTimeInMillis()),
			new Double(recarga.getIdValor()),
			recarga.getTipoCredito(),
			recarga.getIdOperacao(),
			new Short(recarga.getAssinante().getPlanoPreco()),
			recarga.getSistemaOrigem(),
			recarga.getOperador(),
			recarga.getCpf(),
			recarga.getHash_cc(),
			(recarga.getDataContabil() != null) ? formataDataContabil.format(recarga.getDataContabil()) : null,
			recarga.getCodLoja(),
			recarga.getIdTerminal(),
			recarga.getTipoTerminal(),
			recarga.getNsuInstituicao(),
			((recarga.getTipoTransacao() != null) && (recarga.getTipoTransacao().length() >= 2)) ? recarga.getTipoTransacao().substring(0, 2) : null,
			((recarga.getTipoTransacao() != null) && (recarga.getTipoTransacao().length() >= 2)) ? recarga.getTipoTransacao().substring(2   ) : null,
			new Double(recarga.getValores().getValorEfetivoPago()),
			new Double(recarga.getValores().getSaldoPrincipal()),
			new Double(recarga.getValores().getSaldoBonus()),
			new Double(recarga.getValores().getSaldoSMS()),
			new Double(recarga.getValores().getSaldoGPRS()),
			new Double(recarga.getAssinante().getCreditosPrincipal()),
			new Double(recarga.getAssinante().getCreditosBonus()),
			new Double(recarga.getAssinante().getCreditosSms()),
			new Double(recarga.getAssinante().getCreditosDados()),
			new Short(recarga.getValores().getNumDiasExpPrincipal()),
			new Short(recarga.getValores().getNumDiasExpBonus()),
			new Short(recarga.getValores().getNumDiasExpSMS()),
			new Short(recarga.getValores().getNumDiasExpGPRS()),
			recarga.getDescricao()
		};
		
		conexaoPrep.executaPreparedUpdate(RecargaDAO.INS_RECARGA_TFPP, parametros, conexaoPrep.getIdProcesso());
	}
	
	/**
	 *	Insere o registro de recarga com erro na TBL_REC_RECARGAS_NOK.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		codigoRetorno			Codigo de retorno da operacao de recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	public static void inserirRecargaNokTFPP(ParametrosRecarga recarga, short codigoRetorno, PREPConexao conexaoPrep) throws Exception
	{
		Object[] parametros = 
		{
			(recarga.getIdentificacaoRecarga() != null) ? recarga.getIdentificacaoRecarga() : RecargaDAO.newIdRecarga(conexaoPrep),
			recarga.getTipoTransacao(),
			recarga.getMSISDN(),
			new Timestamp(recarga.getDatOrigem().getTime()),
			new Timestamp(recarga.getDatRecarga().getTime()),
			new Timestamp(Calendar.getInstance().getTimeInMillis()),
			new Double(recarga.getIdValor()),
			recarga.getTipoCredito(),
			recarga.getIdOperacao(),
			(recarga.getAssinante() != null) ? new Short(recarga.getAssinante().getPlanoPreco()) : null,
			recarga.getSistemaOrigem(),
			recarga.getOperador(),
			recarga.getCpf(),
			recarga.getHash_cc(),
			(recarga.getDataContabil() != null) ? formataDataContabil.format(recarga.getDataContabil()) : null,
			recarga.getCodLoja(),
			recarga.getIdTerminal(),
			recarga.getTipoTerminal(),
			recarga.getNsuInstituicao(),
			((recarga.getTipoTransacao() != null) && (recarga.getTipoTransacao().length() >= 2)) ? recarga.getTipoTransacao().substring(0, 2) : null,
			((recarga.getTipoTransacao() != null) && (recarga.getTipoTransacao().length() >= 2)) ? recarga.getTipoTransacao().substring(2   ) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getValorEfetivoPago()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoPrincipal  ()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoBonus      ()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoSMS        ()) : null,
			(recarga.getValores() != null) ? new Double(recarga.getValores().getSaldoGPRS       ()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosPrincipal()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosBonus    ()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosSms      ()) : null,
			(recarga.getAssinante() != null) ? new Double(recarga.getAssinante().getCreditosDados    ()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpPrincipal()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpBonus    ()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpSMS      ()) : null,
			(recarga.getValores() != null) ? new Short(recarga.getValores().getNumDiasExpGPRS     ()) : null,
			new Short(codigoRetorno),
			new Timestamp(recarga.getDatOrigem().getTime()),
			new Integer(1),
			recarga.getObservacao()
		};
		
		conexaoPrep.executaPreparedUpdate(RecargaDAO.INS_RECARGA_NOK_TFPP, parametros, conexaoPrep.getIdProcesso());
	}
	
	/**
	 *	Atualiza os saldos finais no registro de recarga na TBL_REC_RECARGAS.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 */
	public static boolean atualizarSaldoFinal(ParametrosRecarga recarga, PREPConexao conexaoPrep)
	{
		try
		{
			Object[] parametros = 
			{
				new Double(recarga.getAssinante().getCreditosPrincipal()),
				new Double(recarga.getAssinante().getCreditosPeriodico()),
				new Double(recarga.getAssinante().getCreditosBonus    ()),
				new Double(recarga.getAssinante().getCreditosSms      ()),
				new Double(recarga.getAssinante().getCreditosDados    ()),
				recarga.getIdentificacaoRecarga(),
				recarga.getTipoTransacao(),
				new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(recarga.getDatOrigem())
			};
			
			return (conexaoPrep.executaPreparedUpdate(RecargaDAO.UPD_ATUALIZAR_SALDO_FINAL, parametros, conexaoPrep.getIdProcesso()) > 0);
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 *	Incrementa o numero de tentativas de recarga com erro na TBL_REC_RECARGAS_NOK. Caso a recarga nunca tenha sido 
	 *	realizada com erro, insere novo registro na tabela.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		idProcesso				Identificador do processo.
	 *	@param		codigoRetorno			Codigo de retorno da operacao de recarga.
	 */
	public static boolean incrementarRecargaNok(ParametrosRecarga recarga, short codigoRetorno, long idProcesso)
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			return RecargaDAO.incrementarRecargaNok(recarga, codigoRetorno, conexaoPrep);
		}
		catch(Exception e)
		{
			return false;
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep, idProcesso);
		}
	}
	
	/**
	 *	Incrementa o numero de tentativas de recarga com erro na TBL_REC_RECARGAS_NOK. Caso a recarga nunca tenha sido 
	 *	realizada com erro, insere novo registro na tabela.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		codigoRetorno			Codigo de retorno da operacao de recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 */
	public static boolean incrementarRecargaNok(ParametrosRecarga recarga, short codigoRetorno, PREPConexao conexaoPrep)
	{
		try
		{
			//Verificando se os parametros obrigatorios estao definidos. Isto e necessario devido as restricoes 
			//(constraints) da tabela.
			if(recarga.getMSISDN() == null)
				return false;
			
			//Direcionando a insercao da recarga de acordo com a plataforma do assinante.
			if(recarga.getAssinante().getNaturezaAcesso().equals("TFPP"))
				RecargaDAO.incrementarRecargaNokTFPP(recarga, codigoRetorno, conexaoPrep);
			else
				RecargaDAO.incrementarRecargaNokGSM(recarga, codigoRetorno, conexaoPrep);
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 *	Incrementa o numero de tentativas de recarga com erro na TBL_REC_RECARGAS_NOK. Caso a recarga nunca tenha sido 
	 *	realizada com erro, insere novo registro na tabela.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		codigoRetorno			Codigo de retorno da operacao de recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	private static void incrementarRecargaNokGSM(ParametrosRecarga recarga, short codigoRetorno, PREPConexao conexaoPrep) throws Exception
	{
		Object[] parametros =
		{
			new java.sql.Timestamp(recarga.getDatRecarga().getTime()),
			recarga.getIdentificacaoRecarga(),
			recarga.getTipoTransacao(),
			new SimpleDateFormat(Definicoes.MASCARA_DATE).format(recarga.getDatOrigem()),
			new SimpleDateFormat(Definicoes.MASCARA_DATE).format(recarga.getDatOrigem())
		};
		
		if(conexaoPrep.executaPreparedUpdate(RecargaDAO.UPD_INCREMENTAR_NOK_GSM, parametros, conexaoPrep.getIdProcesso()) <= 0)
			RecargaDAO.inserirRecargaNokGSM(recarga, codigoRetorno, conexaoPrep);
	}
	
	/**
	 *	Incrementa o numero de tentativas de recarga com erro na TBL_REC_RECARGAS_NOK. Caso a recarga nunca tenha sido 
	 *	realizada com erro, insere novo registro na tabela.
	 *
	 *	@param		recarga					Informacoes da recarga.
	 *	@param		codigoRetorno			Codigo de retorno da operacao de recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	private static void incrementarRecargaNokTFPP(ParametrosRecarga recarga, short codigoRetorno, PREPConexao conexaoPrep) throws Exception
	{
		Object[] parametros =
		{
			new java.sql.Timestamp(recarga.getDatRecarga().getTime()),
			recarga.getIdentificacaoRecarga(),
			recarga.getTipoTransacao(),
			new SimpleDateFormat(Definicoes.MASCARA_DATE).format(recarga.getDatOrigem()),
			new SimpleDateFormat(Definicoes.MASCARA_DATE).format(recarga.getDatOrigem())
		};
		
		if(conexaoPrep.executaPreparedUpdate(RecargaDAO.UPD_INCREMENTAR_NOK_TFPP, parametros, conexaoPrep.getIdProcesso()) <= 0)
			RecargaDAO.inserirRecargaNokTFPP(recarga, codigoRetorno, conexaoPrep);
	}
	
	/**
	 *	Retorna o proximo valor da sequence de identificadores de recarga.
	 *
	 *	@param		idProcesso				Identificador do processo.
	 *	@return		Proximo valor da sequence de identificadores de recarga.
	 *	@throws		Exception
	 */
	public static String newIdRecarga(long idProcesso) throws Exception
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			return RecargaDAO.newIdRecarga(conexaoPrep);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep, idProcesso);
		}
	}
	
	/**
	 *	Retorna o proximo valor da sequence de identificadores de recarga.
	 *
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		Proximo valor da sequence de identificadores de recarga.
	 *	@throws		Exception
	 */
	public static String newIdRecarga(PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros = conexaoPrep.executaQuery(RecargaDAO.QRY_ID_RECARGA, conexaoPrep.getIdProcesso());
		try
		{
			if(registros.next())
				return registros.getString("id_recarga");
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
		return null;
	}
	
	/**
	 *	Atualiza o valor de credito utilizado pelo agente de revenda varejo. O metodo tem como premissa a validacao 
	 *	previa do agente, ou seja, que ele existe, nao esta bloqueado e possui saldo.
	 * 
	 *	@param		tipoTransacao			Tipo de transacao da recarga, que corresponde ao identificador do agente
	 *										de revenda varejo. 
	 *	@param		valorPago				Valor pago pelo assinante.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se a atualizacao foi executada com sucesso e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean atualizarCreditoCanalVarejo(String tipTransacao, double valorPago, PREPConexao conexaoPrep) throws Exception
	{
		Object[] parametros =
		{
			new Double(valorPago),
			tipTransacao,
			new Double(valorPago)
		};
		
		return (conexaoPrep.executaPreparedUpdate(RecargaDAO.UPD_CREDITO_VAREJO, parametros, conexaoPrep.getIdProcesso()) > 0);
	}
		
	/**
	 *	Verifica se a recarga GSM ja foi realizada. 
	 * 
	 *	@param		idRecarga				Identificador da recarga.
	 *	@param		tipTransacao			Tipo de transacao da recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se a recarga ja foi executada e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean isNovaRecargaGSM(String idRecarga, String tipTransacao, Date datOrigem, PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros = null;
		
		try
		{
			Object[] parametros = 
			{
				idRecarga,
				tipTransacao,
				new SimpleDateFormat(Definicoes.MASCARA_DATE).format(datOrigem),
				new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(datOrigem)
			};
			
			registros = conexaoPrep.executaPreparedQuery(RecargaDAO.QRY_NOVA_RECARGA_GSM, parametros, conexaoPrep.getIdProcesso());
			
			return !registros.next(); 
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
		
	}
	
	/**
	 *	Verifica se a recarga TFPP ja foi realizada. 
	 * 
	 *	@param		idRecarga				Identificador da recarga.
	 *	@param		tipTransacao			Tipo de transacao da recarga.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se a recarga ja foi executada e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean isNovaRecargaTFPP(String idRecarga, String tipTransacao, Date datOrigem, PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros = null;
		
		try
		{
			Object[] parametros = 
			{
				idRecarga,
				tipTransacao,
				new SimpleDateFormat(Definicoes.MASCARA_DATE).format(datOrigem),
				new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(datOrigem)
			};
			
			registros = conexaoPrep.executaPreparedQuery(RecargaDAO.QRY_NOVA_RECARGA_TFPP, parametros, conexaoPrep.getIdProcesso());
			
			return !registros.next(); 
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
		
	}
	
	/**
	 *	Retorna as informacoes da revenda de varejo. 
	 * 
	 *	@param		codRevenda				Codigo da revenda.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		Informacoes da revenda de varejo.
	 *	@throws		Exception
	 */
	public static RevendaVarejo getRevendaVarejo(String codRevenda, PREPConexao conexaoPrep) throws Exception
	{
		RevendaVarejo	result		= null;
		ResultSet		registros	= null;
		
		try
		{
			Object[] parametros =
			{
				codRevenda
			};
			
			registros = conexaoPrep.executaPreparedQuery(RecargaDAO.QRY_REVENDA_VAREJO, parametros, conexaoPrep.getIdProcesso());
			
			if(registros.next())
			{
				result = new RevendaVarejo();
				
				result.setCodRevenda    (codRevenda);
				result.setIndBloqueio   ((registros.getShort("ind_bloqueio") != 0));
				result.setDatAtualizacao(registros.getDate  ("dat_atualizacao_mcr"));
				result.setVlrUtilizado  (registros.getDouble("vlr_utilizado"));
				result.setVlrLimite     (registros.getDouble("vlr_limite_credito"));
			}
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
		
		return result;
	}
	
	/**
	 *	Retorna se um assinante fez recarga ou não desde o aprovisionamento. 
	 * 
	 *	@param		msisdn  				Número do Assinante em tipo String.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		TRUE caso o assinante tenha feito recarga, ou FALSE caso contrário.
	 *	@throws		Exception
	 */	
	
	public static boolean fezRecarga(String msisdn, Date dataPromocao, PREPConexao conexaoPrep) throws Exception
	{
		return fezRecargaPeriodo(msisdn, dataPromocao, Calendar.getInstance().getTime() ,conexaoPrep);
	}
	
	public static boolean fezRecargaPeriodo(String msisdn, Date dataLimiteInferior, Date dataLimite ,PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros	= null;
		boolean fezRecarga = false;
		try
		{   
			//Verifica se o assinante realizou alguma recarga após a data de ativação
			Object[] parametros = 
			{
					msisdn,
					new SimpleDateFormat(Definicoes.MASCARA_DAT_MES).format(dataLimiteInferior),
					new SimpleDateFormat(Definicoes.MASCARA_DAT_MES).format(dataLimite)
			};
			
			registros = conexaoPrep.executaPreparedQuery(RecargaDAO.QRY_FEZ_RECARGA, parametros, conexaoPrep.getIdProcesso());
			
			while(registros.next())
			{
				if(registros.getInt("qtd") > 0) 
					fezRecarga = true;
			}
			
			registros.close();
			
			if (!fezRecarga)
			{
				//Verifica se o assinante realizou troca de MSISDN e chama novamente a verificação de recarga
				parametros = new Object[]
				{
						msisdn,
						new SimpleDateFormat(Definicoes.MASCARA_DATE).format(dataLimiteInferior),
						new SimpleDateFormat(Definicoes.MASCARA_DATE).format(dataLimite)
				};
				registros = conexaoPrep.executaPreparedQuery(RecargaDAO.QRY_FEZ_RECARGA_SE_MUDOU_MSISDN, parametros, conexaoPrep.getIdProcesso());
			
				if(registros.next())
					fezRecarga = fezRecargaPeriodo(registros.getString("MSISDN"), dataLimiteInferior, registros.getDate("DATA"),conexaoPrep);
			}
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
		return fezRecarga;
	}
}
