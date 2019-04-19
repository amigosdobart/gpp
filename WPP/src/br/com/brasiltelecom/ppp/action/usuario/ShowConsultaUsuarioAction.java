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
 * Classe responsável por mostrar o filtro de consulta de Usuários do sistema.
 * 
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 */
public class ShowConsultaUsuarioAction extends ShowAction {


	private String codOperacao = Constantes.MENU_PERFIL_USUARIO; 
	/*** Guarda o content type da página.*/
     String  strContentType = "text/html";



	/**
	 * Método para pegar a Tela do Filtro de Consulta de Usuários.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaUsuario.vm";
	}


	/**
	 * Método principal da Classe, é o corpo da Classe.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
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
		//coloca a página atual
		context.put("page",Integer.toString(page)); 
		//coloca o total
		context.put("total",Integer.toString(report.size()));
		
		context.put("tamanho",String.valueOf(report.size()));
		
		context.put("util", new Util());

	}

	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
