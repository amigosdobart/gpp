/*
 * Created on 09/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.entity.PedidoVoucher;

/**
 * @author André Gonçalves
 * @since 09/07/2004
 */
public class ConsultaAlteraPedidoVoucherAction extends ActionPortal {

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		ActionForward result = null;
		
		// O parametro item está na forma: nroPedido, nroOrdem, dataPedido, statusPedido, statusVoucherString, statusVoucher
		String[] parametros = request.getParameter("item").split(",");
		
		String nroPedidoSelecionado = parametros[0];
		String nroOrdem = parametros[1];
		//String dataPedido = parametros[2];
		//String statusPedido = parametros[3];
		String statusVoucherString = parametros[4];
		String statusVoucher = parametros[5];
		
		request.setAttribute("numOrdem", nroOrdem);
		request.setAttribute("numPedido", nroPedidoSelecionado);
		
		request.setAttribute("statusVoucherInicial", statusVoucher);
		request.setAttribute("statusVoucherInicialString", statusVoucherString);
		
		
		if (statusVoucher.equals("0"))
		{
			// quando o status for 0, o usuario entra com o número da ordem
			request.setAttribute("statusVoucherFinal", "1");
			request.setAttribute("statusVoucherFinalString", PedidoVoucher.getStatusVoucherString("1"));
		}
		else if (statusVoucher.equals("2"))
		{
			request.setAttribute("statusVoucherFinal", "3");
			request.setAttribute("statusVoucherFinalString", PedidoVoucher.getStatusVoucherString("3"));
		}
		else if (statusVoucher.equals("3"))
		{
			request.setAttribute("statusVoucherFinal", "4");
			request.setAttribute("statusVoucherFinalString", PedidoVoucher.getStatusVoucherString("4"));
		}
		
		result = actionMapping.findForward("success");		    
		
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
