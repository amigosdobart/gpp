/*
 * Created on 19/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaHistoricoExtrato;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConsultaHistoricoExtratoHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Consulta o histórico de comprovante de serviços
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ConsultaHistoricoExtratoAction extends ActionPortal {
	
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
		throws Exception {
	
		ActionForward result = null;
		db.begin();
		
		logger.info("Consulta a um histórico de comprovante de serviços solicitada");
		
		Map map = new HashMap();
		
		String msisdn = request.getParameter("obr_msisdn");
		if (msisdn != null  && msisdn.length() == 13)
			msisdn = msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
		map.put("msisdn", "55" + msisdn);
		
		
		map.put("idTipoExtrato",request.getParameter("idTipoExtrato"));
		// Filtro do Periodo
		String tipoPeriodo=request.getParameter("tipoPeriodo");
		if (tipoPeriodo != null){
			if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo"))){
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(request.getParameter("periodo")));
				Date dataInicial = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				map.put("dataInicial",sdf.format(dataInicial));
				map.put("dataFinal",sdf.format(new Date()));
			} else if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("dataInicial")) && !"".equals(request.getParameter("dataFinal"))){
				map.put("dataInicial",request.getParameter("dataInicial"));
				map.put("dataFinal",request.getParameter("dataFinal"));
			}
		}
		
		if (request.getParameter("filtro_sql") != null){
			map.put("filtro_sql",request.getParameter("filtro_sql"));
		} else {
			request.setAttribute(Constantes.MENSAGEM, "Consulta ao histórico de comprovantes do número de acesso '" +
							request.getParameter("obr_msisdn") + "' realizada com sucesso!");
			this.codOperacao = Constantes.COD_CONSULTA_HISTORICO;

		}

		Collection resultSet;

		try
		{
			resultSet = ConsultaHistoricoExtratoHome.findByFilter(db,map);			
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta de histórico de comprovantes de serviço, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		
		
		int contador = 1;
		String filtro_sql = "";
		for (Iterator it = resultSet.iterator(); it.hasNext();)	{	

					if (contador == resultSet.size()) {

						filtro_sql = (String) it.next();
						it.remove();
				
					} else {
 
						it.next();	
					}
					contador++;
				}
		
		request.setAttribute(Constantes.RESULT, resultSet);
		request.setAttribute("filtro_sql",filtro_sql);
		request.setAttribute("obr_msisdn",request.getParameter("obr_msisdn"));
		request.setAttribute("page",request.getParameter("page")) ;
							
		result = actionMapping.findForward("success");		    
	
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}