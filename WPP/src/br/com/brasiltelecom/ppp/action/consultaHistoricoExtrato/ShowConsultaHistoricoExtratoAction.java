/*
 * Created on 23/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaHistoricoExtrato;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * Mostra a tela de resultado da consulta de hist�rico de comprovante de servi�os
 * 
 * @author Andr� Gon�alves
 * @since 21/05/2004
 */
public class ShowConsultaHistoricoExtratoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_HIS_EXTRATO;
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaHistoricoExtrato.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
			
		int page = 0 ;
		int pagesize = 90;
		//int excel = 0;
		
		Collection result = new ArrayList();
		
//		recupera pagina atual
		if (request.getAttribute("page") != null && !"".equals(request.getAttribute("page"))){
		   page = Integer.parseInt(request.getAttribute("page").toString());
		}
		
		if (request.getAttribute(Constantes.RESULT) != null){
		
			result = (Collection) request.getAttribute(Constantes.RESULT);
			
			context.put("obr_msisdn",request.getAttribute("obr_msisdn"));
//			recupera pagina de uma colecao 	
			 context.put("historicoExtratos",Util.getPagedItem(page,pagesize,result));
		   //guarda o ultimo filtro  
			 context.put("filtro_sql",request.getAttribute("filtro_sql"))	;  
		   //recupera collection de paginas
			 context.put("paginas",Util.getPages(result.size(),pagesize)); 
		   //coloca a p�gina atual
			 context.put("page", new Integer(page)); 
			//coloca o total
			 context.put("total",Integer.toString(result.size()));
			 
			 context.put("paginaAtual",new Integer(page + 1));
			 
			 context.put("totalPaginas",new Integer(result.size()/pagesize + 1));
			
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
