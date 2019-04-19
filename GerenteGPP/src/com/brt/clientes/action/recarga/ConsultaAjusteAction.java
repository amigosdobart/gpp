/*
 * Created on 07/06/2004
 *
 */
package com.brt.clientes.action.recarga;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.recarga.RecargaAjusteForm;
import com.brt.clientes.interfacegpp.RecargaGPP;
import com.brt.clientes.servlet.Util;

/**
 * @author André Gonçalves
 * @since 07/06/2004
 */
public class ConsultaAjusteAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		RecargaAjusteForm recargaForm = (RecargaAjusteForm)form;
		
		recargaForm.setResultado(
				RecargaGPP.doAjuste(
						Util.parseMsisdn(recargaForm.getMsisdn()),
						recargaForm.getTipoTransacao(),
						recargaForm.getTipoCredito(),
						Util.parseValor(recargaForm.getValor()).toString(),
						recargaForm.getTipo(),
						recargaForm.getDataHora(),
						recargaForm.getSistemaOrigem(),
						usuario,
						recargaForm.getDataExpiracao()) );
		
		return mapping.findForward("success");
	}

}
