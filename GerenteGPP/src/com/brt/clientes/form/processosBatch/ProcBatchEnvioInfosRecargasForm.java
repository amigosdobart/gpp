/*
 * Created on 08/06/2004
 */
package com.brt.clientes.form.processosBatch;

import org.apache.struts.action.ActionForm;

/**
 * @author Daniel Ferreira
 * @since 08/06/2004
 */
public class ProcBatchEnvioInfosRecargasForm extends ActionForm 
{
	
  private String dataExecucao;
  private short resultado = -1;
  
  /**
   * @return Returns the Date of Execution.
   */
  public String getDataExecucao() 
  {
    return dataExecucao;
  }
	
  /**
   * @param dataExecucao The Date of Execution to set.
   */
  public void setDataExecucao(String dataExecucao) 
  {
    this.dataExecucao = dataExecucao;
  }
  
  /**
   * @return Returns the Result of the Execution.
   */
  public short getResultado() 
  {
	return resultado;
  }
  
  /**
   * @param resultado The Result of the Execution to set.
   */
  public void setResultado(short resultado) 
  {
    this.resultado = resultado;
  }

}
