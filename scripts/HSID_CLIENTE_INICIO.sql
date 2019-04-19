select regiao, cod_loja, no_fabricante, no_modelo, plano, count(1)
from (

SELECT substr(a.nu_msisdn, 3, 2) regiao, b.cod_loja, d.no_fabricante, c.no_modelo,
CASE
                    WHEN E.idt_plano_preco IN (1, 6) THEN 'PRE-PAGO - Flat'
                    WHEN E.idt_plano_preco IN (2, 7) THEN 'PRE-PAGO - Diurno'
                    WHEN E.idt_plano_preco IN (3, 8) THEN 'PRE-PAGO - Noturno'
                    WHEN  E.IDT_PLANO_PRECO IN (4, 5)   THEN 'HIBRIDO'
                    ELSE 'POS-PAGO'
                 END plano

  FROM hsid_cliente_inicio a, hsid_estoque_sap b,
  hsid_modelo c, hsid_fabricante d, TBL_APR_ASSINANTE@PREPR_GPPUSER E
  where
  a.nu_imei = b.imei and
  a.dt_inclusao >= '01-jan-2006' and
  a.dt_inclusao < '01-fev-2006' and
  a.co_modelo = c.co_modelo and
  c.co_fabricante = d.co_fabricante and
  a.nu_msisdn = e.idt_msisdn(+)
)
group by regiao, cod_loja, no_fabricante, no_modelo, plano
