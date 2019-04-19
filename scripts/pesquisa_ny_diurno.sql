SELECT a.sub_id, a.timestamp, PROC_HORA_TECNOMEN(a.start_time),
    
       a.call_duration, A.call_value/100000, 
       a.idt_modulacao, a.tip_cdr, A.rate_name,
       to_char(TIMESTAMP, 'D')
  FROM 
    tbl_ger_cdr a, 
    tbl_ger_tip_transacao_tecnomen B
  WHERE
  TIMESTAMP >= TO_DATE('27/09/2004', 'DD/MM/YYYY') AND
  TIMESTAMP <= TO_DATE('22/10/2004', 'DD/MM/YYYY') AND
  substr(a.sub_id, 1,4) in ('5551', '5553', '5554', '5555', '5547', '5548', '5549', 
  '5541', '5542', '5543', '5544', '5545', '5546') and
  to_char(TIMESTAMP, 'D') <> '1' and
  (a.start_time between 72000 and 86400 or
  a.start_time between 0 and 28799) and

  A.profile_id = 2 AND
  A.tip_cdr = 'PP' AND
  a.tip_chamada in ('VC1MF-------',
                    'RCVC1MF-----',
                    'RCVC1MFDSL2-',
                    'RCVC1MFDSL3-',
                    'RCVC1MFI----',
                    'RCVC1MFIDSL2',
                    'RCVC1MFIDSL3',
                    'VC1MFI------',
                    'VC1MFR------',
                    'VC1MFRI-----',
                    'VC1MFRII----',
                    'VCMM--------',
                    'RCVCMM------',
                    'RCVCMMDSL2--',
                    'RCVCMMDSL3--',
                    'RCVCMMI-----',
                    'RCVCMMIDSL2-',
                    'RCVCMMIDSL3-',
                    'VCMMI-------',
                    'VCMMR-------',
                    'VCMMRI------',
                    'VCMMRII-----',
                    'UNICO_VC1FF-',
                    'UNICO_VC1FM-') and 
  a.idt_modulacao <> '-' and 
  A.transaction_type = B.transaction_type AND
  B.idt_sentido = 'O'
  ORDER BY 2, 3
  
  
  select PROC_HORA_TECNOMEN(71999) from dual --20:00:00
  select PROC_HORA_TECNOMEN(86399) from dual -- 23:59:59
  select PROC_HORA_TECNOMEN(28799) from dual -- 07:59:59


 select PROC_HORA_TECNOMEN(64800) from dual --18:00:00
  
  
  select to_char(TO_DATE('27/09/2004', 'DD/MM/YYYY'), 'D') from dual
  
  select * from tbl_ger_cdr

/*****************
Diurno 3
*****************/

SELECT a.sub_id,    sum(a.call_duration), sum(A.call_value)/100000
  FROM 
    tbl_ger_cdr a, 
    tbl_ger_tip_transacao_tecnomen B
  WHERE
  TIMESTAMP >= TO_DATE('27/09/2004', 'DD/MM/YYYY') AND
  TIMESTAMP <= TO_DATE('22/10/2004', 'DD/MM/YYYY') AND
  substr(a.sub_id, 1,4) in ('5551', '5553', '5554', '5555') and
  (to_char(TIMESTAMP, 'D') = '1' or
   trunc(TIMESTAMP) =TO_DATE('12/10/2004', 'DD/MM/YYYY') ) and
  a.start_time between 28800 and 71999 and
  A.profile_id = 2 AND
  A.tip_cdr = 'PP' AND
  a.tip_chamada in ('VC1MF-------',
                    'RCVC1MF-----',
                    'RCVC1MFDSL2-',
                    'RCVC1MFDSL3-',
                    'RCVC1MFI----',
                    'RCVC1MFIDSL2',
                    'RCVC1MFIDSL3',
                    'VC1MFI------',
                    'VC1MFR------',
                    'VC1MFRI-----',
                    'VC1MFRII----',
                    'VCMM--------',
                    'RCVCMM------',
                    'RCVCMMDSL2--',
                    'RCVCMMDSL3--',
                    'RCVCMMI-----',
                    'RCVCMMIDSL2-',
                    'RCVCMMIDSL3-',
                    'VCMMI-------',
                    'VCMMR-------',
                    'VCMMRI------',
                    'VCMMRII-----',
                    'UNICO_VC1FF-',
                    'UNICO_VC1FM-') and 
  a.idt_modulacao <> '-' and 
  A.transaction_type = B.transaction_type AND
  B.idt_sentido = 'O'
  group by sub_id
  
  
