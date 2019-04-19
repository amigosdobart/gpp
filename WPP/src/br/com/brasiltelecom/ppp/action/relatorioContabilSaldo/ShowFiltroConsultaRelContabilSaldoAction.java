package br.com.brasiltelecom.ppp.action.relatorioContabilSaldo;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.home.GrupoServicoSFAHome;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;


/**
 * Mostra a tela de consulta de relatorio Contabilidade Saldos
 * 
 * @author Alberto Magno
 * @since 27/05/2004
 */
public class ShowFiltroConsultaRelContabilSaldoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CONTABIL_SALDO;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelContabilSaldo.vm";
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
				Collection c = GrupoServicoSFAHome.findById(db,Constantes.CLASSE_SFA_SALDOS);
				context.put("grupos",c);
				c = CodigoNacionalHome.findAllBrt(db);
				context.put("cns",c);
				
			} catch (PersistenceException pe) {
				logger.error("N�o foi poss�vel realizar a consulta de relat�rio de BS/atendente, problemas na conex�o com o banco de dados (" +
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
