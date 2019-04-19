select cliente.msisdn, 
 (SELECT /*+ index(a xpktbl_ger_cdr) */ 
 SUM(  (CALL_DURATION / 60) *  B.VLR_BONUS_MINUTO ) as credito  
 FROM tbl_ger_cdr  
 --partition(pc200503) 
 a,  
 TBL_GER_BONUS_PULA_PULA B, TBL_GER_PROMOCAO_ASSINANTE C 
 where   
 a.sub_id = cliente.msisdn and
 c.idt_msisdn = cliente.msisdn and
 C.IDT_MSISDN = A.SUB_ID AND
 a.timestamp  between add_months(to_date(:data_ini, 'dd/mm/yyyy'),  -1)
                  and add_months(to_date(:data_fim || ' 23:59:59',  
                              'dd/mm/yyyy hh24:mi:ss'), -1) and 
 a.transaction_type in (select transaction_type from  
                        tbl_ger_promocao_transaction   
                        where idt_promocao =  1 ) and 
 a.tip_chamada in (select rate_name from   
                   tbl_ger_promocao_rate_name   
                   where idt_promocao =  1 ) and 
 a.num_csp in ('00', '14')  AND   
 SUBSTR(A.SUB_ID, 3,2) = B.ID_CODIGO_NACIONAL (+)  AND
  a.sub_id = cliente.msisdn 
 
 ) credito_pula_pula, 
 
 (
 
select sum(vlr_credito_bonus) from tbl_rec_recargas 
where 
idt_msisdn = cliente.msisdn and
dat_recarga between to_date(:data_ini, 'dd/mm/yyyy') and to_date(:data_fim , 'dd/mm/yyyy') and
tip_transacao = '08001'
) creditado,
(
select sum(vlr_credito_bonus) from tbl_rec_recargas 
where 
idt_msisdn = cliente.msisdn and
dat_recarga between to_date(:data_ini, 'dd/mm/yyyy') and to_date(:data_fim , 'dd/mm/yyyy') and
tip_transacao = '05024'

) ajustes
from 
(
select '554184091469' msisdn from dual union 
select '554784099255' msisdn from dual union 
select '555184057795' msisdn from dual union 
select '555184060090' msisdn from dual union 
select '555184413278' msisdn from dual union 
select '556184022266' msisdn from dual union 
select '556184026200' msisdn from dual union 
select '556184075648' msisdn from dual union 
select '556184077550' msisdn from dual union 
select '556184085868' msisdn from dual union 
select '556184095589' msisdn from dual union 
select '556184095590' msisdn from dual union 
select '556184095621' msisdn from dual union 
select '556184095623' msisdn from dual union 
select '556184096566' msisdn from dual union 
select '556184097617' msisdn from dual union 
select '556184097618' msisdn from dual union 
select '556184111908' msisdn from dual union 
select '556184127269' msisdn from dual union 
select '556184137840' msisdn from dual union 
select '556184153459' msisdn from dual union 
select '556184153672' msisdn from dual union 
select '556184158236' msisdn from dual union 
select '556184158243' msisdn from dual union 
select '556184158276' msisdn from dual union 
select '556184158342' msisdn from dual union 
select '556184165084' msisdn from dual union 
select '556184175482' msisdn from dual union 
select '556184190468' msisdn from dual union 
select '556184190469' msisdn from dual union 
select '556184190472' msisdn from dual union 
select '556184190473' msisdn from dual union 
select '556184190763' msisdn from dual union 
select '556184190764' msisdn from dual union 
select '556184190765' msisdn from dual union 
select '556184190768' msisdn from dual union 
select '556184190769' msisdn from dual union 
select '556184190781' msisdn from dual union 
select '556184190783' msisdn from dual union 
select '556184190784' msisdn from dual union 
select '556184190785' msisdn from dual union 
select '556184191076' msisdn from dual union 
select '556184191429' msisdn from dual union 
select '556184191942' msisdn from dual union 
select '556184211583' msisdn from dual union 
select '556184214141' msisdn from dual union 
select '556184222616' msisdn from dual union 
select '556184234141' msisdn from dual union 
select '556184240133' msisdn from dual union 
select '556184263922' msisdn from dual union 
select '556184268444' msisdn from dual union 
select '556284024010' msisdn from dual union 
select '556284035868' msisdn from dual union 
select '556284036817' msisdn from dual union 
select '556284038786' msisdn from dual union 
select '556284056729' msisdn from dual union 
select '556284123342' msisdn from dual union 
select '556284167018' msisdn from dual union 
select '556284167019' msisdn from dual union 
select '556284170607' msisdn from dual union 
select '556584022505' msisdn from dual union 
select '556584039830' msisdn from dual union 
select '556584040941' msisdn from dual   	
) cliente
