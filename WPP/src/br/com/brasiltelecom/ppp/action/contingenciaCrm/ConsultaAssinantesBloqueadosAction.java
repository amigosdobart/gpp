package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.home.ContingenciaCrmHome;

/**
 * Consulta os assinantes bloqueados pelo processo de contingencia do CRM
 * 
 * @author Joao Carlos
 * @since 02/09/2004
 */
public class ConsultaAssinantesBloqueadosAction extends ActionPortal
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
		ActionForward result = null;
		db.begin();
	
		logger.info("Consulta aos assinantes bloqueados pela contingencia CRM solicitada");
		Collection resultSet = new java.util.LinkedList();
		try
		{
			String dataInicial   		= ((String)request.getParameter("dataInicial"))+ " 00:00:00";
			String dataFinal     		= ((String)request.getParameter("dataFinal"))+ " 23:59:59";
			String motBloqueio 		=                   request.getParameter("motivoBloqueio");
			String atendente		= 					request.getParameter("atendente");
			String msisdn		= 					request.getParameter("msisdn");

			resultSet = ContingenciaCrmHome.findByMotivoEData(db,dataInicial,dataFinal,motBloqueio,atendente,msisdn);			
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta dos assinantes bloqueados, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		request.setAttribute(Constantes.RESULT, resultSet);
		request.setAttribute("page",request.getParameter("page")) ;
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta aos assinantes bloqueados realizada com sucesso!");
		//codOperacao = Constantes.COD_CONSULTA_ASSINANTES_BLOQUEADOS;
							
		result = actionMapping.findForward("success");
	
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return this.codOperacao;
	}
}