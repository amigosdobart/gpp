package br.com.brasiltelecom.ppp.action.usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

//import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.GrupoHome;
import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * 
 * Classe para mostrar a p�gina de cria��o e altera��o de Usu�rios Internos e Externos.
 * 
 * @author Victor Paulo A. de Almeida / Sandro Augusto.
 * 
 */
public class ShowEditarUsuarioAction extends ShowAction {

	private String codOperacao = Constantes.MENU_PERFIL_USUARIO;
	/**
	 * M�todo para pegar a Tela de Edi��o de Usu�rios Internos e Externos.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "editUsuario.vm";
	}

	/**
	 * M�todo principal da Classe, � o corpo da Classe.<BR>
	 * Respons�vel pela a��o no formul�rio de edi��o dde Usu�rios Internos e Externos.
	 * 
	 * @param context par�metro do tipo VelocityContext.
	 * @param request par�metro do tipo HttpServletRequest.
	 * @param db	   par�metro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, Database db) throws Exception {

		/************Declaracao das variaveis do metodo**************/
		 String matricula = (String) request.getParameter("obr_matricula");
		 String acao = request.getParameter("acao");
		 String tpUsuario= request.getParameter("tpUsuario");
		 Usuario usuario = null;
		 Collection grupos = null;
		 //Collection fornecedores = null;
		 //Collection fornecedor = null;
		 
		 Collection filiais = null;
		 //Collection filialLeft = null;
		 //Collection filialRight = null;

		 
//		 FilialHome fh = null;
//		 EmpresaHome eh = null;
		 ArrayList filial = null, empresa = null;
		/************************************************************/
		
		//ServletContext servletContext = servlet.getServletContext();
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaUsuario.setPermissao(login, context);

		db.begin();
		
		if(("editar".equals(acao)) || ("copiar".equals(acao))) {
			
			if(matricula != null) {
				usuario = UsuarioHome.findByID(db, matricula);
				
				if(usuario == null) {
					usuario = new Usuario();
					usuario.setMatricula(matricula);
				}
				
				if( matricula.startsWith("c ") ) {
					context.put("novo", "S");	
				}
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
			//filiais = (Collection)FilialHome.findAll(db);
			context.put("filiais", filiais);
		}
		
		grupos = GrupoHome.findByFilter(db, new HashMap());

		context.put("usuario", usuario);
		context.put("grupos", grupos);
		
		//PEGA TODAS AS FILIAIS
		//filial = (ArrayList) fh.findAll(db);

		if (filial != null) {				
			context.put("filial",filial);
		}		
		
		//PEGA TODAS AS EMPRESAS
		//empresa = (ArrayList) eh.findAll(db);

		if (empresa != null) {				
			context.put("empresa",empresa);
		}
		context.put("tpUsuario",tpUsuario);
		context.put("acao",acao);
		
		if ( tpUsuario.equalsIgnoreCase("Externo") && (!acao.equals("novo")) ){	
			
			context.put("fornecedor", usuario.getFornecedores());	

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
