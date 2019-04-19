package br.com.brasiltelecom.ppp.action.usuario;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * Classe que recupera os Usuários de acordo com os parâmetros passados via filtro na WEB.
 * 
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 */
public class ConsultaUsuarioAction extends ActionPortal {
	
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Método principal da classe, reponsável pela recuperação dos Usuários.
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

		db.begin();
		logger.info("Consulta por usuário solicitada");
		
		Collection resultSet;
		
		try
		{
			resultSet = UsuarioHome.findByFilterUsuarioVO(db,Util.parameterToMap(request));
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta por usuário, problemas na conexão com o banco de dados (" +
					pe.getMessage() + ")");
			throw pe;
		}
		
		
		//Pega o filtro
		for (Iterator itUsuario = resultSet.iterator();itUsuario.hasNext();) {	

			if (contador == resultSet.size()) {

				filtro_sql = (String) itUsuario.next();
				itUsuario.remove();

			} else {

				itUsuario.next();	
			}

			contador++;
		}
		
		request.setAttribute(Constantes.RESULT, resultSet);
		request.setAttribute("page",request.getParameter("page"));
		request.setAttribute("filtro_sql",filtro_sql);
		result = actionMapping.findForward("success");
		
		//String usuarioConsultado = filtro_sql.substring(filtro_sql.indexOf("'")+1, filtro_sql.length()-1).toLowerCase();
		String usuarioConsultado = Util.parameterToMap(request).get("matricula").toString().trim();
		request.setAttribute(Constantes.MENSAGEM, "Consulta pelo usuário '" + usuarioConsultado + 
									"' realizada com sucesso!");
		this.codOperacao = Constantes.COD_CONSULTAR_USUARIO;
		
		return result;
	}

	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

}
