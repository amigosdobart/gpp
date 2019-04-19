package com.brt.gpp.aplicacoes.exportacaoDW;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

import java.sql.SQLException;
import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Este arquivo refere-se a classe EnvioDadosPulaPulaDW, responsavel pela implementacao da
 * interface de dados do bonus pula-pula com o DW
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
public final class EnvioDadosPulaPulaDW extends Aplicacoes implements ProcessoBatchProdutor
{
	private PREPConexao             conexaoPrep;
	private String                  status;
	private String                  mensagem;

	public EnvioDadosPulaPulaDW (long logId)
	{
		super(logId, Definicoes.CL_ENVIO_DADOS_PULA_PULA_DW);

		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	}

	 /**
	  * Retorna a data inicial de analise a ser executada
	  *
	  * @param data Data desejada de processamento
	  * @return Data inicial de analise do bonus
	  */
	 private Date getDataInicialAnalise(Date data)
	 {
		 Calendar c = Calendar.getInstance();
		 c.setTime(data);
		 // Primeiro dia do mes
		 c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		 // Primeiro segundo do dia 00:00:00.000
		 c.clear(Calendar.HOUR_OF_DAY);
		 c.clear(Calendar.MINUTE);
		 c.clear(Calendar.SECOND);
		 c.clear(Calendar.MILLISECOND);

		 return c.getTime();
	 }

	 /**
	  * Retorna a data final de analise a ser executada
	  *
	  * @param data Data desejada de processamento
	  * @return Data final de analise do bonus
	  */
	 private Date getDataFinalAnalise(Date data)
	 {
		 Calendar c = Calendar.getInstance();
		 c.setTime(data);
		 // Ultimo dia do mes
		 c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		 // Ultimo segundo do dia 23:59:59.999
		 c.set(Calendar.HOUR_OF_DAY , c.getMaximum(Calendar.HOUR_OF_DAY));
		 c.set(Calendar.MINUTE      , c.getMaximum(Calendar.MINUTE));
		 c.set(Calendar.SECOND      , c.getMaximum(Calendar.SECOND));
		 c.set(Calendar.MILLISECOND , c.getMaximum(Calendar.MILLISECOND));

		 return c.getTime();
	 }

	 public void startup(String[] params) throws Exception
	 {
		status = Definicoes.TIPO_OPER_SUCESSO;
		if(params == null || params.length < 1)
		{
			status = Definicoes.TIPO_OPER_ERRO;
			throw new Exception("Parametro Data necessario para execucao do processo.");
		}

		conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());

