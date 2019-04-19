/*
 * Created on 03/06/2004
 *
 */
package com.brt.clientes.action.recarga;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;

import com.brt.clientes.action.base.ShowAction;

/**
 * @author André Gonçalves
 * @since 03/06/2004
 */
public class ShowFiltroRecargaGenericaAction extends ShowAction {

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
		return "filtroRecargaGenericaAssinante.vm";
	}

}
