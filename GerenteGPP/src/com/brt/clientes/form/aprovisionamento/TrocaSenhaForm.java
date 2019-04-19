
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modela a troca de senha
 * @author Alex Pitacci Simões
 * @since 04/06/2004
 */
public class TrocaSenhaForm extends ActionForm {
	private String msisdn;
	private String senha;
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
	/**
	 * @return Returns the senha.
	 */
	public String getSenha() {
		return senha;
	}
	/**
	 * @param senha The senha to set.
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