		Date data = null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
			data = sdf.parse(params[0]);
		}
		catch(ParseException e)
		{
			status = Definicoes.TIPO_OPER_ERRO;
			throw new Exception("Formato de data invalido."+params[0]+". Favor informar data no formato MM/yyyy");
		}

		enviaDadosPulaPulaDW(data);
     }

	 /**
	  * Varre a base de recargas para todos os assinantes que receberam bonus
	  * pula-pula no período e insere estes na tabela TBL_INT_DW_BONUSPULAPULA
	  *
	  * @param 	data		Periodo de analize. Formato (MM/YYYY).
	  *
	  * @return	<code>Definicoes.RET_OPERACAO_OK</code> se sucesso ou diferente em caso de falha
	  *
	  * @throws GPPInternalErrorException Caso ocorra alguma falha no processo
	  */
	 public short enviaDadosPulaPulaDW (Date data) throws GPPInternalErrorException
	 {
	 	short promocao = 0;
	 	return enviaDadosPulaPulaDW (data, promocao);
	 }

	/**
	 * Varre a base de recargas para todos os assinantes que receberam bonus
	 * pula-pula no período e insere estes na tabela TBL_INT_DW_BONUSPULAPULA
	 *
	 * @param 	data		Periodo de analize. Formato (MM/YYYY).
	 * @param	promocao	Promocao a ser analiazda.<b>(Nao utilizado)</b>
	 *
	 * @return	<code>Definicoes.RET_OPERACAO_OK</code> se sucesso ou diferente em caso de falha
	 *
	 * @throws GPPInternalErrorException Caso ocorra alguma falha no processo
	 */
	public short enviaDadosPulaPulaDW (Date data, short promocao) throws GPPInternalErrorException
	{
        //Inicializa variaveis do metodo
		short retorno = 0;
		int linhasProcessadas = 0;

		super.log(Definicoes.DEBUG, "enviaDadosPulaPulaDW", "Inicio do processo de levantamento de dados de Bonus Pula-Pula para o DW.");
		try
		{
			conexaoPrep.setAutoCommit(false);
			String sql =
			"INSERT INTO TBL_INT_DW_BONUSPULAPULA (DAT_GERACAO_REGISTRO,                                          "+
			"                                      IDT_MSISDN,                                                    "+
			"                                      VLR_BONUS,                                                     "+
			"                                      VLR_SEGUNDOS_BONUS,                                            "+
			"                                      IDT_PROMO_CLARIFY,                                             "+
			"                                      DAT_PERIODO_INICIAL,                                           "+
			"                                      DAT_PERIODO_FINAL,                                             "+
			"                                      VLR_DIA_BONUS,                                                 "+
			"                                      IDT_STATUS_PROCESSAMENTO)                                      "+
			"SELECT SYSDATE                                                          AS DAT_GERACAO_REGISTRO,     "+
			"       PASS.IDT_MSISDN                                                  AS IDT_MSISDN,               "+
			"       RREC.VLR_CREDITO_BONUS                                           AS VLR_BONUS,                "+
			"       ROUND((RREC.VLR_CREDITO_BONUS / PBON.VLR_BONUS_MINUTO) * 60, 0)  AS VLR_SEGUNDO_BONUS,        "+
			"       PPRO.IDT_PROMOCAO_CRM                                            AS IDT_PROMO_CLARIFY,        "+
			"       ?                                                                AS DAT_PERIODO_INICIAL,      "+
			"       ?                                                                AS DAT_PERIODO_FINAL,        "+
			"       EXEC.VLR_DIA_BONUS                                               AS VLR_DIA_BONUS,            "+
			"       'N'                                                              AS IDT_STATUS_PROCESSAMENTO  "+
			"FROM  TBL_PRO_PROMOCAO        PPRO,                                                                  "+
			"      TBL_PRO_ASSINANTE       PASS,                                                                  "+
			"      TBL_REC_RECARGAS        RREC,                                                                  "+
			"      TBL_PRO_BONUS_PULA_PULA PBON,                                                                  "+
			"     (SELECT IDT_PROMOCAO,                                                                           "+
			"             MAX(NUM_DIA_EXECUCAO_RECARGA) AS VLR_DIA_BONUS                                          "+
			"      FROM TBL_PRO_DIA_EXECUCAO                                                                      "+
			"      WHERE TIP_EXECUCAO = 'DEFAULT'                                                                 "+
			"      GROUP BY IDT_PROMOCAO) EXEC                                                                    "+
			"WHERE  PASS.IDT_MSISDN = RREC.IDT_MSISDN                                                             "+
			"  AND  PPRO.IDT_PROMOCAO = PASS.IDT_PROMOCAO                                                         "+
			"  AND  PBON.IDT_CODIGO_NACIONAL = SUBSTR(PASS.IDT_MSISDN, 3, 2)                                      "+
			"  AND  PBON.IDT_PLANO_PRECO = RREC.IDT_PLANO_PRECO                                                   "+
			"  AND  RREC.TIP_TRANSACAO = '08001'                                                                  "+
			"  AND  EXEC.IDT_PROMOCAO = PASS.IDT_PROMOCAO                                                         "+
			"  AND  RREC.DAT_ORIGEM >= PBON.DAT_INI_PERIODO                                                       "+
			"  AND (RREC.DAT_ORIGEM <= PBON.DAT_FIM_PERIODO OR PBON.DAT_FIM_PERIODO IS NULL)                      "+
			"  AND  RREC.DAT_ORIGEM >= ?                                                                          "+
			"  AND  RREC.DAT_ORIGEM <= ?                                                                          ";

			Object parametros[] = {new java.sql.Date(getDataInicialAnalise(data).getTime()),
								   new java.sql.Date(getDataFinalAnalise(data).getTime()),
								   new java.sql.Timestamp(getDataInicialAnalise(data).getTime()),
								   new java.sql.Timestamp(getDataFinalAnalise(data).getTime())};

			// Executa o comando de inserção dos dados na tabela TBL_INT_DW_BONUSPULAPULA
			super.log(Definicoes.INFO,"enviaDadosPulaPulaDW","Rodando Query Principal...");
			linhasProcessadas = conexaoPrep.executaPreparedUpdate(sql,parametros,super.getIdLog());

			super.log(Definicoes.INFO,"enviaDadosPulaPulaDW","Executando Commit...");
			conexaoPrep.commit();
			super.log(Definicoes.INFO,"enviaDadosPulaPulaDW","Commit executado com sucesso!");
			conexaoPrep.setAutoCommit(true);
			this.mensagem = "Processo EnvioDadosPulaPulaDW executado com sucesso!";
			super.log(Definicoes.INFO,"enviaDadosPulaPulaDW","Interface GPP-DW de bonus pula-pula atualizada com sucesso! "+linhasProcessadas+" linhas inseridas.");
		}
		catch (GPPInternalErrorException e)
		{
			status = Definicoes.TIPO_OPER_ERRO;
			retorno = 1;
			super.log(Definicoes.ERRO, "enviaDadosPulaPulaDW", "Excecao Interna GPP ocorrida: "+ e);
			mensagem = "Excecao:" + e.getMessage();
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);
		}
		catch (SQLException e)
		{
			status = Definicoes.TIPO_OPER_ERRO;
			retorno = 1;
			super.log(Definicoes.ERRO, "enviaDadosPulaPulaDW", "Erro Banco de Dados: "+ e);
			mensagem = "Excecao:" + e.getMessage();
			throw new GPPInternalErrorException ("Excecao Banco de Dados: " + e);
		}
		return retorno;
	}

	/**
	 * Retorna para as threads consumidoras o proximo registro a ser processado. Retorna Nulo pois este é um processo só produtor
	 *
	 * @param Object
	 *            params Lista de parametros. Nao utilizado.
	 * @throws Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		return null;
	}

	/**
	 *	Finaliza o processo.
	 *
	 *	@throws		Exception
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		super.log(Definicoes.DEBUG, "finish", "Inicio");
		super.log(Definicoes.INFO, "finish", "Liberando Conexao...");
		try
		{
			this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "finish", "Falha ao liberar conexao."+e.getMessage());
		}
		super.log(Definicoes.INFO, "finish", "Conexao liberada com sucesso!");
		super.log(Definicoes.DEBUG, "Produtor.finish", "Fim");
	}

	/**
	 *	Trata excecoes lancadas pelo produtor. Nao utilizado pelo processo.
	 *
	 *	@throws		Exception
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException()
	{

	}

	/**
	 *	Retorna o identificador do processo batch.
	 *
	 *	@return		int													Identificador do processo batch.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_DW_BONUSPULAPULA;
	}

	/**
	 *	Retorna a mensagem informativa sobre a execucao do processo batch.
	 *
	 *	@return		String					mensagem					Mensagem informativa sobre a execucao.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		return this.mensagem;
	}

	/**
	 *	Retorna o status da execucao do processo.
	 *
	 *	@return		String					status						Status da execucao do processo.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
	 */
	public String getStatusProcesso()
	{
		return this.status;
	}

	/**
	 *	Atribui o status da execucao do processo.
	 *
	 *	@param		String					status						Status da execucao do processo.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status)
	{
		this.status = status;
	}

	/**
	 *	Retorna a data de processamento (data de referencia).
	 *  O processo retorna a data efetiva de execucao.
	 *
	 *	@param		String												Data de execucao no formato dd/mm/yyyy.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}

	/**
	 *	Retorna a conexao PREP para os consumidores.
	 *
	 *	@param		PREPConexao											Conexao PREP.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}
}