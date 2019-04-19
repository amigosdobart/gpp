package br.com.brasiltelecom.ppp.action.grupo;

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
import br.com.brasiltelecom.ppp.home.GrupoHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * 
 * Classe para realizar a consulta de grupos.
 * 
 * @author Luciano Vilela
 * @since 22/03/2003
 * 
 */
public class ConsultaGrupoAction extends ActionPortal {
	
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * M�todo principal da classe, repons�vel pela recupera��o dos Grupos do Sistema.
	 * 
	 * @param actionMapping par�metro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm par�metro do tipo org.apache.struts.action.ActionForm.
	 * @param request  par�metro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response par�metro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db par�metro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
		ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,
		Database db) throws Exception{
		
		ActionForward result = null;
		db.begin();
		logger.info("Consulta por grupo de perfil solicitada");

		Collection resultSet;
		
		try
		{
			resultSet = GrupoHome.findByFilterGrupoVO(db,Util.parameterToMap(request));
		}
		catch (PersistenceException pe)
		{
			logger.error("N�o foi poss�vel realizar a consulta por grupo de perfil, problemas na conex�o com o banco de dados (" +
					pe.getMessage() + ")");
			throw pe;
		}
		
		request.setAttribute(Constantes.RESULT, resultSet);
		result = actionMapping.findForward("success");
		
		String perfilConsultado = Util.parameterToMap(request).toString().substring(1);
		perfilConsultado = perfilConsultado.substring( perfilConsultado.indexOf("=")+1, perfilConsultado.indexOf(","));		
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta pelo grupo '" + perfilConsultado + 
							"' realizada com sucesso!");
		this.codOperacao = Constantes.COD_CONSULTAR_GRUPO;
		
		return result;
	}

	/**
	 * M�todo para pegar a Opera��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Opera��o realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

}
