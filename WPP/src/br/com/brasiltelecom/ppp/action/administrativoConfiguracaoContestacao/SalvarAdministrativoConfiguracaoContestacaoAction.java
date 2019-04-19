/*
 * Created on 29/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.administrativoConfiguracaoContestacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConfiguracaoHome;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Salva as informações de configuração de constestação 
 * 
 * @author Alberto Magno
 * @since 20/05/2004
 */
public class SalvarAdministrativoConfiguracaoContestacaoAction extends ActionPortal {

	/********Atributos da Classe*******/
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	/**********************************/

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
		//HttpSession session = request.getSession();
	
		db.begin();
		
		logger.info("Alteração de configurações de contestação solicitada");

		try
		{
			Configuracao config = ConfiguracaoHome.findByID(db, "NUM_MAX_BS");
			config.setVlrConfiguracao(request.getParameter("obr_maxBS"));

			config = ConfiguracaoHome.findByID(db, "VALOR_MAX_CONTESTACAO");
			config.setVlrConfiguracao(request.getParameter("obr_valorMax"));

	    	config = ConfiguracaoHome.findByID(db, "PERIODO_CONSULTA_BS");
			config.setVlrConfiguracao(request.getParameter("obr_periodo"));
			
			this.codOperacao = Constantes.COD_ALTERAR_CONFIG_CONTESTACAO;
			request.setAttribute(Constantes.MENSAGEM, "Configurações de contestação alteradas com sucesso!");	
		}
		catch(PersistenceException pe)
		{
			request.setAttribute(Constantes.MENSAGEM, "Erro ao salvar as configurações de contestação!");
			logger.error("Não foi possível salvar as configurações de contestação, problemas na conexão com o banco de dados (" +
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
