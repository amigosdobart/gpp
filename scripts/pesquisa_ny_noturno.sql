SELECT a.sub_id, a.timestamp, PROC_HORA_TECNOMEN(a.start_time),
    
       a.call_duration, A.call_value/100000, 
       a.idt_modulacao, a.tip_cdr, A.rate_name
  FROM 
    tbl_ger_cdr a, 
    tbl_ger_tip_transacao_tecnomen B
  WHERE
  TIMESTAMP >= TO_DATE('27/09/2004', 'DD/MM/YYYY') AND
  TIMESTAMP <= TO_DATE('22/10/2004', 'DD/MM/YYYY') AND
  to_char(TIMESTAMP, 'D') <> '1' and
  a.start_time between 28800 and 64799 and
  A.profile_id = 3 AND
  A.tip_cdr = 'PP' AND
  a.idt_modulacao <> '-' and 
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
  A.transaction_type = B.transaction_type AND
  B.idt_sentido = 'O'
  ORDER BY 2, 3
  
  
  select PROC_HORA_TECNOMEN(72000) from dual --20:00:00
  select PROC_HORA_TECNOMEN(86399) from dual -- 23:59:59
  select PROC_HORA_TECNOMEN(28799) from dual -- 07:59:59


 select PROC_HORA_TECNOMEN(64800) from dual --18:00:00
  
  
  select to_char(TO_DATE('27/09/2004', 'DD/MM/YYYY'), 'D') from dual
  
  
SELECT a.sub_id, sum(a.call_duration), sum(A.call_value)/100000
  FROM 
    tbl_ger_cdr a, 
    tbl_ger_tip_transacao_tecnomen B
  WHERE
  TIMESTAMP >= TO_DATE('27/09/2004', 'DD/MM/YYYY') AND
  TIMESTAMP <= TO_DATE('22/10/2004', 'DD/MM/YYYY') AND
  to_char(TIMESTAMP, 'D') <> '1' and
  a.start_time between 28800 and 64799 and
  A.profile_id = 3 AND
  A.tip_cdr = 'PP' AND
  a.idt_modulacao <> '-' and 
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
  A.transaction_type = B.transaction_type AND
  B.idt_sentido = 'O'
  group by sub_id
