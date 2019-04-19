SELECT a.id_processamento, a.dat_cadastro, a.idt_evento_negocio,
       to_char(a.xml_document), a.idt_status_processamento
  FROM gpp.tbl_int_ppp_in a
  where
  a.idt_status_processamento = 'RqAcessoColaborador' 
  
  and
  a.id_processamento = 1305600
  a.xml_document like '%TR020583%'
  
  
  SELECT *
  FROM tbl_int_ppp_in a
  where
  a.idt_status_processamento = 'RqAcessoColaborador' 
  
