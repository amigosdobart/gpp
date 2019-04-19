
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modela o friends and family
 * @author Alex Pitacci Simões
 * @since 04/06/2004
 */
public class FriendsFamilyForm extends ActionForm{
	private String msisdn;
	private String ff;
	private short resultado;
	/**
	 * @return Returns the ff.
	 */
	public String getFf() {
		return ff;
	}
	/**
	 * @param ff The ff to set.
	 */
	public void setFf(String ff) {
		this.ff = ff;
	}
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
