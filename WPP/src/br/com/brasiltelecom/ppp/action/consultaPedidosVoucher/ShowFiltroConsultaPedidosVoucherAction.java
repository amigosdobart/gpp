/*
 * Created on 22/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

/**
 * Mostra a tela de consulta de Pedidos de Voucher
 * 
 * @author Alberto Magno
 * @since 29/06/2004
 */
public class ShowFiltroConsultaPedidosVoucherAction extends ShowAction {
	
	private String codOperacao = null;//Constantes.MENU_PEDIDO_VOUCHER;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaPedidosVoucher.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception 
	{
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
