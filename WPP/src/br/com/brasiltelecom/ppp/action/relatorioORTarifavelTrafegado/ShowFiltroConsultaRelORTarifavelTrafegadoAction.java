package br.com.brasiltelecom.ppp.action.relatorioORTarifavelTrafegado;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.RatingHome;


/**
 * Mostra a tela de consulta de relatorio Contabilidade Consumos
 * 
 * @author Alberto Magno
 * @since 27/05/2004
 */
public class ShowFiltroConsultaRelORTarifavelTrafegadoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_REL_OR_MINUTOS;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelORTarifavelTrafegado.vm";
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
				Collection c = CodigoNacionalHome.findAllBrt(db);
				context.put("cns",c);
				HashMap map = new HashMap();
				map.put("tarifavel","S");
				c= RatingHome.findbyMAP(db,map);
				context.put("tarifaveis",c);
				map.put("tarifavel","N");
				c= RatingHome.findbyMAP(db,map);
				context.put("ntarifaveis",c);
				
			} catch (PersistenceException pe) {
				logger.error("Não foi possível realizar a consulta de relatório Contabil/Consumo, problemas na conexão com o banco de dados (" +
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
