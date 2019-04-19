package br.com.brasiltelecom.ppp.action.base;

import java.text.SimpleDateFormat;
import java.util.Date;

//import javax.servlet.ServletContext;
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
 * Classe Respons�vel por redirecionar aplica��o fora do Struts para o CRM
 * 
 * @author Alberto Magno
 * @since 08/07/2003
 * 
 */
public class ShowRedirectCRMAction extends Action {

	/**
	 * M�todo para pegar a Tela de Apresenta��o.
	 * Tem como finalidade principal, mostrar a tela de redirecionamento.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 */
	public String getTela() {
	
		return "redirectCRM.vm";
	}
	
	/**
	 * M�todo para pegar a Opera��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Opera��o realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {

			return null;
	}

		public final ActionForward execute(ActionMapping actionMapping,
		  ActionForm actionForm,HttpServletRequest request,HttpServletResponse response)throws Exception {
			//ServletContext servletContext = servlet.getServletContext();
			Template template = Velocity.getTemplate("redirectCRM.vm");
			VelocityContext vctx = new VelocityContext();
			vctx.put("endereco", (String) request.getAttribute("endereco"));
			
			//HttpSession session = request.getSession();

			String mensagem = (String) request.getAttribute(Constantes.MENSAGEM);
			vctx.put(Constantes.MENSAGEM, mensagem);	
			vctx.put("statusGPP", StatusGPP.getStatus());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			vctx.put("dataCabecalho",sdf.format(new Date()));
			template.merge(vctx, response.getWriter());
			
			return null;			


	}
}
