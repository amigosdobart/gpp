package com.brt.gpp.comum.mapeamentos.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Entidade da tabela TBL_REC_TIPOS_CREDITO.
 *
 *	@version		1.0		04/05/2007		Primeira versao.
 *	@author			Daniel Ferreira
 */
public class TipoCredito 
{

	/**
	 *	Identificador do tipo de credito.
	 */
	private String idTipoCredito;
	
	/**
	 *	Descricao do tipo de credito.
	 */
	private String desTipoCredito;
	
	/**
	 *	Tipo de saldo relacionado ao tipo de credito.
	 */
	private TipoSaldo tipoSaldo;

	/**
	 *	Construtor da classe.
	 *
	 *	@param		idTipoCredito			Identificador do tipo de credito.
	 *	@param		desTipoCredito			Descricao do tipo de credito.
	 *	@param		tipoSaldo				Tipo de saldo relacionado ao tipo de credito.
	 */
	public TipoCredito(String idTipoCredito, String desTipoCredito, TipoSaldo tipoSaldo)
	{
		this.idTipoCredito	= idTipoCredito;
		this.desTipoCredito	= desTipoCredito;
		this.tipoSaldo		= tipoSaldo;
	}
	
	
	/**
	 *	Construtor da classe.
	 */
	public TipoCredito()
	{
	}
	
	/**
	 *	Retorna o identificador do tipo de credito.
	 *
	 *	@return		Identificador do tipo de credito.
	 */
	public String getIdTipoCredito()
	{
		return this.idTipoCredito;
	}
	
	/**
	 *	Retorna a descricao do tipo de credito.
	 *
	 *	@return		Descricao do tipo de credito.
	 */
	public String getDesTipoCredito()
	{
		return this.desTipoCredito;
	}
	
	/**
	 *	Retorna o tipo de saldo relacionado ao tipo de credito.
	 *
	 *	@return		Tipo de saldo relacionado ao tipo de credito.
	 */
	public TipoSaldo getTipoSaldo()
	{
		return this.tipoSaldo;
	}

	/**
	 * @param desTipoCredito the desTipoCredito to set
	 */
	public void setDesTipoCredito(String desTipoCredito) 
	{
		this.desTipoCredito = desTipoCredito;
	}

	/**
	 * @param idTipoCredito the idTipoCredito to set
	 */
	public void setIdTipoCredito(String idTipoCredito) 
	{
		this.idTipoCredito = idTipoCredito;
	}

	/**
	 * @param tipoSaldo the tipoSaldo to set
	 */
	public void setTipoSaldo(TipoSaldo tipoSaldo) 
	{
		this.tipoSaldo = tipoSaldo;
	}
	
}
