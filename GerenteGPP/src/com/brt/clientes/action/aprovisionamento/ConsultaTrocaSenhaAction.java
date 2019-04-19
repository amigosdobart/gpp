
package com.brt.clientes.action.aprovisionamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.aprovisionamento.TrocaSenhaForm;
import com.brt.clientes.interfacegpp.AprovisionamentoGPP;
import com.brt.clientes.servlet.Util;

/**
 * Efetua a troca de senha
 * @author Alex Pitacci Simões
 * @since 04/06/2004
 */
public class ConsultaTrocaSenhaAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		TrocaSenhaForm trocaSenhaForm = (TrocaSenhaForm)form;
		
		trocaSenhaForm.setResultado(AprovisionamentoGPP.getTrocaSenha(
				Util.parseMsisdn(trocaSenhaForm.getMsisdn()),
				trocaSenhaForm.getSenha()
				).replaceAll("><","> <").replaceAll("<","&lt;"));
		return mapping.findForward("success");
	}

}
