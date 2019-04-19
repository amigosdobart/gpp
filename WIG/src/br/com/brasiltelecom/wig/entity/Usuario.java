package br.com.brasiltelecom.wig.entity;

import java.util.Date;
import java.util.Calendar;

/**
 * Esta classe armazena as informacoes da autenticacao de usuarios
 * 
 * @author Joao Carlos
 * Data..: 14/07/2005
 *
 */
public class Usuario
{
	private String 	msisdn;
	private Date	dataLogon;
	private long	codigoServico;
	
	public Usuario(String msisdn)
	{
		this.msisdn 	= msisdn;
		this.dataLogon 	= Calendar.getInstance().getTime();
	}
	
	public long getCodigoServico()
	{
		return codigoServico;
	}

	public Date getDataLogon()
	{
		return dataLogon;
	}
	
	public String getMsisdn()
	{
		return msisdn;
	}
	
	public void setCodigoServico(long codigoServico)
	{
		this.codigoServico = codigoServico;
	}
	
	public void setDataLogon(Date dataLogon)
	{
		this.dataLogon = dataLogon;
	}
	
	public int hashCode()
	{
		return getMsisdn().hashCode();
	}

	public boolean equals(Object obj)
	{
		if ( !(obj instanceof Usuario) )
			return false;
		
		if ( ((Usuario)obj).getMsisdn().equals(getMsisdn()) )
			return true;
		else return false;
	}
	
	public String toString()
	{
		return getMsisdn();
	}
}
