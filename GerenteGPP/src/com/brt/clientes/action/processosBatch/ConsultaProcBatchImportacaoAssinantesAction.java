
package com.brt.clientes.action.processosBatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.processosBatch.ProcBatchImportacaoAssinantesForm;
import com.brt.clientes.interfacegpp.ProcessosBatchGPP;

/**
 * Realiza o processo batch de importacao de assiantes
 * @author Alex Pitacci Simões
 * @since 10/06/2004
 */
public class ConsultaProcBatchImportacaoAssinantesAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ProcBatchImportacaoAssinantesForm procBatchForm = (ProcBatchImportacaoAssinantesForm)form;
		  procBatchForm.setResultado
		    (ProcessosBatchGPP.doProcBatchImportacaoAssinantes());
			
		  return mapping.findForward("success");
	}

}
