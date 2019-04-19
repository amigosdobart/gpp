
package com.brt.clientes.action.aprovisionamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.aprovisionamento.TrocaMsisdnForm;
import com.brt.clientes.interfacegpp.AprovisionamentoGPP;
import com.brt.clientes.servlet.Util;

/**
 * Realiza a troca de Msisdn
 * @author Alex Pitacci Simões
 * @since 03/06/2004
 */
public class ConsultaTrocaMsisdnAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		TrocaMsisdnForm trocaMsisdnForm = (TrocaMsisdnForm)form;
		
		trocaMsisdnForm.setResultado(AprovisionamentoGPP.getTrocaMsisdn(
				Util.parseMsisdn(trocaMsisdnForm.getMsisdnAntigo()),
				Util.parseMsisdn(trocaMsisdnForm.getMsisdnNovo()),
				trocaMsisdnForm.getId(),
				Util.parseValor(trocaMsisdnForm.getTarifa()).doubleValue(),
				usuario));
		return mapping.findForward("success");
	}

}
