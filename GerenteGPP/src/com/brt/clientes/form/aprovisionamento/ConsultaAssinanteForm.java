
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modela a consulta de assinante
 * @author Alex Pitacci Simões
 * @since 04/06/2004
 */
public class ConsultaAssinanteForm extends ActionForm {

	private String msisdn;
	private String resultado;
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
	public String getResultado() {
		return resultado;
	}
	/**
	 * @param resultado The resultado to set.
	 */
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
}
