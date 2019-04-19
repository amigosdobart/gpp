package br.com.brasiltelecom.ppp.action.relatorioEvento;


import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.TipoEventoHome;

import org.apache.log4j.Logger;


/**
 * Mostra a tela de consulta de relatorio Contabilidade Consumos
 * 
 * @author Luciano Vilela
 * @since 29/09/2004
 */
public class ShowFiltroConsultaRelEventoAction extends ShowAction {

	private String codOperacao = "MENU_REL_EVENTO";
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelEvento.vm";
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
				Collection tipos = TipoEventoHome.findAll(db);
				context.put("tipos", tipos);
			} catch (PersistenceException pe) {
				logger.error("Não foi possível realizar a consulta de relatório Contabil/Extrato, problemas na conexão com o banco de dados (" +
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
