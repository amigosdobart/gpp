package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Entidade abstrata base para a definicao de valores de insercao de creditos em saldos do assinante na plataforma.
 *
 *	@version	1.0		30/04/2007		Primeira versao.
 *	@author		Daniel Ferreira
 */
public abstract class ValorCredito implements Serializable
{

	/**
	 *	Informacoes do tipo de saldo.
	 */
	private TipoSaldo tipoSaldo;
	
	/**
	 *	Valor de credito da recarga.
	 */
	private double vlrCredito;
	
    public ValorCredito()
    {
    }
    
	/**
	 *	Construtor da classe.
	 *
	 *	@param		tipoSaldo				Informacoes do tipo de saldo.
	 *	@param		vlrCredito				Valor de credito da recarga.
	 */
	public ValorCredito(TipoSaldo tipoSaldo, double vlrCredito)
	{
		this.tipoSaldo	= tipoSaldo;
		this.vlrCredito	= vlrCredito;
	}
	
	/**
	 *	Retorna as informacoes do tipo de saldo.
	 *
	 *	@return		Informacoes do tipo de saldo.
	 */
	public TipoSaldo getTipoSaldo()
	{
		return this.tipoSaldo;
	}
	
	/**
	 *	Retorna o valor de credito da recarga.
	 *
	 *	@return		Valor de credito da recarga.
	 */
	public double getVlrCredito()
	{
		return this.vlrCredito;
	}
	
	public void setTipoSaldo(TipoSaldo tipoSaldo)
    {
        this.tipoSaldo = tipoSaldo;
    }

    public void setVlrCredito(double vlrCredito)
    {
        this.vlrCredito = vlrCredito;
    }

    /**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		
		if(this.tipoSaldo != null)
			result.append(this.tipoSaldo.toString());
		
		result.append(" - Valor: " + this.vlrCredito);
		
		return result.toString();
	}
	
}
