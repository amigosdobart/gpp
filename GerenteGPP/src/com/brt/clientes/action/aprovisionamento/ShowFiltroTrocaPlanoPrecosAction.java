
package com.brt.clientes.action.aprovisionamento;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;

import com.brt.clientes.action.base.ShowAction;

/**
 * Mostra o filtro de plano de pre�os
 * @author Alex Pitacci Sim�es
 * @since 03/06/2004
 */
public class ShowFiltroTrocaPlanoPrecosAction extends ShowAction {

	/**
	 * @see com.brt.clientes.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
	 */
	public void updateVelocityContext(VelocityContext vctx,
			HttpServletRequest request, ActionForm form) {

	}

	/**
	 * @see com.brt.clientes.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroAprovisionamentoTrocaPlanoPrecos.vm";
	}

}