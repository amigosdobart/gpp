package com.brt.gpp.aplicacoes.exportacaoDW;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Cronometro;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 * Este arquivo refere-se a classe EnvioDadosPulaPulaDW, responsavel pela implementacao da
 * interface de dados do bonus pula-pula com o DW. <br><br>
 *
 * <b>Parametro necessario</b>: data da recarga (dd/mm/yyyy).<br>
 *
 * Exemplo: Se a data valer "14/01/2008" entao serao exportados para DW os bonus dessa
 * data com o periodo "01/01/2008" a "01/02/2008"
 *
 * <hr>
 * <b>Modificado por:</b> Bernardo Dias<br>
 * <b>Data:</b> 11/01/2008<br>
 * <b>Razao:</b>
 * <ul>
 *   <li>Reformulacao do SQL para otimização de desempenho
 *   <li>Requer execução diária
 *   <li>Reescrita completa da classe (codigo simplificado)
 *   <li>Adicionado comentarios e LOGs
 * </ul>
 * <hr>
 * <b>Modificado por:</b> Leone Parise<br>
 * <b>Data:</b> 14/08/2007<br>
 * <b>Razao:</b>
 * <ul>
 *   <li>Reformulacao do SQL para retornar os nomes das promocoes definidos em tabela
 *   <li>Adocao de padrao Produtor Consumidor
 * </ul>
 * <hr>
 *
 * @author:		Lawrence Josuá
 * @since: 		10/05/2005
 */
public final class EnvioBonusPulaPulaDWProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
    private PREPConexao             conexaoPrep;
    private String                  status;
	private String                  mensagem;
	private Date					dataReferencia;
    private SimpleDateFormat        sdf;

    private final String            SQL_INSERT_SELECT =
        "INSERT INTO TBL_INT_DW_BONUSPULAPULA (DAT_GERACAO_REGISTRO,                                          "+
        "                                      IDT_MSISDN,                                                    "+
        "                                      VLR_BONUS,                                                     "+
        "                                      VLR_SEGUNDOS_BONUS,                                            "+
        "                                      IDT_PROMO_CLARIFY,                                             "+
        "                                      DAT_PERIODO_INICIAL,                                           "+
        "                                      DAT_PERIODO_FINAL,                                             "+
        "                                      VLR_DIA_BONUS,                                                 "+
        "                                      IDT_STATUS_PROCESSAMENTO)                                      "+
        "SELECT                                                                                               "+
        "       SYSDATE                                                          AS DAT_GERACAO_REGISTRO,     "+
        "       PASS.IDT_MSISDN                                                  AS IDT_MSISDN,               "+
        "       RREC.VLR_CREDITO_BONUS                                           AS VLR_BONUS,                "+
        "       ROUND((RREC.VLR_CREDITO_BONUS / PBON.VLR_BONUS_MINUTO) * 60, 0)  AS VLR_SEGUNDO_BONUS,        "+
        "       PPRO.IDT_PROMOCAO_CRM                                            AS IDT_PROMO_CLARIFY,        "+
        "       to_date(?,'dd/mm/yyyy')                                          AS DAT_PERIODO_INICIAL,      "+
        "       to_date(?,'dd/mm/yyyy')                                          AS DAT_PERIODO_FINAL,        "+
        "       EXEC1.VLR_DIA_BONUS                                              AS VLR_DIA_BONUS,            "+
        "       'N'                                                              AS IDT_STATUS_PROCESSAMENTO  "+
        "FROM  TBL_PRO_PROMOCAO        PPRO,                                                                  "+
        "      TBL_PRO_ASSINANTE       PASS,                                                                  "+
        "      TBL_REC_RECARGAS        RREC,                                                                  "+
        "      TBL_PRO_BONUS_PULA_PULA PBON,                                                                  "+
        "     (SELECT IDT_PROMOCAO,                                                                           "+
        "             MAX(NUM_DIA_EXECUCAO_RECARGA) AS VLR_DIA_BONUS                                          "+
        "      FROM TBL_PRO_DIA_EXECUCAO                                                                      "+
        "      WHERE TIP_EXECUCAO = 'DEFAULT'                                                                 "+
        "      GROUP BY IDT_PROMOCAO) EXEC1                                                                   "+
        "WHERE PASS.IDT_MSISDN    = RREC.IDT_MSISDN                                                           "+
        " AND  EXEC1.IDT_PROMOCAO = PASS.IDT_PROMOCAO                                                         "+
        " AND  PPRO.IDT_PROMOCAO  = PASS.IDT_PROMOCAO                                                         "+
        " AND  PPRO.IDT_CATEGORIA = 1                                                                         "+
        " AND  PBON.IDT_CODIGO_NACIONAL = SUBSTR(RREC.IDT_MSISDN, 3, 2)                                       "+
        " AND  PBON.IDT_PLANO_PRECO = RREC.IDT_PLANO_PRECO                                                    "+
        " AND  (       PBON.DAT_INI_PERIODO <= TRUNC(RREC.DAT_INCLUSAO)                                       "+
        "       AND ( (PBON.DAT_FIM_PERIODO >= TRUNC(RREC.DAT_INCLUSAO)) OR (PBON.DAT_FIM_PERIODO IS NULL) )  "+
        "       )                                                                                             "+
        " AND  RREC.TIP_TRANSACAO = '08001'                                                                   "+
        " AND  RREC.DAT_INCLUSAO >= to_date(?,'dd/mm/yyyy')                                                   "+
        " AND  RREC.DAT_INCLUSAO <  to_date(?,'dd/mm/yyyy')                                                   "+
        " AND  RREC.ID_TIPO_RECARGA = 'A'                                                                     ";


	public EnvioBonusPulaPulaDWProdutor(long logId)
	{
		super(logId, Definicoes.CL_ENVIO_DADOS_PULA_PULA_DW);
        this.status = Definicoes.TIPO_OPER_SUCESSO;
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	}

    /**
     * Retorna a lista de parametros do SQL_INSERT_SELECT.<br>
     * Formato: {dataInicialPeriodo, dataFinalPeriodo, dataInicialRecarga, dataFinalRecarga}<br>
     *
     * Todas as datas sao strings no padrao 'dd/MM/yyyy'
     */
    public Object[] getParametrosSQL()
    {
        String dataInicialPeriodo;
        String dataFinalPeriodo;
        String dataInicialRecarga;
        String dataFinalRecarga;

        Calendar cal = Calendar.getInstance();
        cal.setTime(dataReferencia);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        dataInicialPeriodo = sdf.format(cal.getTime());

        cal.add(Calendar.MONTH , 1);
        dataFinalPeriodo = sdf.format(cal.getTime());

        cal.setTime(dataReferencia);
        dataInicialRecarga = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_MONTH, 1);
        dataFinalRecarga = sdf.format(cal.getTime());

        Object[] parametros = {
                dataInicialPeriodo,
                dataFinalPeriodo,
                dataInicialRecarga,
                dataFinalRecarga
                };

        return parametros;
    }

    /**
     * Valida o parametro de entrada
     */
    private void validaParametro(String[] params)
    {
        if (params == null || params.length < 1)
        {
            status = Definicoes.TIPO_OPER_ERRO;
            throw new IllegalArgumentException(
                    "Parametro Data (dd/mm/yyyy) necessario para execucao do processo.");
        }

        try
        {
            dataReferencia = sdf.parse(params[0]);
        }
        catch (ParseException e)
        {
            status = Definicoes.TIPO_OPER_ERRO;
            throw new IllegalArgumentException(
                    "A data informada (" + params[0] + ") nao esta no formato valido (dd/mm/yyyy)");
        }
    }

	 public void startup(String[] params) throws Exception
	 {
		Cronometro cron = new Cronometro();
        validaParametro(params);

		try
		{
            /*
             * Inicia conexao com o banco
             */

            conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
            conexaoPrep.setAutoCommit(false);

            /*
             * Gera os parametros do SQL
             */

            String dataInicialPeriodo;
            String dataFinalPeriodo;
            String dataInicialRecarga;
            String dataFinalRecarga;

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataReferencia);

            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            dataInicialPeriodo = sdf.format(cal.getTime());

            cal.add(Calendar.MONTH , 1);
            dataFinalPeriodo = sdf.format(cal.getTime());

            cal.setTime(dataReferencia);
            dataInicialRecarga = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, 1);
            dataFinalRecarga = sdf.format(cal.getTime());

            Object[] parametros = {
                    dataInicialPeriodo,
                    dataFinalPeriodo,
                    dataInicialRecarga,
                    dataFinalRecarga
                    };

            /*
             * Executa o SQL e faz commit
             */

			super.log(Definicoes.INFO,"startup","Executando exportacao DW para recargas do dia " +
                    dataInicialRecarga + ". Periodo: " + dataInicialPeriodo + " a " + dataFinalPeriodo);

			int registros = conexaoPrep.executaPreparedUpdate(SQL_INSERT_SELECT, parametros, super.getIdLog());
            conexaoPrep.commit();

            this.mensagem = "Processo concluido. Exportacao de " + registros + " recargas do dia " + dataInicialRecarga;

            super.log(Definicoes.INFO, "startup", "Exportacao DW concluida com sucesso para recargas do dia " +
                    dataInicialRecarga + ". " + registros + " registros criados. " + cron.getTempoDecorrido());
		}
		catch (Exception e)
		{
			status = Definicoes.TIPO_OPER_ERRO;
            mensagem = "Erro na exportacao DW. " + e.getMessage();
			super.log(Definicoes.ERRO, "startup", mensagem);

            // Faz o rollback em caso de erro
            try
            {
                if (conexaoPrep != null && status == Definicoes.TIPO_OPER_ERRO)
                    conexaoPrep.rollback();
            }
            catch (Exception e2)
            {
                super.log(Definicoes.ERRO, "finish", "Erro ao executar rollback. " + e.getMessage());
            }
		}
     }

	public Object next() throws Exception
	{
        return null;
	}

	public void finish() throws Exception
	{
		try
        {
            if (this.conexaoPrep != null)
                gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
        }
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "finish", "Falha ao liberar conexao." + e.getMessage());
		}
	}

	public void handleException()
	{
	}

	public int getIdProcessoBatch()
	{
		return Definicoes.IND_DW_BONUSPULAPULA;
	}

	public String getDescricaoProcesso()
	{
		return this.mensagem;
	}

	public String getStatusProcesso()
	{
		return this.status;
	}

	public void setStatusProcesso(String status)
	{
		this.status = status;
	}

	public String getDataProcessamento()
	{
        Date dataProcessamento = dataReferencia != null ? dataReferencia : Calendar.getInstance().getTime();
		return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(dataProcessamento);
	}

	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}
}