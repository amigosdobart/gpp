package br.com.brasiltelecom.ppp.action.consultaStatusAssinanteOptIn;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de passagem de parâmetros
 * @author	Geraldo Palmeira
 * @since	11/09/2006
 *
 */
public class ShowFiltroConsultaAssinanteOptIn extends ShowAction
{

	public String getTela()
	{
		return "filtroConsultaAssinanteOptIn.vm";
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		// Somente chama a tela de apresentação sem passar parâmetros
	}

	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_ASS_OPTIN;
	}
}
