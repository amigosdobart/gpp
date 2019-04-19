package br.com.brasiltelecom.ppp.action.consultaStatusRecargaRecorrente;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Apresenta a tela com o resultado da consulta
 * @author	Marcelo Alves Araujo
 * @since	13/04/2006
 */
public class ShowConsultaStatusRecargaRecorrente extends ShowAction
{

	/**
	 * Retorna o nome da VM a ser chamada
	 * @param String
	 */
	public String getTela()
	{
		return "consultaStatusRecargaRecorrente.vm";
	}

	/**
	 * Envia os dados para a VM
	 * @param VelocityContext
	 * @param HttpServletRequest
	 * @param Database
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db)
	{
		context.put("safra", request.getAttribute("safra"));
		context.put("tamanho", request.getAttribute("tamanho"));
		context.put("msisdn", request.getParameter("obr_msisdn"));
		context.put("contrato", request.getAttribute("contrato"));
		context.put("filial", request.getAttribute("filial"));
		context.put("dataInicial", request.getParameter("dataInicial"));
		context.put("dataFinal", request.getParameter("dataFinal"));
		context.put("listaRecargas", request.getAttribute("listaRecargas"));
	}

	/**
	 * Retorna o código da operação
	 * @return String
	 */
	public String getOperacao()
	{
		return Constantes.COD_CONSULTA_REC_RECORRENTE;
	}
}
