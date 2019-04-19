package com.brt.gpp.aplicacoes.contabil.consolidacaoContabil;
 
//Imports Java.
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPeriodoContabil;
import com.brt.gpp.comum.mapeamentos.entidade.PeriodoContabil;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *  Classe responsavel pelo gerenciamento do processo de Consolidacao Contabil por dia.
 *
 *	@author	Magno Batista Correa, Bernardo Vergne Dias, Daniel Ferreira
 *	Criado em 23/11/2007
 */
public class ConsolidacaoContabilDiariaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
    /** acumulador de dias jah processados */
    private int							  accDias;
    private String						  status;
    private	String						  mensagem;
 	private PeriodoContabil               periodoContabil; 
	private Date                          dataReferencia;
    
    /** Lista de datas com todos os periodos dias (data) do periodo contabil */
	private Iterator                      datasProcessamento;

	/**
	 * Este metodo realiza a verificacao de parametros
     * 
	 * @param params Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException, Exception
	 */
	private void validaParametro(String params[]) throws GPPInternalErrorException, Exception
	{
		// uma data valida
		if (params == null || params.length == 0 ||params[0] == null)
		{
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Parametro de data (dd/mm/aaaa) obrigatorio para o processo. " + this.mensagem;
			throw new GPPInternalErrorException(this.mensagem);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			this.dataReferencia    = sdf.parse(params[0]);
		}
		catch(ParseException pe)
		{
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Data invalida ou esta em formato invalido. Valor: " + params[0] + ". " + this.mensagem;
			throw new GPPInternalErrorException(this.mensagem);
		}
		periodoContabil = MapPeriodoContabil.getInstance().getPeriodoContabil(this.dataReferencia);
		if(periodoContabil.isIndFechado())
		{
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Periodo contabil fechado! Nao pode ser reprocessado. " + this.mensagem;
			throw new GPPInternalErrorException(this.mensagem);
		}

	}
    
    /**
     *	@param	idLog Identificador de LOG.
     */
	public ConsolidacaoContabilDiariaProdutor(long logId)
	{ 
		super(logId, "ConsolidacaoContabilDiariaProdutor");
		this.accDias        = 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de dias processados: ";
	}

	//Implementacao de Produtor.

    /**
     *	Inicia a execucao do processo de Consolidacao Contabil diaria.
     *  O metodo executa a selecao dos dias do periodo contabil que sao 
     *	processados pelas threads consumidoras.
     *
     *	@param		params						Lista de parametros. Não utilizado pelo processo.
     *	@throws		Exception
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "Produtor.startup", "Inicio do processo de Consolidacao Contabil Diaria");
		
		// Executa a verificacao de parametros
		validaParametro(params);
		this.datasProcessamento = MapPeriodoContabil.getInstance().getListaDatasAteReferencia(this.dataReferencia).iterator();
		
		super.log(Definicoes.INFO, "Produtor.startup", "Consolidacao Contabil Diaria iniciado com sucesso. Parametro: "+params[0]);
	}

    /**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{        
        if (!this.datasProcessamento.hasNext())
            return null;
      
        Date dataAProcessar = (Date)this.datasProcessamento.next();
        return new ConsolidacaoContabilDiariaVO(dataAProcessar, periodoContabil.getIdtPeriodoContabil());
	}
	
    /**
     *  @see        com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento()
    {
        return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
    }
    
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
	public void finish() throws Exception
	{
	    this.mensagem = this.mensagem.concat(String.valueOf(this.accDias));
	    super.log(Definicoes.INFO, "Produtor.finish", "Fim do produtor da Consolidacao Contabil Diaria. Status: "+this.status);
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
	public void handleException()
	{
		this.status = Definicoes.PROCESSO_ERRO;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_CONSOLIDACAO_CONTABIL_DIARIA;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
	public PREPConexao getConexao()
	{
	    return null;
	}
}