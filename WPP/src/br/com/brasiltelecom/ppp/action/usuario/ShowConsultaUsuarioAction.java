package br.com.brasiltelecom.ppp.action.usuario;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;


/**
 * Classe respons�vel por mostrar o filtro de consulta de Usu�rios do sistema.
 * 
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 */
public class ShowConsultaUsuarioAction extends ShowAction {


	private String codOperacao = Constantes.MENU_PERFIL_USUARIO; 
	/*** Guarda o content type da p�gina.*/
     String  strContentType = "text/html";



	/**
	 * M�todo para pegar a Tela do Filtro de Consulta de Usu�rios.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaUsuario.vm";
	}


	/**
	 * M�todo principal da Classe, � o corpo da Classe.
	 * 
	 * @param context par�metro do tipo VelocityContext.
	 * @param request par�metro do tipo HttpServletRequest.
	 * @param db	   par�metro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,
	   Database db) {
	   	
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaUsuario.setPermissao(login, context);		

		/*****Variaveis que serao utilizadas no Metodo*****/
		  int page = 0 ;
		  int pagesize = 50;
		  //int excel = 0;
		  Collection report = new ArrayList();
		    
		/*************************************************/
	    //recupera pagina atual
	    if ((request.getAttribute("page") != null) 
	   		&& (!"".equals(request.getAttribute("page"))))
	    {
	   	 	page = Integer.parseInt(request.getAttribute("page").toString());
	    }
		 
		report = (Collection) request.getAttribute(Constantes.RESULT);
            	
		//recupera pagina de uma colecao
		context.put("usuarios",Util.getPagedItem(page,pagesize,report));
		//guarda o ultimo filtro
		context.put("filtro_sql",request.getAttribute("filtro_sql"));  
	    //recupera collection de paginas
		context.put("paginas",Util.getPages(report.size(),pagesize)); 
		//coloca a p�gina atual
		context.put("page",Integer.toString(page)); 
		//coloca o total
		context.put("total",Integer.toString(report.size()));
		
		context.put("tamanho",String.valueOf(report.size()));
		
		context.put("util", new Util());

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
