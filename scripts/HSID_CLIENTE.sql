/* Formatted on 2006/03/16 18:04 (Formatter Plus v4.8.0) */
SELECT   dia_mes, plano, regiao, in_aparelho_proprio, COUNT (*)
    FROM (SELECT /*+ RULE, INDEX (A IDX_HSID_CLIENTE_03) */
                 TO_CHAR (dt_inclusao, 'RRRR/MM') dia_mes,
                 CASE
                    WHEN b.idt_plano_preco IN (1, 2, 3, 6, 7, 8)
                       THEN 'PRE-PAGO'
                    ELSE 'HIBRIDO'
                 END plano,
                 SUBSTR (a.nu_msisdn, 3, 2) regiao, in_aparelho_proprio
            FROM hsid_cliente_inicio a, tbl_apr_assinante@prepr_gppuser b,
            hsid_modelo c, hsid_fabricante d 
           WHERE b.idt_msisdn = a.nu_msisdn AND dt_inclusao >= '01-dez-2005' and
           a.co_modelo = c.co_modelo and
           c.co_fabricante = d.co_fabricante)
GROUP BY dia_mes, plano, regiao, in_aparelho_proprio



SELECT 
    TO_CHAR (dt_inclusao, 'RRRR/MM') dia_mes,
    count(*)
    FROM hsid_cliente a
group by TO_CHAR (dt_inclusao, 'RRRR/MM')

begin
for linha in(
select a.rowid id --a.nu_msisdn, a.nu_imei, a.dt_inclusao, b.dat_importacao, b.cod_loja
from hsid_cliente a, hsid_estoque_sap b
where
a.nu_imei = b.imei and
in_aparelho_proprio = 'N') loop
    update hsid_cliente set
    in_aparelho_proprio = 'S'
    where
    rowid = linha.id;
    commit;
end loop;

for linha in(
select a.rowid id --a.nu_msisdn, a.nu_imei, a.dt_inclusao, b.dat_importacao, b.cod_loja
from hsid_cliente_inicio a, hsid_estoque_sap b
where
a.nu_imei = b.imei and
in_aparelho_proprio = 'N') loop
    update hsid_cliente_inicio set
    in_aparelho_proprio = 'S'
    where
    rowid = linha.id;
    commit;
end loop;
end;


