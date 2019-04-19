package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTMigracoesPulaPula;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * Mostra a tela de consulta do relatório de clientes em shutdown
 * 
 * @author Marcos Castelo Magalhães
 * @since 27/10/2005
 */
public class ShowFiltroConsultaRelMKTMigracoesPulaPula extends ShowAction 
{
	private String codOperacao = Constantes.MENU_REL_MKT_MIGRACOES_PP;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelMKTMigracoesPulaPula.vm";
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
