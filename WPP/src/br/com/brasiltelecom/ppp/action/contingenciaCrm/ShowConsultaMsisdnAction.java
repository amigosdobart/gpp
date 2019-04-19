/*
 * Created on 01/09/2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Henrique Canto
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowConsultaMsisdnAction extends ShowAction {

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "contingenciaBloqueio.vm";
	}


	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) 
	throws Exception 
	{
		context.put("assinante",request.getAttribute("assinante"));
		context.put("tamanho",request.getAttribute("tamanho"));
		context.put("msisdn",request.getParameter("msisdn"));
		Configuracao contingencia = (Configuracao) request.getAttribute("contingenciaCrm");
		context.put("contingenciaCrm", contingencia);
		
		if (request.getAttribute(Constantes.RESULT)!=null)
		{
			Collection result = (Collection) request.getAttribute(Constantes.RESULT);
			context.put("motivosBloqueio", result);
		}
	}

	public String getOperacao() {
		return null;
	}

}
