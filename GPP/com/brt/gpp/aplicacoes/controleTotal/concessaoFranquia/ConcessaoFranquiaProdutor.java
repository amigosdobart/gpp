package com.brt.gpp.aplicacoes.controleTotal.concessaoFranquia;
 
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
 *	Classe responsavel pelo gerenciamento do processo de Concessao de Franquia para o Controle Total.
 *
 *	@author	Magno Batista Corrêa
 *	@since	2007/05/17 (yyyy/mm/dd)
 */
public class ConcessaoFranquiaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{

    private int							numRegistros;
    private String						status;
    private	String						mensagem;
    private PREPConexao					conexaoPrep;
	private ResultSet					result;
	private ConcessaoFranquiaDAO		dao;

	//Construtores.
    
    /**
     *	Construtor da classe.
     *
     *	@param		long					idLog						Identificador de LOG.
     */
	public ConcessaoFranquiaProdutor(long logId)
	{ 
		super(logId, Definicoes.CL_CONCESSAO_FRANQUIA_CONTROLE_TOTAL_PRODUTOR);
		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
	}

	//Implementacao de Produtor.

    /**
     *	Inicia a execucao do processo de  Concessao de Franquia para o Controle Total. O metodo executa a selecao de 
     *	registros que sao processados pelas threads consumidoras.
     *
     *	@param		String[]				params						Lista de parametros. Não utilizado pelo processo.
     *	@throws		Exception
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");
		
				
		// Executando a consulta
		try
		{
			// Obtem conexao
			this.conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.logId);
			super.log(Definicoes.INFO, "execute", "Processando Concessao de Franquia para o Controle Total.");
			// Instancia o DAO
			this.dao = new ConcessaoFranquiaDAO(this.conexaoPrep, super.logId);
			// Monta o ResultSet para a ser consumido
			this.result = dao.getAssinanteParaConcessao();
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
			return dao.getConcessaoFranquiaVO(this.result);
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
	    return Definicoes.IND_CONCESSAO_FRANQUIA_CONTROLE_TOTAL;
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