
select nome_promocao,  count(*),  sum(regra_ff)
	from (
		SELECT 
			b.nom_promocao nome_promocao,
            (((c.min_credito - c.min_ff)/60) * d.vlr_bonus_minuto) + ((c.min_ff/60) * d.vlr_bonus_minuto_ff) 
 regra_ff
		
		FROM
		TBL_GER_PROMOCAO B,
		TBL_GER_TOTALIZACAO_PULA_PULA C,
		TBL_GER_BONUS_PULA_PULA D
		WHERE
		C.dat_mes = '0507' and
		d.id_codigo_nacional = substr(c.idt_msisdn, 3, 2) and
		b.idt_promocao in (1) and
		c.idt_msisdn = '556384144163'
	)
	GROUP BY 
	nome_promocao
	

