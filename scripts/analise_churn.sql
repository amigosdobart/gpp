select idt_status,
    (select idt_promocao from tbl_pro_assinante@prepr_gppuser b where idt_msisdn = '554284021133')
    promocao_atual,
    (SELECT idt_antigo_campo
  FROM tbl_apr_eventos@prepr_gppuser a
 WHERE idt_msisdn = '554284021133'
   AND tip_operacao = 'PROMOCAO_SAIDA'
   AND dat_aprovisionamento >= TO_DATE ('01/02/2008', 'dd/mm/yyyy')
   AND ROWNUM = 1 ) promocao_antiga
   from tbl_apr_assinante@prepr_gppuser
   where
   idt_msisdn = '554284021133'
    
    
    
    



 
