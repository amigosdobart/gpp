/*
 * Created on 19/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.administrativoCadastroAjustes;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de filtro do motivo de ajuste
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class ShowFiltroAdministrativoAjusteAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CADASTRO_AJU;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroAdministrativoAjuste.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
			
		context.put("gravarMotivoAjuste","S");
		

	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
