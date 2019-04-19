create table tbl_tmp_diferenca_pula_sem
as

SELECT 
       a.idt_msisdn, 
       sum(a.min_credito * b.vlr_bonus_minuto) bonus_devido
       
  FROM tbl_ger_totalizacao_pula_pula a,
        tbl_ger_bonus_pula_pula@prepr_gppuser b ,
        tbl_ger_promocao_assinante@prepr_gppuser d
        
where
a.dat_mes = '0504' and
b.id_codigo_nacional = substr(a.idt_msisdn, 3,2) and
d.idt_msisdn = a.idt_msisdn and
d.idt_promocao <> 3 and
not exists (select 1 from tbl_rec_recargas@prepr_gppuser c
where
c.dat_recarga > '01-mai-2005' and
c.tip_transacao = '08001' and
c.idt_msisdn = a.idt_msisdn)
group by a.idt_msisdn


select * from tbl_tmp_diferenca_pula_sem


drop table tbl_tmp_diferenca_pula
