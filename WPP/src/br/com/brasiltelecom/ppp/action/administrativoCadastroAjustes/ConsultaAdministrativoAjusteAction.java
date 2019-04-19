/*
 * Created on 10/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.administrativoCadastroAjustes;

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
import br.com.brasiltelecom.ppp.home.AdministrativoAjusteHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Consulta os motivos de ajuste.
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class ConsultaAdministrativoAjusteAction extends ActionPortal {

	private String codOperacao;
	/*
	 * Inicialize o log4j
	 */
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
		/*
		 * Inicializa o banco de dados no castor
		 */
		db.begin();

		logger.info("Consulta de motivo de ajuste solicitada");
		Map map = new HashMap();
		map.put("descOrigem",request.getParameter("descOrigem"));
		Collection resultSet;
		
		/*
		 * Busca os dados no banco 
		 */
		try
		{
			resultSet = AdministrativoAjusteHome.findByFilter(db,map);
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta de motivo de ajuste, problemas na conexão com o banco de dados (" +
						pe.getMessage() + ")");
			throw pe;
		}
		
		request.setAttribute(Constantes.RESULT, resultSet);
		result = actionMapping.findForward("success");
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta pelo motivo de ajuste '" +
							request.getParameter("descOrigem") + "' realizado com sucesso!");
		this.codOperacao = Constantes.COD_CONSULTAR_MOTIVO_AJUSTE;
		
		return result;
	}


	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

}