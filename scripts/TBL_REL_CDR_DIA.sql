SELECT /*+ index(a XPKTBL_REL_CDR_DIA) */
       to_char(a.dat_cdr, 'yyyy/mm'),  
       b.des_plano_preco, a.num_csp, 
       a.idt_sentido, 
       a.tip_chamada,  
       a.idt_codigo_nacional,
       sum(a.qtd_registros),
       sum(a.vlr_duracao), 
       sum(a.vlr_total_principal),  
       sum(a.vlr_total_bonus),
       sum(a.vlr_total_sms), 
       sum(a.vlr_total_dados)
  FROM gpp.tbl_rel_cdr_dia a, tbl_ger_plano_preco b
 WHERE a.dat_cdr >= '01-set-2006'
   AND a.idt_plano IN ('40', '43', '44', '45', '46', '47', '48') and
   a.idt_plano = b.idt_plano_preco
group by
       to_char(a.dat_cdr, 'yyyy/mm'),  
       b.des_plano_preco, a.num_csp, 
       a.idt_sentido, 
       a.tip_chamada,  
       a.idt_codigo_nacional
