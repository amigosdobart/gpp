
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modela troca de msisdn
 * @author Alex Pitacci Simões
 * @since 03/06/2004
 */
public class TrocaMsisdnForm extends ActionForm{
	
	private String msisdnAntigo;
	private String msisdnNovo;
	private String id;
	private String tarifa;
	private short resultado;
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the msisdnAntigo.
	 */
	public String getMsisdnAntigo() {
		return msisdnAntigo;
	}
	/**
	 * @param msisdnAntigo The msisdnAntigo to set.
	 */
	public void setMsisdnAntigo(String msisdnAntigo) {
		this.msisdnAntigo = msisdnAntigo;
	}
	/**
	 * @return Returns the msisdnNovo.
	 */
	public String getMsisdnNovo() {
		return msisdnNovo;
	}
	/**
	 * @param msisdnNovo The msisdnNovo to set.
	 */
	public void setMsisdnNovo(String msisdnNovo) {
		this.msisdnNovo = msisdnNovo;
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
