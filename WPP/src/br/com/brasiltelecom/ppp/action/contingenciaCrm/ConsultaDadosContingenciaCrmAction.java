/*
 * Created on 03/09/2004
 *
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ContingenciaCrmHome;
import br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm;

/**
 *	Esta classe realiza a pesquisa na base de dados da atividade de bloqueio/desbloqueio
 *  executada durante o processo de contingencia do CRM
 */
public class ConsultaDadosContingenciaCrmAction extends ActionPortal {

	Logger logger = Logger.getLogger(this.getClass());
	
	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response,
		Database db)
		throws Exception
	{
		ActionForward result = null;
		db.begin();
	
		logger.info("Consulta aos dados do bloqueio/desbloqueio do assinante pela contingencia CRM...");
		ContingenciaCrm resultSet = null;
		try
		{
			long idAtividade = Long.parseLong((String)request.getParameter("item"));
			resultSet = ContingenciaCrmHome.findByAtividade(db,idAtividade);			
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta dos dados de bloqueio/desbloqueio do assinante, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		request.setAttribute(Constantes.RESULT, resultSet);		
		result = actionMapping.findForward("success");
		return result;
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return null;
	}
}