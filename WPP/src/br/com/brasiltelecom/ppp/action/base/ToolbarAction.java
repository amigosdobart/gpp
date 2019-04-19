package br.com.brasiltelecom.ppp.action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

/**
 * Classe que realiza a insercao de dados do Toolbar no session do usuario.
 * 
 * @author Joao Carlos
 * @since 11/04/2007
 * 
 */
public class ToolbarAction extends ActionPortal
{
	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return null;
	}

	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
	{
		// Esta servlet tem por objetivo capturar informacoes enviadas pelo Toolbar
		// e disponibilizar na sessao do usuario para que possa ser utilizada por
		// outras telas do sistema. As informacoes coletadas abaixo serao repassadas
		// ao contexto para que possam ser utilizadas pela URL que serah redirecionada
		// que foi previamente configurada no struts-config.xml
		request.setAttribute("telefone", request.getParameter("telefone"));
		
		return actionMapping.findForward("success");
	}
}
