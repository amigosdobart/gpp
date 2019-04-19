package br.com.brasiltelecom.ppp.action.consultaStatusGerenteFeliz;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de passagem de parâmetros
 * @author	Geraldo Palmeira
 * @since	27/09/2006
 *
 */
public class ShowFiltroConsultaStatusGerenteFeliz extends ShowAction
{

	public String getTela()
	{
		return "filtroConsultaStatusGerenteFeliz.vm";
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		context.put("happyManager",request.getAttribute("happyManager"));
	}

	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_GERENTE_FELIZ;
	}
}
