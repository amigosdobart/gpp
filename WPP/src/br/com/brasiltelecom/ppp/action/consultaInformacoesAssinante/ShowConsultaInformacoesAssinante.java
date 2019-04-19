/*
 * Created on 29/10/2004
 */
package br.com.brasiltelecom.ppp.action.consultaInformacoesAssinante;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Henrique Canto
 * 
 * Adaptado por: Bernardo Vergne (novo XML de consulta completa)
 * Data: 08/10/2007
 */
public class ShowConsultaInformacoesAssinante extends ShowAction 
{
	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaInformacoesAssinante.vm";
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception 
	{
		context.put("cn", request.getAttribute("cn"));
		context.put("alteraFF", request.getAttribute("alteraFF"));
		context.put("assinante", request.getAttribute("assinante"));
		context.put("msisdn", request.getParameter("msisdn"));
		context.put("sdf", new SimpleDateFormat("dd/MM/yyyy"));
		context.put("df", new DecimalFormat("#,##0.00",new DecimalFormatSymbols(new Locale("pt","BR"))));
		context.put("mensagem", request.getAttribute(Constantes.MENSAGEM));
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
