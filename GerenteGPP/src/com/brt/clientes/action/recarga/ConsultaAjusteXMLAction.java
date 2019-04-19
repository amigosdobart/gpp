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
import com.brt.clientes.form.recarga.RecargaAjusteXMLForm;
import com.brt.clientes.interfacegpp.RecargaGPP;
import com.brt.clientes.servlet.Util;

/**
 * @author André Gonçalves
 * @since 07/06/2004
 */
public class ConsultaAjusteXMLAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		RecargaAjusteXMLForm recargaForm = (RecargaAjusteXMLForm)form;
		
		String xml =    "<?xml version=\"1.0\"?>" +
						"<GPPRecarga>" + 
						"<MSISDN>" + Util.parseMsisdn(recargaForm.getMsisdn()) + "</MSISDN>" + 
						"<tipoTransacao>" + recargaForm.getTipoTransacao() + "</tipoTransacao>" + 
						"<tipoCredito>" + recargaForm.getTipoCredito() + "</tipoCredito>" + 
						"<valor>" + Util.parseValor(recargaForm.getValor()) + "</valor>" +
						"<tipo>" + recargaForm.getTipo() + "</tipo>"+ 
						"<dataHora>" + recargaForm.getDataHora() + "</dataHora>" + 
						"<sistemaOrigem>" + recargaForm.getSistemaOrigem() + "</sistemaOrigem>" + 
						"<operador>" + usuario + "</operador>" +
						"<dataExpiracao>" + recargaForm.getDataExpiracao() + "</dataExpiracao>"+
						"</GPPRecarga>";
		
		recargaForm.setXml(xml.replaceAll("><","> <").replaceAll("<","&lt;"));
		
		recargaForm.setResultado(
				RecargaGPP.doAjusteXML(xml)	);

		return mapping.findForward("success");
	}

}
