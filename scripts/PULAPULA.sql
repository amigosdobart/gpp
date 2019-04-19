 SELECT SUB_ID /*+ index(a xpktbl_ger_cdr) */ ,
 SUM(  (CALL_DURATION / 60) *  B.VLR_BONUS_MINUTO ) as credito  
 FROM tbl_ger_cdr  
 partition(pc200503 ) 
 a,  
 TBL_GER_BONUS_PULA_PULA B
 where
  
 a.sub_id = '556984033149'  AND
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
 group by sub_id
 
 

