/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.extratoXmlGPP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaExtratoGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Recebe um XML do comprovante de serviços vindo do GPP
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class XmlComprovanteServicoAction extends ActionPortal {
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception {
	
				ActionForward result = null;

				String msisdn = request.getParameter("obr_msisdn");
				if (msisdn.length() == 10)
					msisdn = "55" + msisdn;
					

				String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
				String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
				String resultSet = ConsultaExtratoGPP.getXmlRelatorio(msisdn,request.getParameter("dataInicial"),request.getParameter("dataFinal"),servidor,porta);

				request.setAttribute(Constantes.RESULT, resultSet);
				result = actionMapping.findForward("success");		    
  
				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}
}
