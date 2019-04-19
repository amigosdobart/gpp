//Definicao do Pacote
package com.brt.gpp.aplicacoes.prorrogarExpiracaoHibrido;

import java.util.Date;

/**
  *
  * Este arquivo contem a definicao da classe de DadosSMS
  * <P> Versao:        	1.0
  *
  * @Autor:             Marcos C. Magalhaes
  * Data:               12/12/2005
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class UsuariosHibridosAtivadosVO
{
	private String msisdn;
	private Date   data;
	private short  numDias;



	/**
	 * Metodo...: ProrrogacaoExpiracaoHibrido
	 * Descricao: Construtor Padrao
	 * @param	
	 * @return									
	 */
	public UsuariosHibridosAtivadosVO()
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
	public UsuariosHibridosAtivadosVO(String msisdn, Date data, short numDias)
	{
		this.msisdn = msisdn;
		this.data = data;
		this.numDias = numDias;
	}

	/**
	 * @return Returns the data.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @return Returns the numDias.
	 */
	public short getNumDias() {
		return numDias;
	}


	/**
	 * @param data The data to set.
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * @param numDias The numDias to set.
	 */
	public void setNumDias(short numDias) {
		this.numDias = numDias;
	}

}