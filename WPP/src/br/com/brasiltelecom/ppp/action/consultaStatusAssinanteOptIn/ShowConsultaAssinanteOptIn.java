package br.com.brasiltelecom.ppp.action.consultaStatusAssinanteOptIn;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Apresenta a tela com o resultado da consulta
 * @author	Geraldo Palmeira
 * @since	11/09/2006
 */
public class ShowConsultaAssinanteOptIn extends ShowAction
{

	/**
	 * Retorna o nome da VM a ser chamada
	 * @param String
	 */
	public String getTela()
	{
		return "consultaStatusAssinanteOptIn.vm";
	}

	/**
	 * Envia os dados para a VM
	 * @param VelocityContext
	 * @param HttpServletRequest
	 * @param Database
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db)
	{
		context.put("optIn",request.getAttribute("optIn"));
	}

	/**
	 * Retorna o código da operação
	 * @return String
	 */
	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_ASS_OPTIN;
	}
}
