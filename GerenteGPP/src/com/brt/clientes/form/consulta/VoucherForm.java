
package com.brt.clientes.form.consulta;

import org.apache.struts.action.ActionForm;

/**
 * Modela a consulta de Voucher
 * @author Alex Pitacci Simões
 * @since 07/06/2004
 */
public class VoucherForm extends ActionForm {
	
	private String numeroVoucher;
	private String resultado;
	
	
	/**
	 * @return Returns the numeroVoucher.
	 */
	public String getNumeroVoucher() {
		return numeroVoucher;
	}
	/**
	 * @param numeroVoucher The numeroVoucher to set.
	 */
	public void setNumeroVoucher(String numeroVoucher) {
		this.numeroVoucher = numeroVoucher;
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
