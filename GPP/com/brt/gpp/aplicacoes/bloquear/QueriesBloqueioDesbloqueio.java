package com.brt.gpp.aplicacoes.bloquear;

/**
 * Este arquivo refere-se à classe QueriesBloqueioDesbloqueio, 
 * utilizada para armazenar as queries utilizadas pela classe BloquearServico
 *
 * <P> Versao:			1.0
 *
 * @Autor: 			Denys Oliveira
 * Data: 				02/12/2004
 *
 */
public class QueriesBloqueioDesbloqueio 
{
	String dataInicial;
	String dataFinal;
	double valorMinimo;
	double valorMinimoBlackList;
	
	/**
	 * Metodo...: QueriesBloqueioDesbloqueio
	 * Descricao: Construtor
	 * @param 	String		anoMesParticao		String justapondo o ano e o mes da partição
	 * 											que deve ser usada na busca de CDRs(ex: 200412)
	 */
	public QueriesBloqueioDesbloqueio(String _dataInicial, String _dataFinal, double _valorMinimo, double _valorMinimoBlackList)
	{
		this.dataInicial = _dataInicial;
		this.dataFinal = _dataFinal;
		this.valorMinimo = _valorMinimo;
		this.valorMinimoBlackList = _valorMinimoBlackList;
	}
	
	public String getQuery()
	{
		return 	this.getSqlBloqueioSMS_FC() +" union all "+ 
				this.getSqlBloqueioRE() +" union all "+ 
				this.getSqlDesblIDCh() +" union all "+ 
				this.getSqlDesblSMS_FC();
	}

