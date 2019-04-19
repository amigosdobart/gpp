/*
 * Created on 22/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.administrativoTipoExtrato;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.AdministrativoTipoExtratoHome;
import br.com.brasiltelecom.ppp.portal.entity.TipoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de edição de comprovantes de serviços
 * 
 * @author André Gonçalves
 * @since 20/05/2004
 */
public class ShowEditarAdministrativoTipoExtratoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_TIPO_EXTRATO;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "editAdministrativoTipoExtrato.vm";
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
			TipoExtrato te = AdministrativoTipoExtratoHome.findByID(db,Integer.parseInt(request.getParameter("idTipoExtrato")));
			context.put("tipoExtrato",te);
		}

	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
