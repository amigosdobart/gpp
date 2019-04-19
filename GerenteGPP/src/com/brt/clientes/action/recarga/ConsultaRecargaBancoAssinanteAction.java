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
import com.brt.clientes.form.recarga.RecargaBancoAssinanteForm;
import com.brt.clientes.interfacegpp.RecargaGPP;
import com.brt.clientes.servlet.Util;

/**
 * @author André Gonçalves
 * @since 07/06/2004
 */
public class ConsultaRecargaBancoAssinanteAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		RecargaBancoAssinanteForm recargaForm = (RecargaBancoAssinanteForm)form;
		
		recargaForm.setResultado(
				RecargaGPP.doRecargaBancoAssinante(
						Util.parseMsisdn(recargaForm.getMsisdn()),
						recargaForm.getTipoTransacao(),
						recargaForm.getIdentificadorRecarga(),
						recargaForm.getNsuInstituicao(),
						recargaForm.getCodigoLoja(),
						recargaForm.getTipoCredito(),
						Util.parseValor(recargaForm.getValor()).toString(),
						recargaForm.getDataHora(),
						recargaForm.getDataHoraBanco(),
						recargaForm.getDataContabil(),
						recargaForm.getTerminal(),
						recargaForm.getTipoTerminal(),
						recargaForm.getSistemaOrigem(),
						usuario ) );
		
		return mapping.findForward("success");
	}

}
