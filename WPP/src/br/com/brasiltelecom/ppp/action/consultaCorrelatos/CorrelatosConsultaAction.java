package br.com.brasiltelecom.ppp.action.consultaCorrelatos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConsultaNumerosCorrelatosHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Consulta números correlatos
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class CorrelatosConsultaAction extends ActionPortal {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
		ActionMapping actionMapping,	
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response,
		Database db) throws Exception 
	{
		ActionForward result = null;
		db.begin();
				
		Map map = new HashMap();
		
		String msisdnOrigem = "";  
		String msisdnDestino = "";
		
		if(request.getParameter("msisdnOrigem") != null && !request.getParameter("msisdnOrigem").equals("") ){
		   msisdnOrigem = (String) request.getParameter("msisdnOrigem");
		   map.put("msisdnOrigem", msisdnOrigem);				   		
		}
		
		if(request.getParameter("msisdnDestino") != null && !request.getParameter("msisdnDestino").equals("") ){
		   msisdnDestino = 	(String) request.getParameter("msisdnDestino");
		   
		   if (msisdnDestino.length() == 13){
			  msisdnDestino = msisdnDestino.substring(1,3) + msisdnDestino.substring(4,8) + msisdnDestino.substring(9,13);
		   } else
		   if (msisdnDestino.length() == 12){
			  msisdnDestino = msisdnDestino.substring(1,3) + msisdnDestino.substring(4,7) + msisdnDestino.substring(8,12);
		   }
		   
		   String auxMsisdnDestino = "0" + msisdnDestino;
		   map.put("msisdnDestino", auxMsisdnDestino);
		   Collection resultSet;
		   try
		   {
		   	   resultSet = ConsultaNumerosCorrelatosHome.findByFilter(db, map);
		   }
		   catch (PersistenceException pe)
		   {
		   	   logger.error("Não foi possível consultar o número correlato, problemas na conexão com o banco de dados (" +
					pe.getMessage() + ")");
			   throw pe;
		   }		   
		   request.setAttribute(Constantes.RESULT, resultSet);
		} 
		
		result = actionMapping.findForward("success");
		    
		return result;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return null;
	}
}