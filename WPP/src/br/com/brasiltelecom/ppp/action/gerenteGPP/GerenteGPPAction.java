/*
 * Created on 28/05/2004
 *
 */
package br.com.brasiltelecom.ppp.action.gerenteGPP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Alex Pitacci Simões
 * @since 28/05/2004
 */
public class GerenteGPPAction extends ActionPortal {

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
		
		Usuario usuario = (Usuario)request.getSession().getAttribute(Constantes.USUARIO);
		response.sendRedirect(request.getParameter("protocol")+"//"+request.getParameter("host")+"/gppConsole/showWelcomeAction.do?matricula=" + usuario.getMatricula());
		return null;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
