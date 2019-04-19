
insert into tbl_int_conciliacao_out
                            (IDT_NSU,
						    IDT_NSU_INSTITUICAO,
						    ID_TERMINAL,
						    TIP_TRANSACAO,
						    NOM_OPERADOR,
						    ID_SISTEMA_ORIGEM,
						    DAT_TRANSACAO,
						    VLR_TRANSACAO,
						    IDT_MSISDN,
						    IDT_STATUS_PROCESSAMENTO,
						    ID_RECARGA,
						    IDT_EMPRESA,
						    DAT_CARGA)

select /*+ index(a XIE9TBL_REC_RECARGAS) */
						    a.id_recarga IDT_NSU,
						    a.idt_nsu_instituicao IDT_NSU_INSTITUICAO,
						    a.idt_terminal ID_TERMINAL,
						    a.tip_transacao TIP_TRANSACAO,
						    a.nom_operador NOM_OPERADOR,
						    a.id_sistema_origem ID_SISTEMA_ORIGEM,
						    a.dat_inclusao DAT_TRANSACAO,
						    a.vlr_pago VLR_TRANSACAO,
						    a.idt_msisdn IDT_MSISDN,
						    'N' IDT_STATUS_PROCESSAMENTO,
						    a.id_recarga ID_RECARGA,
						    'SMPE',
						    sysdate
 from tbl_tmp_bradesco b, tbl_rec_recargas a
where
a.idt_msisdn = b.idt_msisdn and
a.dat_origem = b.dat_contestacao and
a.tip_transacao = '00237' and
a.idt_nsu_instituicao = b.idt_nsu
