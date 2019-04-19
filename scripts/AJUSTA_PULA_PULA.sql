DECLARE 
    IDENTIFICADOR ROWID;
    contador number(3) :=0;
begin
       for linha in (     SELECT A.idt_msisdn
                            FROM TBL_REC_RECARGAS A
                            WHERE
                            A.tip_transacao = '05007' AND
                            A.dat_recarga >= '07-jan-2005'
                            GROUP BY
                            A.idt_msisdn
                            HAVING 
                            COUNT(*) > 1) loop 

                FOR LINHA2 IN (SELECT A.ROWID IDENTIFICADOR FROM 
                TBL_REC_RECARGAS A,
                (SELECT C.idt_msisdn,  MIN(id_recarga) ID, MIN(DAT_RECARGA) DAT
                FROM TBL_REC_RECARGAS C
                WHERE
                C.tip_transacao = '05007' AND
                C.dat_recarga >= '07-JAN-2005' AND
                C.idt_msisdn = linha.idt_msisdn
                GROUP BY
                C.idt_msisdn
                )B
                WHERE
                A.idt_msisdn = linha.idt_msisdn AND
                a.tip_transacao = '05007' AND
                a.dat_recarga >= '07-JAN-2005' AND
                --A.DAT_RECARGA = B.DAT AND
				A.ID_RECARGA <> B.ID) LOOP
                
                DELETE FROM TBL_REC_RECARGAS
                WHERE
                ROWID = LINHA2.IDENTIFICADOR;
                
                END LOOP;
                contador := contador +1;
                if contador > 100 then
                	contador := 0;
                	commit;
                end if;
        end loop;
        commit;
end;
