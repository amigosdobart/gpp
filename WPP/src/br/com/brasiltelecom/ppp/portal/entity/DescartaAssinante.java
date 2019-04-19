package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela as informações de Assinantes Descartados (BlackList)
 * @author Geraldo Palmeira
 * @since 03/01/2007
 */
public class DescartaAssinante 
{

	private String idtMsisdn;
	private int indMascara;
	
	public String getIdtMsisdn()
	{
		return idtMsisdn;
	}
	
	public void setIdtMsisdn(String idtMsisdn)
	{
		this.idtMsisdn = idtMsisdn;
	}
	
	public int getIndMascara()
	{
		return indMascara;
	}
	
	public void setIndMascara(int indMascara)
	{
		this.indMascara = indMascara;
	}
}