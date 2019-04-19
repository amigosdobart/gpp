SELECT to_char(a.dat_recarga, 'mm/RRRR'), 
       substr(idt_msisdn,2,2),
       count(1),
       sum(a.vlr_credito)

  FROM gpp.tbl_rec_recargas a
  where
  a.tip_transacao = '08003' and
  a.dat_recarga between '01-out-2004' and '01-jan-2005'
  group by 
  to_char(a.dat_recarga, 'mm/RRRR'), 
  substr(idt_msisdn,2,2)
