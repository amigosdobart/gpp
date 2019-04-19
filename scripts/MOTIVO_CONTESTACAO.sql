SELECT MES, PLANO_PRECO, DES_MOTIVO_CONTESTACAO, COUNT(1)
FROM(
SELECT  TO_CHAR(DAT_ABERTURA, 'dd/MM/YYYY') MES,
        CASE
            WHEN C.idt_plano_preco IN (1, 2, 3, 6, 7, 8) THEN
                'PRE-PAGO'
            WHEN C.idt_plano_preco IN (4, 5) THEN
                'CONTROLE'
            ELSE
                'LIGMIX'
        END PLANO_PRECO,
        B.des_motivo_contestacao
FROM
TBL_GER_CONTESTACAO A,
TBL_GER_MOTIVO_CONTESTACAO B,
TBL_APR_ASSINANTE C
WHERE
A.id_motivo_contestacao  = B.id_motivo_contestacao AND
(
(A.dat_abertura >= '01-abr-2006' AND
A.dat_abertura <= '30-abr-2006' )or
(A.dat_abertura >= '01-mai-2006' AND
A.dat_abertura <= '30-mai-2006' )or
(A.dat_abertura >= '01-jun-2006' AND
A.dat_abertura <= '30-jun-2006' )) and 
A.idt_msisdn = C.idt_msisdn
)
GROUP BY MES, PLANO_PRECO, DES_MOTIVO_CONTESTACAO
