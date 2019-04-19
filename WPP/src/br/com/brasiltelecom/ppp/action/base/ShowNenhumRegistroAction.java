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
//import br.com.brasiltelecom.portalNF.webComponent.MenuVertical;


/**
 * Classe responsável por tratar e mostrar todos os erros do sistema.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowNenhumRegistroAction extends Action {

	/**
	 * Método para pegar a Tela de Apresentação.
	 * Tem como finalidade principal, mostrar a tela de erros ocorridos no sistema.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 */
	public String getTela() {
	
		return "nenhumRegistro.vm";
	}
	
	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {

			return null;
	}

		public final ActionForward execute(ActionMapping actionMapping,
		  ActionForm actionForm,HttpServletRequest request,HttpServletResponse response)throws Exception {
			//ServletContext servletContext = servlet.getServletContext();
			Template template = Velocity.getTemplate("nenhumRegistro.vm");
			VelocityContext vctx = new VelocityContext();
			String mensagem = (String) request.getAttribute(Constantes.MENSAGEM);
			vctx.put(Constantes.MENSAGEM, mensagem);	
			vctx.put("imagem", (String) request.getAttribute("imagem"));	
			//HttpSession session = request.getSession();
			vctx.put("statusGPP", StatusGPP.getStatus());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			vctx.put("dataCabecalho",sdf.format(new Date()));
			template.merge(vctx, response.getWriter());
			
			return null;			


	}
}
