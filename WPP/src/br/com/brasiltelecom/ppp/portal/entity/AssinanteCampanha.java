package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Esta classe armazena as informacoes da inscricao de assinantes em campanhas 
 * promocionais. Estas informacoes serao utilizadas para tratamento da concessao de 
 * creditos.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class AssinanteCampanha 
{
	private long   idCampanha;
	private String msisdn;
	private Date   dataInclusao;
	private Date   dataRetiradaCampanha;
	private Date   dataUltimoSMS;
	private String xmlDocument;
	private Campanha campanha;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * Access method for the msisdn property.
	 * 
	 * @return   the current value of the msisdn property
	 */
	public String getMsisdn() 
	{
		return msisdn;    
	}
	
	/**
	 * Sets the value of the msisdn property.
	 * 
	 * @param aMsisdn the new value of the msisdn property
	 */
	public void setMsisdn(String aMsisdn) 
	{
		msisdn = aMsisdn;    
	}
	
	/**
	 * @return Retorna o msisdn Formatado (XX) 84XX-XXXX.
	 */
	public String getFMsisdn() 
	{
		return "(" + msisdn.substring(2,4) + ") " + msisdn.substring(4,8) + "-"+ msisdn.substring(8,12);
	}
	
	/**
	 * Access method for the dataInclusao property.
	 * 
	 * @return   the current value of the dataInclusao property
	 */
	public Date getDataInclusao() 
	{
		return dataInclusao;    
	}
	
	public String getDataInclusaoStr()
	{
		if (getDataInclusao() != null)
			return sdf.format(getDataInclusao());
		return "";
	}
	
	/**
	 * Sets the value of the dataInclusao property.
	 * 
	 * @param aDataInclusao the new value of the dataInclusao property
	 */
	public void setDataInclusao(Date aDataInclusao) 
	{
		dataInclusao = aDataInclusao;    
	}
	
	/**
	 * Sets the value of the dataUltimoSMS property.
	 * 
	 * @param aDataUltimoSMS the new value of the dataInclusao property
	 */
	public void setDataUltimoSMS(Date aDataUltimoSMS) 
	{
		dataUltimoSMS = aDataUltimoSMS;    
	}
	
	/**
	 * Access method for the dataUltimoSMS property.
	 * 
	 * @return   the current value of the dataUltimoSMS property
	 */
	public Date getDataUltimoSMS() 
	{
		return dataUltimoSMS;    
	}
	
	public String getDataUltimoSMSStr()
	{
		if (getDataUltimoSMS() != null)
			return sdf.format(getDataUltimoSMS());
		return "";
	}
	
	/**
	 * Access method for the dataRetiradaCampanha property.
	 * 
	 * @return   the current value of the dataRetiradaCampanha property
	 */
	public Date getDataRetiradaCampanha() 
	{
		return dataRetiradaCampanha;    
	}
	
	public String getDataRetiradaCampanhaStr()
	{
		if (getDataRetiradaCampanha() != null)
			return sdf.format(getDataRetiradaCampanha());
		return "";
	}
	
	/**
	 * Sets the value of the dataRetiradaCampanha property.
	 * 
	 * @param aDataRetiradaCampanha the new value of the dataRetiradaCampanha property
	 */
	public void setDataRetiradaCampanha(Date aDataRetiradaCampanha) 
	{
		dataRetiradaCampanha = aDataRetiradaCampanha;    
	}
	
	/**
	 * Access method for the campanha property.
	 * 
	 * @return   the current value of the campanha property
	 */
	public Campanha getCampanha() 
	{
		return campanha;    
	}
	
	/**
	 * Sets the value of the campanha property.
	 * 
	 * @param aCampanha the new value of the campanha property
	 */
	public void setCampanha(Campanha aCampanha) 
	{
		campanha = aCampanha;
	}

	/**
	 * @return Returns the idCampanha.
	 */
	public long getIdCampanha() 
	{
		return idCampanha;
	}

	/**
	 * @param idCampanha The idCampanha to set.
	 */
	public void setIdCampanha(long aIdCampanha) 
	{
		idCampanha = aIdCampanha;
	}

	/**
	 * @return Returns the xmlDocument.
	 */
	public String getXmlDocument() 
	{
		return xmlDocument;
	}

	/**
	 * @param xmlDocument The xmlDocument to set.
	 */
	public void setXmlDocument(String xmlDocument) 
	{
		this.xmlDocument = xmlDocument;
	}
}
