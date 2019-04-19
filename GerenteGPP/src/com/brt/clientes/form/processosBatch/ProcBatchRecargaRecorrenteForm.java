/*
 * Created on 16/06/2004
 *
 */
package com.brt.clientes.form.processosBatch;

import org.apache.struts.action.ActionForm;

/**
 * @author André Gonçalves
 * @since 16/06/2004
 */
public class ProcBatchRecargaRecorrenteForm extends ActionForm {

	private boolean resultado;
	
	/**
	 * @return Returns the resultado.
	 */
	public boolean isResultado() {
		return resultado;
	}
	/**
	 * @param resultado The resultado to set.
	 */
	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}
}
