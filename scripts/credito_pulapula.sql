/* Formatted on 2006/07/12 16:03 (Formatter Plus v4.8.0) */
SELECT 
/*+ index (b XPKTBL_APR_ASSINANTE) */
a.idt_msisdn, b.idt_status, b.vlr_saldo_bonus, b.vlr_saldo_principal,
       (SELECT SUM (vlr_credito_bonus)
          FROM tbl_rec_recargas r
         WHERE r.idt_msisdn = a.idt_msisdn
           AND (   (    r.tip_transacao IN ('08001', '05024')
                    AND r.dat_recarga BETWEEN '01-mar-2005' AND '31-mar-2005'
                   )
                OR (    r.tip_transacao = '08101'
                    AND r.dat_recarga BETWEEN '15-fev-2005' AND '28-fev-2005'
                   )
               )
           AND r.vlr_credito_bonus > 0) rec_marco,
       (SELECT SUM (vlr_credito_bonus)
          FROM tbl_rec_recargas r
         WHERE r.idt_msisdn = a.idt_msisdn
           AND (   (    r.tip_transacao IN ('08001', '05024')
                    AND r.dat_recarga BETWEEN '01-abr-2005' AND '30-abr-2005'
                   )
                OR (    r.tip_transacao = '08101'
                    AND r.dat_recarga BETWEEN '15-mar-2005' AND '31-mar-2005'
                   )
               )
           AND r.vlr_credito_bonus > 0) rec_abr,
       (SELECT SUM (vlr_credito_bonus)
          FROM tbl_rec_recargas r
         WHERE r.idt_msisdn = a.idt_msisdn
           AND (   (    r.tip_transacao IN ('08001', '05024')
                    AND r.dat_recarga BETWEEN '01-mai-2005' AND '31-mai-2005'
                   )
                OR (    r.tip_transacao = '08101'
                    AND r.dat_recarga BETWEEN '15-abr-2005' AND '30-abr-2005'
                   )
               )
           AND r.vlr_credito_bonus > 0) rec_maio,
       (SELECT SUM (vlr_credito_bonus)
          FROM tbl_rec_recargas r
         WHERE r.idt_msisdn = a.idt_msisdn
           AND (   (    r.tip_transacao IN ('08001', '05024')
                    AND r.dat_recarga BETWEEN '01-jun-2005' AND '30-jun-2005'
                   )
                OR (    r.tip_transacao = '08101'
                    AND r.dat_recarga BETWEEN '15-mai-2005' AND '31-mai-2005'
                   )
               )
           AND r.vlr_credito_bonus > 0) rec_jun
           

  FROM tbl_pro_assinante a, tbl_apr_assinante b
 WHERE a.idt_promocao = 1 AND a.idt_msisdn = b.idt_msisdn

