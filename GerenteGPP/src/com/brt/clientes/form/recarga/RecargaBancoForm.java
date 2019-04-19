/*
 * Created on 04/06/2004
 *
 */
package com.brt.clientes.form.recarga;

import org.apache.struts.action.ActionForm;

/**
 * @author André Gonçalves
 * @since 04/06/2004
 */
public class RecargaBancoForm extends ActionForm {

	public String msisdn;
	public String valor;
	public short resultado = -1;
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
	 * @return Returns the valor.
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
}
