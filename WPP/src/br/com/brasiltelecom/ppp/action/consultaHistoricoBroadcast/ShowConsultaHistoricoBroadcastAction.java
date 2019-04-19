/*
 * Created on 23/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaHistoricoBroadcast;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de resultado da consulta de histórico de comprovante de serviços
 * 
 * @author Marcelo Alvesa Araujo
 * @since 03/08/2005
 */
public class ShowConsultaHistoricoBroadcastAction extends ShowAction 
{
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaHistoricoBroadcastSMS.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,Database db)throws Exception 
	{
		context.put("resultado",request.getAttribute(Constantes.RESULT));
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return Constantes.COD_BROADCAST_SMS;
	}

}
