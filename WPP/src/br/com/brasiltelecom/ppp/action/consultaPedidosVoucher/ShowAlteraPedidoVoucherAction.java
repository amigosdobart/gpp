/*
 * Created on 06/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

/**
 * @author André Gonçalves
 * @since 06/07/2004
 */
public class ShowAlteraPedidoVoucherAction extends ShowAction {

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "editPedidoVoucher.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		
		context.put("numeroPedido", request.getAttribute("numPedido"));
		context.put("numeroOrdem", request.getAttribute("numOrdem") );
		
		context.put("statusVoucherInicial", request.getAttribute("statusVoucherInicial") );
		context.put("statusVoucherInicialString", request.getAttribute("statusVoucherInicialString") );		
		context.put("statusVoucherFinal", request.getAttribute("statusVoucherFinal") );
		context.put("statusVoucherFinalString", request.getAttribute("statusVoucherFinalString") );
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
