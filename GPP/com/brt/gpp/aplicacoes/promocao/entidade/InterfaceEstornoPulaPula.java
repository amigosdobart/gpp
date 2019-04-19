package com.brt.gpp.aplicacoes.promocao.entidade;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapPromocaoOrigemEstorno;

/**
 *	Classe que representa a entidade da tabela TBL_INT_ESTORNO_PULA_PULA.
 *
 *	@author	Daniel Ferreira
 *	@since	15/12/2005
 */

public class InterfaceEstornoPulaPula
{
		
	/**
	 *	Constante referente a data em que as ligacoes indevidas foram efetuadas.
	 */
	public static final int DAT_REFERENCIA = 0;
	
	/**
	 *	Constante referente a data de cadastro da requisicao de expurgo/estorno.
	 */
	public static final int DAT_CADASTRO = 1;
	
	/**
	 *	Constante referente a data de processamento das ligacoes indevidas.
	 */
	public static final int DAT_PROCESSAMENTO = 2;
	
	/**
	 *	Identificador do lote de requisicao de expurgo/estorno.
	 */
	private String idtLote;
	
	/**
	 *	Data de referencia de execucao das ligacoes indevidas.
	 */
	private Date datReferencia;
	
	/**
	 *	MSISDN do assinante que recebeu as ligacoes indevidas.
	 */
	private String idtMsisdn;
	
	/**
	 *	Numero originador das ligacoes indevidas.
	 */
	private String idtNumeroOrigem;
	
	/**
	 *	Identificador da natureza de origem das ligacoes indevidas.
	 */
	private String idtOrigem;
	
	/**
	 *	Data de cadastro da requisicao de expurgo/estorno.
	 */
	private Timestamp datCadastro;
	
	/**
	 *	Data de processamento do expurgo/estorno.
	 */
	private Timestamp datProcessamento;
	
	/**
	 *	Identificador do status de processamento da requisicao de expurgo/estorno.
	 */
	private String idtStatusProcessamento;
	
	/**
	 *	Codigo de retorno da operacao.
	 */
	private int idtCodigoRetorno;
	
	/**
	 *	Construtor da classe.
	 */
	public InterfaceEstornoPulaPula()
	{
		this.idtLote				= null;
	    this.datReferencia			= null;
		this.idtMsisdn				= null;
		this.idtNumeroOrigem		= null;
		this.idtOrigem				= null;
		this.datCadastro			= null;
		this.datProcessamento		= null;
		this.idtStatusProcessamento	= null;
		this.idtCodigoRetorno		= -1;
	}
	
	/**
	 *	Retorna o identificador do lote de requisicao de expurgo/estorno.
	 * 
	 *	@return		Identificador do lote de requisicao de expurgo/estorno.
	 */
	public String getIdtLote() 
	{
		return this.idtLote;
	}
	
