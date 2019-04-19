package br.com.brasiltelecom.ppp.action.logOperacao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.webComponent.MenuVertical;
import br.com.brasiltelecom.ppp.session.util.Util;


/**
 * 
 * Classe para mostrar o resultado da consulta de Log das Opera��es.
 * 
 * @author Luciano Vilela
 * @since 22/09/2003
 * 
 */
public class ShowConsultaLogOperacaoAction extends ShowAction {


	private String codOperacao = Constantes.MENU_LOG_OPERACOES;
	
     //guarda o content type da p�gina
     String  strContentType = "text/html";
     
	/**
	 * M�todo para pegar a Tela do Filtro de Consulta do Log da Opera��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaLogOperacao.vm";
	}

	/**
	 * M�todo principal da classe, repons�vel por definir o content type para o tipo de relat�rio de Log.
	 * 
	 * @param actionMapping par�metro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm par�metro do tipo org.apache.struts.action.ActionForm.
	 * @param request  par�metro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response par�metro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db par�metro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
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
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		vctx.put("login", login);
		vctx.put("session", session);
		vctx.put("util", new Util());
  	    response.setContentType(strContentType);//linha adicionada para modificar o content type 		
		template.merge(vctx, response.getWriter());
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
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, Database db) {
		 /**==variaveis==*/
		 int page = 0 ;
		 int pagesize = 90;
		 Collection report = new ArrayList();		    
		 /**=============**/
		 
		 //recupera pagina atual
		 if (request.getAttribute("page") != null && !"".equals(request.getAttribute("page"))){
		 	 page = Integer.parseInt(request.getAttribute("page").toString());
		 }		  
		 
		 report = (Collection)request.getAttribute("report") ;
		 	
		 //recupera pagina de uma colecao 	
		 context.put("report",Util.getPagedItem(page,pagesize,report));
		 //guarda o ultimo filtro  
		 context.put("filtro_sql",request.getAttribute("filtro_sql"))	;  
	     //recupera collection de paginas
		 context.put("paginas",Util.getPages(report.size(),pagesize)); 
		 //coloca a p�gina atual
		 context.put("page", new Integer(page)); 
		 //coloca o total
		 context.put("total",Integer.toString(report.size()));
		   
		 context.put("paginaAtual",new Integer(page + 1));
			 
		 context.put("totalPaginas",new Integer(report.size()/pagesize + 1));
		   
		 context.put("tamanho",String.valueOf(report.size()));

		 this.strContentType = "text/html";

		 
		 HttpSession session = request.getSession();
  		 Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		 Map operacoesUsuario = login.getOperacoesPermitidas("MENU_CONTROLE");

		 if(operacoesUsuario.get("MENU_LOG_OPERACOES") != null){
			context.put("consultar", "S");				
		 }	  
	}

	/**
	 * M�todo para pegar a Opera��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Opera��o realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
