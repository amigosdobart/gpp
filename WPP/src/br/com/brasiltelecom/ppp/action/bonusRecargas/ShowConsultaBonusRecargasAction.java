/*
 * Created on 04/08/2004
 *
 */
package br.com.brasiltelecom.ppp.action.bonusRecargas;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author André Gonçalves
 * @since 04/08/2004
 */
public class ShowConsultaBonusRecargasAction extends ShowAction {

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaBonusRecargas.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		context.put("dados", request.getAttribute("dados"));
		context.put("tamanho", request.getAttribute("tamanho"));
		context.put (Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
