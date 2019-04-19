/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.extratoXmlGPP;


import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra o XML do comprovante de serviços gerado pelo GPP
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowXmlComprovanteServicoAction extends ShowAction {
	
	private String codOperacao = Constantes.MENU_COMP_SERVICOS;
		/**
		 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
		 */
		public String getTela() {
			return "extratoXmlGPP.vm";
		}

		/**
		 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
		 */
		public void updateVelocityContext (
			VelocityContext context,
			HttpServletRequest request,
			Database db)
			throws Exception {
				//HttpSession session = request.getSession();

				String result = (String) request.getAttribute(Constantes.RESULT);
				context.put("xml",result);

		}

		/**
		 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
		 */
		public String getOperacao() {
			return codOperacao;
		}
}
