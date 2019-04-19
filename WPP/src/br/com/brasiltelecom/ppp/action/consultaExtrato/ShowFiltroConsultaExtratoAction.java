/*
 * Created on 15/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaExtrato;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a confirmação de geração do extrato na tela
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowFiltroConsultaExtratoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_COMP_SERVICOS;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroExtrato.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
		
		context.put("mensagem",request.getAttribute(Constantes.MENSAGEM));
		context.put("erro", request.getAttribute("erro"));
		
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

	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
