
package com.brt.clientes.action.aprovisionamento;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;

import com.brt.clientes.action.base.ShowAction;

/**
 * Mostra a tela de filtro de ativacao
 * @author Alex Pitacci Sim�es
 * @since 02/06/2004
 */
public class ShowFiltroAtivacaoAction extends ShowAction {

	/**
	 * @see com.brt.clientes.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest)
	 */
	public void updateVelocityContext(VelocityContext vctx,
			HttpServletRequest request, ActionForm form) {

	}

	/**
	 * @see com.brt.clientes.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroAprovisionamentoAtivacao.vm";
	}

}