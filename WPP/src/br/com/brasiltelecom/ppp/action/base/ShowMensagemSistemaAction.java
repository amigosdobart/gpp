package br.com.brasiltelecom.ppp.action.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

//import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
//import org.apache.velocity.app.Velocity;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.interfacegpp.StatusGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
//import br.com.brasiltelecom.portalNF.webComponent.MenuVertical;


/**
 * Classe responsável por mostrar todas as mensagens do sistema.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowMensagemSistemaAction extends ShowAction {

	/**
	 * 
	 * Tem como finalidade principal, mostrar a tela de mensagem do sistema.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 */
	public String getTela() {
	
		return "mensagemSistema.vm";
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

	/**
	 * Método principal da Classe, é o corpo da Classe.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,
				HttpServletRequest request, Database db) throws Exception, ServletException{

		ServletContext servletContext = servlet.getServletContext();
		Object msgObj =  request.getAttribute(Constantes.MENSAGEM);

		String mensagem=null;

		if(msgObj != null){
			mensagem= msgObj.toString();
		}

		try{

//			Template template = Velocity.getTemplate("mensagemSistema.vm");
			VelocityContext vctx = new VelocityContext();
//			MenuVertical.getMenu(request, vctx);
			vctx.put("statusGPP", StatusGPP.getStatus());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			vctx.put("dataCabecalho",sdf.format(new Date()));
			if(mensagem != null){
				vctx.put(Constantes.MENSAGEM, mensagem);	  
			}

			//template.merge(vctx, response.getWriter());
		}
		catch(Exception e){
			servletContext.log("Erro", e);	
		}

	}
}
