/*
 * Created on 17/05/2005
 */
package br.com.brasiltelecom.ppp.action.ajustaStatusServico;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Lawrence Josuá
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowTrocaStatusServico extends ShowAction {
	
	public String getTela() {
		return "trocaStatusServico.vm";

	}
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		
		context.put("mensagem", request.getAttribute(Constantes.MENSAGEM));

	}

	public String getOperacao() {
		return null;
	}

}
