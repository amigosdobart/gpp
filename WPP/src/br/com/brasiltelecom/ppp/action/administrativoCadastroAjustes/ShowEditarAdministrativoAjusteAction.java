/*
 * Created on 22/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.administrativoCadastroAjustes;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.AdministrativoAjusteHome;
import br.com.brasiltelecom.ppp.portal.entity.Origem;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * Mostra a tela de edição de motivo de ajuste
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class ShowEditarAdministrativoAjusteAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CADASTRO_AJU;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "editAdministrativoAjuste.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
		
		String acao = request.getParameter("acao");
		db.begin();
		
		if (acao.equals("novo")){
			context.put(Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));
		} else
		if (acao.equals("editar")){
			Origem origem = AdministrativoAjusteHome.findByID(db,Integer.parseInt(request.getParameter("idOrigem")),Integer.parseInt(request.getParameter("idCanal")));
			context.put("origem",origem);
		}

	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
