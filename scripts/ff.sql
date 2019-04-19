SELECT *
  FROM (SELECT idt_msisdn,
               (SELECT COUNT (1)
                  FROM tbl_tmp_ff b
                 WHERE b.idt_amigo = a.idt_msisdn
                   AND EXISTS (
                          SELECT 1
                            FROM tbl_tmp_ff c
                           WHERE c.idt_msisdn = b.idt_amigo 
                             
                             AND c.idt_amigo = a.idt_msisdn)) amigos
          FROM (SELECT DISTINCT idt_msisdn
                           FROM tbl_tmp_ff) a)
 WHERE amigos = 13
 group by amigos




    
    

