DECLARE
    CONTADOR NUMBER(4):=0;
BEGIN
    FOR LINHA IN (SELECT IDT_MSISDN, DAT_PROCESSAMENTO FROM TBL_REC_FILA_RECARGAS A
            WHERE
            A.tip_transacao = '08001' AND
            A.idt_status_processamento = 2 AND
            A.dat_execucao >= '05-JAN-2005') LOOP

        UPDATE TBL_REC_RECARGAS
        SET
        DAT_RECARGA = LINHA.DAT_PROCESSAMENTO
        WHERE
        TIP_TRANSACAO = '08001' AND
        DAT_RECARGA >= SYSDATE -1 AND
        IDT_MSISDN = LINHA.IDT_MSISDN;
        
        CONTADOR := CONTADOR +1;
        IF CONTADOR >= 9999 THEN
            CONTADOR := 0;
            COMMIT;
        END IF;
    END LOOP;
COMMIT;
END;
            

