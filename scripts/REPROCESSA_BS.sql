declare
    documento XMLType := null;
    existeBS number(3) := 0;
    transaction_type number(4);
    COD_ERRO number(10);
    DES_ERRO VARCHAR2(4000);
begin

    for linha in (
                select
                rowid id
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/numeroBS/text()')) numeroBS
                from tbl_int_ppp_in i
                where idt_evento_negocio='SFA_rqFecharItemBS' and
                idt_status_processamento = 'Y'
                
                    ) loop
                    
                    
            select count(1) into existeBS from hsid.tbl_contestacao a where a.idt_nu_bs = linha.numeroBS;
            
            if(existeBS > 0) then
				update tbl_int_ppp_in set idt_status_processamento = 'N' where
				rowid = linha.id;
				commit;
            end if;
            
    end loop;
end;


