package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.interfacegpp.PedidoVoucherGPP;

/**
 * Resubmete pedidos de voucher ao GPP
 * 
 * @author Joao Carlos
 * @since 29/12/2006
 */
public class ResubmeterPedidoVoucherAction extends ActionPortal
{
	private String codOperacao = Constantes.COD_CONSULTA_PEDIDOS_VOUCHER;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal( ActionMapping actionMapping,
										ActionForm actionForm,
										HttpServletRequest request,
										HttpServletResponse response,
										Database db) throws Exception
	{
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta 	= (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		long numPedido = request.getParameter("numPedido") != null ? Long.parseLong(request.getParameter("numPedido")) : 0;
		try
		{
			// Realiza a chamada ao sistema GPP para enviar o pedido novamente
			// para o processo de criacao de cartoes.
			PedidoVoucherGPP.resubmetePedidoVoucher(numPedido, servidor, porta);
			
			request.setAttribute(Constantes.MENSAGEM, "Pedido Numero "+ numPedido +" foi resubmetido ao processo de criacao de cartoes");
		}
		catch (Exception e)
		{
			logger.error("Não foi possivel resubmeter pedido "+numPedido+" Erro:"+e.getMessage(),e);
			request.setAttribute(Constantes.MENSAGEM,e.getMessage());
			throw e;
		}
		
		ActionForward result = actionMapping.findForward("success");
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return this.codOperacao;
	}
}