SELECT a.transaction_type, a.des_tipo_transacao,
       a.ind_impressao_comp_chamadas, a.ind_impressao_comp_eventos,
       a.idt_sentido, a.des_complemento
  FROM tbl_ger_tip_transacao_tecnomen@prepr_gppuser a
  where
  transaction_type = '120'
  
  
  select * from tbl_rec_recargas@prepr_gppuser
  where
  idt_msisdn = '554184138296' and
  tip_transacao = '00237'
      
  Não Processadas c/ C.Proc	17133	27/08/2007	17:36:16	41	84138296	4184138296	 15,00 

update tbl_rec_recargas@prepr_gppuser
set
    tip_transacao = '00237',
    dat_inclusao = sysdate, 
    id_tipo_recarga = 'R',
    id_canal = '00',
    id_origem = '237',
    ID_SISTEMA_ORIGEM = 'BCO',
    idt_nsu_instituicao = '17133'
where
  idt_msisdn = '554184138296' and
  tip_transacao = '05002' and
  id_recarga = 100078509


commit;





