/*
 * Created on 17/05/2005
 */
package br.com.brasiltelecom.ppp.action.ajustaStatusServico;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

/**
 * @author Lawrence Josuá
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowFiltroAjustaStatusServico extends ShowAction {
	
	public String getTela() {
		return "filtroAjustaStatusServico.vm";

	}
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {

	}

	public String getOperacao() {
		return null;
	}

}
