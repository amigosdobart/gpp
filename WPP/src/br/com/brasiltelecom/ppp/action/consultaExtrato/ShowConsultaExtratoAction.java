/*
 * Created on 08/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaExtrato;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.model.RetornoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela com o extrato gerado
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowConsultaExtratoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_COMP_SERVICOS;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaExtrato.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
			//HttpSession session = request.getSession();
			//Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
			//SegurancaEmpresa.setPermissao(login, context);


			if (request.getAttribute(Constantes.RESULT) != null){

				RetornoExtrato result = (RetornoExtrato) request.getAttribute(Constantes.RESULT);
	
				context.put("extratos", result.getExtratos());
				context.put("eventos", result.getEventos());
				context.put("saldoInicialPrincipal", result.getSaldoInicialPrincipal());
				context.put("saldoFinalPrincipal", result.getSaldoFinalPrincipal());
				context.put("totalCreditosPrincipal", result.getTotalCreditosPrincipal());
				context.put("totalDebitosPrincipal", result.getTotalDebitosPrincipal());
				
				//Se o assinante for LigMix, colocar no context do velocity
				if (Integer.parseInt(result.getIndAssinanteLigMix())==1)
					context.put("assinanteLigMix",result.getIndAssinanteLigMix());
				
				context.put("saldoInicialBonus", result.getSaldoInicialBonus());
				context.put("saldoFinalBonus", result.getSaldoFinalBonus());
				context.put("totalCreditosBonus", result.getTotalCreditosBonus());
				context.put("totalDebitosBonus", result.getTotalDebitosBonus());
				
				context.put("saldoInicialSMS", result.getSaldoInicialSMS());
				context.put("saldoFinalSMS", result.getSaldoFinalSMS());
				context.put("totalCreditosSMS", result.getTotalCreditosSMS());
				context.put("totalDebitosSMS", result.getTotalDebitosSMS());
				
				context.put("saldoInicialGPRS", result.getSaldoInicialGPRS());
				context.put("saldoFinalGPRS", result.getSaldoFinalGPRS());
				context.put("totalCreditosGPRS", result.getTotalCreditosGPRS());
				context.put("totalDebitosGPRS", result.getTotalDebitosGPRS());
				
				context.put("saldoInicialTotal", result.getSaldoInicialTotal());
				context.put("saldoFinalTotal", result.getSaldoFinalTotal());
				context.put("totalCreditosTotal", result.getTotalCreditosTotal());
				context.put("totalDebitosTotal", result.getTotalDebitosTotal());
				
				context.put("MS",request.getParameter("MS"));
				context.put("DI",request.getParameter("DI"));
				context.put("DF",request.getParameter("DF"));
				context.put("CS",request.getParameter("CS"));
				context.put("NM",request.getParameter("NM"));
				context.put("EM",request.getParameter("EM"));
				context.put("LG",request.getParameter("LG"));
				context.put("CP",request.getParameter("CP"));
				context.put("BR",request.getParameter("BR"));
				context.put("CD",request.getParameter("CD"));
				context.put("UF",request.getParameter("UF"));
				context.put("CE",request.getParameter("CE"));

				context.put("tamanho",String.valueOf(result.getExtratos().size()+result.getEventos().size()+1));
			}
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
