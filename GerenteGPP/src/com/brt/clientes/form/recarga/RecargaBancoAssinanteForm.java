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
public class RecargaBancoAssinanteForm extends ActionForm {
	
	private String msisdn;
	private String tipoTransacao;
	private String identificadorRecarga;
	private String tipoCredito;
	private String valor;
	private String dataHora;
	private String nsuInstituicao;
	private String codigoLoja;
	private String dataHoraBanco;
	private String dataContabil;
	private String sistemaOrigem;
	private String terminal;
	private String tipoTerminal;
	private short resultado = -1;

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
	 * @return Returns the terminal.
	 */
	public String getTerminal() {
		return terminal;
	}
	/**
	 * @param terminal The terminal to set.
	 */
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	/**
	 * @return Returns the tipoTerminal.
	 */
	public String getTipoTerminal() {
		return tipoTerminal;
	}
	/**
	 * @param tipoTerminal The tipoTerminal to set.
	 */
	public void setTipoTerminal(String tipoTerminal) {
		this.tipoTerminal = tipoTerminal;
	}
	/**
	 * @return Returns the codigoLoja.
	 */
	public String getCodigoLoja() {
		return codigoLoja;
	}
	/**
	 * @param codigoLoja The codigoLoja to set.
	 */
	public void setCodigoLoja(String codigoLoja) {
		this.codigoLoja = codigoLoja;
	}
	/**
	 * @return Returns the dataContabil.
	 */
	public String getDataContabil() {
		return dataContabil;
	}
	/**
	 * @param dataContabil The dataContabil to set.
	 */
	public void setDataContabil(String dataContabil) {
		this.dataContabil = dataContabil;
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
	 * @return Returns the dataHoraBanco.
	 */
	public String getDataHoraBanco() {
		return dataHoraBanco;
	}
	/**
	 * @param dataHoraBanco The dataHoraBanco to set.
	 */
	public void setDataHoraBanco(String dataHoraBanco) {
		this.dataHoraBanco = dataHoraBanco;
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
}
