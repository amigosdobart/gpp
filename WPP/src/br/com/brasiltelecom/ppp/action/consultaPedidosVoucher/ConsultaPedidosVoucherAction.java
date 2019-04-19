package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.home.PedidoVoucherHome;
/**
 * Consulta Pedidos de Voucher de acordo com Status;
 * 
 * @author Alberto Magno
 * @since 29/06/2004
 */
public class ConsultaPedidosVoucherAction extends ActionPortal {
	
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
		ActionMapping actionMapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response,
		Database db)
		throws Exception {
	
		ActionForward result = null;
		db.begin();
		
		logger.info("Consulta aos pedidos de voucher solicitada");
		
		Map map = new HashMap();
		
		map.put("status", request.getParameter("status"));
		map.put("tipoPeriodo", request.getParameter("tipoPeriodo"));
		map.put("periodo", request.getParameter("periodo"));
		map.put("dataInicial", request.getParameter("dataInicial"));
		map.put("dataFinal", request.getParameter("dataFinal"));
		
		Collection resultSet;

		try
		{
			resultSet = PedidoVoucherHome.findByStatus(db,map);			
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta dos pedidos de voucher, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		request.setAttribute(Constantes.RESULT, resultSet);
		request.setAttribute("status",request.getParameter("status"));
		request.setAttribute("page",request.getParameter("page")) ;
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta aos pedidos de voucher realizada com sucesso!");
		codOperacao = Constantes.COD_CONSULTA_PEDIDOS_VOUCHER;
							
		result = actionMapping.findForward("success");
	
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}