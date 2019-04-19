SELECT a.id_processamento, a.dat_cadastro, a.idt_evento_negocio,
       TO_CHAR(a.xml_document), a.idt_status_processamento
  FROM gpp.tbl_int_ppp_in a
  WHERE
  A.idt_evento_negocio = 'SAP_RqAtivarVouchers' and
  a.idt_status_processamento = 'N'
  order by dat_cadastro
