
package com.brt.clientes.action.aprovisionamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.aprovisionamento.TrocaPlanoPrecosForm;
import com.brt.clientes.interfacegpp.AprovisionamentoGPP;
import com.brt.clientes.servlet.Util;

/**
 * Efetua a troca de plano de preços
 * @author Alex Pitacci Simões
 * @since 03/06/2004
 */
public class ConsultaTrocaPlanoPrecosAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		TrocaPlanoPrecosForm trocaPlanoPrecosForm = (TrocaPlanoPrecosForm)form;
		
		trocaPlanoPrecosForm.setResultado(AprovisionamentoGPP.getTrocaPlanoPreco(
				Util.parseMsisdn(trocaPlanoPrecosForm.getMsisdn()),
				trocaPlanoPrecosForm.getCodigoPlano(),
				Util.parseValor(trocaPlanoPrecosForm.getTarifa()).doubleValue(),
				Util.parseValor(trocaPlanoPrecosForm.getFranquia()).doubleValue(),
				usuario
				));
		return mapping.findForward("success");
	}

}
