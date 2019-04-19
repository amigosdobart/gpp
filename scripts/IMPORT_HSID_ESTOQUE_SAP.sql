/* Formatted on 2006/03/17 12:14 (Formatter Plus v4.8.0) */
DECLARE
   c   NUMBER (4) := 0;
BEGIN
   FOR linha IN (SELECT   dat_inclusao, SUBSTR (a.des_dados, 1, 15) imei,
                          SUBSTR (a.des_dados, 16, 2) status,
                          SUBSTR (a.des_dados, 18) loja
                     FROM tbl_int_eti_in@prepr_gppuser a
                 ORDER BY a.dat_inclusao)
   LOOP
      proc_ajusta_imei_sap (linha.dat_inclusao,
                            linha.imei,
                            linha.status,
                            linha.loja
                           );
      c := c + 1;

      IF (c > 1000)
      THEN
         COMMIT;
         c := 0;
      END IF;
   END LOOP;

   COMMIT;
END;

