package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * Modela as informações de Periodo Contabil
 * @author Geraldo Palmeira
 * @since 12/12/2006
 */
public class PeriodoContabil 
{

	private String idtPeriodoContabil;
	private Date   datInicioPeriodo;
	private Date   datFinalPeriodo;
	private char   indFechado;
	
	/**
	 * @return the datFinalPeriodo
	 */
	public Date getDatFinalPeriodo()
	{
		return datFinalPeriodo;
	}
	/**
	 * @param datFinalPeriodo the datFinalPeriodo to set
	 */
	public void setDatFinalPeriodo(Date datFinalPeriodo)
	{
		this.datFinalPeriodo = datFinalPeriodo;
	}
	/**
	 * @return the datInicioPeriodo
	 */
	public Date getDatInicioPeriodo()
	{
		return datInicioPeriodo;
	}
	/**
	 * @param datInicioPeriodo the datInicioPeriodo to set
	 */
	public void setDatInicioPeriodo(Date datInicioPeriodo)
	{
		this.datInicioPeriodo = datInicioPeriodo;
	}
	/**
	 * @return the idtPeriodoContabil
	 */
	public String getIdtPeriodoContabil()
	{
		return idtPeriodoContabil;
	}
	/**
	 * @param idtPeriodoContabil the idtPeriodoContabil to set
	 */
	public void setIdtPeriodoContabil(String idtPeriodoContabil)
	{
		this.idtPeriodoContabil = idtPeriodoContabil;
	}
	/**
	 * @return the indFechado
	 */
	public char getIndFechado()
	{
		return indFechado;
	}
	/**
	 * @param indFechado the indFechado to set
	 */
	public void setIndFechado(char indFechado)
	{
		this.indFechado = indFechado;
	}
}
