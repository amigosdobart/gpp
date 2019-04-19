/* 
 * Mostra a tela de busca de características do Assinante
 * Criado em 15/06/2005
 * 
 */
package br.com.brasiltelecom.ppp.action.consultaCaracteristicaAssinante;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Marcelo Alves Araujo
 */
public class ShowFiltroConsultaCaracteristicaAssinante extends ShowAction 
{
    private String codOperacao = Constantes.MENU_CONSULTAR_CARAC_ASS;
	
    /**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaCaracteristicaAssinante.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception 
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