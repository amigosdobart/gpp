/*
 * Created on 22/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.administrativoTipoExtrato;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela com o resultado da consulta pelos comprovantes de serviço cadastrados no sistema
 * 
 * @author André Gonçalves
 * @since 20/05/2004
 */
public class ShowConsultaAdministrativoTipoExtratoAction extends ShowAction{

	private String codOperacao = Constantes.MENU_TIPO_EXTRATO;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaAdministrativoTipoExtrato.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception {
		
		context.put("alterarTipoExtrato","S");
		context.put("excluirTipoExtrato","S");
		
		if (request.getAttribute(Constantes.RESULT) != null){
			
			Collection result = (Collection) request.getAttribute(Constantes.RESULT);
	
			context.put("tipoExtratos", result);

			context.put("tamanho",String.valueOf(result.size()));
		}
		
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
