package br.com.brasiltelecom.ppp.action.relatoriosBatch.mktClientesShutdown;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta do relatório batch de clientes em shutdown
 * 
 * @author Marcelo Alves Araujo
 * @since 17/09/2005
 */
public class ShowFiltroBatchMKTClientesSDAction extends ShowAction 
{
	private String codOperacao = Constantes.MENU_BAT_MKT_CLIENTES_SD;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaBatMKTClientesShutdown.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,Database db) throws Exception 
	{
		// Pega dados do usuário conetado
		Usuario login = (Usuario) request.getSession().getAttribute(Constantes.LOGIN);
		// Coloca o e-mail do usuário no contexto para ser usado pelo .vm
		context.put("eMail",login.getEmail());
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
