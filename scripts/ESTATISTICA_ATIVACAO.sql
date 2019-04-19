SELECT a.id_origem, a.des_origem, a.ind_modificar_data_exp, a.id_canal,
       a.ind_ativo
  FROM gpp.tbl_rec_origem a
  where
  id_canal = '05'
  
  
  select dat_importacao, account_status, count(*)
  from
  tbl_apr_assinante_tecnomen
  group by
  dat_importacao, account_status
