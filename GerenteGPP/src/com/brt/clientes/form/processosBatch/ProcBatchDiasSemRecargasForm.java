/*
 * Created on 17/06/2004
 *
 */
package com.brt.clientes.form.processosBatch;

import org.apache.struts.action.ActionForm;

/**
 * @author André Gonçalves
 * @since 17/06/2004
 */
public class ProcBatchDiasSemRecargasForm extends ActionForm {

	private String dataExecucao;
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
	/**
	 * @return Returns the dataExecucao.
	 */
	public String getDataExecucao() {
		return dataExecucao;
	}
	/**
	 * @param dataExecucao The dataExecucao to set.
	 */
	public void setDataExecucao(String dataExecucao) {
		this.dataExecucao = dataExecucao;
	}
}
