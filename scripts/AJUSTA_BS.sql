declare
    documento XMLType := null;
    existeBS number(3) := 0;
    transaction_type number(4);
    COD_ERRO number(10);
    DES_ERRO VARCHAR2(4000);
begin

    for linha in (
                select
                id_processamento
                ,xml_document
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/header/idEvento/text()')) idEvento
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/numeroBS/text()')) numeroBS
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/numeroContrato/text()')) numeroContrato
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/statusBS/text()')) statusBS
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/datahoraAbertura/text()')) datahoraAbertura
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/motivoReclamacao/text()')) motivoReclamacao
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/descricao/text()')) descricao
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/operador/text()')) operador
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/canal/text()')) canal
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/plano/text()')) plano
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/item/msisdnOrigem/text()')) msisdnOrigem
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/item/msisdnDestino/text()')) msisdnDestino
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/item/datahoraServico/text()')) datahoraServico
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/item/tipoTarifacao/text()')) tipoTarifacao
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/item/codigoServico/text()')) codigoServico
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/item/duracao/text()')) duracao
                ,XMLType.getStringVal(XMLType.extract(XMLType.createXML(to_char(i.xml_document)),'/root/USER/item/valor/text()')) valor
                from tbl_int_ppp_out i
                where idt_evento_negocio='WPP_crItemBS'
                and id_processamento=8127289
                    ) loop
                    
                    
            select count(1) into existeBS from tbl_ger_contestacao a where a.idt_nu_bs = linha.numeroBS;
            
            if(existeBS = 0) then
                begin
                    insert into hsid.tbl_contestacao values (linha.numeroBS);
                exception
                    when others then
                        DBMS_OUTPUT.put_line(SQLERRM);
                end;
                insert into tbl_ger_contestacao(idt_nu_bs, dat_abertura,  idt_msisdn,
                            id_usuario, id_motivo_contestacao, des_contestacao,
                            id_canal_origem_bs, idt_status_analise)
                values(
                    trim(linha.numeroBS), 
                    to_date(linha.datahoraAbertura, 'RRRRMMDDHH24MISS'),
                    
                    '55'||trim(linha.numeroContrato),
                    trim(linha.operador),
                    linha.motivoreclamacao,
                    substr(trim(replace(replace(linha.descricao, '<![CDATA[', ''), ']]>', '')), 1, 1000),
                    trim(linha.canal),
                    'A'
                );
                commit;
            end if;
            select count(1) into existeBS from tbl_ger_item_contestacao a where 
               IDT_NU_BS = linha.numeroBS AND SUB_ID = '55'||trim(linha.numeroContrato) AND 
               FNC_FORMATAMSISDNDESTINOCST(CALL_ID) = FNC_FORMATAMSISDNDESTINOCST(linha.msisdnDestino) 
		        AND TIMESTAMP = to_date(linha.datahoraServico, 'YYYYMMDDHH24MISS');
           
             
           
            if(existeBS = 0)  THEN  
                BEGIN
                    if(linha.tipoTarifacao = '0') then
                        transaction_type :=0;
                    else
                        begin
                            select transaction_type into transaction_type from tbl_ger_cdr a where
                            a.sub_id = '55'||trim(linha.numeroContrato) and
                            a.timestamp = to_date(linha.datahoraServico, 'YYYYMMDDHH24MISS') ;
                        exception
                            WHEN OTHERS THEN
                                transaction_type :=0;
                        end;
                    end if;
                    insert into tbl_ger_item_contestacao(
                           id_item_contestacao,  
                           idt_nu_bs,
                           sub_id, 
                           timestamp,
                           start_time, 
                           call_id, 
                           transaction_type,
                           idt_status_item_contestacao, 
                           call_duration,
                           vlr_contestado_principal                
                    )
                    values(
                        SEQ_ITEM_CONTESTACAO.NEXTVAL,
                        linha.numeroBS,
                        '55'||trim(linha.numeroContrato),
                        to_date(linha.datahoraServico, 'YYYYMMDDHH24MISS'),
                        (to_char(to_date(linha.datahoraServico, 'YYYYMMDDHH24MISS'), 'hh24')*3600) +  
                        (to_char(to_date(linha.datahoraServico, 'YYYYMMDDHH24MISS'), 'mi')*60) +  
                        to_char(to_date(linha.datahoraServico, 'YYYYMMDDHH24MISS'), 'ss'),
                        to_char(to_number(linha.msisdnDestino)),
                        transaction_type,
                        'A',
                        linha.duracao,
                        linha.valor / 100000 
                    );
                    commit;
                EXCEPTION
                    
                    WHEN OTHERS THEN 
                        COD_ERRO := SQLCODE;
                        DES_ERRO := SQLERRM;
                        insert into hsid.TBL_CONTESTACAO_PROBLEMA
                        values(
                        linha.id_processamento,
                        linha.xml_document,
                        DES_ERRO); 
                        DBMS_OUTPUT.put_line(des_erro);
                        commit;
                        
                        
                END;

            end if;
            
    end loop;
end;


