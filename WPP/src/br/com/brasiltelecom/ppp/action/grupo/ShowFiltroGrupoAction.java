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
 * Classe para mostrar a p�gina inicial dos Grupos, possui os seguintes bot�es:
 * 	- de Consulta;
 *  - de Cria��o;
 *  - e de Limpar os Dados Preenchidos.
 * 
 * @author Luciano Vilela
 * @since 22/06/2003
 * 
 */

public class ShowFiltroGrupoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CADASTRO_PERFIL;
	/**
	 * M�todo para pegar a Tela de Apresenta��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 */
	public String getTela() {
		return "filtroGrupo.vm";
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
		   HttpServletRequest request, Database db) {
	
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaGrupo.setPermissao(login, context);
		   	
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
