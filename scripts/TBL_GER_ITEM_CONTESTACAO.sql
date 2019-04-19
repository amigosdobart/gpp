SELECT a.transaction_type, b.des_tipo_transacao, nvl(a.idt_status_item_contestacao, 'A'), 
    sum(a.vlr_contestado_principal), sum(a.vlr_contestado_bonus),
    sum(a.vlr_contestado_sms), sum(a.vlr_contestado_gprs),
    sum(a.vlr_ajustado_principal), sum(a.vlr_ajustado_bonus),
    sum(a.vlr_ajustado_sms), sum(a.vlr_ajustado_gprs),
    count(1)
  FROM gpp.tbl_ger_item_contestacao a,
  tbl_ger_tip_transacao_tecnomen b
  where
  timestamp > '01-fev-2006' and
  timestamp < '01-mar-2006' and
  a.transaction_type =  b.transaction_type (+)
  group by 
  a.transaction_type, b.des_tipo_transacao, nvl(a.idt_status_item_contestacao, 'A')
