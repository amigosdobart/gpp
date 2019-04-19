package br.com.brasiltelecom.ppp.action.logOperacao;

import java.util.Collection;
import java.util.Iterator;
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
import br.com.brasiltelecom.ppp.home.LogOperacaoHome;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * 
 * Classe para realizar a consulta de Log das Operações.
 * 
 * @author Luciano Vilela
 * @since 02/02/2003
 * 
 */
public class ConsultaLogOperacaoAction extends ActionPortal {

	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Método principal da classe, reponsável pela recuperação
	 * dos Logs das operações realizadas no Sistema.
	 * 
	 * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db parâmetro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
		ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,
		Database db)throws Exception {
		
		int contador = 1;
		ActionForward result = null;
		String filtro_sql = null;
		Map mapRequest = Util.parameterToMap(request);
		
		db.begin();
		
		logger.info("Consulta por registros de operações solicitada");
		
		Collection resultLog;
		
		try
		{
			resultLog = LogOperacaoHome.findByFilter( db, mapRequest );
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta por registros de operações, problemas na conexão com o banco de dados (" +
						pe.getMessage() + ")");
			throw pe;
		}

		for (Iterator itLog = resultLog.iterator(); itLog.hasNext();)	{	

			if (contador == resultLog.size()) {

				filtro_sql = (String) itLog.next();
				itLog.remove();
				
			} else {
 
 				itLog.next();	
			}
			contador++;
		}

		request.setAttribute("report",resultLog) ;   
		request.setAttribute("page",mapRequest.get("page")) ;
		request.setAttribute("filtro_sql", filtro_sql) ;

		result = actionMapping.findForward("success");
		
		return result;
	}

	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
