package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.home.MotivoBloqueioCrmHome;

/**
 * Consulta os assinantes bloqueados pelo processo de contingencia do CRM
 * 
 * @author Joao Carlos
 * @since 02/09/2004
 */
public class FiltroConsultaAssinantesBloqueadosAction extends ActionPortal
{
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
		ActionMapping actionMapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response,
		Database db)
		throws Exception
	{
		db.begin();
		logger.info("Consulta de tipos de bloqueio solicitada");
		Collection resultset;
		try
		{
			resultset = MotivoBloqueioCrmHome.findAll(db);
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta de motivos de bloqueio/desbloqueio, problemas na conexão com o banco de dados (" +
								pe.getMessage() + ")");
			throw pe;
		}
		request.setAttribute(Constantes.RESULT,resultset);
		return actionMapping.findForward("success");
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return this.codOperacao;
	}
}