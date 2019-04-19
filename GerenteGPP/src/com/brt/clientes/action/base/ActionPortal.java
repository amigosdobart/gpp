package com.brt.clientes.action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Classe Principal do Struts
 * @author Alex Pitacci Simões
 * @since 31/05/2004
 */

public abstract class ActionPortal extends Action {


	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (request.getSession().getAttribute("matricula") == null){
			request.getSession().setAttribute("matricula","GerenteGPP");
		}
		ActionForward forward = performPortal(mapping, form, request,response);
		return forward;
	}
	
	public abstract ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception ;
}
