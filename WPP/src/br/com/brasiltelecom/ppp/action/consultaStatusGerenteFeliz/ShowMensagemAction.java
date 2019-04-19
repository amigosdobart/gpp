package br.com.brasiltelecom.ppp.action.consultaStatusGerenteFeliz;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
/**
 * @author Geraldo Palmeira
 * @since 28/09/2006
 */

public class ShowMensagemAction extends ShowAction {

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "mensagemRetorno.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request, Database db) throws Exception 
	{
		// Apenas mostra a mensagem de retorno na tela do portal.
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return null;
	}
}
