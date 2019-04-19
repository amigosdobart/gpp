select * from tbl_ppp_usuario where lower(nom_usuario) like '%simone alves%'


select idt_evento_negocio, count(1)  group by idt_evento_negocio


select a.dat_cadastro, to_char(a.xml_document)  from tbl_int_ppp_in a 
where 
a.idt_evento_negocio = 'RqAcessoColaborador' and
lower(a.xml_document) like '%tr055944%'
order by a.id_processamento desc





select XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(xml_document)),'/root/CASE/ID_USUARIO/text()')) idEvento,
XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(xml_document)),'/root/CASE/acao/text()')) idEvento from tbl_int_ppp_in 
where
id_processamento in  ('1353939',
'1354157',
'1303633',
'1303796',
'1303802',
'1300892',
'1301582',
'1301585',
'1302001',
'1279817',
'1305600',
'1303146',
'1305160',
'1101819',
'1300664',
'1300616',
'1301508',
'1301541',
'1301558')
 and 
idt_evento_negocio = 'RqAcessoColaborador' 

commit
