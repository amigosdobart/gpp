package br.com.brasiltelecom.ppp.portal.entity;

public class DadosAssinanteNDS
{
	private String nrcAssinante;
	private String nomeAssinante;
	private String tipoDocumento;
	private String numeroDocumento;
	private String tipoCliente;
	private String numeroCPFCNPJ;
	private String domainName;

	/**
	 * @return
	 */
	public String getNomeAssinante() 
	{
		return nomeAssinante;
	}

	/**
	 * @return
	 */
	public String getNrcAssinante() 
	{
		return nrcAssinante;
	}

	/**
	 * @return
	 */
	public String getNumeroCPFCNPJ() 
	{
		return numeroCPFCNPJ;
	}

	/**
	 * @return
	 */
	public String getNumeroDocumento() 
	{
		return numeroDocumento;
	}

	/**
	 * @return
	 */
	public String getTipoCliente() 
	{
		return tipoCliente;
	}

	/**
	 * @return
	 */
	public String getTipoDocumento() 
	{
		return tipoDocumento;
	}

	/**
	 * @param string
	 */
	public void setNomeAssinante(String string) 
	{
		nomeAssinante = string;
	}

	/**
	 * @param string
	 */
	public void setNrcAssinante(String string) 
	{
		nrcAssinante = string;
	}

	/**
	 * @param string
	 */
	public void setNumeroCPFCNPJ(String string) 
	{
		numeroCPFCNPJ = string;
	}

	/**
	 * @param string
	 */
	public void setNumeroDocumento(String string) 
	{
		numeroDocumento = string;
	}

	/**
	 * @param string
	 */
	public void setTipoCliente(String string) 
	{
		tipoCliente = string;
	}

	/**
	 * @param string
	 */
	public void setTipoDocumento(String string) 
	{
		tipoDocumento = string;
	}

	public String getDomainName()
	{
		return domainName;
	}

	public void setDomainName(String domainName)
	{
		this.domainName = domainName;
	}
}
