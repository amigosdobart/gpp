/*

SELECT a.sub_id, a.dat_ativacao, a.val_credito, a.idt_promocao
  FROM tbl_tmp_previa a
  order by val_credito desc
  
  create table tbl_tmp_previa_resumo(
    idt_promocao number(1),
    dat_credito date,
    val_credito number(13,3))
    
*/    
declare
    c number(4) :=0;
    valor_credito number(13,3) :=0;
    data_credito date;
    DIA VARCHAR2(2);
begin
        for linha in (select * from tbl_tmp_previa)loop
            if linha.idt_promocao = 0 then
                data_credito := to_date('17/04/2005', 'dd/mm/yyyy');
                valor_credito := linha.val_credito;
            elsif linha.idt_promocao = 1 then
                if(linha.val_credito > 1000) then
                    valor_credito := 1000;
                else
                    valor_credito := linha.val_credito;
                end if;
            elsif linha.idt_promocao = 2 then
                if(linha.val_credito > 500) then
                    valor_credito := 500;
                else
                    valor_credito := linha.val_credito;
                end if;
            elsif linha.idt_promocao = 4 then
                if(linha.val_credito > 300) then
                    valor_credito := 300;
                else
                    valor_credito := linha.val_credito;
                end if;
            end if;

            DIA := TO_CHAR(linha.DAT_ATIVACAO, 'DD');
            IF(DIA IN ('01', '02', '03', '04', '05', '06')) THEN
                IF(linha.idt_promocao = 1) THEN
                    data_credito :=  TO_DATE('01042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 2) THEN
                    data_credito :=  TO_DATE('06042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 4) THEN
                    data_credito :=  TO_DATE('11042005', 'DDMMYYYY');
                END IF;
    
            ELSIF (DIA IN ('07', '08', '09', '10', '11', '12')) THEN
                IF(linha.idt_promocao = 1) THEN
                    data_credito :=  TO_DATE('02042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 2) THEN
                    data_credito :=  TO_DATE('07042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 4) THEN
                    data_credito :=  TO_DATE('12042005', 'DDMMYYYY');
                END IF;
            ELSIF (DIA IN ('13', '14', '15', '16', '17', '18')) THEN
                IF(linha.idt_promocao = 1) THEN
                    data_credito :=  TO_DATE('03042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 2) THEN
                    data_credito :=  TO_DATE('08042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 4) THEN
                    data_credito :=  TO_DATE('13042005', 'DDMMYYYY');
                END IF;
            ELSIF (DIA IN ('19', '20', '21', '22', '23', '24')) THEN
                IF(linha.idt_promocao = 1) THEN
                    data_credito :=  TO_DATE('04042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 2) THEN
                    data_credito :=  TO_DATE('09042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 4) THEN
                    data_credito :=  TO_DATE('14042005', 'DDMMYYYY');
                END IF;
            ELSIF (DIA IN ('25', '26', '27', '28', '29', '30', '31')) THEN
                IF(linha.idt_promocao = 1) THEN
                    data_credito :=  TO_DATE('05042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 2) THEN
                    data_credito :=  TO_DATE('10042005', 'DDMMYYYY');
                ELSIF (linha.idt_promocao = 4) THEN
                    data_credito :=  TO_DATE('15042005', 'DDMMYYYY');
                END IF;
            END IF;
    
            update tbl_tmp_previa_resumo
            set
                valor = valor + valor_credito
            where
                idt_promocao = linha.idt_promocao and
                dat_credito = dat_credito;
            if sql%rowcount = 0 then
                insert into tbl_tmp_previa_resumo(idt_promocao, dat_credito, val_credito)
                values(linha.idt_promocao, data_credito, valor_credito);
            end if;
        end loop;
        commit;
    
end;
