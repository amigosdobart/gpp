
select nome_promocao, data_execucao, count(*),  sum(regra_ff)
	from (
		SELECT 
			b.nom_promocao nome_promocao,
			e.num_dia_execucao_recarga||'/10/2005' data_execucao, 
			case
				when (((c.min_credito - c.min_ff)/60 * d.vlr_bonus_minuto) + ((c.min_ff/60) * d.vlr_bonus_minuto_ff) > B.vlr_max_credito_bonus)  and 
				(a.ind_isento_limite is null  or a.ind_isento_limite = 0) then
					B.vlr_max_credito_bonus
				else
					(((c.min_credito - c.min_ff)/60) * d.vlr_bonus_minuto) + ((c.min_ff/60) * d.vlr_bonus_minuto_ff) 
			end regra_ff
		
		FROM
		TBL_GER_PROMOCAO_ASSINANTE A,
		TBL_GER_PROMOCAO B,
		TBL_GER_TOTALIZACAO_PULA_PULA C,
		TBL_GER_BONUS_PULA_PULA D,
		tbl_ger_promocao_dia_execucao e
		WHERE
		A.idt_msisdn = C.idt_msisdn AND
		A.idt_promocao = B.idt_promocao AND
		C.dat_mes = '0509' and
		d.id_codigo_nacional = substr(a.idt_msisdn, 3, 2) and
		b.idt_promocao in (1, 2, 4, 5, 6) and
		e.idt_promocao = a.idt_promocao and
		e.num_dia_entrada = to_number(to_char(a.dat_entrada_promocao, 'dd'))
	)
	GROUP BY 
	nome_promocao, data_execucao
