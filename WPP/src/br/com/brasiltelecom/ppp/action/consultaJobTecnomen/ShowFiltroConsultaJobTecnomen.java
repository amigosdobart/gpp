package br.com.brasiltelecom.ppp.action.consultaJobTecnomen;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

/**
 * @author Joao Carlos
 * Data..: 14/02/2005
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowFiltroConsultaJobTecnomen extends ShowAction {
	
	public String getTela() {
		return "filtroConsultaJobTecnomen.vm";
	}
	
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
	}

	public String getOperacao() {
		return null;
	}
}
