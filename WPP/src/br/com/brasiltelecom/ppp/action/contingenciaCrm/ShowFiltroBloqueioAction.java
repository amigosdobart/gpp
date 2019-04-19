/*
 * Created on 23/08/2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Henrique Canto
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowFiltroBloqueioAction extends ShowAction {

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		
		return "contingenciaBloqueio.vm";
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {

		Configuracao contingencia = (Configuracao) request.getAttribute("contingenciaCrm");
		context.put("contingenciaCrm", contingencia);
		context.put("mensagem",request.getAttribute(Constantes.MENSAGEM));

	}

	/* (non-Javadoc);
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {

		return null;
	}

}
