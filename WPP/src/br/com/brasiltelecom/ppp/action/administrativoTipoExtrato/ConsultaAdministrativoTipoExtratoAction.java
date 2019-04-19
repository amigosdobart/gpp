/*
 * Created on 22/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.administrativoTipoExtrato;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.AdministrativoTipoExtratoHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/** 
 * Consulta os comprovantes de serviço cadastrados no sistema
 * 
 * @author André Gonçalves
 * @since 20/05/2004
 */
public class ConsultaAdministrativoTipoExtratoAction extends ActionPortal {

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
		
		db.begin();
		
		logger.info("Consulta de tipos de extrato solicitada");
		
		Map map = new HashMap();
		map.put("descTipoExtrato",request.getParameter("descTipoExtrato"));
		Collection resultset;
		
		try
		{
			resultset = AdministrativoTipoExtratoHome.findByFilter(db,map);
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta de tipos de extrato, problemas na conexão com o banco de dados (" +
								pe.getMessage() + ")");
			throw pe;
		}
		
		request.setAttribute(Constantes.RESULT,resultset);
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta pelo tipo de extrato '" + 
							request.getParameter("descTipoExtrato") + "' realizada com sucesso!");
		this.codOperacao = Constantes.COD_CONSULTAR_TIPO_EXTRATO;
		return actionMapping.findForward("success");
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

}
