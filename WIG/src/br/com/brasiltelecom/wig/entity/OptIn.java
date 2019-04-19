package br.com.brasiltelecom.wig.entity;

import java.util.Date;
import br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptIn;
import br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptOut;

public class OptIn
{
	private String 			msisdn;
	private Date 			dataOptIn;
	private Date 			dataFidelizacao;
	private Date 			dataOptOut;
	private long 			lac;
	private long			cellId;
	private int 			coModelo;
	private boolean			isMandatorio;
	private int				coConteudo;
	private ValidadorOptIn 	validadorOptIn;
	private ValidadorOptOut validadorOptOut;
	private String			operador;
	
	/**
	 * @return Returns the cellId.
	 */
	public long getCellId()
	{
		return cellId;
	}
	/**
	 * @return Returns the coModelo.
	 */
	public int getCoModelo()
	{
		return coModelo;
	}
	/**
	 * @return Returns the dataFidelizacao.
	 */
	public Date getDataFidelizacao()
	{
		return dataFidelizacao;
	}
	/**
	 * @return Returns the dataOptIn.
	 */
	public Date getDataOptIn()
	{
		return dataOptIn;
	}
	/**
	 * @return Returns the dataOptOut.
	 */
	public Date getDataOptOut()
	{
		return dataOptOut;
	}
	/**
	 * @return Returns the lac.
	 */
	public long getLac()
	{
		return lac;
	}
	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn()
	{
		return msisdn;
	}
	/**
	 * @return Returns the validadorOptIn.
	 */
	public ValidadorOptIn getValidadorOptIn()
	{
		return validadorOptIn;
	}
	/**
	 * @return Returns the validadorOptOut.
	 */
	public ValidadorOptOut getValidadorOptOut()
	{
		return validadorOptOut;
	}
	/**
	 * @return Returns the operador.
	 */
	public String getOperador()
	{
		return operador;
	}
	
	/**
	 * @param cellId The cellId to set.
	 */
	public void setCellId(long cellId)
	{
		this.cellId = cellId;
	}
	/**
	 * @param coModelo The coModelo to set.
	 */
	public void setCoModelo(int coModelo)
	{
		this.coModelo = coModelo;
	}
	/**
	 * @param dataFidelizacao The dataFidelizacao to set.
	 */
	public void setDataFidelizacao(Date dataFidelizacao)
	{
		this.dataFidelizacao = dataFidelizacao;
	}
	/**
	 * @param dataOptIn The dataOptIn to set.
	 */
	public void setDataOptIn(Date dataOptIn)
	{
		this.dataOptIn = dataOptIn;
	}
	/**
	 * @param dataOptOut The dataOptOut to set.
	 */
	public void setDataOptOut(Date dataOptOut)
	{
		this.dataOptOut = dataOptOut;
	}
	/**
	 * @param lac The lac to set.
	 */
	public void setLac(long lac)
	{
		this.lac = lac;
	}
	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	/**
	 * @param validadorOptIn The validadorOptIn to set.
	 */
	public void setValidadorOptIn(ValidadorOptIn validadorOptIn)
	{
		this.validadorOptIn = validadorOptIn;
	}
	/**
	 * @param validadorOptOut The validadorOptOut to set.
	 */
	public void setValidadorOptOut(ValidadorOptOut validadorOptOut)
	{
		this.validadorOptOut = validadorOptOut;
	}
	/**
	 * @return Returns the isMandatorio.
	 */
	public boolean isMandatorio()
	{
		return isMandatorio;
	}
	/**
	 * @param isMandatorio The isMandatorio to set.
	 */
	public void setMandatorio(boolean isMandatorio)
	{
		this.isMandatorio = isMandatorio;
	}
	/**
	 * @return Returns the coConteudo.
	 */
	public int getCoConteudo()
	{
		return coConteudo;
	}
	/**
	 * @param coConteudo The coConteudo to set.
	 */
	public void setCoConteudo(int coConteudo)
	{
		this.coConteudo = coConteudo;
	}
	
	/**
	 * @param coConteudo The coConteudo to set.
	 */
	public void setOperador(String operador) 
	{
		this.operador = operador;
	}

}
 