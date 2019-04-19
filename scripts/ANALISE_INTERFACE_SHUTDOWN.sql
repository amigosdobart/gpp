/* Formatted on 2007/06/28 17:21 (Formatter Plus v4.8.7) */
SELECT dat_envio, SUM (desativado) AS desativados,
       SUM (nao_desativado) AS nao_desativados
  FROM (SELECT dat_envio, DECODE ("shutdown", 1, 1, 0) desativado,
               DECODE ("shutdown", NULL, 1, 0) nao_desativado
          FROM (SELECT (SELECT distinct 1
                          FROM tbl_apr_eventos a
                         WHERE a.tip_operacao = 'DESATIVACAO'
                           AND a.dat_aprovisionamento > '16-jun-2007'
                           AND a.idt_msisdn =
                                  XMLTYPE.getstringval
                                     (XMLTYPE.EXTRACT
                                         (XMLTYPE.createxml
                                                        (TO_CHAR (xml_document)
                                                        ),
                                          '/GPPStatus/msisdn/text()'
                                         )
                                     )) "shutdown",
                       trunc (dat_cadastro) AS dat_envio
                  FROM tbl_int_ppp_out o
                 WHERE idt_evento_negocio = 'GPP_atSubscriber'
                   AND dat_cadastro >= '16-jun-2007'
                   --AND idt_status_processamento = 'Y'
                   AND xml_document LIKE '%<status>S%'))
GROUP BY DAT_ENVIO

