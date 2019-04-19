/*
 * Created on 05/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author André Gonçalves
 * @since 05/07/2004
 */
public class ShowDetalhePedidoVoucherAction extends ShowAction {

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaDetalhePedidoVoucher.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		
		Collection result = new ArrayList();
		
		if (request.getAttribute(Constantes.RESULT) != null) {
			
			result = (Collection) request.getAttribute(Constantes.RESULT);
			context.put("detalhesPedido",result);
			
			context.put("nroPedido", request.getAttribute("nroPedido"));
			context.put("nroOrdem", request.getAttribute("nroOrdem"));
			context.put("dataPedido", request.getAttribute("dataPedido"));
			context.put("statusPedido", request.getAttribute("statusPedido"));
			context.put("statusVoucher", request.getAttribute("statusVoucher"));
			
			context.put("tamanho",String.valueOf(result.size()));
		}

	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
