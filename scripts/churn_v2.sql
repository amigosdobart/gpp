SELECT TO_CHAR (assinante1.activation_date, 'dd/mm/yyyy') AS data_ativacao,
       (SELECT NVL (COUNT (1), 0)
          FROM tbl_rec_recargas@prepr_gppuser r
         WHERE r.idt_msisdn = assinante1.sub_id
           AND r.id_tipo_recarga = 'R'
           AND r.dat_origem >= assinante1.activation_date) AS qtde_recargas,
       sub_id AS msisdn,
       TO_CHAR (assinante1.dat_importacao, 'dd/mm/yyyy') AS data_churn,
       DECODE ((SELECT COUNT (1)
                  FROM tbl_tmp_churn c
                 WHERE c.nu_msisdn = assinante1.sub_id),
               0, 'primeiro',
               'novo'
              ) situacao1,
        (select nu_imei || ';' || no_fabricante|| ';' || no_modelo 
        from hsid_modelo a, hsid_fabricante b, hsid_cliente c
        where
        c.nu_msisdn = assinante1.sub_id and
        a.co_modelo = c.co_modelo and
        a.co_fabricante = b.co_fabricante) modelo,
        (SELECT in_aparelho_proprio
                FROM hsid_cliente
                where
                nu_msisdn = assinante1.sub_id) proprio
  FROM tbl_apr_assinante_tecnomen@prepr_gppuser assinante1
 WHERE assinante1.account_status = 4
   AND assinante1.profile_id NOT IN (4, 5)
   AND assinante1.service_status = 1
   AND assinante1.dat_importacao >= '01-mar-2006'
   AND assinante1.dat_importacao < '01-abr-2006'
   AND EXISTS (
          SELECT 1
            FROM tbl_apr_assinante_tecnomen@prepr_gppuser assinante2
           WHERE assinante2.sub_id = assinante1.sub_id
             AND assinante2.account_status = 3
             AND assinante2.dat_importacao = (assinante1.dat_importacao - 1))



