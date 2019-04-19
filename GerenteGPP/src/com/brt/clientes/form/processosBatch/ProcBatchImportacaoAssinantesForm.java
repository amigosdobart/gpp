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
public class ProcBatchImportacaoAssinantesForm extends ActionForm {
	
	private short resultado = -1;
	
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
}
