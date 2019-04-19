/*
 * Created on 07/06/2004
 *
 */
package com.brt.clientes.form.recarga;

import org.apache.struts.action.ActionForm;

/**
 * @author André Gonçalves
 * @since 07/06/2004
 */
public class RecargaXMLForm extends ActionForm {

	private String msisdn;
	private String tipoTransacao;
	private String identificadorRecarga;
	private String tipoCredito;
	private String valor;
	private String dataHora;
	private String sistemaOrigem;
	private String nsuInstituicao;
	private String hashCartaoCredito;
	private String cpf;
	private String xml = "";
	private String resultado = "";
	
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
	 * @return Returns the dataHora.
	 */
	public String getDataHora() {
		return dataHora;
	}
	/**
	 * @param dataHora The dataHora to set.
	 */
	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}
	/**
	 * @return Returns the hashCartaoCredito.
	 */
	public String getHashCartaoCredito() {
		return hashCartaoCredito;
	}
	/**
	 * @param hashCartaoCredito The hashCartaoCredito to set.
	 */
	public void setHashCartaoCredito(String hashCartaoCredito) {
		this.hashCartaoCredito = hashCartaoCredito;
	}
	/**
	 * @return Returns the identificadorRecarga.
	 */
	public String getIdentificadorRecarga() {
		return identificadorRecarga;
	}
	/**
	 * @param identificadorRecarga The identificadorRecarga to set.
	 */
	public void setIdentificadorRecarga(String identificadorRecarga) {
		this.identificadorRecarga = identificadorRecarga;
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
	 * @return Returns the nsuInstituicao.
	 */
	public String getNsuInstituicao() {
		return nsuInstituicao;
	}
	/**
	 * @param nsuInstituicao The nsuInstituicao to set.
	 */
	public void setNsuInstituicao(String nsuInstituicao) {
		this.nsuInstituicao = nsuInstituicao;
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
	 * @return Returns the tipoCredito.
	 */
	public String getTipoCredito() {
		return tipoCredito;
	}
	/**
	 * @param tipoCredito The tipoCredito to set.
	 */
	public void setTipoCredito(String tipoCredito) {
		this.tipoCredito = tipoCredito;
	}
	/**
	 * @return Returns the tipoTransacao.
	 */
	public String getTipoTransacao() {
		return tipoTransacao;
	}
	/**
	 * @param tipoTransacao The tipoTransacao to set.
	 */
	public void setTipoTransacao(String tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
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
	/**
	 * @return Returns the xml.
	 */
	public String getXml() {
		return xml;
	}
	/**
	 * @param xml The xml to set.
	 */
	public void setXml(String xml) {
		this.xml = xml;
	}
}
