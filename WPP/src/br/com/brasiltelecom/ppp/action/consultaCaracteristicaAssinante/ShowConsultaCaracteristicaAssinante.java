/*
 * Apresenta a tela de resultado da consulta e de teste do SMS
 * Criado em 16/06/2005
 * 
 */
package br.com.brasiltelecom.ppp.action.consultaCaracteristicaAssinante;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ShowAction;


/**
 * @author Marcelo Alves Araujo
 */
public class ShowConsultaCaracteristicaAssinante extends ShowAction {

	Logger logger = Logger.getLogger(this.getClass());
	/* 
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		
		return "consultaCaracteristicaAssinante.vm";
	}

	/* 
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception 
	{	
		HttpSession sessao = request.getSession(true);
		
	    // Passa para a VM os valores consultados
	    context.put("msisdn", sessao.getAttribute("msisdn"));
	    context.put("retorno", sessao.getAttribute("retorno"));
		context.put("aparelho", sessao.getAttribute("aparelho"));
		context.put("testes", sessao.getAttribute("testes"));
	}

	/* 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return null;
	}
}