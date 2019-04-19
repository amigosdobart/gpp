/* Formatted on 2006/04/04 08:23 (Formatter Plus v4.8.0) */
SELECT a.idt_msisdn,
       (SELECT COUNT (1)
          FROM tbl_rec_recargas b
         WHERE b.idt_msisdn = a.idt_msisdn
           AND id_tipo_recarga = 'R'
           AND dat_origem >= '01-JAN-2006'
           AND dat_origem < '01-FEV-2006') rec_janeiro,
       (SELECT COUNT (1)
          FROM tbl_rec_recargas b
         WHERE b.idt_msisdn = a.idt_msisdn
           AND id_tipo_recarga = 'R'
           AND dat_origem >= '01-FEV-2006'
           AND dat_origem < '01-MAR-2006') rec_fevereiro,
       (SELECT COUNT (1)
          FROM tbl_rec_recargas b
         WHERE b.idt_msisdn = a.idt_msisdn
           AND id_tipo_recarga = 'R'
           AND dat_origem >= '01-MAR-2006'
           AND dat_origem < '01-ABR-2006') rec_marco,
       (SELECT COUNT (1)
          FROM tbl_rec_recargas b
         WHERE b.idt_msisdn = a.idt_msisdn
           AND id_tipo_recarga = 'R'
           AND dat_origem >= '01-abr-2006'
           AND dat_origem < '01-mai-2006') rec_abril
  FROM tbl_apr_eventos a
 WHERE a.dat_aprovisionamento >= '01-JAN-2006'
   AND a.dat_aprovisionamento < '31-JAN-2006'
   AND a.tip_operacao = 'ATIVACAO'



SELECT A.nu_msisdn FROM HSID_CLIENTE_INICIO A


SELECT COUNT(1) FROM TBL_REC_RECARGAS A
WHERE
A.idt_msisdn = '556184066666' AND
ID_TIPO_RECARGA = 'R' AND
DAT_ORIGEM >= '01-JAN-2006' AND 
DAT_ORIGEM <'01-FEV-2006'



select * from tbl_apr_eventos a
where
a.idt_msisdn = '554184012271' 
order by a.dat_aprovisionamento


/* Formatted on 2006/04/03 19:02 (Formatter Plus v4.8.0) */
SELECT   mes, COUNT (1)
    FROM (SELECT   a.idt_msisdn, TO_CHAR (MIN (b.dat_origem), 'YYYY/MM') mes
              FROM tbl_apr_eventos a, tbl_rec_recargas b
             WHERE a.dat_aprovisionamento >= '01-JAN-2006'
               AND a.dat_aprovisionamento < '31-JAN-2006'
               AND a.tip_operacao = 'ATIVACAO'
               AND a.idt_msisdn = b.idt_msisdn
               AND b.id_tipo_recarga = 'R' 
               AND B.dat_origem >= '01-JAN-2006'
          GROUP BY a.idt_msisdn)
GROUP BY mes



select count(1) from tbl_apr_eventos a 
WHERE a.dat_aprovisionamento >= '01-JAN-2006'
               AND a.dat_aprovisionamento < '31-JAN-2006'
               AND a.tip_operacao = 'ATIVACAO'
               
               
SELECT   QTD, MES, COUNT (1)
    FROM (SELECT   X.idt_msisdn, TO_CHAR (MIN (b.dat_origem), 'YYYY/MM') MES, TRUNC(COUNT(1)/3) QTD
              FROM (select DISTINCT idt_msisdn 
                    FROM
                     tbl_apr_eventos a
                   where
                    a.dat_aprovisionamento >= '01-JAN-2006'
                    AND a.dat_aprovisionamento < '31-JAN-2006'
                    AND a.tip_operacao = 'ATIVACAO' 
                    
            ) x, tbl_rec_recargas b
             WHERE 
                B.idt_msisdn = X.idt_msisdn
               AND b.id_tipo_recarga = 'R' 
               AND B.dat_origem >= '01-JAN-2006'
          GROUP BY X.idt_msisdn)
GROUP BY QTD, MES


          
SELECT   mes, COUNT (1)
    FROM (SELECT   a.idt_msisdn, TO_CHAR (MIN (b.dat_origem), 'YYYY/MM') mes
              FROM tbl_apr_assinante a, tbl_rec_recargas b
             WHERE a.dat_ativacao >= '01-JAN-2006'
               AND a.dat_ativacao < '31-JAN-2006'
               and a.idt_status in (2, 3, 4, 5) 
               and a.idt_plano_preco in (1, 2, 3, 6, 7, 8)
               AND a.idt_msisdn = b.idt_msisdn
               AND b.id_tipo_recarga = 'R' 
               AND B.dat_origem >= '01-JAN-2006'
          GROUP BY a.idt_msisdn)
GROUP BY mes


select count(*) from tbl_apr_assinante where 
dat_ativacao >= '01-jan-2006' and 
dat_ativacao < '31-jan-2006' 
and idt_plano_preco in (1, 2, 3, 6, 7, 8)


SELECT   qtd, mes, COUNT (1)
    FROM (SELECT   a.idt_msisdn, TO_CHAR (b.dat_origem, 'YYYY/MM') mes, trunc(count(1)/3) qtd
              FROM tbl_apr_assinante a, tbl_rec_recargas b
             WHERE a.dat_ativacao >= '01-JAN-2006'
               AND a.dat_ativacao < '31-JAN-2006'
               and a.idt_status in (2, 3, 4, 5)
               and a.idt_plano_preco in (1, 2, 3, 6, 7, 8)
               AND a.idt_msisdn = b.idt_msisdn
               AND b.id_tipo_recarga = 'R' 
               AND B.dat_origem >= '01-JAN-2006'
          GROUP BY a.idt_msisdn, TO_CHAR (b.dat_origem, 'YYYY/MM'))
GROUP BY qtd, mes


sELECT   a.idt_msisdn, TO_CHAR (b.dat_origem, 'YYYY/MM') mes, count(1) qtd
              FROM tbl_apr_assinante a, tbl_rec_recargas b
             WHERE a.dat_ativacao >= '01-JAN-2006'
               AND a.dat_ativacao < '31-JAN-2006'
               and a.idt_status in (2, 3, 4, 5)
               and a.idt_plano_preco in (1, 2, 3, 6, 7, 8)
               AND a.idt_msisdn = b.idt_msisdn
               AND b.id_tipo_recarga = 'R' 
               AND B.dat_origem >= '01-JAN-2006'
          GROUP BY a.idt_msisdn, TO_CHAR (b.dat_origem, 'YYYY/MM')
          order by 3 desc
