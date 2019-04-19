package br.com.brasiltelecom.ppp.action.usuario;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.GrupoHome;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * Possui a finalidade mostrar a página inicial dos Usuários
 * possibilitando ao usuário consultar ou entrar com um novo Usuário Interno ou Externo.
 * 
 * @author Victor Paulo Alves de Almeida / Sandro Augusto
 * 
 */
public class ShowFiltroUsuarioAction extends ShowAction {

	private String codOperacao = Constantes.MENU_PERFIL_USUARIO;
	/**
	 * Método para pegar a Tela Principal do Usuário.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroUsuario.vm";
	}

	/**
	 * Método principal da Classe, é o corpo da Classe.<BR>
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, Database db) throws Exception{

//		String empresaId = request.getParameter("empresa");

		HttpSession session = request.getSession();
		db.begin();
		//PEGA TODAS AS FILIAIS
/*
		Collection filial =  FilialHome.findAll(db);
		context.put("filial",filial);
*/
		//PEGA TODAS AS EMPRESAS

/*		Collection empresa =  EmpresaHome.findAll(db);
		context.put("empresa",empresa);
*/
		// Consulta grupos
		Collection grupo =  GrupoHome.findByFilter(db, new HashMap());
		context.put("grupo",grupo);
		

		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaUsuario.setPermissao(login, context);
		 
		context.put("usuarios", request.getAttribute(Constantes.RESULT));
		 
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
