package br.com.brasiltelecom.ppp.action.acesso;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe que mostra a página para as alterações das senhas dos usuários.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowAlteraSenhaAction extends Action {

	/**
	 * Método principal do Struts, é o corpo da Classe.
	 * 
	 * @param actionMapping parâmetro do tipo ActionMapping.
	 * @param actionForm parâmetro do tipo ActionForm.
	 * @param request  parâmetro do tipo HttpServletRequest.
	 * @param response parâmetro do tipo HttpServletResponse.
	 *
	 * @throws IOException, ServletException.
	 */
	public ActionForward execute(
		        ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response)throws IOException, ServletException {
		
		ServletContext servletContext = servlet.getServletContext();
		String mensagem = (String) request.getAttribute(Constantes.MENSAGEM);

		try{
			
			Template template = Velocity.getTemplate("alteraSenha.vm");
			VelocityContext vctx = new VelocityContext();

			vctx.put("tamanhoSenha", "0");

			if(mensagem != null){
				vctx.put(Constantes.MENSAGEM, mensagem);
			}

			vctx.put("matricula", request.getParameter("nome"));
			template.merge(vctx, response.getWriter());
		}
		catch(Exception e){
			servletContext.log("Erro", e);	
			return actionMapping.findForward("error");
		}
			
		return null;

	}
}