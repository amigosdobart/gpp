/*
 * Created on 28/09/2005
 */
package br.com.brasiltelecom.ppp.action.consultaCreditoPulaPula;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * @author Marcos C. Magalhaes
 */
public class ShowConsultaCreditoPulaPula extends ShowAction {

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		
		return "consultaCreditoPulaPula.vm";
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		
		context.put("msisdn", request.getParameter("msisdn"));
		//context.put("promocao", request.getAttribute("promocao"));
		//context.put("count", request.getAttribute("count"));
		context.put("valor", request.getAttribute("valor"));
		context.put("util", new Util());
		context.put("mensagem", request.getAttribute(Constantes.MENSAGEM));
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
