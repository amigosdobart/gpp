insert into tbl_rec_fila_recargas
select idt_msisdn, '08001', sysdate, sysdate, null, 
null, 'PULA', 0, 0, 0, 0, regra_ff, 0,0, 0,0,0,0,0, 0,0
from (

select cliente.idt_msisdn, 
			case
				when (((e.min_credito - e.min_ff)/60 * f.vlr_bonus_minuto) + ((e.min_ff/60) * f.vlr_bonus_minuto_ff) > 300)   then
					300
				else
					(((e.min_credito - e.min_ff)/60) * f.vlr_bonus_minuto) + ((e.min_ff/60) * f.vlr_bonus_minuto_ff) 
			end regra_ff

from
TBL_GER_PROMOCAO_ASSINANTE C,
tbl_ger_totalizacao_pula_pula e, 
tbl_ger_bonus_pula_pula f,
(
select '554484081392' idt_msisdn from dual union all
select '555184294932' idt_msisdn from dual union all
select '555184277329' idt_msisdn from dual union all
select '555184189144' idt_msisdn from dual union all
select '555384028330' idt_msisdn from dual union all
select '555384036122' idt_msisdn from dual union all
select '555184043665' idt_msisdn from dual 
)
cliente
where
C.idt_msisdn = cliente.idt_msisdn and
e.dat_mes = '0509' and
e.idt_msisdn = cliente.idt_msisdn and
f.id_codigo_nacional = substr(cliente.idt_msisdn, 3,2)


) x;



