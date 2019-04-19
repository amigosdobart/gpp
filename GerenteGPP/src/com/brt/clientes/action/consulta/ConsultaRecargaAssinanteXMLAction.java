
package com.brt.clientes.action.consulta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.consulta.RecargaAssinanteXMLForm;
import com.brt.clientes.interfacegpp.ConsultaGPP;
import com.brt.clientes.servlet.Util;

/**
 * Realiza a consulta de recarga de assinante (XML)
 * @author Alex Pitacci Sim�es
 * @since 08/06/2004
 */
public class ConsultaRecargaAssinanteXMLAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		RecargaAssinanteXMLForm recargaAssinanteForm = (RecargaAssinanteXMLForm)form;
		
		recargaAssinanteForm.setResultado(ConsultaGPP.getRecargaAssinanteXML(
				Util.parseMsisdn(recargaAssinanteForm.getMsisdn()),
				Util.parseValor(recargaAssinanteForm.getValor()).toString(),
				Util.parseCpf(recargaAssinanteForm.getCpf()),
				recargaAssinanteForm.getCategoria(),
				recargaAssinanteForm.getHashCartao(),
				recargaAssinanteForm.getSistemaOrigem()).replaceAll("><","> <").replaceAll("<","&lt;"));
		return mapping.findForward("success");
	}

}
