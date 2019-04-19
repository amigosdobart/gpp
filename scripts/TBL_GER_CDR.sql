SELECT a.sub_id, a.timestamp, PROC_HORA_TECNOMEN(a.start_time),
       a.call_duration, A.call_value/100000, 
       a.idt_modulacao, a.tip_cdr, A.rate_name
  FROM 
    tbl_ger_cdr a, 
    tbl_ger_tip_transacao_tecnomen B
  WHERE
  TIMESTAMP >= TO_DATE('27/09/2004', 'DD/MM/YYYY') AND
  TIMESTAMP <= TO_DATE('22/10/2004', 'DD/MM/YYYY') AND
  a.start_time between 28800 and 64799 and
  A.profile_id = 3 AND
  A.tip_cdr = 'PP' AND
  a.idt_modulacao <> '-' and 
  A.transaction_type = B.transaction_type AND
  B.idt_sentido = 'O'
  ORDER BY 3, 4
  
  
  select PROC_HORA_TECNOMEN(72000) from dual --20:00:00
  select PROC_HORA_TECNOMEN(86399) from dual -- 23:59:59
  select PROC_HORA_TECNOMEN(28799) from dual -- 07:59:59


 select PROC_HORA_TECNOMEN(64800) from dual --18:00:00
  
