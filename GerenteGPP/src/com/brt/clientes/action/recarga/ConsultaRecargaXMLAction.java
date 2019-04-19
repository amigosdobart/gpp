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
import com.brt.clientes.form.recarga.RecargaXMLForm;
import com.brt.clientes.interfacegpp.RecargaGPP;
import com.brt.clientes.servlet.Util;

/**
 * @author André Gonçalves
 * @since 07/06/2004
 */
public class ConsultaRecargaXMLAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = (String)request.getSession().getAttribute("matricula");
		
		RecargaXMLForm recargaForm = (RecargaXMLForm)form;
		
		String xml =    "<?xml version=\"1.0\"?>" + 
						"<GPPRecarga>" + 
						"  <msisdn>" + Util.parseMsisdn(recargaForm.getMsisdn()) +  "</msisdn>" + 
						"  <tipoTransacao>" + recargaForm.getTipoTransacao() + "</tipoTransacao>" + 
						"  <identificacaoRecarga>" + recargaForm.getIdentificadorRecarga() + "</identificacaoRecarga>" + 
						"  <tipoCredito>" + recargaForm.getTipoCredito() + "</tipoCredito>" + 
						"  <valor>" + Util.parseValor(recargaForm.getValor()) + "</valor>" + 
						"  <dataHora>" + recargaForm.getDataHora() + "</dataHora>" + 
						"  <sistemaOrigem>" + recargaForm.getSistemaOrigem() + "</sistemaOrigem>" + 
						"  <operador>" + usuario + "</operador>" +
						"  <nsuInstituicao>" + recargaForm.getNsuInstituicao() +  "</nsuInstituicao>" +
						"  <hashCc>" + recargaForm.getHashCartaoCredito() + "</hashCc>"+
						"  <cpfCnpj>" + Util.parseCpf(recargaForm.getCpf()) + "</cpfCnpj> "+
						"</GPPRecarga>";
		
		recargaForm.setXml(xml.replaceAll("><","> <").replaceAll("<","&lt;"));
		
		recargaForm.setResultado( RecargaGPP.insereRecargaXML(xml));
		 
		return mapping.findForward("success");
	}

}
