
package com.brt.clientes.action.aprovisionamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.aprovisionamento.TrocaSimcardForm;
import com.brt.clientes.interfacegpp.AprovisionamentoGPP;
import com.brt.clientes.servlet.Util;

/**
 * Realiza a troca de simcard
 * @author Alex Pitacci Simões
 * @since 04/06/2004
 */
public class ConsultaTrocaSimcardAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		TrocaSimcardForm trocaSimcardForm = (TrocaSimcardForm)form;
		
		trocaSimcardForm.setResultado(AprovisionamentoGPP.getTrocaSimcard(
				Util.parseMsisdn(trocaSimcardForm.getMsisdn()),
				trocaSimcardForm.getSimcard(),
				Util.parseValor(trocaSimcardForm.getTarifa()).doubleValue(),
				usuario
				));
		return mapping.findForward("success");
	}

}
