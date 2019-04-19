SELECT 0,
    case
        when vlr_limite_credito > 0 then
            (a.vlr_utilizado/a.vlr_limite_credito) * 100
        else
            100
    end as percentual,
       a.ind_bloqueio
  FROM tbl_rec_limite_credito a;
  
  
  INSERT INTO gpp.tbl_alr_alarmes(
id_alarme, nom_alarme, dat_prevista_execucao,
       dat_ultima_execucao, idt_status,
       ind_envio_trap_snmp_alerta, ind_envio_trap_snmp_falha,
       ind_envio_sms_alerta, ind_envio_sms_falha, idt_dia,
       idt_mes, idt_ano, idt_dia_semana, idt_hora, idt_minuto,
       vlr_minimo_alerta, vlr_minimo_falha, vlr_maximo_alerta,
       vlr_maximo_falha, idt_atraso_maximo_falha,
       idt_atraso_maximo_alerta, ind_envio_email_alerta,
       ind_envio_email_falha, sql_busca_contador, des_motivo,
       num_dias_historico  
  )

select 'LIM_CRT_' || TO_CHAR(a.cod_revenda_sap),
       'Limite de credito ' || a.nom_revenda,
       null,
       null,
       'OK',
       0,0,1,1,'*','*', '*', '*', '*', '30', -1, -1,80, 90, -1, -1, 
       1, 1, 
       'SELECT 0,
    case
        when vlr_limite_credito > 0 then
            (a.vlr_utilizado/a.vlr_limite_credito) * 100
        else
            100
    end as percentual
  FROM tbl_rec_limite_credito a where cod_revenda_sap = ' ||  TO_CHAR(a.cod_revenda_sap) , 
       '', 7 

from 
tbl_rec_limite_credito a
where
cod_revenda_sap != '3000000023';

insert into tbl_alr_grupo_alarmes 
select 31, 'LIM_CRT_' || TO_CHAR(a.cod_revenda_sap)
from 
tbl_rec_limite_credito a
where
cod_revenda_sap != '3000000023';

COMMIT;



select * from 
tbl_rec_limite_credito a

select * from tbl_alr_alarmes a, tbl_alr_grupo_alarmes b
where
a.id_alarme = b.id_alarme and
b.id_grupo = 31;

select * from tbl_alr_eventos_alarme a, tbl_alr_grupo_alarmes b
where
a.id_alarme = b.id_alarme and
b.id_grupo = 31


update  tbl_alr_alarmes a
set
a.idt_status = 'OK',
A.des_motivo = '',
a.dat_ultima_execucao = sysdate 
where
a.id_alarme in (select b.id_alarme from tbl_alr_grupo_alarmes b where b.id_grupo = 31);


commit;

insert into TBL_ALR_ALARME_EMAIL

select a.id_alarme, 'luciano.dourado@brasiltelecom.com.br'
 from tbl_alr_alarmes a, tbl_alr_grupo_alarmes b
where
a.id_alarme = b.id_alarme and
b.id_grupo = 31;

joao.ferreira@brasiltelecom.com.br
luciano.dourado@brasiltelecom.com.br

556184267517


insert into TBL_ALR_ALARME_ASSINANTES
select a.id_alarme, '556184018906'
 from tbl_alr_alarmes a, tbl_alr_grupo_alarmes b
where
a.id_alarme = b.id_alarme and
b.id_grupo = 31;

COMMIT



select *
 from tbl_alr_alarme_email a, tbl_alr_grupo_alarmes b
where
a.id_alarme = b.id_alarme and
b.id_grupo = 31;
