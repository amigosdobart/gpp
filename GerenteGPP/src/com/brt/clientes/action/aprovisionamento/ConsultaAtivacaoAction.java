
package com.brt.clientes.action.aprovisionamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.aprovisionamento.AtivacaoForm;
import com.brt.clientes.interfacegpp.AprovisionamentoGPP;
import com.brt.clientes.servlet.Util;

/**
 * Realiza o aprovisionamento de ativação
 * @author Alex Pitacci Simões
 * @since 02/06/2004
 */
public class ConsultaAtivacaoAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		AtivacaoForm ativacaoForm = (AtivacaoForm)form;
		
		ativacaoForm.setResultado(AprovisionamentoGPP.getAtivacao(
				Util.parseMsisdn(ativacaoForm.getMsisdn()),
				ativacaoForm.getSimcard(),
				ativacaoForm.getPlanoPreco(),
				Util.parseValor(ativacaoForm.getCreditoInicial()).doubleValue(),
				Short.parseShort(ativacaoForm.getIdioma()),
				usuario));
		return mapping.findForward("success");
	}

}
