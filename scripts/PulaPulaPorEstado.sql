/* Formatted on 2005/10/07 10:07 (Formatter Plus v4.8.0) */
SELECT  a.idt_promocao, C.nom_promocao, SUBSTR (a.idt_msisdn, 3, 2), COUNT (1),
         SUM (vlr_credito_bonus)
    FROM tbl_ger_promocao_assinante a, tbl_rec_recargas b,
    tbl_ger_promocao c
   WHERE 
     A.idt_promocao = C.idt_promocao
     AND a.idt_promocao IN (1, 2, 4, 5, 6)
     AND a.idt_msisdn = b.idt_msisdn
     AND b.tip_transacao IN ('08001', '05024')
     AND b.dat_recarga BETWEEN '01-AGO-2005' AND '31-AGO-2005'
     AND vlr_credito_bonus > 0
GROUP BY a.idt_promocao, C.NOM_promocao, SUBSTR (a.idt_msisdn, 3, 2)

