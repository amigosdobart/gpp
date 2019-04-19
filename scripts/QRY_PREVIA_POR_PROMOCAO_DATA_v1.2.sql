-- Executa a previa do Pula-Pula e retorna os valores por Promocao.
SELECT NOME_PROMOCAO, DATA_RECARGA, SITUACAO, COUNT(1), SUM(VALOR)
FROM(
	select nome_promocao, data_recarga,
  	case	
  		when account_status in (1, 3,4, 5)	then
			'02-STATUS_INVALIDO'
		when saldo_principal < 0.001 then
			'04-SALDO_PRICIPAL_ZERO'
  		when data_recarga  >= bonus_expiry then 
			'03-PREVISAO_MUDANCA_STATUS'
		when valor  + saldo_bonus >= 10000 then
	 		'06-SALDO_EXCEDIDO'
		when service_status <> 1 then
			'05-BLOQUEADO'
  		else
  			'01-NORMAL'
  	end situacao,
  	valor
  	from(
			select
			  p.idt_promocao || '-' || p.nom_promocao as nome_promocao,
			  to_date(d.num_dia_execucao_recarga || '/' || :mes_referencia || '/2006', 'DD/MM/YYYY') as data_recarga,
			  e.saldo_principal,
			  e.service_status,
			  trunc(
			   case
			     when (p.vlr_max_credito_bonus is not null) and 
			          (((nvl(t.num_segundos_total, 0) - 
			             nvl(t.num_segundos_ff, 0) - 
			             nvl(t.num_segundos_plano_noturno, 0) - 
			             nvl(t.num_segundos_nao_bonificado, 0) - 
			             nvl(t.num_segundos_tup,0) -
			             nvl(t.num_segundos_aigualb,0) -
						nvl(num_segundos_estorno_fraude,0)-
			             nvl(t.num_segundos_expurgo_fraude, 0))*b.vlr_bonus_minuto + 
			            (nvl(t.num_segundos_ff, 0)*b.vlr_bonus_minuto_ff) + 
			            (nvl(t.num_segundos_plano_noturno, 0)*b.vlr_bonus_minuto_noturno))/60 > p.vlr_max_credito_bonus)
			         then p.vlr_max_credito_bonus
			         else ((nvl(t.num_segundos_total, 0) - 
			                nvl(t.num_segundos_ff, 0) - 
			                nvl(t.num_segundos_plano_noturno, 0) - 
			                nvl(t.num_segundos_nao_bonificado, 0) - 
			             	nvl(t.num_segundos_tup,0) -
			             	nvl(t.num_segundos_aigualb,0) -
			             	nvl(num_segundos_estorno_fraude,0)-
			                nvl(t.num_segundos_expurgo_fraude, 0))*b.vlr_bonus_minuto + 
			               (nvl(t.num_segundos_ff, 0)*b.vlr_bonus_minuto_ff) + 
			               (nvl(t.num_segundos_plano_noturno, 0)*b.vlr_bonus_minuto_noturno))/60
			     end
			  , 2) as valor,
			  	e.bonus_expiry,
			  	e.account_status,
			  	e.saldo_bonus
			  			
			from
			  tbl_pro_assinante a,
			  tbl_pro_promocao p,
			  tbl_pro_totalizacao_pula_pula t,
			  tbl_pro_bonus_pula_pula b,
			  tbl_pro_dia_execucao d,
			  (select sub_id, profile_id, account_status, bonus_expiry, bonus_balance/100000 saldo_bonus,
			  		service_status, account_balance/100000 saldo_principal
			   from tbl_apr_assinante_tecnomen 
			   where
					dat_importacao = to_date(:dia_importacao, 'DD/MM/YYYY') ) e
			where
			  p.idt_promocao = a.idt_promocao and
			  p.idt_categoria = 1 and
			  t.idt_msisdn (+) = a.idt_msisdn and
			  t.dat_mes (+) = :dat_mes and
			  b.idt_codigo_nacional = substr(a.idt_msisdn, 3, 2) and
			  b.dat_fim_periodo is null and
			  d.idt_promocao = a.idt_promocao and
			  d.tip_execucao = 'DEFAULT' and
			  d.num_dia_entrada = to_number(to_char(a.dat_entrada_promocao, 'DD')) and
			  p.idt_promocao in (1,2,4, 5, 6, 8) and
			  e.sub_id = a.IDT_MSISDN and
			  b.idt_plano_preco = e.profile_id and
			  b.dat_fim_periodo is null
		)
	)
