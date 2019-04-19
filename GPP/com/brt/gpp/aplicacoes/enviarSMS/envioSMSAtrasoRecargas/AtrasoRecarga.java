package com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoRecargas;

/**
 *	Classe que representa as informacoes para processamento dos registros de recarga com atraso. 
 * 
 *	@author	Daniel Ferreira
 *	@since	16/03/2006
 */
public class AtrasoRecarga
{

    private	String	rowId;
    private	String	idtStatusProcessamento;
    private	String	idtOrigem;
	private String	idtMsisdn;
	private String	tipTransacao;
	private String	codRecarga;
	private	double	vlrRecarga;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public AtrasoRecarga()
	{
		this.rowId					= null;
		this.idtStatusProcessamento	= null;
		this.idtOrigem				= null;
		this.idtMsisdn				= null;
		this.tipTransacao			= null;
		this.codRecarga				= null;
		this.vlrRecarga				= 0.0;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador do registro.
	 * 
	 *	@return		String					rowId						Identificador do registro.
	 */
	public String getRowId() 
	{
		return this.rowId;
	}
	
	/**
	 *	Retorna o status de processamento da recarga.
	 * 
	 *	@return		String					idtStatusProcessamento		Status de processamento da recarga.
	 */
	public String getIdtStatusProcessamento() 
	{
		return this.idtStatusProcessamento;
	}
	
	/**
	 *	Retorna o identificador da origem da recarga.
	 * 
	 *	@return		String					idtOrigem					Identificador da origem da recarga.
	 */
	public String getIdtOrigem()
	{
		return this.idtOrigem;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 * 
	 *	@return		String					idtMsisdn					MSISDN do assinante.
	 */
	public String getIdtMsisdn()
	{
		return this.idtMsisdn;
	}
	
	/**
	 *	Retorna o tipo de transacao da recarga.
	 * 
	 *	@return		String					tipTransacao				Tipo de transacao da recarga.
	 */
	public String getTipTransacao()
	{
		return this.tipTransacao;
	}
	
	/**
	 *	Retorna o codigo da recarga.
	 * 
	 *	@return		String					codRecarga					Codigo da recarga.
	 */
	public String getCodRecarga()
	{
		return this.codRecarga;
	}
	
	/**
	 *	Retorna o valor da recarga.
	 * 
	 *	@return		double					vlrRecarga					Valor da recarga.
	 */
	public double getVlrRecarga()
	{
		return this.vlrRecarga;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador do registro.
	 * 
	 *	@param		String					rowId						Identificador do registro.
	 */
	public void setRowId(String rowId) 
	{
		this.rowId = rowId;
	}
	
	/**
	 *	Atribui o status de processamento da recarga.
	 * 
	 *	@param		String					idtStatusProcessamento		Status de processamento da recarga.
	 */
	public void setIdtStatusProcessamento(String idtStatusProcessamento) 
	{
		this.idtStatusProcessamento = idtStatusProcessamento;
	}
	
	/**
	 *	Atribui o identificador da origem da recarga.
	 * 
	 *	@param		String					idtOrigem					Identificador da origem da recarga.
	 */
	public void setIdtOrigem(String idtOrigem)
	{
		this.idtOrigem = idtOrigem;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 * 
	 *	@param		String					idtMsisdn					MSISDN do assinante.
	 */
	public void setIdtMsisdn(String idtMsisdn)
	{
		this.idtMsisdn = idtMsisdn;
	}
	
	/**
	 *	Atribui o tipo de transacao da recarga.
	 * 
	 *	@param		String					tipTransacao				Tipo de transacao da recarga.
	 */
	public void setTipTransacao(String tipTransacao)
	{
		this.tipTransacao = tipTransacao;
	}
	
	/**
	 *	Atribui o codigo da recarga.
	 * 
	 *	@param		String					codRecarga					Codigo da recarga.
	 */
	public void setCodRecarga(String codRecarga)
	{
		this.codRecarga = codRecarga;
	}
	
	/**
	 *	Atribui o valor da recarga.
	 * 
	 *	@param		double					vlrRecarga					Valor da recargas.
	 */
	public void setVlrRecarga(double vlrRecarga)
	{
		this.vlrRecarga = vlrRecarga;
	}
	
}
