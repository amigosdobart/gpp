SELECT  substr(x.idt_msisdn, 3,2)  || ';' || 
        x.id_grupo || ';' ||
        x.nom_grupo || ';' ||
        x.id_tipo_extrato || ';' || 
        x.des_tipo_extrato || ';' || 
        count(*)
  from
  (
  select distinct a.idt_msisdn,
        c.id_grupo,
        c.nom_grupo,
        d.id_tipo_extrato,
        d.des_tipo_extrato
   
  FROM tbl_ppp_historico_extrato a, tbl_ppp_grupo_usuario b, tbl_ppp_grupo c,
  tbl_ppp_tipo_extrato d
  where
  a.dat_historico >= '01-nov-2005' and a.dat_historico <= '30-nov-2005' and
  b.id_usuario = a.id_usuario and
  c.id_grupo = b.id_grupo and
  d.id_tipo_extrato = a.id_tipo_extrato
) x  
  group by substr(x.idt_msisdn, 3,2), 
        x.id_grupo,
        x.nom_grupo,
        x.id_tipo_extrato, 
        x.des_tipo_extrato
        
        
        
        
  
