 SELECT  /*+ index(a xpktbl_ger_cdr) */ 
 SUB_ID, 
 call_id,
 to_char(timestamp, 'dd/mm/yyyy hh24:mi:ss'),
 PROC_HORA_TECNOMEN(call_duration),
 (call_duration / 60) * b.VLR_BONUS_MINUTO VALOR
 FROM tbl_ger_cdr  
 partition(pc200505 ) 
 a,  
 TBL_GER_BONUS_PULA_PULA B
 where
  
 a.sub_id = '555184121001'  AND
-- a.timestamp  between to_date('01/02/2005', 'dd/mm/yyyy') 
--                  and to_date('28/02/2005 23:59:59',  
--                              'dd/mm/yyyy hh24:mi:ss') and 
 a.transaction_type in (select transaction_type from  
                        tbl_ger_promocao_transaction   
                        where idt_promocao =  1 ) and 
 a.tip_chamada in (select rate_name from   
                   tbl_ger_promocao_rate_name   
                   where idt_promocao =  1 ) and 
 a.num_csp in ('00', '14')  AND   
 SUBSTR(A.SUB_ID, 3,2) = B.ID_CODIGO_NACIONAL (+)  
 
 

