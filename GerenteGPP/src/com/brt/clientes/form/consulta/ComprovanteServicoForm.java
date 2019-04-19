
package com.brt.clientes.form.consulta;

import org.apache.struts.action.ActionForm;

/**
 * Modela o comprovante de servico
 * @author Alex Pitacci Simões
 * @since 07/06/2004
 */
public class ComprovanteServicoForm extends ActionForm {

	private String dataInicial;
	private String dataFinal;
	private String msisdn;
	private String resultado;
	
	
	/**
	 * @return Returns the dataFinal.
	 */
	public String getDataFinal() {
		return dataFinal;
	}
	/**
	 * @param dataFinal The dataFinal to set.
	 */
	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	/**
	 * @return Returns the dataInicial.
	 */
	public String getDataInicial() {
		return dataInicial;
	}
	/**
	 * @param dataInicial The dataInicial to set.
	 */
	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
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
}
