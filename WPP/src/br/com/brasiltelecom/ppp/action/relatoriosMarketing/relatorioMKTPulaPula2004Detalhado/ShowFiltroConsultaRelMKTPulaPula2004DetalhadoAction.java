package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTPulaPula2004Detalhado;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de relatorio detalhado do Bonus PulaPula 2004
 * 
 * @author Magno Batista Corrêa
 * @since 2006/06/02
 */
public class ShowFiltroConsultaRelMKTPulaPula2004DetalhadoAction extends ShowAction {
	/** <Magno>Abrir uma entrada em Constantes.java</Magno>*/
	private String codOperacao = Constantes.MENU_MKT_PULA_PULA_2004_DETALHADO;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelMKTPulaPula2004Detalhado.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
