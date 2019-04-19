package br.com.brasiltelecom.ppp.action.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * Classe que mostra a primeira página depois do logon do usuário.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowWelcomeAction extends ShowAction {

	/**
	 * Método para pegar a Tela de Apresentação.
	 * Tem como finalidade principal, mostrar a tela de apresentação do sistemas após o login no sistema.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 */

/*	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response)throws IOException, ServletException {

		ServletContext servletContext = servlet.getServletContext();		
		HttpSession sessaoUsuario = request.getSession();
		Usuario login = (Usuario) sessaoUsuario.getAttribute(Constantes.LOGIN);
		ActionForward result = null;


		String mensagem = (String) request.getAttribute(Constantes.MENSAGEM);
		try {
			
			Template template = Velocity.getTemplate("welcome.vm");
			VelocityContext vctx = new VelocityContext();
			MenuVertical.getMenu(request, vctx);
			
			if( mensagem != null ){
				vctx.put("ehHome","S");
				vctx.put(Constantes.MENSAGEM, mensagem);	

			}

			template.merge(vctx, response.getWriter());
			
		} catch( Exception e ) {

			servletContext.log("Erro", e);	

		}
			
		return null;

	}*/


	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "welcome.vm";
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception {
		
		String mensagem = (String) request.getAttribute(Constantes.MENSAGEM);
		context.put("refresh", "S");
		if( mensagem != null ){
				context.put("ehHome","S");
				context.put(Constantes.MENSAGEM, mensagem);	
				

			}
		
	}


	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}


}
