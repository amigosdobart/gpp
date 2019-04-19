/*
 * Created on 05/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ItemPedidoVoucherHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
;

/**
 * @author André Gonçalves
 * @since 05/07/2004
 */
public class ConsultaDetalhePedidoVoucherAction extends ActionPortal {

	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		ActionForward result = null;
		db.begin();
		
		logger.info("Consulta de detalhes de pedidos de voucher solicitada");
		
		Collection resultSet;
		
		// O parametro item está na forma: nroPedido, nroOrdem, dataPedido, statusPedido, statusVoucher
		String dados = request.getParameter("item");
		
		String[] parametros = dados.split(",");
		
		String nroPedidoSelecionado = parametros[0];
		String nroOrdem = parametros[1];
		String dataPedido = parametros[2];
		String statusPedido = parametros[3];
		String statusVoucher = parametros[4];
		
		try
		{
			resultSet = ItemPedidoVoucherHome.findByPedido(db,nroPedidoSelecionado);			
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta de detalhes do pedido de voucher, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		
		request.setAttribute(Constantes.RESULT, resultSet);
		
		request.setAttribute("nroPedido", nroPedidoSelecionado);
		request.setAttribute("nroOrdem", nroOrdem);
		request.setAttribute("dataPedido", dataPedido);
		request.setAttribute("statusPedido", statusPedido);
		request.setAttribute("statusVoucher", statusVoucher);
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta a detalhes do voucher " + nroPedidoSelecionado + 
				" realizada com sucesso!");
		codOperacao = Constantes.COD_CONSULTA_DETALHE_PEDIDOS_VOUCHER;
		
		result = actionMapping.findForward("success");		    
		
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