	/**
	 *	Retorna a data de referencia das chamadas.
	 * 
	 *	@return		Data de referencia das chamadas.
	 */
	public Date getDatReferencia() 
	{
		return this.datReferencia;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 * 
	 *	@return		MSISDN do assinante.
	 */
	public String getIdtMsisdn() 
	{
		return this.idtMsisdn;
	}
	
	/**
	 *	Retorna o numero de origem da ligacao.
	 * 
	 *	@return		Numero de origem da ligacao.
	 */
	public String getIdtNumeroOrigem() 
	{
		return this.idtNumeroOrigem;
	}
	
	/**
	 *	Retorna o tipo de estorno. Ate a data da implementacao foram definidos dois tipos de estorno: FRAUDE e TUP.
	 *	O primeiro identifica as ligacoes que sao suspeitas de serem fraudulentas. O segundo identifica ligacoes 
	 *	feitas com a utilizacao de orelhoes. 
	 * 
	 *	@return		Tipo de estorno.
	 */
	public String getIdtOrigem() 
	{
		return this.idtOrigem;
	}
	
	/**
	 *	Retorna a data de cadastro do registro.
	 * 
	 *	@return		Data de cadastro do registro.
	 */
	public Timestamp getDatCadastro() 
	{
		return this.datCadastro;
	}
	
	/**
	 *	Retorna a data de processamento do registro.
	 * 
	 *	@return		Data de processamento do registro.
	 */
	public Timestamp getDatProcessamento() 
	{
		return this.datProcessamento;
	}
	
	/**
	 *	Retorna o status de processamento do registro.
	 * 
	 *	@return		Status de processamento do registro.
	 */
	public String getIdtStatusProcessamento() 
	{
		return this.idtStatusProcessamento;
	}
	
	/**
	 *	Retorna o codigo de retorno do processamento do registro.
	 * 
	 *	@return		Codigo de retorno do processamento do registro.
	 */
	public int getIdtCodigoRetorno() 
	{
		return this.idtCodigoRetorno;
	}
	
	/**
	 *	Atribui o identificador do lote de requisicao de expurgo/estorno.
	 * 
	 *	@param		idtLote					Identificador do lote de requisicao de expurgo/estorno.
	 */
	public void setIdtLote(String idtLote) 
	{
		this.idtLote = idtLote;
	}
	
	/**
	 *	Atribui a data de referencia das chamadas.
	 * 
	 *	@param		datReferencia			Data de referencia das chamadas.
	 */
	public void setDatReferencia(Date datReferencia) 
	{
		this.datReferencia = datReferencia;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 * 
	 *	@param		idtMsisdn				MSISDN do assinante.
	 */
	public void setIdtMsisdn(String idtMsisdn) 
	{
		this.idtMsisdn = idtMsisdn;
	}
	
	/**
	 *	Atribui o numero de origem da ligacao.
	 * 
	 *	@param		idtNumeroOrigem			Numero de origem da ligacao.
	 */
	public void setIdtNumeroOrigem(String idtNumeroOrigem) 
	{
		this.idtNumeroOrigem = idtNumeroOrigem;
	}
	
	/**
	 *	Atribui o tipo de estorno. Ate a data da implementacao foram definidos dois tipos de estorno: FRAUDE e TUP.
	 *	O primeiro identifica as ligacoes que sao suspeitas de serem fraudulentas. O segundo identifica ligacoes 
	 *	feitas com a utilizacao de orelhoes. 
	 * 
	 *	@param		idtOrigem				Tipo de estorno.
	 */
	public void setIdtOrigem(String idtOrigem) 
	{
		this.idtOrigem = idtOrigem;
	}
	
	/**
	 *	Atribui a data de cadastro do registro.
	 * 
	 *	@param		datCadastro				Data de cadastro do registro.
	 */
	public void setDatCadastro(Timestamp datCadastro) 
	{
		this.datCadastro = datCadastro;
	}
	
	/**
	 *	Atribui a data de processamento do registro.
	 * 
	 *	@param		datProcessamento		Data de processamento do registro.
	 */
	public void setDatProcessamento(Timestamp datProcessamento) 
	{
		this.datProcessamento = datProcessamento;
	}
	
	/**
	 *	Atribui o status de processamento do registro.
	 * 
	 *	@param		idtStatusProcessamento	Status de processamento do registro.
	 */
	public void setIdtStatusProcessamento(String idtStatusProcessamento) 
	{
		this.idtStatusProcessamento = idtStatusProcessamento;
	}
	
	/**
	 *	Atribui o codigo de retorno do processamento do registro.
	 * 
	 *	@param		idtCodigoRetorno		Codigo de retorno do processamento do registro.
	 */
	public void setIdtCodigoRetorno(int idtCodigoRetorno) 
	{
		this.idtCodigoRetorno = idtCodigoRetorno;
	}
    
	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
	 * 
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Valor no formato String.
	 */
    public String format(int campo)
    {
	    switch(campo)
	    {
    		case InterfaceEstornoPulaPula.DAT_REFERENCIA:
	    	    if((this.idtOrigem != null) && (this.datReferencia != null))
	    	    {
	    	        try
	    	        {
		    	        if(MapPromocaoOrigemEstorno.getInstance().getTipAnalise(this.idtOrigem).equals(PromocaoOrigemEstorno.TIP_ANALISE_DIARIO))
		    	            return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(this.datReferencia);
	    	        }
	    	        catch(Exception e)
	    	        {
	    	            return null;
	    	        }

	    	        return new SimpleDateFormat(Definicoes.MASCARA_MES).format(this.datReferencia);
	    	    }
	    	    return null;
	    	case InterfaceEstornoPulaPula.DAT_CADASTRO:
	    	    return (this.datCadastro != null) ? new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(this.datCadastro) : null;
	    	case InterfaceEstornoPulaPula.DAT_PROCESSAMENTO:
	    	    return (this.datProcessamento != null) ? new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(this.datProcessamento) : null;
	    	default: return null;
	    }
    }
    
	/**
	 *	@see		java.lang.Object#toString()
	 */
    public String toString()
    {
    	StringBuffer result = new StringBuffer();
    	
    	result.append("Lote: ");
    	result.append(this.idtLote);
    	result.append(" - ");
    	
    	result.append("Data de Referencia: ");
    	result.append(this.format(InterfaceEstornoPulaPula.DAT_REFERENCIA));
    	result.append(" - ");
    	
    	result.append("MSISDN: ");
    	result.append(this.idtMsisdn);
    	result.append(" - ");
    	
    	result.append("Numero de Origem: ");
    	result.append(this.idtNumeroOrigem);
    	result.append(" - ");
    	
    	result.append("Origem da Requisicao: ");
    	result.append(this.idtOrigem);
    	
    	return result.toString();
    }
    
}
