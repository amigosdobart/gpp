package br.com.brasiltelecom.ppp.action.acesso;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe que executa o logoff
 * 
 * @author Bernardo Dias
 * @since 09/02/2007
 * 
 */
public class LogoffAction extends Action 
{


	/**
	 * M�todo principal do Struts, � o corpo da Classe.
	 * 
	 * @param actionMapping par�metro do tipo ActionMapping.
	 * @param actionForm par�metro do tipo ActionForm.
	 * @param request  par�metro do tipo HttpServletRequest.
	 * @param response par�metro do tipo HttpServletResponse.
	 *
	 * @throws IOException, ServletException.
	 */
	public ActionForward execute(
		   ActionMapping actionMapping,
		   ActionForm actionForm,
		   HttpServletRequest request,
		   HttpServletResponse response) throws IOException, ServletException 
	{

		ActionForward result = null;
	
		try
		{
			HttpSession session = request.getSession(true);
			session.removeAttribute(Constantes.USUARIO);
			session.invalidate();
			request.setAttribute(Constantes.MENSAGEM, "Logoff realizado com sucesso.");
			result = actionMapping.findForward("showLogon");
		}
		catch(Exception e)
		{
			request.setAttribute(Constantes.MENSAGEM, e.toString());
			result = actionMapping.findForward("error");			
		}

		return result;
	}

	
}