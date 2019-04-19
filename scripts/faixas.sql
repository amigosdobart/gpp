SELECT DDD,
		faixa , 
	   SUM (qtd_pula_pula) qtd_pula_pula, 
	   SUM (valor_pula_pula) valor_pula_pula,
       SUM (qtd_pula_pula_verao) qtd_pula_pula_verao, 
	   SUM (valor_pula_pula_verao) valor_pula_pula_verao,
       SUM (qtd_pula_pula_verao2) qtd_pula_pula_verao2, 
	   SUM (valor_pula_pula_verao2) VALOR_pula_pula_verao2,
       SUM (qtd_pula_pula_2008) qtd_pula_pula_2008, 
	   SUM (valor_pula_pula_2008) valor_pula_pula_2008,
       SUM (qtd_novo_pula_pula_2008) qtd_novo_pula_pula_2008, 
	   SUM (valor_novo_pula_pula_2008) valor_novo_pula_pula_2008
  FROM (SELECT faixa, 
  			   SUBSTR(IDT_MSISDN, 3, 2) DDD, 
			   DECODE (idt_promocao, 1, 1, 0) qtd_pula_pula,
               DECODE (idt_promocao, 1, valor, 0) valor_pula_pula,
               DECODE (idt_promocao, 2, 1, 0) qtd_pula_pula_verao,
               DECODE (idt_promocao, 2, valor, 0) valor_pula_pula_verao,
               DECODE (idt_promocao, 4, 1, 0) qtd_pula_pula_verao2,
               DECODE (idt_promocao, 4, valor, 0) valor_pula_pula_verao2,
               DECODE (idt_promocao, 5, 1, 0) qtd_pula_pula_2008,
               DECODE (idt_promocao, 5, valor, 0) valor_pula_pula_2008,
               DECODE (idt_promocao, 6, 1, 0) qtd_novo_pula_pula_2008,
               DECODE (idt_promocao, 6, valor, 0) valor_novo_pula_pula_2008
          FROM (SELECT /*+ index(b XPKTBL_REC_RECARGAS) */  idt_promocao, nom_promocao, faixa, idt_msisdn,
                         SUM (vlr_credito_bonus) valor
                    FROM (SELECT c.idt_promocao, c.nom_promocao, a.idt_msisdn,
                                 CASE 
                                 WHEN vlr_credito_bonus < 100
                                 THEN
									 CASE
	                                    WHEN  vlr_credito_bonus  > nvl(c.vlr_max_credito_bonus, 1000)
	                                       THEN trunc((c.vlr_max_credito_bonus)/10)
	                                    ELSE TRUNC (vlr_credito_bonus / 10)
	                                 END
	                             ELSE
	                             	CASE
	                                    WHEN vlr_credito_bonus  > nvl(c.vlr_max_credito_bonus, 1000)
	                                       THEN trunc((nvl(c.vlr_max_credito_bonus, 1000))/50)+8
	                                    ELSE TRUNC (vlr_credito_bonus / 50)+8
	                                 END
                                 END
								 
								 faixa,
                                 vlr_credito_bonus
                            FROM tbl_ger_promocao_assinante a,
                                 tbl_rec_recargas b,
                                 tbl_ger_promocao c
                           WHERE a.idt_promocao IN (1, 2, 4, 5, 6)
                             AND a.idt_msisdn = b.idt_msisdn
                             AND ( (b.tip_transacao IN ('08001', '05024')
                             AND b.dat_recarga BETWEEN '01-dec-2005'
                                                   AND '31-dec-2005') or
                                  (b.tip_transacao = '08101'
                             AND b.dat_recarga BETWEEN '15-nov-2005'
                                                   AND '30-nov-2005'))                 
                             AND vlr_credito_bonus > 0
                             AND c.idt_promocao = a.idt_promocao)
                GROUP BY idt_promocao, nom_promocao, faixa, idt_msisdn)
)
group by DDD, faixa

