package com.brt.gpp.aplicacoes.promocao.sumarizacaoPulaPula;
 
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela execucao do processo de Sumarizacao do Pula-Pula.
 *
 *	@author	Magno Batista Corrêa
 *	@since	18/04/2005 (dd/mm/yyyy)
 *
 *	
 *	Sumariza por Promoção e CN
 *	@author	Marcelo Alves Araujo
 *	@since	08/01/2007
 */
public final class SumarizacaoPulaPulaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    // Conexao com o banco de dados.
    private PREPConexao conexaoPrep;
    private Consulta	consulta;
    
    private static final String SQL_INSERE  = "INSERT INTO tbl_rel_pula_pula                                           "
                                            + "(DAT_PROCESSAMENTO,                                                     "
                                            + " IDT_PROMOCAO,                                                          "
                                            + " IDT_CODIGO_NACIONAL,                                                   "
                                            + " IDT_PLANO_PRECO,                                                       "
                                            + " DAT_INICIAL_EXECUCAO,                                                  "
                                            + " DAT_FINAL_EXECUCAO,                                                    "
                                            + " QTD_ASSINANTES,                                                        "
                                            + " QTD_STATUS_0,                                                          "
                                            + " QTD_STATUS_1,                                                          "
                                            + " QTD_STATUS_2,                                                          "
                                            + " QTD_STATUS_3,                                                          "
                                            + " QTD_STATUS_4,                                                          "
                                            + " QTD_STATUS_5,                                                          "
                                            + " NUM_SEGUNDOS_TOTAL,                                                    "
                                            + " NUM_SEGUNDOS_NORMAL,                                                   "
                                            + " NUM_SEGUNDOS_FF,                                                       "
                                            + " NUM_SEGUNDOS_NAO_BONIFICADO,                                           "
                                            + " NUM_SEGUNDOS_PLANO_NOTURNO,                                            "
                                            + " NUM_SEGUNDOS_PLANO_DIURNO,                                             "
                                            + " NUM_SEGUNDOS_DURAC_EXCEDIDA,                                           "
                                            + " NUM_SEGUNDOS_EXPURGO_FRAUDE,                                           "
                                            + " NUM_SEGUNDOS_ESTORNO_FRAUDE,                                           " 
                                            + " NUM_SEGUNDOS_TUP,                                                      "
                                            + " NUM_SEGUNDOS_AIGUALB,                                                  "
                                            + " NUM_SEGUNDOS_ATH,                                                      "
                                            + " NUM_SEGUNDOS_MOVEL_NAO_BRT,                                            " 
                                            + " NUM_SEGUNDOS_FALEGRATIS,                                               "
                                            + " NUM_SEGUNDOS_BONUS,                                                    "  
                                            + " NUM_SEGUNDOS_CT,                                                       "  
                                            + " VLR_RECARGAS,                                                          "
                                            + " VLR_BONUS_TOTAL,                                                       "
                                            + " VLR_BONUS_ADIANTAMENTO,                                                "
                                            + " VLR_SALDO_PRINCIPAL,                                                   "
                                            + " VLR_SALDO_BONUS,                                                       "
                                            + " VLR_SALDO_SMS,                                                         "
                                            + " VLR_SALDO_GPRS,                                                        "
                                            + " VLR_SALDO_CONCESSAO_FRACIONADA)                                        "
                                            + "SELECT                                                                  "
                                            + " TRUNC(sysdate) DAT_PROCESSAMENTO,                                      "
                                            + " b.idt_promocao IDT_PROMOCAO,                                           "
                                            + " f.idt_codigo_nacional IDT_CODIGO_NACIONAL,                             "
                                            + " c.idt_plano_preco IDT_PLANO_PRECO,                                     "
                                            + " SYSDATE DAT_INICIAL_EXECUCAO,                                          "
                                            + " SYSDATE DAT_FINAL_EXECUCAO,                                            "
                                            + " COUNT(1) QTD_ASSINANTES,                                               "
                                            + " SUM(DECODE(c.idt_status,0,1,0)) QTD_STATUS_0,                          "
                                            + " SUM(DECODE(c.idt_status,1,1,0)) QTD_STATUS_1,                          "
                                            + " SUM(DECODE(c.idt_status,2,1,0)) QTD_STATUS_2,                          "
                                            + " SUM(DECODE(c.idt_status,3,1,0)) QTD_STATUS_3,                          "
                                            + " SUM(DECODE(c.idt_status,4,1,0)) QTD_STATUS_4,                          "
                                            + " SUM(DECODE(c.idt_status,5,1,0)) QTD_STATUS_5,                          "
                                            + " SUM(NUM_SEGUNDOS_TOTAL) NUM_SEGUNDOS_TOTAL,                            "
                                            + " SUM(nvl(NUM_SEGUNDOS_TOTAL,0)         -                                "
                                            + "     nvl(NUM_SEGUNDOS_FF,0)            -                                "
                                            + "     nvl(NUM_SEGUNDOS_PLANO_NOTURNO,0) -                                "
                                            + "     nvl(NUM_SEGUNDOS_PLANO_DIURNO,0)  -                                "
                                            + "     nvl(NUM_SEGUNDOS_NAO_BONIFICADO,0)-                                "
                                            + "     nvl(NUM_SEGUNDOS_DURAC_EXCEDIDA,0)-                                "
                                            + "     nvl(NUM_SEGUNDOS_EXPURGO_FRAUDE,0)-                                "
                                            + "     nvl(NUM_SEGUNDOS_ESTORNO_FRAUDE,0)-                                "
                                            + "     nvl(NUM_SEGUNDOS_TUP,0)           -                                "
                                            + "     nvl(NUM_SEGUNDOS_AIGUALB,0)       -                                "
                                            + "     nvl(NUM_SEGUNDOS_ATH,0)           -                                "
                                            + "     nvl(NUM_SEGUNDOS_FALEGRATIS,0)    -                                "
                                            + "     nvl(NUM_SEGUNDOS_BONUS,0)         -                                "  
                                            + "     nvl(NUM_SEGUNDOS_CT,0)          ) NUM_SEGUNDOS_NORMAL,             "
                                            + " SUM(NUM_SEGUNDOS_FF) NUM_SEGUNDOS_FF,                                  "
                                            + " SUM(NUM_SEGUNDOS_NAO_BONIFICADO) NUM_SEGUNDOS_NAO_BONIFICADO,          "
                                            + " SUM(NUM_SEGUNDOS_PLANO_NOTURNO) NUM_SEGUNDOS_PLANO_NOTURNO,            "
                                            + " SUM(NUM_SEGUNDOS_PLANO_DIURNO ) NUM_SEGUNDOS_PLANO_DIURNO,             "
                                            + " SUM(NUM_SEGUNDOS_DURAC_EXCEDIDA) NUM_SEGUNDOS_DURAC_EXCEDIDA,          "
                                            + " SUM(NVL(NUM_SEGUNDOS_EXPURGO_FRAUDE,0)) NUM_SEGUNDOS_EXPURGO_FRAUDE,   "
                                            + " SUM(NVL(NUM_SEGUNDOS_ESTORNO_FRAUDE,0)) NUM_SEGUNDOS_ESTORNO_FRAUDE,   "
                                            + " SUM(NUM_SEGUNDOS_TUP           ) NUM_SEGUNDOS_TUP,                     "
                                            + " SUM(NUM_SEGUNDOS_AIGUALB       ) NUM_SEGUNDOS_AIGUALB,                 "
                                            + " SUM(NUM_SEGUNDOS_ATH           ) NUM_SEGUNDOS_ATH,                     "
                                            + " SUM(NUM_SEGUNDOS_MOVEL_NAO_BRT ) NUM_SEGUNDOS_MOVEL_NAO_BRT,           "
                                            + " SUM(NUM_SEGUNDOS_FALEGRATIS    ) NUM_SEGUNDOS_FALEGRATIS,              "
                                            + " SUM(NUM_SEGUNDOS_BONUS         ) NUM_SEGUNDOS_BONUS,                   "
                                            + " SUM(NUM_SEGUNDOS_CT            ) NUM_SEGUNDOS_CT,                      "
                                            + " SUM(d.vlr_pago) VLR_RECARGAS,                                          "
                                            + " SUM(DECODE(b.idt_promocao,                                                                   "
                                            + "            1,                                                                                "
                                            + "            ( (nvl(NUM_SEGUNDOS_TOTAL,0)         -                                            "
                                            + "               nvl(NUM_SEGUNDOS_FF,0)            -                                            "
                                            + "               nvl(NUM_SEGUNDOS_PLANO_NOTURNO,0) -                                            "
                                            + "               nvl(NUM_SEGUNDOS_PLANO_DIURNO,0)  -                                            "
                                            + "               nvl(NUM_SEGUNDOS_NAO_BONIFICADO,0)-                                            "
                                            + "               nvl(NUM_SEGUNDOS_DURAC_EXCEDIDA,0)-                                            "
                                            + "               nvl(NUM_SEGUNDOS_EXPURGO_FRAUDE,0)-                                            "
                                            + "               nvl(NUM_SEGUNDOS_ESTORNO_FRAUDE,0)-                                            "
                                            + "               nvl(NUM_SEGUNDOS_TUP,0)           -                                            "
                                            + "               nvl(NUM_SEGUNDOS_AIGUALB,0)       -                                            "
                                            + "               nvl(NUM_SEGUNDOS_ATH,0)           -                                            "
                                            + "               nvl(NUM_SEGUNDOS_FALEGRATIS,0)    -                                            "
                                            + "               nvl(NUM_SEGUNDOS_BONUS,0)         -                                            "  
                                            + "               nvl(NUM_SEGUNDOS_CT,0)            )*f.vlr_bonus_minuto        +                "  
                                            + "              nvl(NUM_SEGUNDOS_FF,0)              *f.vlr_bonus_minuto_ff     +                "  
                                            + "              nvl(NUM_SEGUNDOS_PLANO_NOTURNO,0)   *f.vlr_bonus_minuto_noturno+                "
                                            + "              nvl(NUM_SEGUNDOS_PLANO_DIURNO,0)    *f.vlr_bonus_minuto_diurno +                "
                                            + "              nvl(NUM_SEGUNDOS_ATH,0)             *f.vlr_bonus_minuto_ath    +                "
                                            + "              nvl(NUM_SEGUNDOS_CT,0)              *f.vlr_bonus_minuto_ct                      "
                                            + "            ),                                                                                "  
                                            + "            LEAST(( (nvl(NUM_SEGUNDOS_TOTAL,0)         -                                      "
                                            + "                     nvl(NUM_SEGUNDOS_FF,0)            -                                      "
                                            + "                     nvl(NUM_SEGUNDOS_PLANO_NOTURNO,0) -                                      "
                                            + "                     nvl(NUM_SEGUNDOS_PLANO_DIURNO,0)  -                                      "                                            
                                            + "                     nvl(NUM_SEGUNDOS_NAO_BONIFICADO,0)-                                      "
                                            + "                     nvl(NUM_SEGUNDOS_DURAC_EXCEDIDA,0)-                                      "
                                            + "                     nvl(NUM_SEGUNDOS_EXPURGO_FRAUDE,0)-                                      "
                                            + "                     nvl(NUM_SEGUNDOS_ESTORNO_FRAUDE,0)-                                      "
                                            + "                     nvl(NUM_SEGUNDOS_TUP,0)           -                                      "
                                            + "                     nvl(NUM_SEGUNDOS_AIGUALB,0)       -                                      "
                                            + "                     nvl(NUM_SEGUNDOS_ATH,0)           -                                      "
                                            + "                     nvl(NUM_SEGUNDOS_FALEGRATIS,0)    -                                      "
                                            + "                     nvl(NUM_SEGUNDOS_BONUS,0)         -                                      "
                                            + "                     nvl(NUM_SEGUNDOS_CT,0)            )*f.vlr_bonus_minuto        +          "
                                            + "                     nvl(NUM_SEGUNDOS_FF,0)             *f.vlr_bonus_minuto_ff     +          "  
                                            + "                     nvl(NUM_SEGUNDOS_PLANO_NOTURNO,0)  *f.vlr_bonus_minuto_noturno+          "
                                            + "                     nvl(NUM_SEGUNDOS_PLANO_DIURNO,0)   *f.vlr_bonus_minuto_diurno +          "
                                            + "                     nvl(NUM_SEGUNDOS_ATH,0)            *f.vlr_bonus_minuto_ath    +          "
                                            + "                     nvl(NUM_SEGUNDOS_CT,0)             *f.vlr_bonus_minuto_ct                "
                                            + "                  ),                                                                          "
                                            + "                  DECODE(i.ind_limite_dinamico,                                               "
                                            + "                         0,                                                                   "
                                            + "                         g.vlr_max_credito_bonus,                                             "
                                            + "                         LEAST(h.vlr_threshold_inferior+d.vlr_pago,h.vlr_threshold_superior)))"
                                            + "           )) VLR_BONUS_TOTAL,                                                                "
                                            + " 0 VLR_BONUS_ADIANTAMENTO,                                              "
                                            + " SUM(C.vlr_saldo_principal) VLR_SALDO_PRINCIPAL,                        "
                                            + " SUM(C.vlr_saldo_bonus) vlr_saldo_bonus,                                "
                                            + " SUM(C.vlr_saldo_sm) vlr_saldo_sm,                                      "
                                            + " SUM(C.vlr_saldo_dados) vlr_saldo_dados,                                "
                                            + " 0 VLR_SALDO_CONCESSAO_FRACIONADA                                       "
                                            + "FROM                                                                    "
                                            + " tbl_pro_totalizacao_pula_pula a,                                       "
                                            + " tbl_pro_assinante b,                                                   "
                                            + " tbl_apr_assinante c,                                                   "
                                            + " tbl_pro_totalizacao_recargas d,                                        "
                                            + " tbl_ger_plano_preco e,                                                 "
                                            + " tbl_pro_bonus_pula_pula f,                                             "
                                            + "(SELECT                                                                 "
                                            + "   g.idt_promocao,                                                      "
                                            + "   g.vlr_max_credito_bonus                                              "
                                            + "  FROM                                                                  "
                                            + "   tbl_pro_limite_promocao g                                            "
                                            + "  WHERE                                                                 "
                                            + "       g.DAT_INI_PERIODO <= sysdate                                     "
                                            + "   AND nvl(g.DAT_FIM_PERIODO,SYSDATE) >= SYSDATE) g,                    "
                                            + " tbl_pro_limite_dinamico h,                                             "
                                            + " tbl_pro_promocao i                                                     "
                                            + "WHERE                                                                   "
                                            + "     a.idt_msisdn LIKE '55'||?||'%'                                     "
                                            + " AND a.dat_mes = TO_CHAR(SYSDATE,'yyyymm')                              "
                                            + " AND a.idt_msisdn = b.idt_msisdn                                        "
                                            + " AND b.idt_promocao = ?                                                 "
                                            + " AND a.idt_msisdn = c.idt_msisdn                                        "
                                            + " AND a.idt_msisdn = d.idt_msisdn(+)                                     "
                                            + " AND a.dat_mes = d.dat_mes(+)                                           "
                                            + " AND c.idt_plano_preco = e.idt_plano_preco                              "
                                            + " AND i.idt_categoria = ?                                                "
                                            + " AND e.idt_categoria = ?                                                "
                                            + " AND f.DAT_INI_PERIODO <= sysdate                                       "
                                            + " AND nvl(f.DAT_FIM_PERIODO,SYSDATE) >= SYSDATE                          "
                                            + " AND f.idt_codigo_nacional = SUBSTR (a.idt_msisdn, 3, 2)                "
                                            + " AND f.idt_plano_preco = c.idt_plano_preco                              "
                                            + " AND g.idt_promocao(+) = b.idt_promocao                                 "
                                            + " AND b.idt_promocao = i.idt_promocao                                    "
                                            + "GROUP BY                                                                "
                                            + " TRUNC(sysdate),                                                        "
                                            + " f.idt_codigo_nacional,                                                 "
                                            + " b.idt_promocao,                                                        "
                                            + " c.idt_plano_preco                                                      ";

    private static final String SQL_ATUALIZA    = "UPDATE tbl_rel_pula_pula c                                             "
                                                + "   SET (dat_final_execucao, vlr_bonus_adiantamento) =                  "
                                                + "          (SELECT SYSDATE, nvl(SUM (a.vlr_credito_bonus),0)            "
                                                + "             FROM tbl_rec_recargas a, tbl_pro_assinante b              "
                                                + "            WHERE tip_transacao = ?                                    "
                                                + "              AND dat_origem >= trunc(SYSDATE,'MONTH')                 "
                                                + "              AND dat_origem < SYSDATE                                 "
                                                + "              AND b.idt_msisdn LIKE '55'||?||'%'                       "
                                                + "              AND a.idt_msisdn = b.idt_msisdn                          "
                                                + "              AND b.idt_promocao = c.idt_promocao                      "
                                                + "              AND a.idt_plano_preco = c.idt_plano_preco)               "
                                                + " WHERE c.idt_promocao = ?                                              "
                                                + "   AND c.idt_codigo_nacional = ?                                       "
                                                + "   AND dat_processamento = TRUNC(SYSDATE)                              ";
    
    /**
     *	Construtor da classe.
     */
	public SumarizacaoPulaPulaConsumidor()
	{
		super(GerentePoolLog.getInstancia(SumarizacaoPulaPulaConsumidor.class).getIdProcesso(Definicoes.CL_SUMARIZACAO_PULA_PULA_PROD), 
		      Definicoes.CL_SUMARIZACAO_PULA_PULA_CONS);
		
		this.conexaoPrep = null;
	}

	/**
     *	Executa o processo de Sumarizacao Pula-Pula.
     *
     *	@return		obj						VO a ser processado. Fornecido pelo produtor.
     *	@throws		Exception
     */
	public void execute(Object obj) throws Exception
	{
	    super.log(Definicoes.DEBUG, "execute", "Inicio");
	    
	    SumarizacaoPulaPulaVO sumariza = (SumarizacaoPulaPulaVO)obj;
		
		try
		{
			// Sumariza os dados por promoção, cn e plano 
			Object [] paramInsere = {	sumariza.getCn(),
										Integer.toString(sumariza.getIdtPromocao()),
										Integer.toString(Definicoes.CATEGORIA_PULA_PULA),
										Integer.toString(Definicoes.CATEGORIA_PREPAGO)};
			
            conexaoPrep.executaPreparedUpdate(SumarizacaoPulaPulaConsumidor.SQL_INSERE, paramInsere, super.getIdLog());
            
            // Busca se a promoção possui adiantamento
            PromocaoDiaExecucao dia = consulta.getPromocaoDiaExecucao(new Integer(sumariza.getIdtPromocao()),Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL,new Date());
            
            if(dia != null)
            {
	            Object [] paramAtualiza = {	Definicoes.AJUSTE_BONUS_ADIANTAMENTO_PULA_PULA,
	            							sumariza.getCn(),
											Integer.toString(sumariza.getIdtPromocao()),
											sumariza.getCn()};
	
	            conexaoPrep.executaPreparedUpdate(SumarizacaoPulaPulaConsumidor.SQL_ATUALIZA, paramAtualiza, super.getIdLog());
            }

		}
		catch(GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "execute", "Excecao: " + e);
		    throw e;
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "execute", "Fim");
		}
	}	
	
	public void finish()
	{
	}

	/**
     *	Inicializa o objeto.
     *
     *	@param		ProcessoBatchProdutor	produtor					Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.conexaoPrep = produtor.getConexao();
		startup();
	}
	
    /**
     *	Inicializa o objeto. Uma vez que a unica operacao necessaria para o startup e a atribuicao do produtor, neste
     *	caso o metodo nao faz nada. 
     *
     *	@throws		Exception
     */
	public void startup() throws Exception
	{
		consulta = new Consulta(super.getIdLog());
	}

    /**
     *	Inicializa o objeto.
     *
     *	@param		Produtor				produtor					Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor) produtor);
	}	
}