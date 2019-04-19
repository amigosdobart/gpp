declare
begin
        for linha in (select * from (
        select '556184068869' numero from dual union all
        select '556184053901' numero from dual union all
        select '556184029090' numero from dual union all
        select '556184021816' numero from dual union all
        select '556184022761' numero from dual union all
        select '556184021662' numero from dual union all
        select '556184066131' numero from dual union all
        select '556184023340' numero from dual union all
        select '556184060998' numero from dual union all
        select '556184080136' numero from dual union all
        select '556184080116' numero from dual union all
        select '556184080095' numero from dual union all
        select '556184080084' numero from dual union all
        select '556184080068' numero from dual union all
        select '556184032934' numero from dual union all
        select '556184012792' numero from dual union all
        select '556184016797' numero from dual)) loop
        insert into tbl_ger_cdr
        SELECT a.sequence_number, linha.numero, add_months(a.timestamp, 1), a.start_time,
               a.call_duration, a.service_type, a.imsi, a.account_number,
               a.transaction_type, a.call_id, a.final_account_balance,
               a.call_origin_id, a.sub_class, a.profile_id,
               a.final_tariff_plan_id, a.expiry_type, a.orig_called_number,
               a.cli_out, a.rate_name, a.discount_type,
               a.percent_discount_applied, a.external_transaction_type,
               a.tariffing_method, a.operator_id, a.location_id, a.cell_name,
               a.carrier_prefix, a.sm_delivery_method, a.sm_package_id,
               a.airtime_cost, a.interconnection_cost, a.tax,
               a.destination_name, a.ff_discount, a.call_reference,
               a.additional_calling_partynumber, a.redirecting_number,
               a.calling_party_category, a.home_msc_address,
               a.visited_msc_address, a.orig_sub_id, a.bonus_type,
               a.bonus_percentage, a.bonus_amount, a.service_key,
               a.correlation_id, a.peak_time, a.application_type,
               a.billing_event_id, a.service_provider_id, a.message_size,
               a.primary_message_content_type, a.message_class,
               a.recipient_address, a.recipient_type,
               a.originator_vas_tariff_class, a.originator_type, a.tax_percent,
               a.num_csp, a.idt_plano, a.tip_chamada, a.tip_deslocamento,
               a.idt_modulacao, a.tip_cdr, a.idt_operadora_origem,
               a.idt_localidade_origem, a.idt_operadora_destino,
               a.idt_localidade_destino, a.idt_hora_transacao_cgw,
               a.idt_data_transacao_cgw, a.account_status, a.idt_area,
               a.dat_importacao_cdr, a.account_balance_delta,
               a.periodic_balance, a.periodic_balance_delta, a.bonus_balance,
               a.bonus_balance_delta, a.sm_balance, a.sm_balance_delta,
               a.data_balance, a.data_balance_delta, a.call_type_id, a.cost,
               a.number_of_vouchers, a.vtt_queue, a.vtt_tariff_plan_id,
               a.initial_account_balance, a.initial_data_balance
        FROM TBL_GER_CDR A
        WHERE 
        SUB_ID = '556184017942' AND
        RATE_NAME LIKE '%RLOCAL%';
        commit;
end loop;
end;







