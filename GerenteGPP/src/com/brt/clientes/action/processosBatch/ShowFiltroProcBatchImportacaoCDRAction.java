
package com.brt.clientes.action.processosBatch;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;

import com.brt.clientes.action.base.ShowAction;

/**
 * Mostra a tela de importa��o de cdrs
 * @author Alex Pitacci Sim�es
 * @since 10/06/2004
 */
public class ShowFiltroProcBatchImportacaoCDRAction extends ShowAction {

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
		return "filtroProcBatchImportacaoCDR.vm";
	}

}
