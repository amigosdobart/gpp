
UPDATE TBL_GER_PROMOCAO_ASSINANTE
SET
IDT_PROMOCAO = :PROMOCAO,
DAT_EXECUCAO = CASE 
                            WHEN :PROMOCAO = 1 THEN
                                TO_DATE('01' || TO_CHAR(SYSDATE, '/MM/YYYY'), 'DD/MM/YYYY')
                            WHEN :PROMOCAO = 2 THEN
                                TO_DATE('06' || TO_CHAR(SYSDATE, '/MM/YYYY'), 'DD/MM/YYYY')
                            WHEN :PROMOCAO = 4 THEN
                                TO_DATE('11' || TO_CHAR(SYSDATE, '/MM/YYYY'), 'DD/MM/YYYY')
                            ELSE
                            	TO_DATE('16' || TO_CHAR(SYSDATE, '/MM/YYYY'), 'DD/MM/YYYY')
                        END,
DAT_ENTRADA_PROMOCAO = CASE 
                            WHEN :PROMOCAO = 1 THEN
                                TO_DATE('31/12/2004', 'DD/MM/YYYY')
                            WHEN :PROMOCAO = 2 THEN
                                TO_DATE('08/02/2005', 'DD/MM/YYYY')
                            WHEN :PROMOCAO = 4 THEN
                                TO_DATE('31/03/2005', 'DD/MM/YYYY')
                            ELSE
                            	TRUNC(SYSDATE)
                        END,
IND_ISENTO_LIMITE = :LIMITE
WHERE
    IDT_MSISDN = :MSISDN;
COMMIT;

SELECT * FROM TBL_GER_PROMOCAO_ASSINANTE 
WHERE
IDT_MSISDN = :MSISDN;
