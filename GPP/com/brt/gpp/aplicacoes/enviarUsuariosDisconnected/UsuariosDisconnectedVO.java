//Definicao do Pacote
package com.brt.gpp.aplicacoes.enviarUsuariosDisconnected;

/**
 * 
 *
 * Este arquivo contem a definicao da classe 
 * de UsuariosDisconnectedVO
 * 
 * <P> Versao:	1.0
 *
 * @Autor:		Marcelo Alves Araujo
 * Data:		13/10/2005
 *
 */

public class UsuariosDisconnectedVO
{
	private String 	msisdn;
	private int		status;
	private String 	data;

	/**
	 * Metodo...: EnvioUsuariosDisconnected
	 * Descricao: Construtor
	 */
	public UsuariosDisconnectedVO()
	{
	}
	
	/**
	 * Metodo...: EnvioUsuariosShutdown
	 * Descricao: Construtor 
	 * @param msisdn	- Numero do usuario
	 * @param status	- Status do usuario
	 * @param data		- Data do processo
	 */
	public UsuariosDisconnectedVO(String msisdn, int status, String data)
	{
		this.msisdn = msisdn;
		this.status = status;
		this.data = data;
	}
	
	// Métodos Get                             
	/**
	 * Metodo...: getMsisdn
	 * Descricao: Busca o valor do MSISDN 
	 * @return	String 	- Valor do MSISDN
	 */
	public String getMsisdn() 
	{
		return msisdn;
	}

	/**
	 * Metodo...: getStatus
	 * Descricao: Busca o valor do Status 
	 * @return	int 	- Valor do Status
	 */
	public int getStatus() 
	{
		return status;
	}

	/**
	 * Metodo...: getData
	 * Descricao: Busca o valor da Data  
	 * @return	String 	- Valor da Data
	 */
	public String getData() 
	{
		return data;
	}

	// Métodos Set                             
	/**
	 * Metodo...: setMsisdn
	 * Descricao: Armazena o valor do MSISDN  
	 * @param 	String 	- Valor do MSISDN 
	 */
	public void setMsisdn(String aMsisdn) 
	{
		msisdn = aMsisdn;
	}

	/**
	 * Metodo...: setMsisdn
	 * Descricao: Armazena o valor do Status  
	 * @param 	int 	- Valor do Status
	 */
	public void setStatus(int aStatus) 
	{
		status = aStatus;
	}


	/**
	 * Metodo...: setMsisdn
	 * Descricao: Armazena o valor da Data  
	 * @param 	String 	- Valor da Data
	 */
	public void setData(String aData) 
	{
		data = aData;
	}
}