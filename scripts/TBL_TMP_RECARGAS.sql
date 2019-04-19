

begin

for linha in (SELECT rowid id, a.idt_msisdn, a.idt_nsu, a.vlr_recarga, a.encontrada, a.tip_transacao
  FROM tbl_tmp_recargas a) loop
  
    for interno in (select dat_origem from tbl_rec_recargas@prepr_gppuser a
    where
     a.IDT_NSU_INSTITUICAO = linha.idt_nsu and
     a.idt_msisdn = linha.idt_msisdn and
     a.dat_origem >= '01-jul-2006' and     
     a.dat_origem < '04-jul-2006' and      
     a.tip_transacao = linha.tip_transacao) loop 
     
         update tbl_tmp_recargas a
         set
         a.encontrada = 'X',
         a.dat_origem = interno.dat_origem
         where
         rowid = linha.id;
         commit;    
     end loop;  
end loop;
  
end;
