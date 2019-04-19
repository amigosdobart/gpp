package br.com.brasiltelecom.ppp.action.relatorioMKTClientesShutdown;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * Mostra a tela de consulta do relatório de clientes em shutdown
 * 
 * @author Marcelo Alves Araujo
 * @since 27/07/2005
 */
public class ShowFiltroConsultaRelMKTClientesSDAction extends ShowAction 
{
	private String codOperacao = Constantes.MENU_MKT_CLIENTES_SD;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelMKTClientesShutdown.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,Database db) throws Exception 
	{
			
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
