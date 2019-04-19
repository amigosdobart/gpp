/*
 * Created on 20/08/2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

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
 * @author Henrique Canto
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FiltroContingenciaCrmAction extends ActionPortal {
	//private String codOperacao = Constantes.COD_CONSULTAR_MOD_OP;

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		ActionForward result = null;
		db.begin();	
		
		Configuracao resultSet = ConfiguracaoHome.findByID(db,"CONTINGENCIA_CRM");
		request.setAttribute("contingenciaCrm", resultSet);
				
		result = actionMapping.findForward("success");		    
    
		return result;
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		//return codOperacao;
		return null;
	}

}
