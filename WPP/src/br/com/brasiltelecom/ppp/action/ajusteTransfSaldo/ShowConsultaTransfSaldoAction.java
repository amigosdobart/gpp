/*
 * Created on 28/01/2005
 *
 */
package br.com.brasiltelecom.ppp.action.ajusteTransfSaldo;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a Tela Informações de Assinante para Transferência de Saldos.
 * 
 * @author Daniel Ferreira
 * @since 28/01/2005
 */
public class ShowConsultaTransfSaldoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_TRANSF_SALDO;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaTransfSaldo.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
		
		context.put("assinante", request.getAttribute("assinante"));
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
