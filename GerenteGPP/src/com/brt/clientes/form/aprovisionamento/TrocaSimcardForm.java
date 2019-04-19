
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modela a troca de simcard
 * @author Alex Pitacci Simões
 * @since 04/06/2004
 */
public class TrocaSimcardForm extends ActionForm{

	private String msisdn;
	private String simcard;
	private String tarifa;
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
	/**
	 * @return Returns the simcard.
	 */
	public String getSimcard() {
		return simcard;
	}
	/**
	 * @param simcard The simcard to set.
	 */
	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}
	/**
	 * @return Returns the tarifa.
	 */
	public String getTarifa() {
		return tarifa;
	}
	/**
	 * @param tarifa The tarifa to set.
	 */
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}
}
