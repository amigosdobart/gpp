/*
 * Created on 03/06/2004
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.brt.clientes.action.recarga;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.recarga.RecargaGenericaForm;
import com.brt.clientes.interfacegpp.RecargaGPP;
import com.brt.clientes.servlet.Util;

/**
 * @author André Gonçalves
 * @since 03/06/2004
 */
public class ConsultaRecargaGenericaAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		RecargaGenericaForm recargaForm = (RecargaGenericaForm)form;
		
		recargaForm.setResultado(
				RecargaGPP.doRecargaGenericaAssinante(
						Util.parseMsisdn( recargaForm.getMsisdn() ),
						recargaForm.getTipoTransacao(),
						recargaForm.getIdentificadorRecarga(),
						recargaForm.getTipoCredito(),
						Util.parseValor(recargaForm.getValor()).toString(),
						recargaForm.getDataHora(),
						recargaForm.getSistemaOrigem(),
						usuario,
						recargaForm.getNsuInstituicao(),
						recargaForm.getHashCartaoCredito(),
						Util.parseCpf(recargaForm.getCpf()) ) );

		return mapping.findForward("success");
	}

}
