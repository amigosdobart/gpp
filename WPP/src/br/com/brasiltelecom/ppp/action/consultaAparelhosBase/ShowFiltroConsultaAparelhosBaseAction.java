/*
 * Created on 11/08/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.consultaAparelhosBase;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de Aparelhos na Base
 * 
 * @author Marcos C. Magalhães
 * @since 11/08/2005
 */
public class ShowFiltroConsultaAparelhosBaseAction extends ShowAction {
	
	private String codOperacao = Constantes.COD_CONSULTAR_BASE_APARELHOS;
	/** 
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	
	public String getTela() 
	{
		return "consultaAparelhosBase.vm";
	}
	
	/**
	* @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	*/
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,Database db)
	{
	//HttpSession session = request.getSession();
	}		

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}
}
