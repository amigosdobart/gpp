/*
 * Created on 26/01/2005
 *
 */
package br.com.brasiltelecom.ppp.action.ajusteTransfSaldo;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a Tela de Consulta de MSISDN para Transfer�ncia de Saldos.
 * 
 * @author Daniel Ferreira
 * @since 25/01/2005
 */
public class ShowFiltroConsultaTransfSaldoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_TRANSF_SALDO;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaTransfSaldo.vm";
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
