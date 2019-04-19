package com.brt.gpp.aplicacoes.promocao.automatizacaoRebarba;
 
//Imports Java.
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//Imports GPP.
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pelo gerenciamento do processo de automatizacao da rebarba.
 *	Esta classe recebe uma série de MSISDNs e Datas de expiração de assinates
 * que irão ser reprocessados pela conceção pula-pula e passa a responsabilidade
 * de processar essa informação para os consumidores.
 *	Por fim chama a concecao pula-pula para conceder realmente a promocao.
 *
 *	@author	Magno Batista Corrêa
 *	@since	2006/07/19 (yyyy/mm/dd)
 */
public class AutomatizacaoRebarbaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{

    private int							numRegistros;
    private String						status;
    private	String						mensagem;
    private PREPConexao					conexaoPrep;
	private ResultSet					result;
	//Statements SQL.
	private static final String SQL_SELECAO = 
		"SELECT                                                                                " +
		"        REC.idt_msisdn                                  	   AS MSISDN,              " +
		"        TRUNC(REC.dat_origem + VAL.num_dias_exp_principal)     AS DAT_EXP_PRICIPAL,   " +
		"        TRUNC(REC.dat_origem + VAL.num_dias_exp_bonus)         AS DAT_EXP_BONUS,      " +
		"        TRUNC(REC.dat_origem + VAL.num_dias_exp_sms)           AS DAT_EXP_SMS,        " +
		"        TRUNC(REC.dat_origem + VAL.num_dias_exp_gprs)          AS DAT_EXP_DADOS       " +
		"  FROM                                                                                " +
		"  		tbl_rec_recargas  REC,                                                         " +
		"  		tbl_apr_assinante ASS,                                                         " +
		"  		tbl_rec_valores VAL,                                                           " +
		"  		tbl_pro_assinante PRO                                                          " +
		"        WHERE                                                                         " +
		"        	REC.vlr_pago = VAL.id_valor                                                " +
		"        AND PRO.idt_promocao IN                                                       " +
		"            (SELECT DISTINCT a.idt_promocao                                           " +
		"    		  FROM tbl_pro_dia_execucao a                                              " +
		"    		  WHERE a.num_dia_execucao = TO_NUMBER (TO_CHAR (sysdate, 'DD'))           " +
		"    		)                                                                          " +
		"        AND ASS.idt_msisdn = PRO.idt_msisdn                                           " +
		"        AND ASS.idt_msisdn = REC.idt_msisdn                                           " +
		"        AND ASS.idt_status <> ?                                                       " +
		"        AND REC.id_tipo_recarga = 'R'                                                 " +
		"        AND REC.dat_origem >= TO_DATE(?,'DD/MM/YYYY')                                 " +
		"        AND REC.dat_origem <  TO_DATE(?,'DD/MM/YYYY')+1                               " ;

	//Construtores.
    
    /**
     *	Construtor da classe.
     *
     *	@param		long					idLog						Identificador de LOG.
     */
	public AutomatizacaoRebarbaProdutor(long logId)
	{ 
		super(logId, Definicoes.CL_AUTO_REBARBA_PROD);
		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
	}

	//Implementacao de Produtor.
	
    /**
     *	Inicia a execucao do processo de  automatizacao da rebarba. O metodo executa a selecao de 
     *	registros que sao processados pelas threads consumidoras.
     *
     *	@param		String[]				params						Lista de parametros. Nao utilizado.
     *	@throws		Exception
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");
		String dataProcessada = params[0];
		try
		{
			this.conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.logId);
			super.log(Definicoes.INFO, "execute", "Processando Automatizacao da rebarba");

			String sqlQuery = SQL_SELECAO;
			Object[] parametros = {new Integer(Definicoes.STATUS_NORMAL_USER),dataProcessada,dataProcessada};
			//Executando a consulta.
			this.result = this.conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "startup", "Excecao: " + e);
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Excecao: " + e;
		}
		finally
		{
			super.log(Definicoes.INFO, "startup", "Fim");
		}
	}

    /**
	 * Retorna para as threads consumidoras o proximo registro a ser processado.
	 * 
	 * @param Object
	 *            params Lista de parametros. Nao utilizado.
	 * @throws Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		super.log(Definicoes.DEBUG, "next", "Inicio");
		AutomatizacaoRebarbaVO VO = null;
		try
		{
			if (this.result.next())
			{
				 VO = new AutomatizacaoRebarbaVO(
						 this.result.getString("MSISDN"),
						 this.result.getDate("DAT_EXP_PRICIPAL"),
						 this.result.getDate("DAT_EXP_BONUS"),
						 this.result.getDate("DAT_EXP_SMS"),
						 this.result.getDate("DAT_EXP_DADOS")
				 	  );
				 this.numRegistros++;
				 return VO;
			}
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "next", "Fim");
		}
		
		return VO;
	}
	
    /**
     *	Fecha a selecao de registros e termina a execucao do processo. 
     *
     *	@throws		Exception
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "Produtor.finish", "Inicio");

		try
		{
		    this.finalizacoes();
		    this.mensagem = this.mensagem.concat(String.valueOf(this.numRegistros));
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "Produtor.finish", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		}
		finally
		{
			//Liberando a conexao
			this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep,super.logId);
		    super.log(Definicoes.INFO, "Produtor.finish", "Fim");
		}
	}

	/**
     *	Realiza as finalizacoes necessarias dentro do finish.
     *	Nao acrescenta as informacoes de log nos devidos arquivos. 
     *
     *	@throws		Exception
     */
	private void finalizacoes() throws Exception
	{
		//Chamar o processo B056 no shell script
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
	
	//Implementacao de ProcessoBatchProdutor.
    /**
     *	Retorna o identificador do processo batch. 
     *
     *	@return		int													Identificador do processo batch.
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_AUTO_REBARBA;
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