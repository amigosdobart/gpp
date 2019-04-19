package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilErrosRecargaControle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe responsavel por chamar a vm do filtro
 * e redirecionar a requisicao para
 * mostrar a tela para o usuario
 * 
 * @author Bernardo Pina
 * @since  06/06/2007
 */
public class ShowFiltroConsultaRelContabilErrosRecControle extends ShowAction 
{
	private String codOperacao = Constantes.MENU_CON_ERR_REC_CONTROLE;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "filtroConsultaRelContabilErrosRecControle.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
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


