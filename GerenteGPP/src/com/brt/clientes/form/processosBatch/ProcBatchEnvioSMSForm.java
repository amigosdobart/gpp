/*
 * Created on 09/06/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.brt.clientes.form.processosBatch;

import org.apache.struts.action.ActionForm;

/**
 * @author Daniel Ferreira
 * @since  09/06/2004
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProcBatchEnvioSMSForm extends ActionForm 
{
	
	  private boolean resultado = false;
	  
	  /**
	   * @return Returns the Result of the Execution.
	   */
	  public boolean getResultado() 
	  {
		return resultado;
	  }
	  
	  /**
	   * @param resultado The Result of the Execution to set.
	   */
	  public void setResultado(boolean resultado) 
	  {
	    this.resultado = resultado;
	  }

}