/* 
	Apos estar operacoes, atualizar todos os mapeamentos do GPP duas vezes, 
	sem esvaziar o conteudo anterior.
*/

insert into tbl_rec_canal(id_canal,des_canal)
values('11', 'BANCO FIXA')
/
insert into tbl_rec_origem(id_origem,
                           des_origem,
                           ind_modificar_data_exp,
                           id_canal,
                           ind_ativo,
                           tip_lancamento,
                           idt_classificacao_recarga,
                           ind_disponivel_portal,
                           ind_ligmix)
select o1.id_origem,
       o1.des_origem,
       o1.ind_modificar_data_exp,
       '11' as id_canal,
       o1.ind_ativo,
       o1.tip_lancamento,
       o1.idt_classificacao_recarga,
       o1.ind_disponivel_portal,
       o1.ind_ligmix
  from tbl_rec_origem o1
 where id_canal = '00'
/
insert into tbl_rec_origem_categoria(id_canal,id_origem,idt_categoria)
select o.id_canal,o.id_origem,c.idt_categoria
  from tbl_rec_origem o,
       tbl_ger_categoria c
 where o.id_canal = '11'
   and c.idt_categoria in (2,4)
/
insert into tbl_rec_origem_sistema(id_canal,id_origem,id_sistema_origem)
select id_canal,id_origem,'BCO'
  from tbl_rec_origem
 where id_canal = '11'
/

