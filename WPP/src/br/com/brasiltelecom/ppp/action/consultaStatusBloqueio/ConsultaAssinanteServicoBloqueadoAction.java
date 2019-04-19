/*
 * Created on 13/09/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaStatusBloqueio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConsultaAssinanteServicoBloqueadoHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Henrique Canto
 *
 */
public class ConsultaAssinanteServicoBloqueadoAction extends ActionPortal {
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		ActionForward result    = null;
		//String dataInicial 		= request.getParameter("datInicial");
		//Date datFinal 			= new Date();
		String msisdn 			= request.getParameter("msisdn");
		String tipoPeriodo		= request.getParameter("tipoPeriodo");
		//String periodo 			= request.getParameter("periodo");
		boolean consultaTodos 	= false;
		Map map 				= new HashMap();
		//this.codOperacao = Constantes.COD_CONSULTAR_STATUS_BLOQUEIO;
		
		
		
		if (msisdn != null  && msisdn.length() == 13)
		{
			msisdn = msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
			map.put("msisdn", "55" + msisdn);
		}
		
	 if (tipoPeriodo!=null)
		{
	 		if (msisdn.equals("") || msisdn==null)
	 		{
	 			consultaTodos 	= true;
	 		}
			if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo")))
			{
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(request.getParameter("periodo")));
					Date datInicial = c.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					map.put("datInicial",sdf.format(datInicial));
					map.put("datFinal",sdf.format(new Date()));
			}
			else 
				if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("datInicial")) && !"".equals(request.getParameter("datFinal")))
				{
					
					map.put("datInicial",request.getParameter("datInicial"));
					map.put("datFinal",request.getParameter("datFinal"));
				}
		}
	 	Collection resultSet;
		try 
		{
			if (!consultaTodos)
			{
				resultSet = ConsultaAssinanteServicoBloqueadoHome.findByFilter(db,map);
				request.setAttribute(Constantes.RESULT,resultSet);
			}else
			{
				resultSet = ConsultaAssinanteServicoBloqueadoHome.findAll(db);
				request.setAttribute(Constantes.RESULT,resultSet);
			}	
		}		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta de histórico de comprovantes de serviço, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		
		request.setAttribute("page",request.getParameter("page")) ;
		request.setAttribute("obr_msisdn",msisdn);

		result =  actionMapping.findForward("success");		    
		return result;
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
