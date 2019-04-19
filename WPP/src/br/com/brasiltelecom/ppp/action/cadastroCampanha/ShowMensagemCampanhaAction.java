package br.com.brasiltelecom.ppp.action.cadastroCampanha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Joao Carlos
 * @since 02/03/2006
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 13/03/2007
 */
public class ShowMensagemCampanhaAction extends ShowAction
{
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "mensagemCampanha.vm";
	}
	
	public ActionForward performPortal(
	        ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response, Database db)throws Exception
	{
		response.setContentType("text/xml");
		response.setHeader( "Pragma", "no-cache" );
		response.addHeader( "Cache-Control", "must-revalidate" );
		response.addHeader( "Cache-Control", "no-cache" );
		response.addHeader( "Cache-Control", "no-store" );
		response.setDateHeader("Expires", 0);
		return super.performPortal(actionMapping,actionForm,request,response,db);
	}
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception
	{
		context.put("codRetorno",request.getAttribute("codRetorno"));
		context.put("msgRetorno",request.getAttribute("msgRetorno"));
		context.put(Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}
}
