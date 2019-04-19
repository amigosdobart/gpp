insert into tbl_rec_fila_recargas
select sub_id_hoje, '08001', sysdate, sysdate, null, 
'Voce recebeu R$ ' || trim(to_char(regra_ff, '999,990.00'))  || ' pela Promocao Pula-Pula.
', 'PULA', 1, 0, 0, 0, regra_ff, 0,0, 0,0,0,0,0
from (
select sub_id_hoje, 
			case
				when (((e.min_credito - e.min_ff)/60 * f.vlr_bonus_minuto) + ((e.min_ff/60) * f.vlr_bonus_minuto_ff) > 1000)  and 
				(c.ind_isento_limite is null  or c.ind_isento_limite = 0) then
					1000
				else
					(((e.min_credito - e.min_ff)/60) * f.vlr_bonus_minuto) + ((e.min_ff/60) * f.vlr_bonus_minuto_ff) 
			end regra_ff

from
(SELECT DAT_IMPORTACAO DT_HOJE, SUB_ID SUB_ID_HOJE, ACCOUNT_STATUS STATUS_HOJE
FROM
TBL_APR_ASSINANTE_TECNOMEN
WHERE
DAT_IMPORTACAO = '06-jul-2005') hoje, 
(
SELECT DAT_IMPORTACAO DT_ONTEM, SUB_ID SUB_ID_ONTEM, ACCOUNT_STATUS STATUS_ONTEM
FROM
TBL_APR_ASSINANTE_TECNOMEN
WHERE
DAT_IMPORTACAO = '05-jul-2005') ontem,
TBL_GER_PROMOCAO_ASSINANTE C,
tbl_ger_totalizacao_pula_pula e, 
tbl_ger_bonus_pula_pula f
where
status_ontem in (2, 3, 4) and
status_hoje = 2 AND
SUB_ID_HOJE = SUB_ID_ONTEM AND
C.IDT_MSISDN = SUB_ID_HOJE AND
C.IDT_PROMOCAO = 1 AND
NOT EXISTS (SELECT 1 FROM TBL_REC_RECARGAS D WHERE
            D.idt_msisdn = SUB_ID_HOJE AND
            TIP_TRANSACAO IN ( '08001', '05024') AND
             DAT_RECARGA > '01-JUL-2005') and
e.dat_mes = '0506' and
e.idt_msisdn = sub_id_hoje and
f.id_codigo_nacional = substr(sub_id_hoje, 3,2)
) x;
commit;



