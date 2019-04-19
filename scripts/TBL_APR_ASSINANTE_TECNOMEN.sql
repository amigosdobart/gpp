insert into tbl_rec_fila_recargas(idt_msisdn, tip_transacao, dat_cadastro, dat_execucao,
        des_mensagem, tip_sms, ind_envia_sms,
        idt_status_processamento,
        vlr_credito_bonus, num_dias_exp_bonus)

SELECT b.idt_msisdn, b.tip_transacao, sysdate dat_cadastro, sysdate dat_execucao,
       b.des_mensagem, b.tip_sms, 0 ind_envia_sms,
       1 idt_status_processamento, 
       b.vlr_credito_bonus,  
       b.num_dias_exp_bonus
 FROM tbl_apr_assinante_tecnomen a, tbl_rec_fila_recargas b
  WHERE
  A.DAT_IMPORTACAO = '09-FEV-2005' AND
  a.sub_id = b.idt_msisdn and
  b.tip_transacao = '08003' and
  b.dat_processamento >= '01-fev-2005' and
  A.expiry > A.bonus_expiry and
  a.bonus_expiry <= '09-fev-2005' and
  a.account_status = 2 and
  tip_sms = 'PULAPULA'
  
  
  SELECT COUNT(*), IDT_STATUS_PROCESSAMENTO FROM tbl_rec_fila_recargas A 
   WHERE
  DAT_EXECUCAO >= '09-FEV-2005' 
  GROUP BY IDT_STATUS_PROCESSAMENTO
  
  commit;
  
  
  
  SELECT b.idt_msisdn, b.tip_transacao, sysdate dat_cadastro, sysdate dat_execucao,
       b.des_mensagem, b.tip_sms, 0 ind_envia_sms,
       1 idt_status_processamento, 
       b.vlr_credito_bonus,  
       b.num_dias_exp_bonus
 FROM tbl_apr_assinante_tecnomen a, tbl_rec_fila_recargas b
  WHERE
  a.sub_id = '556184022372' and
  A.DAT_IMPORTACAO = '09-FEV-2005' AND
  a.sub_id = b.idt_msisdn and
  b.tip_transacao = '08001' and
  b.dat_processamento >= '01-fev-2005' and
  --A.expiry > A.bonus_expiry and
  --a.bonus_expiry <= '09-fev-2005' and
  --a.account_status = 2 and
  tip_sms = 'PULAPULA'
  
  
  
  SELECT * FROM TBL_REC_RECARGAS A
  WHERE
  A.tip_transacao = '08003' AND
  A.dat_recarga >= '01-fev-2005'
