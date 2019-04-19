
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

/**
 * Modela a ativacao de msisdn
 * @author Alex Pitacci Simões
 * @since 02/06/2004
 */
public class AtivacaoForm extends ActionForm {

	private String msisdn;
	private String simcard;
	private String planoPreco;
	private String creditoInicial;
	private String idioma;
	/**
	 * @return Returns the creditoInicial.
	 */
	public String getCreditoInicial() {
		return creditoInicial;
	}
	/**
	 * @param creditoInicial The creditoInicial to set.
	 */
	public void setCreditoInicial(String creditoInicial) {
		this.creditoInicial = creditoInicial;
	}
	/**
	 * @return Returns the idioma.
	 */
	public String getIdioma() {
		return idioma;
	}
	/**
	 * @param idioma The idioma to set.
	 */
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	/**
	 * @return Returns the planoPreco.
	 */
	public String getPlanoPreco() {
		return planoPreco;
	}
	/**
	 * @param planoPreco The planoPreco to set.
	 */
	public void setPlanoPreco(String planoPreco) {
		this.planoPreco = planoPreco;
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
}
