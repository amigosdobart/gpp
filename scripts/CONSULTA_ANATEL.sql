SELECT * FROM (
SELECT A.IDT_MSISDN, A.DAT_ATIVACAO, 
   
        (SELECT SUM(VLR_CREDITO_PRINCIPAL)
        FROM
            TBL_REC_RECARGAS B
            WHERE
            B.IDT_MSISDN = A.IDT_MSISDN AND
            B.tip_transacao = '08001' AND
            B.dat_recarga BETWEEN '01-OUT-2004' AND '31-OUT-2004'
        ) OUTUBRO,
        (SELECT SUM(VLR_CREDITO_PRINCIPAL)
        FROM
            TBL_REC_RECARGAS B
            WHERE
            B.IDT_MSISDN = A.IDT_MSISDN AND
            B.tip_transacao = '08001' AND
            B.dat_recarga BETWEEN '01-NOV-2004' AND '30-NOV-2004'
        ) NOVEMBRO,
        (SELECT SUM(VLR_CREDITO_PRINCIPAL)
        FROM
            TBL_REC_RECARGAS B
            WHERE
            B.IDT_MSISDN = A.IDT_MSISDN AND
            B.tip_transacao = '08001' AND
            B.dat_recarga BETWEEN '01-DEZ-2004' AND '31-DEZ-2004'
        ) DEZEMBRO,
        (SELECT SUM(VLR_CREDITO_PRINCIPAL)
        FROM
            TBL_REC_RECARGAS B
            WHERE
            B.IDT_MSISDN = A.IDT_MSISDN AND
            B.tip_transacao = '08001' AND
            B.dat_recarga BETWEEN '01-JAN-2005' AND '31-JAN-2005'
        ) JANEIRO,
        (SELECT SUM(VLR_CREDITO_BONUS)
        FROM
            TBL_REC_RECARGAS B
            WHERE
            B.IDT_MSISDN = A.IDT_MSISDN AND
            B.tip_transacao = '08001' AND
            B.dat_recarga BETWEEN '01-FEV-2005' AND '28-FEV-2005'
        ) FEVEREIRO,        

        (SELECT SUM(VLR_CREDITO_BONUS)
        FROM
            TBL_REC_RECARGAS B
            WHERE
            B.IDT_MSISDN = A.IDT_MSISDN AND
            B.tip_transacao = '08001' AND
            B.dat_recarga BETWEEN '01-MAR-2005' AND '31-MAR-2005'
        ) MARCO
FROM TBL_APR_ASSINANTE A
WHERE
A.dat_ativacao < '01-JAN-2005')
WHERE
NOVEMBRO IS NOT NULL AND
DEZEMBRO IS NOT NULL AND
JANEIRO IS NOT NULL AND
FEVEREIRO IS NOT NULL AND
MARCO IS NOT NULL AND
NOVEMBRO < 200 AND
DEZEMBRO < 200 AND
JANEIRO < 200 AND
FEVEREIRO < 200 AND
MARCO < 200 


