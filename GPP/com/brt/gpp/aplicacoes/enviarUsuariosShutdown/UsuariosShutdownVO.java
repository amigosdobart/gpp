//Definicao do Pacote
package com.brt.gpp.aplicacoes.enviarUsuariosShutdown;

/**
  *
  * Este arquivo contem a definicao da classe de DadosSMS
  * <P> Versao:        	1.0
  *
  * @Autor:            	Vanessa Andrade
  * Data:               15/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class UsuariosShutdownVO
{
	private String msisdn;
	private String status;
	private String data;

	/**
	 * Metodo...: EnvioUsuariosShutdown
	 * Descricao: Construtor Padrao
	 * @param	
	 * @return									
	 */
	public UsuariosShutdownVO()
	{
	}
	
	/**
	 * Metodo...: EnvioUsuariosShutdown
	 * Descricao: Construtor 
	 * @param msisdn	- Numero do usuario
	 * @param status	- Status do usuario
	 * @param data		- Data do processo
	 * @return
	 */
	public UsuariosShutdownVO(String msisdn, String status, String data)
	{
		this.msisdn = msisdn;
		this.status = status;
		this.data = data;
	}
	
	// Métodos Get                             
	/**
	 * Metodo...: getMsisdn
	 * Descricao: Busca o valor do MSISDN 
	 * @param 
	 * @return	String 	- Valor do MSISDN
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Metodo...: getStatus
	 * Descricao: Busca o valor do Status 
	 * @param 
	 * @return	String 	- Valor do Status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Metodo...: getData
	 * Descricao: Busca o valor da Data  
	 * @param 
	 * @return	String 	- Valor da Data
	 */
	public String getData() {
		return data;
	}

	// Métodos Set                             
	/**
	 * Metodo...: setMsisdn
	 * Descricao: Armazena o valor do MSISDN  
	 * @param 	String 	- Valor do MSISDN 
	 * @return	
	 */
	public void setMsisdn(String aMsisdn) {
		msisdn = aMsisdn;
	}

	/**
	 * Metodo...: setMsisdn
	 * Descricao: Armazena o valor do Status  
	 * @param 	String 	- Valor do Status
	 * @return	
	 */
	public void setStatus(String aStatus) {
		status = aStatus;
	}


	/**
	 * Metodo...: setMsisdn
	 * Descricao: Armazena o valor da Data  
	 * @param 	String 	- Valor da Data
	 * @return	
	 */
	public void setData(String aData) {
		data = aData;
	}

}
