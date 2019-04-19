
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modela o desbloqueio
 * @author Alex Pitacci Simões
 * @since 03/06/2004
 */
public class DesbloqueioForm extends ActionForm {

	private String msisdn;
	private short resultado;
	
	
	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn() {
		return msisdn;
	}
	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	/**
	 * @return Returns the resultado.
	 */
	public short getResultado() {
		return resultado;
	}
	/**
	 * @param resultado The resultado to set.
	 */
	public void setResultado(short resultado) {
		this.resultado = resultado;
	}
}
