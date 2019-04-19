select sum(b.vlr_credito_bonus)

FROM tbl_pro_assinante a,
     tbl_rec_recargas b
WHERE a.idt_promocao IN (1)
 AND a.idt_msisdn = b.idt_msisdn
 AND ( (b.tip_transacao IN ('08001')
 AND b.dat_origem BETWEEN '01-MAR-2007'
                       AND '06-MAR-2007') or
      (b.tip_transacao = '08101'
 AND b.dat_origem BETWEEN '15-FEV-2007'
                       AND '28-FEV-2007'))                 
 AND vlr_credito_bonus > 0

