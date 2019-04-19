package br.com.brasiltelecom.ppp.action.consultaEstornoPulaPula;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 *	Classe responsavel por mostrar a tela de consulta de Estorno de Bonus Pula-Pula por Fraude.
 * 
 *	@author	Daniel Ferreira
 *	@since	06/03/2006
 */

public class ShowFiltroConsultaEstornoPulaPulaAction extends ShowAction 
{

	private String codOperacao = Constantes.MENU_CONS_ESTORNO_PULA;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaEstornoPulaPula.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db)
		throws Exception 
	{
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}

}
