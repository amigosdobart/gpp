
package com.brt.clientes.action.consulta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.consulta.AssinanteForm;
import com.brt.clientes.interfacegpp.ConsultaGPP;
import com.brt.clientes.servlet.Util;

/**
 * Realiza a consulta de assinante
 * @author Alex Pitacci Simões
 * @since 08/06/2004
 */
public class ConsultaAssinanteAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		AssinanteForm assinanteForm = (AssinanteForm)form;
		
		assinanteForm.setResultado(ConsultaGPP.getAssinante(
				Util.parseMsisdn(assinanteForm.getMsisdn())).replaceAll("><","> <").replaceAll("<","&lt;"));
		return mapping.findForward("success");
	}

}