	/**
	 * Metodo...: getSqlBloqueioSMS_FC
	 * Descricao: Query que busca os usuários que terão seu SMS e Free Call Bloqueados por saldo baixo
	 * @return	String	
	 */
	public String getSqlBloqueioSMS_FC()
	{
		return 		
		"select msisdn, id_servico, 'Bloquear ELM_FREE_CALL/ELM_BLACK_LIST' as acao, '01' as motivo " + // Parametros 1,2
		  "from (select tblSaldoBaixo.msisdn, " +
		               "tblSaldoBaixo.valorPrincipal, " +
		               "tblSaldoBaixo.valorBonus, " +
		               "tblSaldoBaixo.valorDados, " +
		               "ser.id_servico " +
		          "from (select distinct tblUltRecChamMsisdnData.msisdn, " +
		                       "max(tblUltimasRecargasChamadas.valorPrincipal) as valorPrincipal, " +
		                       "max(tblUltimasRecargasChamadas.valorBonus)     as valorBonus, " +
		                       "max(tblUltimasRecargasChamadas.valorSm)        as valorSm, " +
		                       "max(tblUltimasRecargasChamadas.valorDados)     as valorDados " +
		                  "from (select tblUltimasRecargasChamadas.msisdn, " +
		                               "max(tblUltimasRecargasChamadas.dataMaxima) as data " +
		                          "from ( (select tblUltimasRecargas.msisdn                  as msisdn, " +
		                                         "tblUltimasRecargas.dat_recarga_maxima      as dataMaxima, " +
		                                         "tbl_rec_recargas.vlr_saldo_final_principal as valorPrincipal, " +
		                                         "tbl_rec_recargas.vlr_saldo_final_bonus     as valorBonus, " +
		                                         "tbl_rec_recargas.vlr_saldo_final_sms       as valorSm, " +
		                                         "tbl_rec_recargas.vlr_saldo_final_gprs      as valorDados " +
		                                    "from tbl_rec_recargas, " +
		                                         "(select idt_msisdn       as msisdn, " +
		                                                 "max(dat_recarga) as dat_recarga_maxima " +
		                                            "from tbl_rec_recargas " +
		                                           "where dat_recarga between to_date('"+this.dataInicial+"','dd/mm/yyyy hh24:mi:ss') and to_date('"+this.dataFinal+"','dd/mm/yyyy hh24:mi:ss') " + // Parametros 3,4
		                                          "group by idt_msisdn " +
		                                          ") tblUltimasRecargas " +
		                                   "where tbl_rec_recargas.idt_msisdn  = tblUltimasRecargas.msisdn " +
		                                     "and tbl_rec_recargas.dat_recarga = tblUltimasRecargas.dat_recarga_maxima " +
		                                  ") " +
		                                 "union all " +
		                                 "(select /*+ INDEX(tbl_ger_cdr XPKTBL_GER_CDR) */ tblUltimasChamadas.sub_id                   as msisdn, " +
		                                          "tblUltimasChamadas.dataChamadaMaxima        as dataMaxima, " +
		                                          "tbl_ger_cdr.final_account_balance/100000    as valorPrincipal, " +
		                                          "tbl_ger_cdr.bonus_balance/100000            as valorBonus, " +
		                                          "tbl_ger_cdr.sm_balance/100000               as valorSm, " +
		                                          "tbl_ger_cdr.data_balance/100000             as valorDados " +
		                                    "from tbl_ger_cdr , " +
		                                         "(select  sub_id, max(timestamp) as dataChamadaMaxima " +
		                                            "from tbl_ger_cdr  " +
		                                           "where timestamp between to_date('"+this.dataInicial+"','dd/mm/yyyy hh24:mi:ss') and to_date('"+this.dataFinal+"','dd/mm/yyyy hh24:mi:ss') " + // Parametros 5,6
		                                          "group by sub_id order by sub_id " +
		                                          ") tblUltimasChamadas " +
		                                   "where tbl_ger_cdr.sub_id    = tblUltimasChamadas.sub_id " +
		                                     "and tbl_ger_cdr.timestamp = tblUltimasChamadas.dataChamadaMaxima " +
		                                  ") " +
		                               ")tblUltimasRecargasChamadas " +
		                        "group by tblUltimasRecargasChamadas.msisdn " +
		                       ")tblUltRecChamMsisdnData, " +
		                      "( (select  tblUltimasRecargas.msisdn                  as msisdn, " +
		                                 "tblUltimasRecargas.dat_recarga_maxima      as dataMaxima, " +
		                                 "tbl_rec_recargas.vlr_saldo_final_principal as valorPrincipal, " +
		                                 "tbl_rec_recargas.vlr_saldo_final_bonus     as valorBonus, " +
		                                 "tbl_rec_recargas.vlr_saldo_final_sms       as valorSm, " +
		                                 "tbl_rec_recargas.vlr_saldo_final_gprs      as valorDados " +
		                           "from tbl_rec_recargas, " +
		                                "(select  idt_msisdn       as msisdn, " +
		                                         "max(dat_recarga) as dat_recarga_maxima " +
		                                   "from tbl_rec_recargas " +
		                                  "where dat_recarga between to_date('"+this.dataInicial+"','dd/mm/yyyy hh24:mi:ss') and to_date('"+this.dataFinal+"','dd/mm/yyyy hh24:mi:ss') " + // Parametros 7,8
		                                 "group by idt_msisdn " +
		                                 ") tblUltimasRecargas " +
		                          "where tbl_rec_recargas.idt_msisdn  = tblUltimasRecargas.msisdn " +
		                            "and tbl_rec_recargas.dat_recarga = tblUltimasRecargas.dat_recarga_maxima " +
		                         ") " +
		                        "union all " +
		                        "(select  /*+ INDEX(tbl_ger_cdr XPKTBL_GER_CDR) */ tblUltimasChamadas.sub_id                   as msisdn, " +
		                                 "tblUltimasChamadas.dataChamadaMaxima        as dataMaxima, " +
		                                 "tbl_ger_cdr.final_account_balance/100000    as valorPrincipal, " +
		                                 "tbl_ger_cdr.bonus_balance/100000            as valorBonus, " +
		                                 "tbl_ger_cdr.sm_balance/100000               as valorSm, " +
		                                 "tbl_ger_cdr.data_balance/100000             as valorDados " +
		                           "from tbl_ger_cdr , " +
		                                "(select  sub_id, " +
		                                         "max(timestamp) as dataChamadaMaxima " +
		                                   "from tbl_ger_cdr  " +
		                                  "where timestamp between to_date('"+this.dataInicial+"','dd/mm/yyyy hh24:mi:ss') and to_date('"+this.dataFinal+"','dd/mm/yyyy hh24:mi:ss') " + // Parametros 9,10
		                                 "group by sub_id order by sub_id " +
		                                 ") tblUltimasChamadas " +
		                          "where tbl_ger_cdr.sub_id    = tblUltimasChamadas.sub_id " +
		                            "and tbl_ger_cdr.timestamp = tblUltimasChamadas.dataChamadaMaxima " +
		                         ") " +
		                       ")tblUltimasRecargasChamadas " +
		                 "where tblUltRecChamMsisdnData.data = tblUltimasRecargasChamadas.dataMaxima " +
		               "group by tblUltRecChamMsisdnData.msisdn " +
		               ") tblSaldoBaixo "+
		               ",tbl_apr_servicos ser " +
		         "where ser.id_servico in ('ELM_FREE_CALL','ELM_BLACK_LIST') " + // Parametros 11,12
//		           "and (tblSaldoBaixo.msisdn not in (select idt_msisdn " +
//		                                               "from tbl_apr_bloqueio_servico " +
//		                                              "where id_servico = 'ELM_FREE_CALL' or id_servico = 'ELM_BLACK_LIST' " + // Parametros 13,14
//		                                             ") " +
//		                "or tblSaldoBaixo.msisdn in  (select distinct idt_msisdn " +
//		                                               "from tbl_apr_bloqueio_servico " +
//		                                              "where (id_servico = 'ELM_FREE_CALL' and id_status = 'DS') " +  // Parametros 15,16
//		                                                 "or (id_servico = 'ELM_BLACK_LIST' and id_status = 'DS') " +  // Parametros 17,18
//		                                             ") " +
//		                ") " +
		       ") " +
		 "where (id_servico = 'ELM_BLACK_LIST' and valorPrincipal+valorDados < "+valorMinimoBlackList+") " +  // Parametros 19,20
		    "or (id_servico = 'ELM_FREE_CALL' and valorPrincipal            < "+valorMinimo+") ";   // Parametros 21,22
	}

