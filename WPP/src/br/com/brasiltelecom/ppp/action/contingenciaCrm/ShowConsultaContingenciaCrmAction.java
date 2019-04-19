package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm;

/**
 * Mostra a tela de resultado da consulta de assinantes bloqueados do processo de contingencia do CRM
 * 
 * @author Joao Carlos
 * @since 02/09/2004
 */

public class ShowConsultaContingenciaCrmAction extends ShowAction
{
	private String codOperacao = null;

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "consultaContingenciaCrm.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception
	{
		ContingenciaCrm result = null;		
		if (request.getAttribute(Constantes.RESULT) != null)
		{
			result = (ContingenciaCrm) request.getAttribute(Constantes.RESULT);
			context.put("dadosContingenciaCrm",result);
			context.put("tamanho",String.valueOf(1));
		}		
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}
