/*
 * Created on 28/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.administrativoConfiguracaoContestacao;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra o resultado da consulta de configuração de contestação
 * 
 * @author Alberto Magno
 * @since 20/05/2004
 */
public class ShowAdministrativoConfiguracaoContestacaoAction extends ShowAction {
	
	private String codOperacao = Constantes.MENU_CONFIG_CONTESTACAO;
		/**
		 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
		 */
		public String getTela() {
			return "administrativoConfiguracaoContestacao.vm";
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

				Configuracao result = (Configuracao) request.getAttribute("maxBS");
				context.put("maxBS",result.getVlrConfiguracao());
				result = (Configuracao) request.getAttribute("valorMax");
				context.put("valorMax",result.getVlrConfiguracao());
				result = (Configuracao) request.getAttribute("periodo");
				context.put("periodo",result.getVlrConfiguracao());
		
		}

		/**
		 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
		 */
		public String getOperacao() {
			return codOperacao;
		}
}