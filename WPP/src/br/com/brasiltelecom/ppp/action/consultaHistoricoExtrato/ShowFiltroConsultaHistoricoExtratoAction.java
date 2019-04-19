/*
 * Created on 23/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaHistoricoExtrato;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.AdministrativoTipoExtratoHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de histórico de comprovante de serviços
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowFiltroConsultaHistoricoExtratoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_HIS_EXTRATO;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaHistoricoExtrato.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
		
		db.begin();
		
		Collection tipos = AdministrativoTipoExtratoHome.findAll(db);
		
		context.put("tipoExtratos",tipos);

	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}
}
