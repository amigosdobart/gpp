package br.com.brasiltelecom.ppp.action.base;

import java.text.SimpleDateFormat;
import java.util.Date;

//import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.webComponent.MenuVertical;
//import br.com.brasiltelecom.session.util.Util;
import br.com.brasiltelecom.ppp.interfacegpp.StatusGPP;

/**
 * Classe abstrata que deve ser extendida por todas as classes responsáveis pela escolha da tela de apresentação.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 * Atualizado por Bernardo Dias
 * Data: 09/02/2007
 * 
 */
public abstract class ShowAction extends ActionPortal {
	
	/**
	 *  Método principal da Classe, é o corpo da Classe.<br>
	 * 
	 *  Possui a finalidade de exibição de todas as telas do sistema com suas propriedades.
	 * 
	 * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db parâmetro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 *
	 * @see org.apache.struts.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */

	public ActionForward performPortal(
		        ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response, Database db)throws Exception {
		
		//ServletContext servletContext = servlet.getServletContext();
		Template template = Velocity.getTemplate(getTela());
		VelocityContext vctx = new VelocityContext();
		MenuVertical.getMenu(request, vctx);

		updateVelocityContext(vctx,request, db);
		String mensagem = (String) request.getAttribute(Constantes.MENSAGEM);
		vctx.put(Constantes.MENSAGEM, mensagem);	
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		vctx.put("login", login);
		vctx.put("session", session);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		vctx.put("dataCabecalho",sdf.format(new Date()));
//		vctx.put("util", new Util());
				
		vctx.put("statusGPP", StatusGPP.getStatus());
		
		boolean iChain = false;
		
		if("S".equalsIgnoreCase((String)servlet.getServletContext().getAttribute(Constantes.INTEGRACAO_ICHAIN)))
		{
			iChain = true;
		}
		
		vctx.put("iChain", new Boolean(iChain));

		vctx.put("tipoDeploy",  (String)servlet.getServletContext().getAttribute(Constantes.TIPO_DEPLOY));
		vctx.put("versaoDeploy",(String)servlet.getServletContext().getAttribute(Constantes.VERSAO_DEPLOY));
		vctx.put("buildDeploy", (String)servlet.getServletContext().getAttribute(Constantes.BUILD_DEPLOY));
		
		template.merge(vctx, response.getWriter());
			
		return null;			

	}
	
	/**
	 * Método abstrato que pega a tela de apresentação que será utilizada pela classe que extende a classe ShowAction.
	 *
	 * @return Um objeto do tipo String, que representa a tela de apresentação.
	 */
	public abstract String getTela();
	
	/**
     * Método abstrato que representa o corpo de todas as Classes que extendem a Classe ShowAction.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
	 */
	public abstract void updateVelocityContext(VelocityContext context,
				HttpServletRequest request, Database db) throws Exception;
	

}
