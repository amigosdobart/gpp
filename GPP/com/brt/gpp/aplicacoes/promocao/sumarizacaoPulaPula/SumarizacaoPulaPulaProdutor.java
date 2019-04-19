package com.brt.gpp.aplicacoes.promocao.sumarizacaoPulaPula;
 
//Imports Java.
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//Imports GPP.
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pelo gerenciamento do processo de Sumarizacao Pula-Pula.
 *	Esta classe recebe uma série de MSISDNs de assinantes de promoções ordenados
 *por CN e passa a responsabilidade de processar essa informação para os consumidores.
 *	Por fim faz uma atualização com os dados sumarizados.
 *
 *	@author	Magno Batista Corrêa
 *	@since	18/04/2005 (dd/mm/yyyy)
 *
 *
 *	Seleciona os CNs e as promoções para pré-pagos.
 *
 *	@author	Marcelo Alves Araujo
 *	@since	08/01/2007
 */
public class SumarizacaoPulaPulaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{

    private ResultSet					selecao;
    private int							numRegistros;
    private String						status;
    private	String						mensagem;
    private PREPConexao					conexaoPrep;

    private static final String SQL_SUMARIZACAO = "SELECT DISTINCT                            "
										    	+ " a.idt_promocao PROMOCAO,                  "
										    	+ " d.idt_codigo_nacional CN                  "
										    	+ "FROM                                       "
										    	+ " tbl_pro_promocao a,                       "
										    	+ " tbl_pro_plano_preco b,                    "
										    	+ " tbl_ger_plano_preco c,                    "
										    	+ " tbl_pro_codigo_nacional d                 "
										    	+ "WHERE                                      "
										    	+ "     a.idt_categoria = ?                   "
										    	+ " AND a.idt_promocao = b.idt_promocao       "
										    	+ " AND b.idt_plano_preco = c.idt_plano_preco "
										    	+ " AND c.idt_categoria = ?                   "
										    	+ " AND d.idt_promocao = a.idt_promocao       ";
    
    /**
     *	Construtor da classe.
     *
     *	@param		long					idLog						Identificador de LOG.
     */
	public SumarizacaoPulaPulaProdutor(long logId)
	{
		super(logId, Definicoes.CL_SUMARIZACAO_PULA_PULA_PROD);
		
		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
	}

	/**
     *	Inicia a execucao do processo de Sumarizacao do Pula-Pula. 
     *  O metodo seleciona todas as promoções pula-pula para pré-pagos
     *  para cada CN.
     *
     *	@param		String[]				params						Nao utilizado.
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
	public void startup(String[] params)
	{
		super.log(Definicoes.INFO, "startup", "Inicio");

		try 
		{
			// Executando o processo para cada Promocao.
			this.conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.logId);
			this.conexaoPrep.setAutoCommit(false);
			
			Object [] paramSumarizacao = {	Integer.toString(Definicoes.CATEGORIA_PULA_PULA),
											Integer.toString(Definicoes.CATEGORIA_PREPAGO)};
			
			selecao = conexaoPrep.executaPreparedQuery(	SumarizacaoPulaPulaProdutor.SQL_SUMARIZACAO, 
														paramSumarizacao, 
														super.getIdLog());
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
	 * @throws Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		super.log(Definicoes.DEBUG, "next", "Inicio");

		SumarizacaoPulaPulaVO VO = null;
		
		try
		{
		    if(selecao.next())
		    {
			    VO = new SumarizacaoPulaPulaVO(selecao.getInt("Promocao"), selecao.getString("CN"));
		        numRegistros++;
		    }
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "next", "Fim");
		}
		
		return VO;
	}
	
    /**
     *	Fecha o resultSet e libera as conexões. 
     *
     *	@throws		Exception
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
	public void finish() throws Exception
	{
		conexaoPrep.commit();
		conexaoPrep.setAutoCommit(true);
		selecao.close();
		
		super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
	}

	/**
     *	Executa rollback caso ocorra erros na inserção. 
     *
     *	@throws		Exception
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
	public void handleException()
	{
		try
        {
			this.status = Definicoes.PROCESSO_ERRO;
            conexaoPrep.rollback();
        }
        catch(SQLException e)
        {
            super.log(Definicoes.DEBUG, "Produtor.handleException", "Erro ao executar rollback: " + e);
        }
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
	    return Definicoes.IND_SUMARIZACAO_PULA_PULA;
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