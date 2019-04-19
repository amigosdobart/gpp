package br.com.brasiltelecom.ppp.portal.entity;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Modela as informações da tabela tbl_ext_happy_manager (Gerente Feliz)
 * @author Geraldo Palmeira
 * @since 27/09/2006
 */
public class HappyManager 
{
	
	private String msisdn;
	private String tipo;
	private String credito;
	private Date dataInclusao;
	private String lote;
	private String origem;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * @return Retorna o credito.
	 */
	public String getCredito() 
	{
		return credito;
	}
	/**
	 * @param credito The credito to set.
	 */
	public void setCredito(String credito) 
	{
		this.credito = credito;
	}
	/**
	 * @return Retorna a dataInclusao.
	 */
	public Date getDataInclusao() 
	{
		return dataInclusao;
	}
	/**
	 * @param dataInclusao The dataInclusao to set.
	 */
	public void setDataInclusao(Date dataInclusao) 
	{
		this.dataInclusao = dataInclusao;
	}
	/**
	 * @param dataInclusao The dataInclusao to set.
	 */
	public String getFDataInclusao() 
	{
		return sdf.format(dataInclusao);
	}
	/**
	 * @return Retorna o lote.
	 */
	public String getLote() 
	{
		return lote;
	}
	/**
	 * @param lote The lote to set.
	 */
	public void setLote(String lote) 
	{
		this.lote = lote;
	}
	/**
	 * @return Retorna o msisdn.
	 */
	public String getMsisdn() 
	{
		return msisdn;
	}
	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
	/**
	 * @return Retorna o msisdn Formatado (XX) 84XX-XXXX.
	 */
	public String getFMsisdn() 
	{
		return "(" + msisdn.substring(2,4) + ") " + msisdn.substring(4,8) + "-"+ msisdn.substring(8,12);
	}
	/**
	 * @return Retorna o origem.
	 */
	public String getOrigem() 
	{
		return origem;
	}
	/**
	 * @param origem The origem to set.
	 */
	public void setOrigem(String origem) 
	{
		this.origem = origem;
	}
	/**
	 * @return Retorna o tipo.
	 */
	public String getTipo() 
	{
		return tipo;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
	
}
