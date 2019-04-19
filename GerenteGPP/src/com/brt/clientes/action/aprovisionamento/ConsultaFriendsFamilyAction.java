
package com.brt.clientes.action.aprovisionamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.aprovisionamento.FriendsFamilyForm;
import com.brt.clientes.interfacegpp.AprovisionamentoGPP;
import com.brt.clientes.servlet.Util;

/**
 * Executa a alteração do friends and family
 * @author Alex Pitacci Simões
 * @since 04/06/2004
 */
public class ConsultaFriendsFamilyAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String usuario = (String)request.getSession().getAttribute("matricula");
		
		FriendsFamilyForm friendsFamilyForm = (FriendsFamilyForm)form;
		
		friendsFamilyForm.setResultado(AprovisionamentoGPP.getFriendsFamily(
				Util.parseMsisdn(friendsFamilyForm.getMsisdn()),
				friendsFamilyForm.getFf(),
				usuario
				));
		return mapping.findForward("success");	
		}

}
