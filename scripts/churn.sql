/* Formatted on 2006/03/14 11:26 (Formatter Plus v4.8.0) */
WITH primeiro AS
     (
        SELECT a.nu_msisdn,
                  a.nu_imei
               || ';'
               || TO_CHAR (a.dt_inclusao, 'dd/mm/rrrr')
               || ';'
               || c.no_fabricante
               || ';'
               || b.no_modelo
               || ';'
               || a.in_aparelho_proprio info,
               ROW_NUMBER () OVER (ORDER BY dt_inclusao) linha
          FROM hsid_cliente_historico a, hsid_modelo b, hsid_fabricante c
         WHERE a.co_modelo = b.co_modelo AND b.co_fabricante = c.co_fabricante),
     ultimo AS
     (
        SELECT a.nu_msisdn,
                  a.nu_imei
               || ';'
               || TO_CHAR (a.dt_inclusao, 'dd/mm/rrrr')
               || ';'
               || c.no_fabricante
               || ';'
               || b.no_modelo
               || ';'
               || a.in_aparelho_proprio info,
               ROW_NUMBER () OVER (ORDER BY dt_inclusao DESC) linha
          FROM hsid_cliente_historico a, hsid_modelo b, hsid_fabricante c
         WHERE a.co_modelo = b.co_modelo AND b.co_fabricante = c.co_fabricante)
SELECT TO_CHAR (assinante1.activation_date, 'dd/mm/yyyy') AS data_ativacao,
       (SELECT NVL (COUNT (1), 0)
          FROM tbl_rec_recargas@prepr_gppuser r
         WHERE r.idt_msisdn = assinante1.sub_id
           AND r.id_tipo_recarga = 'R'
           AND r.dat_origem >= assinante1.activation_date) AS qtde_recargas,
       sub_id AS msisdn,
       TO_CHAR (assinante1.dat_importacao, 'dd/mm/yyyy') AS data_churn,
       primeiro.info, ultimo.info,
       DECODE ((SELECT COUNT (1)
                  FROM tbl_tmp_churn c
                 WHERE c.nu_msisdn = assinante1.sub_id),
               0, 'primeiro',
               'velho'
              ) situacao1
  FROM tbl_apr_assinante_tecnomen@prepr_gppuser assinante1,
  ultimo, primeiro
 WHERE primeiro.nu_msisdn = assinante1.sub_id
   AND primeiro.linha = 1
   AND ultimo.nu_msisdn = assinante1.sub_id
   AND ultimo.linha = 1
   AND assinante1.account_status = 4
   AND assinante1.profile_id NOT IN (4, 5)
   AND assinante1.service_status = 1
   AND assinante1.dat_importacao >= '01-fev-2006'
   AND assinante1.dat_importacao < '01-mar-2006'
   AND EXISTS (
          SELECT 1
            FROM tbl_apr_assinante_tecnomen@prepr_gppuser assinante2
           WHERE assinante2.sub_id = assinante1.sub_id
             AND assinante2.account_status = 3
             AND assinante2.dat_importacao = (assinante1.dat_importacao - 1))

