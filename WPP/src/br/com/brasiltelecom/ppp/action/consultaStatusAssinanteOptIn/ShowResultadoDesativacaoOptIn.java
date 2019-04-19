package br.com.brasiltelecom.ppp.action.consultaStatusAssinanteOptIn;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Geraldo Palmeira
 * @since 19/09/2006
 */

public class ShowResultadoDesativacaoOptIn extends ShowAction {

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "mostraResultadoDesativacaoOptIn.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request, Database db) throws Exception 
	{
		context.put(Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));
		context.put("msisdn", request.getAttribute("msisdn"));
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return null;
	}
}
