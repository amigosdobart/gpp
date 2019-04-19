package br.com.brasiltelecom.ppp.action.relatorioContabilExtrato;


import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import org.apache.log4j.Logger;


/**
 * Mostra a tela de consulta de relatorio Contabilidade Consumos
 * 
 * @author Alberto Magno
 * @since 27/05/2004
 */
public class ShowFiltroConsultaRelContabilExtratoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CONTABIL_EXTRATO;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelContabilExtrato.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
			try {
				db.begin();
			} catch (PersistenceException pe) {
				logger.error("N�o foi poss�vel realizar a consulta de relat�rio Contabil/Extrato, problemas na conex�o com o banco de dados (" +
							pe.getMessage() + ")");
				throw pe;
			}
			
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
