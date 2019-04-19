declare
    cursor cur_assinantes is
           select idt_msisdn, 
                  vlr_credito_bonus,
                  nvl((select vlr_credito_bonus
                         from tbl_rec_recargas r2
                        where r2.idt_msisdn = r1.idt_msisdn
                          and r2.tip_transacao = '08101'
                          and r2.dat_origem >= '1-abr-2006'
                          and r2.dat_origem <  '1-mai-2006'),0) as vlr_adiantamento,
                  nvl((select sum(vlr_credito_bonus)
                         from tbl_rec_recargas r2
                        where r2.idt_msisdn = r1.idt_msisdn
                          and r2.tip_transacao = '05024'
                          and r2.dat_origem >= '1-mai-2006'
                          and r2.dat_origem <  '1-jun-2006'),0) as vlr_correcao
             from tbl_rec_recargas r1
            where tip_transacao = '08001'
              and dat_origem >= '1-mai-2006';
begin
        	  insert into tbl_ger_envio_sms(id_registro, idt_msisdn, des_mensagem, num_prioridade,
       		dat_envio_sms, idt_status_processamento, tip_sms)
  			values(SEQ_ENVIO_SMS.NEXTVAL, '556184018906', 
			  'Inicio da correcao pula-pula: ' || to_char(sysdate, 'dd/mm/yyyy hh24:mi:ss'), 0, sysdate, 1, 'teste');
			commit;

    for v_assinante in cur_assinantes loop
        declare
            vlr_bonus  number(12,2) := null;
            vlr_insert number(12,2) := null;
        begin
			select vlr_credito_bonus
			  into vlr_bonus 
			  from ((select trunc(case when (p.vlr_max_credito_bonus is not null) and 
			                                (((nvl(t.num_segundos_total, 0) - 
			                                   nvl(t.num_segundos_ff, 0) - 
			                                   nvl(t.num_segundos_nao_bonificado, 0) - 
			                                   nvl(t.num_segundos_tup,0) - 
			                                   nvl(t.num_segundos_aigualb,0) - 
			                                   --nvl(t.num_segundos_estorno_fraude,0) - 
			                                   nvl(t.num_segundos_expurgo_fraude, 0))*b.vlr_bonus_minuto + 
			                                  (nvl(t.num_segundos_ff, 0)*b.vlr_bonus_minuto_ff))/60 > p.vlr_max_credito_bonus) 
			                           then p.vlr_max_credito_bonus 
			                           else ((nvl(t.num_segundos_total, 0) - 
			                                  nvl(t.num_segundos_ff, 0) - 
			                                  nvl(t.num_segundos_nao_bonificado, 0) - 
			                                  nvl(t.num_segundos_tup,0) - 
			                                  nvl(t.num_segundos_aigualb,0) - 
			                                  --nvl(t.num_segundos_estorno_fraude,0) - 
			                                  nvl(t.num_segundos_expurgo_fraude, 0))*b.vlr_bonus_minuto + 
			                                 (nvl(t.num_segundos_ff, 0)*b.vlr_bonus_minuto_ff))/60 
			                            end, 2) as vlr_credito_bonus
			           from tbl_pro_assinante a, 
			                tbl_apr_assinante ass,
			                tbl_pro_promocao p, 
			                tbl_pro_totalizacao_pula_pula t, 
			                tbl_pro_bonus_pula_pula b
			          where p.idt_promocao = a.idt_promocao 
			            and ass.idt_msisdn = a.idt_msisdn
			            and p.idt_categoria = 1 
			            and p.idt_promocao <> 7
			            and t.idt_msisdn = a.idt_msisdn 
			            and t.dat_mes = '200604'
			            and b.idt_codigo_nacional = substr(a.idt_msisdn,3,2)
			            and b.dat_fim_periodo is null
			            and b.idt_plano_preco = ass.idt_plano_preco
			            and a.idt_msisdn = v_assinante.idt_msisdn)
			        union all                         
			        (select trunc(case when (p.vlr_max_credito_bonus is not null) and 
			                                (((nvl(t.num_segundos_total, 0) - 
			                                   nvl(t.num_segundos_ff, 0) - 
			                                   nvl(t.num_segundos_nao_bonificado, 0) - 
			                                   nvl(t.num_segundos_tup,0) - 
			                                   nvl(t.num_segundos_aigualb,0) - 
			                                   --nvl(t.num_segundos_estorno_fraude,0) - 
			                                   nvl(t.num_segundos_expurgo_fraude, 0))*b.vlr_bonus_minuto + 
			                                  (nvl(t.num_segundos_ff, 0)*b.vlr_bonus_minuto_ff))/60) > 
			                                  decode(floor(p.vlr_max_credito_bonus + 
			                                  decode(floor(t.vlr_recargas/20), 0, 0, vlr_recargas-20))/100, 0, 100,p.vlr_max_credito_bonus + 
			                                  decode(floor(t.vlr_recargas/20), 0, 0, vlr_recargas-20)) 
			                           then decode(floor(p.vlr_max_credito_bonus + 
			                                             decode(floor(t.vlr_recargas/20), 0, 0, vlr_recargas-20))/100, 0, 100,p.vlr_max_credito_bonus + 
			                                             decode(floor(t.vlr_recargas/20), 0, 0, vlr_recargas-20) ) 
			                           else ((nvl(t.num_segundos_total, 0) - 
			                                  nvl(t.num_segundos_ff, 0) - 
			                                  nvl(t.num_segundos_plano_noturno, 0) - 
			                                  nvl(t.num_segundos_nao_bonificado, 0) - 
			                                  nvl(t.num_segundos_tup,0) - 
			                                  nvl(t.num_segundos_aigualb,0) - 
			                                  --nvl(t.num_segundos_estorno_fraude,0) - 
			                                  nvl(t.num_segundos_expurgo_fraude, 0))*b.vlr_bonus_minuto + 
			                                 (nvl(t.num_segundos_ff, 0)*b.vlr_bonus_minuto_ff))/60 
			                            end, 2) as vlr_credito_bonus
			           from tbl_pro_assinante a, 
			                tbl_apr_assinante ass,
			                tbl_pro_promocao p, 
			                tbl_pro_totalizacao_pula_pula t, 
			                tbl_pro_bonus_pula_pula b
			          where p.idt_promocao = a.idt_promocao 
			            and ass.idt_msisdn = a.idt_msisdn
			            and p.idt_promocao = 7
			            and t.idt_msisdn = a.idt_msisdn 
			            and t.dat_mes = '200604'
			            and b.dat_fim_periodo is null
			            and b.idt_plano_preco = ass.idt_plano_preco
			            and a.idt_msisdn = v_assinante.idt_msisdn
			            and b.idt_codigo_nacional = substr(a.idt_msisdn,3,2)));
            if(vlr_bonus - v_assinante.vlr_credito_bonus - v_assinante.vlr_adiantamento >= 0.10) 
                then dbms_output.put_line(v_assinante.idt_msisdn || '		' || 
                                          to_char(v_assinante.vlr_adiantamento) || '		' ||
                                          to_char(v_assinante.vlr_credito_bonus) || '		' ||
                                          to_char(vlr_bonus) || '		' ||
                                          to_char(vlr_bonus - 
                                                  v_assinante.vlr_credito_bonus -
                                                  v_assinante.vlr_adiantamento));
                     vlr_insert := vlr_bonus - v_assinante.vlr_credito_bonus - v_assinante.vlr_adiantamento - v_assinante.vlr_correcao;
                     insert into tbl_rec_fila_recargas
                     (
                        idt_msisdn,
                        tip_transacao,
                        dat_cadastro,
                        dat_execucao,
                        des_mensagem,
                        tip_sms,
                        ind_envia_sms,
                        idt_status_processamento,
                        vlr_credito_bonus,
                        num_prioridade
                     )
                     values
                     (
                        v_assinante.idt_msisdn,
                        '05024',
                        sysdate,
                        '2-mai-2006',
                        'BrTGSM informa: Voce recebeu R$' || to_char(vlr_insert) || ' adicionais pela Promocao Pula-Pula.',
                        'PULAPULA',
                        1,
                        4,
                        vlr_insert,
                        1
                     );
                     commit;
                else dbms_output.put_line(v_assinante.idt_msisdn || '		' || 'OK');
            end if;
        exception
            when no_data_found then
                dbms_output.put_line(v_assinante.idt_msisdn || '		' || 
                                     to_char(v_assinante.vlr_adiantamento) || '		' ||
                                     to_char(v_assinante.vlr_credito_bonus) || '		' ||
                                     'NO_DATA_FOUND');        
        end;
    end loop;
            insert into tbl_ger_envio_sms(id_registro, idt_msisdn, des_mensagem, num_prioridade,
       		dat_envio_sms, idt_status_processamento, tip_sms)
  			values(SEQ_ENVIO_SMS.NEXTVAL, '556184018906', 
			  'Fim da correcao pula-pula: ' || to_char(sysdate, 'dd/mm/yyyy hh24:mi:ss'), 0, sysdate, 1, 'teste');
			declare 
				c  number(12,2):=0;
				v  number(12,2):=0;
			begin
				select count(1), sum(vlr_credito_bonus) into c, v 
				from
				tbl_rec_fila_recargas
				where
				tip_transacao = '05024' and
				dat_cadastro >= '02-mai-2006';
            insert into tbl_ger_envio_sms(id_registro, idt_msisdn, des_mensagem, num_prioridade,
       		dat_envio_sms, idt_status_processamento, tip_sms)
  			values(SEQ_ENVIO_SMS.NEXTVAL, '556184018906', 
			  'Totais  qtd:' || to_char(c, '999,999') || ' vlr:' || to_char(nvl(v,0), '999,999,990.00'), 0, 
			  sysdate, 1, 'teste');
			commit;
			end;
			
end;


