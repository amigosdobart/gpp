
package com.brt.clientes.action.processosBatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.processosBatch.ProcBatchEnvioUsuariosStatusShutdownForm;
import com.brt.clientes.interfacegpp.ProcessosBatchGPP;

/**
 * Realiza o processo batch
 * @author André Gonçalves
 * @since 10/06/2004
 */
public class ConsultaProcBatchEnvioUsuarioShutdownAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ProcBatchEnvioUsuariosStatusShutdownForm procBatchForm = (ProcBatchEnvioUsuariosStatusShutdownForm)form;
	  	procBatchForm.setResultado
		  (ProcessosBatchGPP.doProcBatchEnvioUsuariosStatusShutdown
		  (procBatchForm.getDataExecucao()));
	  	
	    return mapping.findForward("success");
	}

}
