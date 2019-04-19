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
public class RecargaAjusteXMLForm extends ActionForm {
	
	private String msisdn;
	private String tipoTransacao;
	private String tipoCredito;
	private String valor;
	private String tipo;
	private String dataHora;
	private String sistemaOrigem;
	private String dataExpiracao;
	private String xml = "";
	private short resultado = -1;

	/**
	 * @return Returns the dataExpiracao.
	 */
	public String getDataExpiracao() {
		return dataExpiracao;
	}
	/**
	 * @param dataExpiracao The dataExpiracao to set.
	 */
	public void setDataExpiracao(String dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
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
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
