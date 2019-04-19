declare
begin
 dbms_random.initialize(123);
 for x in 0..100 loop
     insert into tbl_rel_sem_recargas
     values('556184019' || trim(to_char(x, '000')), 2, ABS(MOD(dbms_random.random(), 9) ),ABS(MOD(dbms_random.random(), 60) ),
     sysdate);
 end loop;
end;

DELETE FROM tbl_rel_sem_recargas


 SELECT a.idt_msisdn, a.idt_status, a.idt_plano_preco,
       a.num_dias_sem_recarga, a.dat_ultima_recarga
  FROM gpp.tbl_rel_sem_recargas a
  


declare
begin  
    dbms_random.initialize(123); 
    DBMS_OUTPUT.put_line(TO_CHAR(dbms_random.random()));
end;


SELECT trim(to_char(1, '00')) FROM DUAL

