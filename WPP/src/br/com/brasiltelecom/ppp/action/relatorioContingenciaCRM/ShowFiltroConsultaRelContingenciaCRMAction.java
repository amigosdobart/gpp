package br.com.brasiltelecom.ppp.action.relatorioContingenciaCRM;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.home.MotivoBloqueioCrmHome;
import br.com.brasiltelecom.ppp.home.CategoriaHome;


/**
 * Consulta os assinantes bloqueados pelo processo de contingencia do CRM
 * 
 * @author Joao Carlos
 * @since 02/09/2004
 */
public class ShowFiltroConsultaRelContingenciaCRMAction extends ShowAction
{
	private String codOperacao = Constantes.MENU_CONT_RELATORIO_CRM;
	Logger logger = Logger.getLogger(this.getClass());

	public String getTela() {
		return "filtroConsultaRelContingenciaCRM.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
			VelocityContext context,
			HttpServletRequest request,
			Database db)
			throws Exception {
		
		db.begin();
		logger.info("Consulta de tipos de bloqueio solicitada para relatorio contigencia CRM");
		Collection resultset;
		try
		{
			resultset = MotivoBloqueioCrmHome.findAll(db);
			context.put("motivosBloqueio",resultset);
			resultset = CategoriaHome.findAll(db);
			context.put("produtos",resultset);
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta de motivos de bloqueio/desbloqueio, problemas na conexão com o banco de dados (" +
								pe.getMessage() + ")");
			throw pe;
		}
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return this.codOperacao;
	}
}