group by nome_promocao, DATA_RECARGA,  SITUACAO
union all
SELECT NOME_PROMOCAO, DATA_RECARGA, SITUACAO, COUNT(1), SUM(VALOR)
FROM(
	select nome_promocao, data_recarga,
  	case	
  		when account_status in (1, 3,4, 5)	then
			'02-STATUS_INVALIDO'
		when saldo_principal < 0.001 then
			'04-SALDO_PRICIPAL_ZERO'
  		when data_recarga  >= bonus_expiry then 
			'03-PREVISAO_MUDANCA_STATUS'
		when valor  + saldo_bonus >= 10000 then
	 		'06-SALDO_EXCEDIDO'
		when service_status <> 1 then
			'05-BLOQUEADO'
  		else
  			'01-NORMAL'
  	end situacao,
  	valor
  	from(
			select
			  p.idt_promocao || '-' ||p.nom_promocao as nome_promocao,
			  to_date(d.num_dia_execucao_recarga || '/' || :mes_referencia || '/2006', 'DD/MM/YYYY') as data_recarga,
			  e.saldo_principal,
			  e.service_status,
			  trunc(
			   case
			     when (p.vlr_max_credito_bonus is not null) and 
			          (((nvl(t.num_segundos_total, 0) - 
			             nvl(t.num_segundos_ff, 0) - 
			             nvl(t.num_segundos_plano_noturno, 0) - 
			             nvl(t.num_segundos_nao_bonificado, 0) - 
			             nvl(t.num_segundos_tup,0) -
			             nvl(t.num_segundos_aigualb,0) -
			             nvl(num_segundos_estorno_fraude,0)-
			             nvl(t.num_segundos_expurgo_fraude, 0))*b.vlr_bonus_minuto + 
			            (nvl(t.num_segundos_ff, 0)*b.vlr_bonus_minuto_ff) + 
			            (nvl(t.num_segundos_plano_noturno, 0)*b.vlr_bonus_minuto_noturno))/60) > decode(floor(p.vlr_max_credito_bonus +
						decode(floor(t.vlr_recargas/20), 0, 0, vlr_recargas-20))/100, 0, 100,p.vlr_max_credito_bonus +
						decode(floor(t.vlr_recargas/20), 0, 0, vlr_recargas-20) )
			         then decode(floor(p.vlr_max_credito_bonus +
						decode(floor(t.vlr_recargas/20), 0, 0, vlr_recargas-20))/100, 0, 100,p.vlr_max_credito_bonus +
						decode(floor(t.vlr_recargas/20), 0, 0, vlr_recargas-20) )
			         else ((nvl(t.num_segundos_total, 0) - 
			                nvl(t.num_segundos_ff, 0) - 
			                nvl(t.num_segundos_plano_noturno, 0) - 
			                nvl(t.num_segundos_nao_bonificado, 0) - 
			                nvl(t.num_segundos_tup,0) -
			                nvl(t.num_segundos_aigualb,0) -
			                nvl(num_segundos_estorno_fraude,0)-
			                nvl(t.num_segundos_expurgo_fraude, 0))*b.vlr_bonus_minuto + 
			               (nvl(t.num_segundos_ff, 0)*b.vlr_bonus_minuto_ff) + 
			               (nvl(t.num_segundos_plano_noturno, 0)*b.vlr_bonus_minuto_noturno))/60
			     end
			    , 2) as valor,
			  	e.bonus_expiry,
			  	e.account_status,
			  	e.saldo_bonus
			  			
			from
			  tbl_pro_assinante a,
			  tbl_pro_promocao p,
			  tbl_pro_totalizacao_pula_pula t,
			  tbl_pro_bonus_pula_pula b,
			  tbl_pro_dia_execucao d,
			  (select sub_id, account_status, bonus_expiry, bonus_balance/100000 saldo_bonus,
			  		service_status, account_balance/100000 saldo_principal
			   from tbl_apr_assinante_tecnomen 
			   where
					dat_importacao = '31-mar-2006' ) e
			where
			  p.idt_promocao = a.idt_promocao and
			  p.idt_categoria = 1 and
			  t.idt_msisdn (+) = a.idt_msisdn and
			  t.dat_mes (+) = :dat_mes and
			  b.idt_codigo_nacional = substr(a.idt_msisdn, 3, 2) and
			  b.dat_fim_periodo is null and
			  d.idt_promocao = a.idt_promocao and
			  d.tip_execucao = 'DEFAULT' and
			  d.num_dia_entrada = to_number(to_char(a.dat_entrada_promocao, 'DD')) and
			  p.idt_promocao = 7 and
			  e.sub_id = a.IDT_MSISDN
		)
	)
group by nome_promocao, DATA_RECARGA,  SITUACAO

