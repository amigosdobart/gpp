truncate table tbl_tmp_churn;
insert into TBL_TMP_CHURN (nu_msisdn, nu_imei, dt_inclusao)
select nu_msisdn, nu_imei, dt_inclusao
  from (select nu_imei
              ,nu_msisdn
              ,dt_inclusao
              ,rank()over(partition by nu_imei order by dt_inclusao desc) as colocacao 
          from (select nu_imei
                      ,nu_msisdn
                      ,dt_inclusao
                      ,rank()over(partition by nu_imei,nu_msisdn order by dt_inclusao desc) as rank
                  from hsid_cliente_historico
                 where dt_inclusao > '01-SEt-2005'
                order by nu_imei,nu_msisdn,dt_inclusao desc
                )
         where rank = 1
        )
 where colocacao = 2;
 commit;
