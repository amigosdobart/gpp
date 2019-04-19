package com.brt.gpp.aplicacoes.promocao.gerentePromocao;

/**
 *	Value Object para consumo no processo de gerenciamento de promocoes de assinantes pendentes de primeira recarga.
 * 
 *	@author		Daniel Ferreira
 *	@since		20/12/2005
 */
public class GerenciadorPendenciaRecargaVO
{

    //Atributos.
    
	/**
	 *	MSISDN do assinante.
	 */
	private String msisdn;
	
	/**
	 *	Identificador da promocao do assinante.
	 */
	private int idPromocao;
	
	/**
	 *	Indicador de que o assinante fez recarga no seu ultimo ciclo de vida.
	 */
	private boolean fezRecarga;
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 */
	public GerenciadorPendenciaRecargaVO()
	{
	    this.msisdn		= null;
	    this.idPromocao	= -1;
	    this.fezRecarga	= false;
	}
	
	//Getters.
	
	/**
	 *	Retorna o MSISDN do assinante.
	 * 
	 *	@return		MSISDN do assinante.
	 */
	public String getMsisdn() 
	{
		return this.msisdn;
	}
	
	/**
	 *	Retorna o identificador da promocao do assinante.
	 * 
	 *	@return		Identificador da promocao do assinante.
	 */
	public int getIdPromocao() 
	{
		return this.idPromocao;
	}
	
	/**
	 *	Retorna o indicador de que o assinante realizou uma recarga em seu ultimo ciclo de vida. 
	 * 
	 *	@return		Indicador de que o assinante realizou uma recarga em seu ultimo ciclo de vida.
	 */
	public boolean getFezRecarga() 
	{
		return this.fezRecarga;
	}
	
	//Setters.
	
	/**
	 *	Atribui o MSISDN do assinante.
	 * 
	 *	@param		msisdn					MSISDN do assinante.
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
	
	/**
	 *	Atribui o identificador da promocao do assinante.
	 * 
	 *	@param		idPromocao				Identificador da promocao do assinante.
	 */
	public void setIdPromocao(int idPromocao) 
	{
		this.idPromocao = idPromocao;
	}
	
	/**
	 *	Atribui o indicador de que o assinante realizou uma recarga em seu ultimo ciclo de vida. 
	 * 
	 *	@return		fezRecarga				Indicador de que o assinante realizou uma recarga em seu ultimo ciclo de vida.
	 */
	public void setFezRecarga(boolean fezRecarga) 
	{
		this.fezRecarga = fezRecarga;
	}
	
	//Outros metodos.
	
	/**
	 *	Indica se o assinante realizou uma recarga em seu ultimo ciclo de vida.
	 *
	 *	@return		True se o assinante realizou uma recarga e false caso contrario.
	 */
	public boolean fezRecarga()
	{
	    return this.fezRecarga;
	}

}
