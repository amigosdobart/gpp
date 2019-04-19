 SELECT SUB_ID /*+ index(a xpktbl_ger_cdr) */ ,
 (CALL_DURATION / 60) minutos,
 
 FROM tbl_ger_cdr  
 partition(pc200509 ) 
 a
 where

 a.sub_id = '556184221477'  AND
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
 a.call_id like '%6134480300' 
 order by timestamp
 

