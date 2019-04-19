/* Formatted on 2005/04/05 19:24 (Formatter Plus v4.8.0) */
SELECT idt_promocao,
       sum((SELECT SUM (call_duration / 60)
          FROM tbl_ger_cdr partition(pc200412) a
         WHERE a.sub_id = ass.idt_msisdn
           and timestamp between '04-dez-2004' and '15-dez-2004' 
           AND a.tip_chamada IN
                             ('RLOCAL------', 'RVC2MMI-----', 'RVC3MMI-----')
           AND EXISTS (
                  SELECT 1
                    FROM tbl_apr_assinante
                   WHERE idt_msisdn = '55' || a.call_id
                     AND INSTR (des_family_friends,'0' || SUBSTR (a.sub_id, 3, 20)) > 0)) )ff,
       sum((SELECT SUM (call_duration / 60)
          FROM tbl_ger_cdr partition(pc200412) a
         WHERE a.sub_id = ass.idt_msisdn
           and timestamp between '04-dez-2004' and '15-dez-2004' 
           AND a.tip_chamada IN
                             ('RLOCAL------', 'RVC2MMI-----', 'RVC3MMI-----')
           AND NOT EXISTS (
                  SELECT 1
                    FROM tbl_apr_assinante
                   WHERE idt_msisdn = '55' || a.call_id
                     AND INSTR (des_family_friends,'0' || SUBSTR (a.sub_id, 3, 20)) > 0)) ) nff
  FROM tbl_ger_promocao_assinante ass

 group by idt_promocao

