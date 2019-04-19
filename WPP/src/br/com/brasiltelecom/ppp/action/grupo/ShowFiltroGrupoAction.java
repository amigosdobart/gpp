package br.com.brasiltelecom.ppp.action.grupo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * 
 * Classe para mostrar a página inicial dos Grupos, possui os seguintes botões:
 * 	- de Consulta;
 *  - de Criação;
 *  - e de Limpar os Dados Preenchidos.
 * 
 * @author Luciano Vilela
 * @since 22/06/2003
 * 
 */

public class ShowFiltroGrupoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CADASTRO_PERFIL;
	/**
	 * Método para pegar a Tela de Apresentação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 */
	public String getTela() {
		return "filtroGrupo.vm";
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
		   HttpServletRequest request, Database db) {
	
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaGrupo.setPermissao(login, context);
		   	
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