/*****************
Diurno 2
*****************/

SELECT a.sub_id,    sum(a.call_duration), sum(A.call_value)/100000
  FROM 
    tbl_ger_cdr a, 
    tbl_ger_tip_transacao_tecnomen B
  WHERE
  TIMESTAMP >= TO_DATE('27/09/2004', 'DD/MM/YYYY') AND
  TIMESTAMP <= TO_DATE('22/10/2004', 'DD/MM/YYYY') AND
  substr(a.sub_id, 1,4) in ('5551', '5553', '5554', '5555') and
  to_char(TIMESTAMP, 'D') <> '1' and
  a.start_time between 28800 and 71999 and
  A.profile_id = 2 AND
  A.tip_cdr = 'PP' AND
  a.tip_chamada in ('VC1MF-------',
                    'RCVC1MF-----',
                    'RCVC1MFDSL2-',
                    'RCVC1MFDSL3-',
                    'RCVC1MFI----',
                    'RCVC1MFIDSL2',
                    'RCVC1MFIDSL3',
                    'VC1MFI------',
                    'VC1MFR------',
                    'VC1MFRI-----',
                    'VC1MFRII----',
                    'VCMM--------',
                    'RCVCMM------',
                    'RCVCMMDSL2--',
                    'RCVCMMDSL3--',
                    'RCVCMMI-----',
                    'RCVCMMIDSL2-',
                    'RCVCMMIDSL3-',
                    'VCMMI-------',
                    'VCMMR-------',
                    'VCMMRI------',
                    'VCMMRII-----',
                    'UNICO_VC1FF-',
                    'UNICO_VC1FM-') and 
  a.idt_modulacao <> '-' and 
  A.transaction_type = B.transaction_type AND
  B.idt_sentido = 'O'
  group by sub_id
/*****************
Diurno 1
*****************/

SELECT a.sub_id,    sum(a.call_duration), sum(A.call_value)/100000
  FROM 
    tbl_ger_cdr a, 
    tbl_ger_tip_transacao_tecnomen B
  WHERE
  TIMESTAMP >= TO_DATE('27/09/2004', 'DD/MM/YYYY') AND
  TIMESTAMP <= TO_DATE('22/10/2004', 'DD/MM/YYYY') AND
  substr(a.sub_id, 1,4) in ('5551', '5553', '5554', '5555', '5547', '5548', '5549', 
  '5541', '5542', '5543', '5544', '5545', '5546') and
  to_char(TIMESTAMP, 'D') <> '1' and
  (a.start_time between 72000 and 86400 or
  a.start_time between 0 and 28799) and

  A.profile_id = 2 AND
  A.tip_cdr = 'PP' AND
  a.tip_chamada in ('VC1MF-------',
                    'RCVC1MF-----',
                    'RCVC1MFDSL2-',
                    'RCVC1MFDSL3-',
                    'RCVC1MFI----',
                    'RCVC1MFIDSL2',
                    'RCVC1MFIDSL3',
                    'VC1MFI------',
                    'VC1MFR------',
                    'VC1MFRI-----',
                    'VC1MFRII----',
                    'VCMM--------',
                    'RCVCMM------',
                    'RCVCMMDSL2--',
                    'RCVCMMDSL3--',
                    'RCVCMMI-----',
                    'RCVCMMIDSL2-',
                    'RCVCMMIDSL3-',
                    'VCMMI-------',
                    'VCMMR-------',
                    'VCMMRI------',
                    'VCMMRII-----',
                    'UNICO_VC1FF-',
                    'UNICO_VC1FM-') and 
  a.idt_modulacao <> '-' and 
  A.transaction_type = B.transaction_type AND
  B.idt_sentido = 'O'
  group by sub_id
  
