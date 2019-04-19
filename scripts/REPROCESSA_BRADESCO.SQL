/* Formatted on 2007/11/01 11:43 (Formatter Plus v4.8.7) */
DECLARE
   contestacao   tbl_rec_recargas%ROWTYPE;
BEGIN
   FOR linha IN (SELECT ROWID ID, a.*
                   FROM tbl_tmp_bradesco a
                  WHERE idt_status = 'N')
   LOOP
      BEGIN
         SELECT *
           INTO contestacao
           FROM (SELECT   a.*
                     FROM tbl_rec_recargas@prepr_gppuser a
                    WHERE idt_msisdn = linha.idt_msisdn
                      AND dat_origem >= linha.dat_recarga
                      AND (   (    tip_transacao = '05002'
                               AND vlr_pago = linha.idt_valor
                              )
                           OR (    a.id_sistema_origem = 'PPP'
                               AND a.nom_operador != 'WIG'
                               AND a.tip_transacao NOT IN ('05022', '06022')
                              )
                          )
                 ORDER BY dat_origem)
          WHERE ROWNUM = 1;

         UPDATE tbl_rec_recargas@prepr_gppuser
            SET tip_transacao = '00237',
                dat_inclusao = SYSDATE - 1,
                id_tipo_recarga = 'R',
                id_canal = '00',
                id_origem = '237',
                id_sistema_origem = 'BCO',
                idt_nsu_instituicao = linha.idt_nsu,
                id_valor = linha.idt_valor
          WHERE idt_msisdn = contestacao.idt_msisdn
            AND tip_transacao = contestacao.tip_transacao
            AND id_recarga = contestacao.id_recarga;

         UPDATE tbl_tmp_bradesco
            SET dat_contestacao = contestacao.dat_origem,
                idt_status = 'Y'
          WHERE ROWID = linha.ID;
      EXCEPTION
         WHEN OTHERS
         THEN                                
            UPDATE tbl_tmp_bradesco
               SET dat_contestacao = contestacao.dat_origem,
                   idt_status = 'E'
             WHERE ROWID = linha.ID;
      END;

      COMMIT;
   END LOOP;
END;

