/*
 * Created on 17/06/2004
 *
 */
package com.brt.clientes.action.processosBatch;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;

import com.brt.clientes.action.base.ShowAction;

/**
 * @author Andr� Gon�alves
 * @since 17/06/2004
 */
public class ShowFiltroProcBatchEnvioConsolidacaoSCRGPPAction extends ShowAction {

	/**
	 * @see com.brt.clientes.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
	 */
	public void updateVelocityContext(VelocityContext vctx,
			HttpServletRequest request, ActionForm form) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.brt.clientes.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroProcBatchEnvioConsolidacaoSCRGPP.vm";
	}

}