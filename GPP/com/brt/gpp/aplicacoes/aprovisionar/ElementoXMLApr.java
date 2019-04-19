package com.brt.gpp.aplicacoes.aprovisionar;

public class ElementoXMLApr 
{
	// Atributos de um serviço de aprovisionamento
	private String macroServico;
	private String tipo;
	private String operacao;
	private String status;
	private String msisdn;
	
	// Construtor com parâmetros
	public ElementoXMLApr(String aMacroServico, String aOperacao, String aStatus, String aMsisdn)
	{
		this.macroServico = aMacroServico;
		this.operacao = aOperacao;
		this.status = aStatus;
		this.msisdn = aMsisdn;
	}
	
	// Construtor padrão
	public ElementoXMLApr()
	{
		// Não faz nada
	}
	/**
	 * @return
	 */
	public String getMacroServico() {
		return macroServico;
	}

	/**
	 * @return
	 */
	public String getOperacao() {
		return operacao;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param string
	 */
	public void setMacroServico(String string) {
		macroServico = string;
	}

	/**
	 * @param string
	 */
	public void setOperacao(String string) {
		operacao = string;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		status = string;
	}

	/**
	 * @param string
	 */
	public void setTipo(String string) {
		tipo = string;
	}

	/**
	 * @return
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @param string
	 */
	public void setMsisdn(String string) {
		msisdn = string;
	}

}
