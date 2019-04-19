
package com.brt.clientes.form.consulta;

import org.apache.struts.action.ActionForm;

/**
 * Modela a consulta de Assinante
 * @author Alex Pitacci Simões
 * @since 07/06/2004
 */
public class AssinanteForm extends ActionForm {
	
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
