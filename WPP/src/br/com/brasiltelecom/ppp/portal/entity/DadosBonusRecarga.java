/*
 * Created on 04/08/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author André Gonçalves
 * @since 04/08/2004
 */

public class DadosBonusRecarga 
{
	private short numeroRecargas;
	private short percentualBonus;
	
	public DadosBonusRecarga() {}
	
	public DadosBonusRecarga(short aNumeroRecargas, short aPercentualBonus)
	{
		this.numeroRecargas = aNumeroRecargas;
		this.percentualBonus = aPercentualBonus;
	}
	public short getNumeroRecargas()
	{
		return this.numeroRecargas;
	}
	public short getPercentualBonus()
	{
		return this.percentualBonus;
	}
	public void setNumeroRecargas(short aNumeroRecargas)
	{
		this.numeroRecargas = aNumeroRecargas;
	}
	public void setPercentualBonus(short aPercentualBonus)
	{
		this.percentualBonus = aPercentualBonus;
	}
}
