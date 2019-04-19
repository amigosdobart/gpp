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
 * Classe respons�vel por tratar e mostrar todos os erros do sistema.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowErrorAction extends Action {

	/**
	 * M�todo para pegar a Tela de Apresenta��o.
	 * Tem como finalidade principal, mostrar a tela de erros ocorridos no sistema.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 */
	public String getTela() {
	
		return "telaError.vm";
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

	/**
	 * M�todo principal da Classe, � o corpo da Classe.
	 * 
	 * @param context par�metro do tipo VelocityContext.
	 * @param request par�metro do tipo HttpServletRequest.
	 * @param db	   par�metro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
/*

	public void updateVelocityContext(VelocityContext context,
				HttpServletRequest request, Database db) throws Exception, ServletException{

		ServletContext servletContext = servlet.getServletContext();
		Object msgObj =  request.getAttribute(Constantes.MENSAGEM);

		String mensagem=null;

		if(msgObj != null){
			mensagem= msgObj.toString();
		}

		try{

			Template template = Velocity.getTemplate("telaError.vm");
			VelocityContext vctx = new VelocityContext();
//			MenuVertical.getMenu(request, vctx);
			
			if(mensagem != null){
				vctx.put(Constantes.MENSAGEM, mensagem);
			}

			//template.merge(vctx, response.getWriter());
		}
		catch(Exception e){
			servletContext.log("Erro", e);
			e.printStackTrace();
		}

	}
*/
		public final ActionForward execute(ActionMapping actionMapping,
		  ActionForm actionForm,HttpServletRequest request,HttpServletResponse response)throws Exception {
//			ServletContext servletContext = servlet.getServletContext();
			Template template = Velocity.getTemplate("telaError.vm");
			VelocityContext vctx = new VelocityContext();
			String mensagem = (String) request.getAttribute(Constantes.MENSAGEM);
			vctx.put(Constantes.MENSAGEM, mensagem);	
//			HttpSession session = request.getSession();
//			vctx.put("util", new Util());
			vctx.put("statusGPP", StatusGPP.getStatus());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			vctx.put("dataCabecalho",sdf.format(new Date()));
			template.merge(vctx, response.getWriter());
			
			return null;			


	}
}
