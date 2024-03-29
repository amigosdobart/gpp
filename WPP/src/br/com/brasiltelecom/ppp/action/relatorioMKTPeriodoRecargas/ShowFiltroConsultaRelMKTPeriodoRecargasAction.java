/*
 * Created on 22/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.relatorioMKTPeriodoRecargas;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de receita por per�odo de recargas
 * 
 * @author Andr� Gon�alves
 * @since 21/05/2004
 */
public class ShowFiltroConsultaRelMKTPeriodoRecargasAction extends ShowAction {

	private String codOperacao = Constantes.MENU_MKT_PERIODO;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelMKTPeriodoRecargas.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}
}
