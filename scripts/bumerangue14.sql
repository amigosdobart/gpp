/* Formatted on 2005/02/25 18:28 (Formatter Plus v4.8.0) */
SELECT *
  FROM tbl_ger_cdr a, tbl_ger_tip_transacao_tecnomen t
 WHERE a.sub_id = '554884012712'
   AND a.TIMESTAMP BETWEEN '28-dez-2004' AND '27-jan-2005'
   AND a.num_csp = '14'
   AND t.transaction_type = a.transaction_type
   AND t.idt_sentido <> 'R'

