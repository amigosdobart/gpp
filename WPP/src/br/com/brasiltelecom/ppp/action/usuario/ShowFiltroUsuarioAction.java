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
 * Possui a finalidade mostrar a p�gina inicial dos Usu�rios
 * possibilitando ao usu�rio consultar ou entrar com um novo Usu�rio Interno ou Externo.
 * 
 * @author Victor Paulo Alves de Almeida / Sandro Augusto
 * 
 */
public class ShowFiltroUsuarioAction extends ShowAction {

	private String codOperacao = Constantes.MENU_PERFIL_USUARIO;
	/**
	 * M�todo para pegar a Tela Principal do Usu�rio.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroUsuario.vm";
	}

	/**
	 * M�todo principal da Classe, � o corpo da Classe.<BR>
	 * 
	 * @param context par�metro do tipo VelocityContext.
	 * @param request par�metro do tipo HttpServletRequest.
	 * @param db	   par�metro do tipo Database do Castor.
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
	 * M�todo para pegar a Opera��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Opera��o realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
