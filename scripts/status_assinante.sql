SELECT A.DAT_IMPORTACAO, a.activation_date, A.sub_id, A.profile_id, A.account_status, a.expiry, 
a.bonus_expiry, a.data_expiry, a.sm_expiry
FROM TBL_APR_ASSINANTE_TECNOMEN A
WHERE
A.sub_id = '554884080942' 

SELECT * 
DELETE FROM TBL_GER_TOTALIZACAO_PULA_PULA A
WHERE
IDT_MSISDN = '556184061433'


SELECT trunc(DAT_ENTRADA_PROMOCAO),COUNT(*) FROM TBL_GER_PROMOCAO_ASSINANTE
WHERE
DAT_ENTRADA_PROMOCAO > '19-mar-2005'
GROUP BY DAT_ENTRADA_PROMOCAO

SELECT DAT_IMPORTACAO, COUNT(*)
FROM
TBL_APR_ASSINANTE_TECNOMEN A
WHERE
DAT_IMPORTACAO > '20-FEV-2005'
GROUP BY DAT_IMPORTACAO





SELECT * FROM TBL_GER_PROMOCAO_ASSINANTE
WHERE
IDT_MSISDN = '556584074717'


SELECT count(*), sum(a.vlr_credito_bonus) 
FROM TBL_rec_fila_recargas a
WHERE
a.tip_transacao = '08001' and
a.dat_execucao >= '01-mai-2005' and
a.idt_status_processamento = 3
IDT_MSISDN = '554184212327'


