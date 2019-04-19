package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;

public class ServicoWig implements Serializable
{
	private int codServicoWig;
	private String nomeServicoWig;
	private String desServicoWig;
	private char validaMSISDN;
	private char validaICCID;
	private char validaModelo;
	private char validaIMEI;
	private char validaProfileICCID;
	private char validaConfigDM;
	private char bloqueioConcorrente;
	
	public char getBloqueioConcorrente() 
	{
		return bloqueioConcorrente;
	}

	public void setBloqueioConcorrente(char bloqueioConcorrente) 
	{
		this.bloqueioConcorrente = bloqueioConcorrente;
	}

	public int getCodServicoWig() 
	{
		return codServicoWig;
	}

	public void setCodServicoWig(int codServicoWig) 
	{
		this.codServicoWig = codServicoWig;
	}

	public String getDesServicoWig() 
	{
		return desServicoWig;
	}

	public void setDesServicoWig(String desServicoWig) 
	{
		this.desServicoWig = desServicoWig;
	}

	public String getNomeServicoWig() 
	{
		return nomeServicoWig;
	}

	public void setNomeServicoWig(String nomeServicoWig) 
	{
		this.nomeServicoWig = nomeServicoWig;
	}

	public char getValidaConfigDM() 
	{
		return validaConfigDM;
	}

	public void setValidaConfigDM(char validaConfigDM) 
	{
		this.validaConfigDM = validaConfigDM;
	}

	public char getValidaICCID() 
	{
		return validaICCID;
	}

	public void setValidaICCID(char validaICCID) 
	{
		this.validaICCID = validaICCID;
	}

	public char getValidaIMEI() 
	{
		return validaIMEI;
	}

	public void setValidaIMEI(char validaIMEI) 
	{
		this.validaIMEI = validaIMEI;
	}

	public char getValidaModelo() 
	{
		return validaModelo;
	}

	public void setValidaModelo(char validaModelo) 
	{
		this.validaModelo = validaModelo;
	}

	public char getValidaMSISDN() 
	{
		return validaMSISDN;
	}

	public void setValidaMSISDN(char validaMSISDN) 
	{
		this.validaMSISDN = validaMSISDN;
	}

	public char getValidaProfileICCID() 
	{
		return validaProfileICCID;
	}

	public void setValidaProfileICCID(char validaProfileICCID) 
	{
		this.validaProfileICCID = validaProfileICCID;
	}
	
	public boolean isValidaMSISDN()
	{
		return (validaMSISDN == 'S' || validaMSISDN == 's') ? true : false;
	}
	
	public boolean isValidaICCID()
	{
		return (validaICCID == 'S' || validaICCID == 's') ? true : false;
	}
	
	public boolean isValidaModelo()
	{
		return (validaModelo == 'S' || validaModelo == 's') ? true : false;
	}
	
	public boolean isValidaIMEI()
	{
		return (validaIMEI == 'S' || validaIMEI == 's') ? true : false;
	}
	
	public boolean isValidaProfileICCID()
	{
		return (validaProfileICCID == 'S' || validaProfileICCID == 's') ? true : false;
	}
	
	public boolean isValidaConfigDM()
	{
		return (validaConfigDM == 'S' || validaConfigDM == 's') ? true : false;
	}
	
	public boolean isBloqueioConcorrente()
	{
		return (bloqueioConcorrente == 'S' || bloqueioConcorrente == 's') ? true : false;
	}
	
	public int hashCode()
	{
		return getCodServicoWig();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof ServicoWig) )
			return false;
		
		if ( ((ServicoWig)obj).getCodServicoWig() == this.getCodServicoWig() )
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return this.getDesServicoWig();
	}
}
