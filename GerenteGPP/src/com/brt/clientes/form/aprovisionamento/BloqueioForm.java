
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modelo de bloqueio
 * @author Alex Pitacci Sim�es
 * @since 03/06/2004
 */
public class BloqueioForm extends ActionForm {

	private String msisdn;
	private String motivo;
	private String tarifa;
	private short resultado;
	
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
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
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