/*
 * Created on 04/06/2004
 *
 */
package com.brt.clientes.action.recarga;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.form.recarga.RecargaBancoForm;
import com.brt.clientes.interfacegpp.RecargaGPP;
import com.brt.clientes.servlet.Util;


/**
 * @author André Gonçalves
 * @since 04/06/2004
 */
public class ConsultaRecargaBancoAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String usuario = (String)request.getSession().getAttribute("matricula");
		
		RecargaBancoForm recargaForm = (RecargaBancoForm)form;
		
		recargaForm.setResultado(
				RecargaGPP.doValidacaoRecargaBanco(
						Util.parseMsisdn(recargaForm.getMsisdn()),
						Util.parseValor(recargaForm.getValor()).toString() ) );
		
		return mapping.findForward("success");
	}

}
