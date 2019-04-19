/* Formatted on 2005/12/23 09:29 (Formatter Plus v4.8.0) */
INSERT INTO tbl_ger_envio_sms
            (id_registro, idt_msisdn, des_mensagem, num_prioridade,
             dat_envio_sms, idt_status_processamento, dat_processamento,
             tip_sms
            )

select seq_envio_sms.NEXTVAL, idt_msisdn,
'Faca sua recarga na rede Salfer e garanta seu bonus Pula-Pula',0, SYSDATE, 1, SYSDATE,
             'SALFER'

 from tbl_apr_assinante a
where
(
idt_msisdn like '5547%' or
idt_msisdn like '5548%' or
idt_msisdn like '5549%'
) and
a.idt_status in (2, 3, 4);

commit;


select * from tbl_ppp_grupo_usuario 
where
id_usuario = 'bt024318'

commit;


select count(1) from tbl_ger_envio_sms a where a.idt_status_processamento = 1

select to_char(timestamp, 'dd'), substr(sub_id, 3,2),
count(1)
from
tbl_ger_cdr partition(pc200512)
where
transaction_type = 9
group by to_char(timestamp, 'dd'), substr(sub_id, 3,2)
