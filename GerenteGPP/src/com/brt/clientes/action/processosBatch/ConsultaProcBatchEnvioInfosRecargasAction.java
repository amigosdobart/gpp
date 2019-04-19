/*
 * Created on 08/06/2004
 */
package com.brt.clientes.action.processosBatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.processosBatch.ProcBatchEnvioInfosRecargasForm;
import com.brt.clientes.interfacegpp.ProcessosBatchGPP;

/**
 * @author Daniel Ferreira
 * @since  08/06/2004
 * TODO
 */
public class ConsultaProcBatchEnvioInfosRecargasAction extends ActionPortal 
{
	
  /* (non-Javadoc)
   * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  public ActionForward performPortal(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
  		                             HttpServletResponse response)throws Exception 
  {
		
	ProcBatchEnvioInfosRecargasForm procBatchForm = (ProcBatchEnvioInfosRecargasForm)form;
	procBatchForm.setResultado(ProcessosBatchGPP.doProcBatchEnvioInfosRecargas(procBatchForm.getDataExecucao()));
	
    return mapping.findForward("success");
    
  }
  
}
