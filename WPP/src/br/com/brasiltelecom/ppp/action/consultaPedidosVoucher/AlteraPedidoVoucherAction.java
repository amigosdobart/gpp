/*
 * Created on 09/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaPedidosVoucher;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.PedidoVoucherHome;
import br.com.brasiltelecom.ppp.portal.entity.PedidoVoucher;
import br.com.brasiltelecom.ppp.portal.entity.HistoricoPedidosVoucher;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Andr� Gon�alves
 * @since 09/07/2004
 */
public class AlteraPedidoVoucherAction extends ActionPortal {

	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		db.begin();
		
		boolean erroAtualizacao = false;
		
		PedidoVoucher pedido = null;
		try
		{
			pedido = PedidoVoucherHome.findByNumeroPedido(db, Long.parseLong(request.getParameter("numeroPedido")));
		}
		catch (Exception e)
		{
			erroAtualizacao = true;
			logger.error("N�o foi poss�vel realizar a altera��o do status de voucher, problemas na conex�o com o banco de dados (" +
					e.getMessage() + ")");
		}
		
		// caso o par�metro numeroOrdem esteja null, quer dizer que n�o foi setado. Neste caso,
		// n�o se deve alterar o n�mero da ordem
		if (request.getParameter("numeroOrdem") != null)
			pedido.setNroOrdem(new Long((request.getParameter("numeroOrdem"))));
		
		pedido.setStatusVoucher(Integer.valueOf(request.getParameter("statusNovo")));
		
		try
		{
			PedidoVoucherHome.setByRequest(pedido, request, db);
		}
		catch (Exception e)
		{
			erroAtualizacao = true;
			logger.error("N�o foi poss�vel realizar a altera��o do status de voucher, problemas na conex�o com o banco de dados (" +
					e.getMessage() + ")");
		}

		HistoricoPedidosVoucher histPedidosVoucher = new HistoricoPedidosVoucher();

		// Seta os atributos do hist�rico de pedidos
		histPedidosVoucher.setDataAtualizacaoStatus( new Date() );
		
		// Obt�m o usu�rio logado no portal
		Usuario usuario = (Usuario)request.getSession().getAttribute(Constantes.USUARIO);
		
		histPedidosVoucher.setNomeUsuario(usuario.getMatricula());
		histPedidosVoucher.setNroPedido( pedido.getNroPedido() );
		histPedidosVoucher.setStatusPedido( pedido.getStatusPedido() );
		histPedidosVoucher.setStatusVoucher( pedido.getStatusVoucher() );
		
		try
		{
			PedidoVoucherHome.setHistorico(histPedidosVoucher, request, db);
		}
		catch (Exception e)
		{
			erroAtualizacao = true;
			logger.error("N�o foi poss�vel realizar a altera��o do status de voucher, problemas na conex�o com o banco de dados (" +
					e.getMessage() + ")");
		}
		
		if (erroAtualizacao)
			request.setAttribute(Constantes.MENSAGEM, "Erro ao alterar o status do voucher n�" 
					+ pedido.getNroPedido());			
		else
		{
			request.setAttribute(Constantes.MENSAGEM, "Status do voucher n� " 
					+ pedido.getNroPedido() + " alterado com sucesso");
			logger.info("Solicitacao de altera��o de status do voucher realizada");
		}
		
		codOperacao = Constantes.COD_ALTERAR_STATUS_VOUCHER;
		
		return actionMapping.findForward("success");
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
