package br.com.brasiltelecom.ppp.action.promocaoLancamento;

//  import java.util.ArrayList;

//import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * 
 * Classe para mostrar a página de criação e alteração de Usuários Internos e Externos.
 * 
 * @author Luciano Vilela
 * 
 */
public class ShowEditarPromocaoAssinanteAction extends ShowAction {

	private String codOperacao = Constantes.MENU_PERFIL_USUARIO;
	/**
	 * Método para pegar a Tela de Edição de Usuários Internos e Externos.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "editPromocaoAssinante.vm";
	}

	/**
	 * Método principal da Classe, é o corpo da Classe.<BR>
	 * Responsável pela ação no formulário de edição dde Usuários Internos e Externos.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, Database db) throws Exception {

		/************Declaracao das variaveis do metodo**************/
		 String matricula = (String) request.getParameter("obr_matricula");
		 String acao = request.getParameter("acao");
		 Usuario usuario = null;

		 
		 //ArrayList filial = null, empresa = null;
		/************************************************************/
		
		//ServletContext servletContext = servlet.getServletContext();
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaUsuario.setPermissao(login, context);

		db.begin();
		
		if(("editar".equals(acao)) ) {
			
			if(matricula != null) {
				usuario = UsuarioHome.findByID(db, matricula);
				
			} else {
				usuario = new Usuario();
			}

		
		} else if ( ("salvar".equals(acao)) || ("Reset Senha".equalsIgnoreCase(acao)) ) {
		
			usuario = new Usuario();
			UsuarioHome.setByRequest(usuario, request, db);
			context.put(Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));				
			acao = "editar";
			
		} else if("desbloquear".equals(acao)){

			usuario = UsuarioHome.findByID(db, matricula);
			context.put(Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));	

		} else if("novo".equals(acao)) {
		
			usuario = new Usuario();
			context.put(Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));				
			context.put("novo", "S");
		}
		

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
