
package com.brt.clientes.action.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;

/**
 * @author ex576091
 * @since 28/06/2004
 */
public class ShowWelcomeAction extends ShowAction {

	public void updateVelocityContext(VelocityContext vctx,
			HttpServletRequest request, ActionForm form) 
	{
				String matricula = request.getParameter("matricula");
				request.getSession().setAttribute("matricula", matricula);
	}


	public String getTela() {
		return "index.vm";
	}

}