	/**
	 * Metodo...: getSqlBloqueioRE 
	 * Descricao: Query que busca os usuários que terão seus serviços bloqueados por entrarem em Recharge Expired
	 * @return	String
	 */
	public String getSqlBloqueioRE()
	{
		return
		"select tblUltimosEventos.idt_msisdn as msisdn, id_servico, ? as tipo, ? as motivo " +  // Parametros 23,24
		  "from ( select idt_msisdn, " +
		                "max(dat_aprovisionamento) as dataUltimoEvento " +
		           "from tbl_apr_eventos " +
		          "where (tip_operacao = ? and idt_antigo_campo = ?) " +  // Parametros 25,26
		             "or (tip_operacao = ? and (idt_antigo_campo in (?,?))) " +  // Parametros 27,28,29
		         "group by idt_msisdn " +
		        ") tblUltimosEventos, " +
		        "tbl_apr_eventos, " +
		        "tbl_apr_servicos " +
		 "where tblUltimosEventos.idt_msisdn       = tbl_apr_eventos.idt_msisdn " +
		   "and tblUltimosEventos.dataUltimoEvento = tbl_apr_eventos.dat_aprovisionamento " +
		   "and tbl_apr_eventos.tip_operacao       = ? " +  // Parametros 30
		   "and id_servico in (?,?,?)";  // Parametros 31,32,33
	}
	
