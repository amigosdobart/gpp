package br.com.brasiltelecom.ppp.action.cadastroCampanha;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Joao Carlos
 * Data..: 09-Mar-2006
 */
public class ShowConsultaAssinanteCampanhaAction extends ShowAction
{
	public String getTela()
	{
		return "consultaAssinanteCampanha.vm";
	}
	
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception
	{
		context.put("assinanteCampanhas",request.getAttribute("assinanteCampanhas"));
	}

	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_ASSIN_CAMPANHA;
	}
}
