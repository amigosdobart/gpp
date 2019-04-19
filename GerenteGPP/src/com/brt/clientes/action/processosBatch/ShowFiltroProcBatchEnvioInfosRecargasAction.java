/*
 * Created on 08/06/2004
 */
package com.brt.clientes.action.processosBatch;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;

import com.brt.clientes.action.base.ShowAction;

/**
 * @author Daniel Ferreira
 */
public class ShowFiltroProcBatchEnvioInfosRecargasAction extends ShowAction {

	/* (non-Javadoc)
	 * @see com.brt.clientes.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
	 */
	public void updateVelocityContext(VelocityContext vctx,
			HttpServletRequest request, ActionForm form) {
	}

	/* (non-Javadoc)
	 * @see com.brt.clientes.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroProcBatchEnvioInfosRecargas.vm";
	}

}