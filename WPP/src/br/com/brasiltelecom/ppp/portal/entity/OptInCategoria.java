package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela as informações de Opt-in Categoria das Tabelas de Hsid
 * @author Geraldo Palmeira
 * @since 01/09/2006
 */
public class OptInCategoria 
{

	private String msisdn;
	private int    idCategoria;
	/**
	 * @return Retorna o idCategoria.
	 */
	public int getIdCategoria() 
	{
		return idCategoria;
	}
	/**
	 * @param idCategoria The idCategoria to set.
	 */
	public void setIdCategoria(int idCategoria) 
	{
		this.idCategoria = idCategoria;
	}
	/**
	 * @return Retorna o msisdn.
	 */
	public String getMsisdn() 
	{
		return msisdn;
	}
	/**
	 * @param msisdn Msisdn.
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
}
