/*
 * Created on 28/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.administrativoConfiguracaoContestacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConfiguracaoHome;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;

/**
 * Mostra as informações de configuração de contestação
 * 
 * @author Alberto Magno
 * @since 20/05/2004
 */
public class AdministrativoConfiguracaoContestacaoAction extends ActionPortal {
	
	
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
				
				Configuracao resultSet = ConfiguracaoHome.findByID(db,"NUM_MAX_BS");
				request.setAttribute("maxBS", resultSet);			
				
				resultSet = ConfiguracaoHome.findByID(db,"VALOR_MAX_CONTESTACAO");
				request.setAttribute("valorMax", resultSet);
							
				resultSet =  ConfiguracaoHome.findByID(db,"PERIODO_CONSULTA_BS");
				request.setAttribute("periodo", resultSet);	
						
				result = actionMapping.findForward("success");		    
		    
				return result;
			}
		
			/**
			 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
			 */
			public String getOperacao() {
				return null;
			}
}
