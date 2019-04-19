package com.brt.gpp.aplicacoes.campanha.entidade;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;

/**
 *	Informacoes de assinantes, com informacoes complementares referentes a subida de TSD.
 * 
 *	@author		Daniel Ferreira
 *	@since		31/10/2006
 */
public class AssinanteTSD extends Assinante 
{
	
	/**
	 *	Data de subida do TSD.
	 */
	private Date dataSubidaTSD;
	
	/**
	 *	ICCID do SIMCard utilizado na subida de TSD.
	 */
	private String iccid;
	
	/**
	 *	IMEI do aparelho utilizado na subida de TSD.
	 */
	private String imei;
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 */
	public AssinanteTSD()
	{
		super();
		
		this.dataSubidaTSD	= null;
		this.iccid			= null;
		this.imei			= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna a data de subida do TSD.
	 *
	 *	@return		Data de subida do TSD.
	 */
	public Date getDataSubidaTSD()
	{
		return this.dataSubidaTSD;
	}
	
	/**
	 *	Retorna o ICCID do SIMCard utilizado na subida de TSD.
	 *
	 *	@return		ICCID do SIMCard utilizado na subida de TSD.
	 */
	public String getICCID()
	{
		return this.iccid;
	}
	
	/**
	 *	Retorna o IMEI do aparelho utilizado na subida de TSD.
	 *
	 *	@return		IMEI do aparelho utilizado na subida de TSD.
	 */
	public String getIMEI()
	{
		return this.imei;
	}
	
	//Setters.
	
	/**
	 *	Atribui a data de subida do TSD.
	 *
	 *	@param		dataSubidaTSD			Data de subida do TSD.
	 */
	public void setDataSubidaTSD(Date dataSubidaTSD)
	{
		this.dataSubidaTSD = dataSubidaTSD;
	}
	
	/**
	 *	Atribui o ICCID do SIMCard utilizado na subida de TSD.
	 *
	 *	@param		iccid					ICCID do SIMCard utilizado na subida de TSD.
	 */
	public void setICCID(String iccid)
	{
		this.iccid = iccid;
	}
	
	/**
	 *	Atribui o IMEI do aparelho utilizado na subida de TSD.
	 *
	 *	@param		imei					IMEI do aparelho utilizado na subida de TSD.
	 */
	public void setIMEI(String imei)
	{
		this.imei = imei;
	}
	
	//Outros metodos.
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return "MSISDN: " + this.getMSISDN() + " - Data TSD: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.dataSubidaTSD); 
	}
	
}
