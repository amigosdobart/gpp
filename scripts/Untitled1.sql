declare
    existe number(1);
    ccount number(3);
BEGIN
/*  for lin in (SELECT a.sub_id,
                       RECALCULA_CHAMADA(A.call_duration, A.call_value, A.interconnection_cost,
                       A.airtime_cost, A.TAX) valor_recalculado
                  FROM tbl_ger_cdr@prepr_gppuser a
                 WHERE airtime_cost <> 0
                   AND orig_called_number IS NOT NULL
                   AND tip_chamada NOT LIKE 'INT%'
                   AND tip_chamada NOT LIKE 'IVR%'
                   AND tip_chamada NOT LIKE 'ESP_EVENTO%'
                   AND TIMESTAMP >= TO_DATE ('25/09/2004', 'dd/mm/yyyy')
                   AND TIMESTAMP < TO_DATE ('15/10/2004', 'dd/mm/yyyy')
                   ) loop
                   */
       for lin in (SELECT a.sub_id,
                       RECALCULA_CHAMADA(A.call_duration, A.call_value, A.inter,
                       A.airtime, A.TAX) valor_recalculado
                  FROM TBL_TEMP_AJUSTE_AUX a
                   ) loop            
                   
                   
        select count(1) into existe from tbl_tmp_ajuste a where a.sub_id = lin.sub_id;
        
        if existe = 1
        then
            update tbl_tmp_ajuste set valor = valor + lin.valor_recalculado, contador = contador + 1 where sub_id = lin.sub_id;
        else
            insert into tbl_tmp_ajuste values (lin.sub_id, lin.valor_recalculado,1);
        end if;
        ccount := ccount +1;
        if ccount >1000
        then
            commit;
            ccount :=0;
        end if;        
    end loop;
    commit;    
end;


