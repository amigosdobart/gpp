/*
 * Created on 23/08/2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConfiguracaoHome;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Henrique Canto
 *
 */
public class SalvarContingenciaCrmAction extends ActionPortal {

	/********Atributos da Classe*******/
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	/**********************************/
	
	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		ActionForward result = null;
		//HttpSession session = request.getSession();
	
		db.begin();
		
		logger.info("Alteração do módulo de operação solicitado");

		try
		{
			Configuracao config = ConfiguracaoHome.findByID(db, "CONTINGENCIA_CRM");
			config.setVlrConfiguracao(request.getParameter("modOperacao"));
			
			this.codOperacao = Constantes.COD_ALTERAR_MOD_OP;
			request.setAttribute(Constantes.MENSAGEM, "Módulo de Operação alterado com sucesso.");	
		}
		catch(PersistenceException pe)
		{
			request.setAttribute(Constantes.MENSAGEM, "Erro ao tentar alterar o módulo de operação!");
			logger.error("Não foi possível alterar o módulo de operação, problemas na conexão com o banco de dados (" +
					pe.getMessage() + ")");
		}
				
		result = actionMapping.findForward("success");
		
		
		return result;
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

}
