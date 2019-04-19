
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modela o plano de preços
 * @author Alex Pitacci Simões
 * @since 03/06/2004
 */
public class TrocaPlanoPrecosForm extends ActionForm{

	private String msisdn;
	private String codigoPlano;
	private String tarifa;
	private String franquia;
	private short resultado;
	
	
	/**
	 * @return Returns the codigoPlano.
	 */
	public String getCodigoPlano() {
		return codigoPlano;
	}
	/**
	 * @param codigoPlano The codigoPlano to set.
	 */
	public void setCodigoPlano(String codigoPlano) {
		this.codigoPlano = codigoPlano;
	}
	/**
	 * @return Returns the franquia.
	 */
	public String getFranquia() {
		return franquia;
	}
	/**
	 * @param franquia The franquia to set.
	 */
	public void setFranquia(String franquia) {
		this.franquia = franquia;
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
