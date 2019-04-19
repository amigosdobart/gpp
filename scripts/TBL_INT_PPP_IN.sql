SELECT a.id_processamento, a.dat_cadastro, a.idt_evento_negocio,
       a.xml_document, a.idt_status_processamento
  FROM tbl_int_ppp_out a
  order by 2 desc
  
  
  SELECT * 
  FROM tbl_int_ppp_in a
  
  
  
  select * from tbl_apr_assinante_tecnomen
  where
  idt_msisdn = '556184039330'
  
  
  insert into tbl_int_ppp_out(
id_processamento, dat_cadastro, idt_evento_negocio,
       xml_document, idt_status_processamento)
       values
(SEQ_ID_PROCESSAMENTO.nextval,
sysdate,
'GPP_atSubscriber',
'<?xml version="1.0" encoding="UTF-8"?><GPPStatus><msisdn>556184039330</msisdn><status>3</status><data>25/04/2007</data></GPPStatus>',
'N');

commit
