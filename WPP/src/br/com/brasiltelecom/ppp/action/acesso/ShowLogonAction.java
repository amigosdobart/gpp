package br.com.brasiltelecom.ppp.action.acesso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import br.com.brasiltelecom.ppp.interfacegpp.StatusGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe que mostra a p�gina para a realiza��o do Logon dos usu�rios.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowLogonAction extends Action {

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
				HttpServletResponse response)throws IOException, ServletException {
		
		ServletContext servletContext = servlet.getServletContext();
		//HttpSession session = request.getSession(true);


		String mensagem 	= (String) request.getAttribute(Constantes.MENSAGEM);
		
		ServletContext context = getServlet().getServletContext();
		boolean mostraLogin  = new Boolean((String) context.getAttribute(Constantes.IDT_MOSTRA_LOGIN)).booleanValue();
		Template template 	= null;
 
		try{
			if (mostraLogin)
			{
				template = Velocity.getTemplate("logon.vm");
			}
			else
			{
				template = Velocity.getTemplate("redirecionamento.vm");
			}
			
			VelocityContext vctx = new VelocityContext();

			if(mensagem != null){
				vctx.put(Constantes.MENSAGEM, mensagem);	
			}
			vctx.put("statusGPP", StatusGPP.getStatus());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			vctx.put("dataCabecalho",sdf.format(new Date()));
			template.merge(vctx, response.getWriter());
		}
		catch(Exception e){
			servletContext.log("Erro", e);	
			return actionMapping.findForward("error");
		}
			
		return null;

	}
}