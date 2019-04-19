package br.com.brasiltelecom.ppp.action.consultaHistoricoBroadcast;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConsultaHistoricoBroadcastHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Consulta o histórico de broadcasts de sms
 * 
 * @author Marcelo Alves Araujo
 * @since 02/08/2005
 */
public class ConsultaHistoricoBroadcastAction extends ActionPortal {
	
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db)throws Exception 
	{
		db.begin();
		
		logger.info("Consulta ao histórico de broadcast de sms");
		
		String msisdn 		= request.getParameter("msisdn");
		if (msisdn != null  && msisdn.length() == 13)
			msisdn = "55" + msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
		
		String dataInicio 	= request.getParameter("dataInicial");
		String dataFim		= request.getParameter("dataFinal");
		String sucesso		= request.getParameter("incluiOk");
		String erro			= request.getParameter("incluiNaoOk");
		Collection resultSet;
		
		try
		{
			resultSet = ConsultaHistoricoBroadcastHome.findByParam(db,msisdn,dataInicio,dataFim,sucesso,erro);			
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta de histórico de broadcast de sms : " + e.getMessage());
			throw e;
		}
		
		request.setAttribute(Constantes.RESULT, resultSet);
							
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