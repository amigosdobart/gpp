/*
 * Created on 10/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.administrativoCadastroAjustes;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra o resultado da consulta de motivo de ajuste
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class ShowConsultaAdministrativoAjusteAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CADASTRO_AJU;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaAdministrativoAjuste.vm";
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

			context.put("alterarMotivoAjuste","S");
			context.put("excluirMotivoAjuste","S");
			
			if (request.getAttribute(Constantes.RESULT) != null){
			
				Collection result = (Collection) request.getAttribute(Constantes.RESULT);
				
				context.put("origens", result);
	
				context.put("tamanho",String.valueOf(result.size()));
			}

	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
