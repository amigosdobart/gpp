package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.PedidoVoucherHome;

/**
 * Mostra a tela de consulta de Pedidos de Voucher com Erro
 * 
 * @author Joao Carlos
 * @since 26/12/2006
 */
public class ShowPedidosVoucherComErroAction extends ShowAction 
{
	private String codOperacao = null;//Constantes.MENU_PEDIDO_VOUCHER;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "consultaPedidosVoucherComErro.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,Database db) throws Exception 
	{
		// Realiza a consulta dos pedidos que estao com status de Erro no 
		// GPP para que a equipe de operacao possa decidir o que fazer
		// para resolver o problema e entao resubmeter ao processo no sistema
		Collection result = new ArrayList();
		
		try
		{
			db.begin();
			result = PedidoVoucherHome.findByStatusPedido(db, "X", true);
		}
		finally
		{
			db.commit();
		}
		
		context.put("dadosPedido", result);
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}
