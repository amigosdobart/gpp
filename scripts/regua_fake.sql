BEGIN
FOR LINHA IN (
    SELECT rowid ID, ACCOUNT_STATUS
    FROM TBL_APR_ASSINANTE_TECNOMEN PARTITION(PA20060824) A
    WHERE
    A.expiry < '24-AGO-2006' AND
    A.service_status = 1 and account_status not in (1, 5)
    ) 
    LOOP 
        if (LINHA.ACCOUNT_STATUS = 2) THEN
            UPDATE TBL_APR_ASSINANTE_TECNOMEN PARTITION(PA20060824) A
            SET EXPIRY = to_date('24-ago-2006')  + 90,
                ACCOUNT_STATUS = 3
            WHERE
            rowid = LINHA.ID;
        
        elsif (LINHA.ACCOUNT_STATUS = 3) THEN
            UPDATE TBL_APR_ASSINANTE_TECNOMEN PARTITION(PA20060824) A
            SET EXPIRY = to_date('24-ago-2006')  + 120,
                ACCOUNT_STATUS = 4
            WHERE
            rowid = LINHA.ID;
        elsif (LINHA.ACCOUNT_STATUS = 4) THEN
            UPDATE TBL_APR_ASSINANTE_TECNOMEN PARTITION(PA20060824) A
            SET ACCOUNT_STATUS = 5
            WHERE
            rowid = LINHA.ID;
        END if;
END LOOP;
END;
/

commit