	/**
	 * Metodo...: getSqlDesblIDCh
	 * Descricao: Query que desbloqueia Bina de usuários que saíram de Recharge Expired
	 * @return	String
	 */
	public String getSqlDesblIDCh()
	{
		return
		"select tblUltimosEventos.idt_msisdn as msisdn, tbl_apr_servicos.id_servico, ? as tipo, ? as motivo " + // Parametros 34,35
		  "from ( select idt_msisdn,  " +
		                "max(dat_aprovisionamento) as dataUltimoEvento  " +
		           "from tbl_apr_eventos " +
		          "where (tip_operacao = ? and idt_antigo_campo = ?) " + // Parametros 36,37
		             "or (tip_operacao = ? and (idt_antigo_campo in (?,?))) " + // Parametros 38,39,40
		         "group by idt_msisdn " +
		        ") tblUltimosEventos, " +
		        "tbl_apr_eventos, " +
		        "tbl_apr_bloqueio_servico, " +
		        "tbl_apr_servicos " +
		 "where tblUltimosEventos.idt_msisdn        = tbl_apr_eventos.idt_msisdn " +
		   "and tblUltimosEventos.dataUltimoEvento  = tbl_apr_eventos.dat_aprovisionamento " +
		   "and tbl_apr_bloqueio_servico.idt_msisdn = tblUltimosEventos.idt_msisdn " +
		   "and tbl_apr_eventos.tip_operacao        = ? " +  // Parametros 41
		   "and tbl_apr_bloqueio_servico.id_servico = ? " +  // Parametros 42
		   "and tbl_apr_bloqueio_servico.id_status in (?,?) " +     // Parametros 43,44
		   "and tbl_apr_servicos.id_servico        in (?) "; // Parametros 45
	}

	
	/**
	 * Metodo...: getSqlDesblSMS_FC
	 * Descricao: // Query que desbloqueia o SMS e Free Call dos usuários que fizeram recarga, elevando seu saldo acima do mínimo
	 * @return	String
	 */
	public String getSqlDesblSMS_FC()
	{
		return
    	"select msisdn, id_servico, 'Desbloquear ELM_FREE_CALL/ELM_BLACK_LIST' as acao, '04' as motivo " + // Parametros 46,47
		  "from (select tblSaldoBaixo.msisdn, " +
		               "tblSaldoBaixo.valorPrincipal, " +
		               "tblSaldoBaixo.valorBonus, " +
		               "tblSaldoBaixo.valorDados, " +
		               "ser.id_servico " +
		          "from ( select distinct tblUltRecChamMsisdnData.msisdn, " +
		                                 "max(tblUltimasRecargasChamadas.valorPrincipal) as valorPrincipal, " +
		                                 "max(tblUltimasRecargasChamadas.valorBonus)     as valorBonus, " +
		                                 "max(tblUltimasRecargasChamadas.valorSm)        as valorSm, " +
		                                 "max(tblUltimasRecargasChamadas.valorDados)     as valorDados " +
		                   "from ( select tblUltimasRecargasChamadas.msisdn, " +
		                                 "max(tblUltimasRecargasChamadas.dataMaxima)     as data " +
		                            "from ( (select tblUltimasRecargas.msisdn                  as msisdn, " +
		                                           "tblUltimasRecargas.dat_recarga_maxima      as dataMaxima, " +
		                                           "tbl_rec_recargas.vlr_saldo_final_principal as valorPrincipal, " +
		                                           "tbl_rec_recargas.vlr_saldo_final_bonus     as valorBonus, " +
		                                           "tbl_rec_recargas.vlr_saldo_final_sms       as valorSm, " +
		                                           "tbl_rec_recargas.vlr_saldo_final_gprs      as valorDados " +
		                                      "from tbl_rec_recargas, " +
		                                           "(select idt_msisdn       as msisdn, " +
		                                                   "max(dat_recarga) as dat_recarga_maxima " +
		                                              "from tbl_rec_recargas " +
		                                             "where dat_recarga between to_date('"+this.dataInicial+"','dd/mm/yyyy hh24:mi:ss') and to_date('"+this.dataFinal+"','dd/mm/yyyy hh24:mi:ss') " + // Parametros 48,49
		                                            "group by idt_msisdn " +
		                                           ") tblUltimasRecargas " +
		                                     "where tbl_rec_recargas.idt_msisdn  = tblUltimasRecargas.msisdn " +
		                                       "and tbl_rec_recargas.dat_recarga = tblUltimasRecargas.dat_recarga_maxima " +
		                                    ") " +
		                                    "union all " +
		                                    "(select /*+ INDEX(tbl_ger_cdr XPKTBL_GER_CDR) */ tblUltimasChamadas.sub_id                as msisdn, " +
		                                            "tblUltimasChamadas.dataChamadaMaxima     as dataMaxima, " +
		                                            "tbl_ger_cdr.final_account_balance/100000 as valorPrincipal, " +
		                                            "tbl_ger_cdr.bonus_balance/100000         as valorBonus, " +
		                                            "tbl_ger_cdr.sm_balance/100000            as valorSm, " +
		                                            "tbl_ger_cdr.data_balance/100000          as valorDados " +
		                                       "from tbl_ger_cdr , " +
		                                            "(select sub_id, " +
		                                                    "max(timestamp) as dataChamadaMaxima " +
		                                               "from tbl_ger_cdr  " +
		                                              "where timestamp between to_date('"+this.dataInicial+"','dd/mm/yyyy hh24:mi:ss') and to_date('"+this.dataFinal+"','dd/mm/yyyy hh24:mi:ss') " +  // Parametros 50,51
		                                             "group by sub_id " +
		                                            ") tblUltimasChamadas " +
		                                      "where tbl_ger_cdr.sub_id    = tblUltimasChamadas.sub_id " +
		                                        "and tbl_ger_cdr.timestamp = tblUltimasChamadas.dataChamadaMaxima " +
		                                     ") " +
		                                  ")tblUltimasRecargasChamadas " +
		                          "group by tblUltimasRecargasChamadas.msisdn " +
		                         ")tblUltRecChamMsisdnData, " +
		                        "( ( select tblUltimasRecargas.msisdn                  as msisdn, " +
		                                   "tblUltimasRecargas.dat_recarga_maxima      as dataMaxima, " +
		                                   "tbl_rec_recargas.vlr_saldo_final_principal as valorPrincipal, " +
		                                   "tbl_rec_recargas.vlr_saldo_final_bonus     as valorBonus, " +
		                                   "tbl_rec_recargas.vlr_saldo_final_sms       as valorSm, " +
		                                   "tbl_rec_recargas.vlr_saldo_final_gprs      as valorDados " +
		                              "from tbl_rec_recargas, " +
		                                   "(select idt_msisdn       as msisdn, " +
		                                           "max(dat_recarga) as dat_recarga_maxima " +
		                                      "from tbl_rec_recargas " +
		                                     "where dat_recarga between to_date('"+this.dataInicial+"','dd/mm/yyyy hh24:mi:ss') and to_date('"+this.dataFinal+"','dd/mm/yyyy hh24:mi:ss') " +  // Parametros 52,53
		                                    "group by idt_msisdn " +
		                                    ") tblUltimasRecargas " +
		                             "where tbl_rec_recargas.idt_msisdn  = tblUltimasRecargas.msisdn " +
		                               "and tbl_rec_recargas.dat_recarga = tblUltimasRecargas.dat_recarga_maxima " +
		                           ") " +
		                          "union all " +
		                          "(select /*+ INDEX(tbl_ger_cdr XPKTBL_GER_CDR) */ tblUltimasChamadas.sub_id                as msisdn, " +
		                                  "tblUltimasChamadas.dataChamadaMaxima     as dataMaxima, " +
		                                  "tbl_ger_cdr.final_account_balance/100000    as valorPrincipal, " +
		                                  "tbl_ger_cdr.bonus_balance/100000            as valorBonus, " +
		                                  "tbl_ger_cdr.sm_balance/100000               as valorSm, " +
		                                  "tbl_ger_cdr.data_balance/100000             as valorDados " +
		                             "from tbl_ger_cdr , " +
		                                  "(select sub_id, " +
		                                          "max(timestamp) as dataChamadaMaxima " +
		                                     "from tbl_ger_cdr  " +
		                                    "where timestamp between to_date('"+this.dataInicial+"','dd/mm/yyyy hh24:mi:ss') and to_date('"+this.dataFinal+"','dd/mm/yyyy hh24:mi:ss') " +  // Parametros 54,55
		                                   "group by sub_id order by sub_id " +
		                                   ") tblUltimasChamadas " +
		                            "where tbl_ger_cdr.sub_id    = tblUltimasChamadas.sub_id " +
		                              "and tbl_ger_cdr.timestamp = tblUltimasChamadas.dataChamadaMaxima " +
		                           ") " +
		                          ")tblUltimasRecargasChamadas " +
		                  "where tblUltRecChamMsisdnData.data = tblUltimasRecargasChamadas.dataMaxima " +
		                 "group by tblUltRecChamMsisdnData.msisdn " +
		            ") tblSaldoBaixo " +
		            ",tbl_apr_servicos ser " +
		         "where ser.id_servico in ('ELM_FREE_CALL','ELM_BLACK_LIST') " +  // Parametros 56,57
//		           "and (msisdn not in (select idt_msisdn  " +
//		                                 "from tbl_apr_bloqueio_servico " +
//		                                "where id_servico = 'ELM_IDCHAMADA' " +  // Parametros 58
//		                               ") " +
//		                "or msisdn in (select idt_msisdn " +
//		                                "from tbl_apr_bloqueio_servico " +
//		                               "where id_servico = 'ELM_IDCHAMADA' " +  // Parametros 59
//		                                 "and id_status  = 'DS' " +  // Parametros 60
//		                              ") " +
//		                ") " +
///		           "and msisdn in ( select distinct idt_msisdn  " +
//		                                      "from tbl_apr_bloqueio_servico " +
//		                                     "where (  (id_servico = 'ELM_FREE_CALL' and id_status in ('DS','BC')) " +  // Parametros 61,62,63 
//		                                            "or(id_servico = 'ELM_BLACK_LIST' and id_status in ('DS','BC')) " +  // Parametros 64,65,66
//		                                            ") " +
//		                          ") " +
		       ") " +
		  "where (id_servico = 'ELM_BLACK_LIST' and valorPrincipal+valorDados >= "+valorMinimoBlackList+") " + // Parametros 67,68
		     "or (id_servico = 'ELM_FREE_CALL' and valorPrincipal            >= "+valorMinimo+") ";  // Parametros 69,70
	}

	/**
	 * Metodo...: getQueriesSemIdChamada
	 * Descricao: Retorna query que não faz nada com o IdChamada
	 * @return	String	query com union das primeira e quarta queries
	 */
	public String getQueriesSemIdChamada()
	{
		return 	this.getSqlBloqueioSMS_FC() + " union all " +
				this.getSqlDesblSMS_FC();
	}
}
