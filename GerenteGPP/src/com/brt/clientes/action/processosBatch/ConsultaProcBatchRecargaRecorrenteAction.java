
package com.brt.clientes.action.processosBatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.processosBatch.ProcBatchRecargaRecorrenteForm;
import com.brt.clientes.interfacegpp.ProcessosBatchGPP;

/**
 * Executa a recarga recorrente
 * @author Alex Pitacci Simões
 * @since 10/06/2004
 */
public class ConsultaProcBatchRecargaRecorrenteAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ProcBatchRecargaRecorrenteForm procBatchForm = (ProcBatchRecargaRecorrenteForm)form;
		procBatchForm.setResultado(ProcessosBatchGPP.doProcBatchRecargaRecorrente());
		
	    return mapping.findForward("success");
	}

}
