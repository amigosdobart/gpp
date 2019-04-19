
		SELECT 
			a.idt_msisdn,
			(((c.min_credito - c.min_ff)/60) * d.vlr_bonus_minuto) + ((c.min_ff/60) * d.vlr_bonus_minuto_ff) regra_ff
		
		FROM
		TBL_GER_PROMOCAO_ASSINANTE A,
		TBL_GER_PROMOCAO B,
		TBL_GER_TOTALIZACAO_PULA_PULA C,
		TBL_GER_BONUS_PULA_PULA D
		WHERE
		A.idt_msisdn = C.idt_msisdn AND
		A.idt_promocao = B.idt_promocao AND
		C.dat_mes = '0505' and
		d.id_codigo_nacional = substr(a.idt_msisdn, 3, 2) and
		b.idt_promocao in (1) and
		(((c.min_credito - c.min_ff)/60) * d.vlr_bonus_minuto) + ((c.min_ff/60) * d.vlr_bonus_minuto_ff) > 1000
		order by 2 desc
		

