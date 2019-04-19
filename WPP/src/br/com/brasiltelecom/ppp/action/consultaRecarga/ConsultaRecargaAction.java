/*
 * Created on 25/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaRecarga;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.RecargaHome;
import br.com.brasiltelecom.ppp.home.RecargaTFPPHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * Consulta a recarga/ajustes 
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ConsultaRecargaAction extends ActionPortal {
	
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
				//int contador = 1;
				//String filtro_sql = null;
				//String consultaRecarga = null;
					
				db.begin();
				
				logger.info("Consulta de recarga/ajuste solicitada");
				
				Collection resultSet = null;
				try
				{						
					if(request.getParameter("tipoRecarga").equals("GSM"))
					{
						resultSet = RecargaHome.findByFilter(db, Util.parameterToMap(request));
						this.codOperacao = Constantes.COD_CONSULTA_RECARGAS;
					}
					else if(request.getParameter("tipoRecarga").equals("TFPP"))
					{
						resultSet = RecargaTFPPHome.findByFilter(db, Util.parameterToMap(request));
						this.codOperacao = Constantes.COD_CONSULTA_RECARGAS_TFPP;
					}
					request.setAttribute(Constantes.RESULT, resultSet);
					request.setAttribute(Constantes.MENSAGEM, "Consulta de recargas/ajustes para o número de acesso '" +
									request.getParameter("msisdn") + "' efetuada com sucesso!");
					request.setAttribute("canalRecarga", request.getParameter("canalRecarga"));
				}
				catch (PersistenceException pe)
				{
					logger.error("Erro ao consultar de recargas, problemas na conexão com o banco de dados (" +
							pe.getMessage() + ")");
				}
				
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