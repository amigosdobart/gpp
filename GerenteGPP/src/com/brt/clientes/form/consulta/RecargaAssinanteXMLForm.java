
package com.brt.clientes.form.consulta;

import org.apache.struts.action.ActionForm;

/**
 * Modela a recarga de assinante por xml
 * @author Alex Pitacci Simões
 * @since 07/06/2004
 */
public class RecargaAssinanteXMLForm extends ActionForm {

	private String msisdn;
	private String valor;
	private String cpf;
	private String categoria;
	private String hashCartao;
	private String sistemaOrigem;
	private String resultado;
	
	
	/**
	 * @return Returns the categoria.
	 */
	public String getCategoria() {
		return categoria;
	}
	/**
	 * @param categoria The categoria to set.
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	/**
	 * @return Returns the cpf.
	 */
	public String getCpf() {
		return cpf;
	}
	/**
	 * @param cpf The cpf to set.
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	/**
	 * @return Returns the hashCartao.
	 */
	public String getHashCartao() {
		return hashCartao;
	}
	/**
	 * @param hashCartao The hashCartao to set.
	 */
	public void setHashCartao(String hashCartao) {
		this.hashCartao = hashCartao;
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
	 * @return Returns the sistemaOrigem.
	 */
	public String getSistemaOrigem() {
		return sistemaOrigem;
	}
	/**
	 * @param sistemaOrigem The sistemaOrigem to set.
	 */
	public void setSistemaOrigem(String sistemaOrigem) {
		this.sistemaOrigem = sistemaOrigem;
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
