SELECT * FROM TBL_REC_RECARGAS A, TBL_REC_ORIGEM B
WHERE
A.idt_msisdn='556184022722'
AND A.id_canal = B.id_canal 
AND A.id_origem = B.id_origem
ORDER BY
DAT_RECARGA


SELECT * FROM TBL_REC_RECARGAS
WHERE
TIP_TRANSACAO = '04008' AND
DAT_RECARGA >= TO_DATE('02/10/2004', 'DD/MM/YYYY')
ORDER BY DAT_RECARGA

SELECT * FROM TBL_REC_ORIGEM WHERE ID_CANAL = '04'

drop table tbl_tmp_ajuste_menos_3;
create table tbl_tmp_ajuste_menos_3
as
select sub_id, sum(a.call_value) valor, count(*) contador from tbl_ger_cdr@prepr_gppuser a
where
a.call_duration > 0 and a.call_duration <= 3 and
a.call_value > 0 and
a.rate_name <> '------IVR_CHARGE----' and 
a.orig_called_number is not null and
a.timestamp >= to_date('27/09/2004', 'dd/mm/yyyy') and
a.sub_id not in (select sub_id from tbl_ext_happy_manager b where b.msisdn = a.sub_id)
group by
a.sub_id



select sum(valor)/100000 from tbl_tmp_ajuste

