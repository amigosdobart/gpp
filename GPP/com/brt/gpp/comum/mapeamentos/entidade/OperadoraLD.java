package com.brt.gpp.comum.mapeamentos.entidade;

/**
 * Entidade <code>OperadoraLD</code>. Referência: TBL_GER_OPERADORAS_LD
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/03/2007 
 */
public class OperadoraLD 
{
 
	private int csp;
	private String nomeOperadora;
	private String rateName0300;
	
	/**
	 * @return Código CSP
	 */
	public int getCsp() 
	{
		return csp;
	}
	
	/**
	 * @param csp Código CSP
	 */
	public void setCsp(int csp) 
	{
		this.csp = csp;
	}
	
	/**
	 * @return Nome da operadora
	 */
	public String getNomeOperadora() 
	{
		return nomeOperadora;
	}
	
	/**
	 * @param nomeOperadora Nome da operadora
	 */
	public void setNomeOperadora(String nomeOperadora) 
	{
		this.nomeOperadora = nomeOperadora;
	}
	
	/**
	 * @return Rate 0300
	 */
	public String getRateName0300() 
	{
		return rateName0300;
	}
	
	/**
	 * @param rateName0300 Rate 0300
	 */
	public void setRateName0300(String rateName0300) 
	{
		this.rateName0300 = rateName0300;
	}
	 
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof OperadoraLD))
			return false;
		
		if (obj == this)
			return true;
		
		return csp == ((OperadoraLD)obj).getCsp();
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.csp);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[OperadoraLD]");
		result.append("CSP=" + this.csp);
		if (nomeOperadora != null) 	result.append(";NOME=" + this.nomeOperadora);
		if (rateName0300 != null)	result.append(";RATE_NAME_0300=" + this.rateName0300);
	
		return result.toString();
	}

}
 
