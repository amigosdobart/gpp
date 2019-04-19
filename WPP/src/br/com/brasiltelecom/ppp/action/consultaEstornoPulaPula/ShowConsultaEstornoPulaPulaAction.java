package br.com.brasiltelecom.ppp.action.consultaEstornoPulaPula;


import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.model.RetornoEstornoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 *	Mostra a tela com a consulta de Estorno de Bonus Pula-Pula por Fraude gerado.
 * 
 *	@author	Daniel Ferreira
 *	@since	07/03/2006
 */
public class ShowConsultaEstornoPulaPulaAction extends ShowAction 
{
	
	private String codOperacao = Constantes.MENU_CONS_ESTORNO_PULA;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaEstornoPulaPula.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception 
	{
		int page = 0 ;
		int pageSize = Constantes.PAGE_SIZE;
		
		//Recupera pagina atual.
		if (request.getAttribute("page") != null && !"".equals(request.getAttribute("page")))
		{
		   page = Integer.parseInt(request.getAttribute("page").toString());
		}
		
		RetornoEstornoPulaPula result = (RetornoEstornoPulaPula) request.getAttribute(Constantes.RESULT);
		context.put("result"	, result);
		context.put("estornos"	, Util.getPagedItem(page, pageSize, result.getListaEstornos()));
		//coloca o tamanho da pagina.
		context.put("pageSize"	, new Integer(pageSize));
	    //coloca collection de paginas.
		context.put("paginas"	, Util.getPages(result.getListaEstornos().size(), pageSize)); 
	    //coloca a pagina atual.		
		context.put("page"		, new Integer(page)); 
	    //coloca o tamanho.
		context.put("tamanho"	, new Integer(result.getListaEstornos().size()));
		
		//Se for a ultima pagina, mostrar os valores totais.
		if (page == Util.getPages(result.getListaEstornos().size(), pageSize).size() - 1)
		{
			context.put("mostra"		, "S");
		}
		else 
		{
			context.put("mostra"		, "N");
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
