package br.com.brasiltelecom.ppp.action.cadastroCampanha;

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

import br.com.brasiltelecom.ppp.home.CampanhaHome;

/**
 * @author Joao Carlos
 */
public class ConsultaAssinanteCampanhaAction extends ActionPortal
{
	private Logger logger = Logger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception
	{
		String msisdn="55"+request.getParameter("msisdn");
		logger.debug("Consulta informacoes de campanha para o assinante:"+msisdn);
		try
		{
			db.begin();
			Collection listaCampanhas = CampanhaHome.findByAssinante(msisdn,db);
			// Insere a lista de campanhas do assinante no objeto request para
			// posteriormente ser visualizado pelo usuario na tela
			request.setAttribute("assinanteCampanhas",listaCampanhas);
			db.commit();
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return actionMapping.findForward("success");
	}
	
	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_ASSIN_CAMPANHA;
	}
}
