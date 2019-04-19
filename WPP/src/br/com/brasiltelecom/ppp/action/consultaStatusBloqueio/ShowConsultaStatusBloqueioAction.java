/*
 * Created on 19/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaStatusBloqueio;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * @author André Gonçalves
 * @since 19/07/2004
 */
public class ShowConsultaStatusBloqueioAction extends ShowAction {

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaStatusBloqueio.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		
		int page = 0 ;
		int pagesize = 90;
		
		context.put("obr_msisdn",request.getAttribute("obr_msisdn"));
		
		if (request.getAttribute("page") != null && !"".equals(request.getAttribute("page")))
			   page = Integer.parseInt(request.getAttribute("page").toString());
		
		if (request.getAttribute(Constantes.RESULT) != null)
		{
			Collection infoStatusLista = (Collection)request.getAttribute(Constantes.RESULT);
			context.put("infosStatus", infoStatusLista);
			context.put("util",new Util());
			
			//recupera collection de paginas
			context.put("paginas",Util.getPages(infoStatusLista.size(),pagesize)); 
			//coloca a página atual
			context.put("page", new Integer(page)); 
			//coloca o total
			context.put("total", new Integer(infoStatusLista.size()));
			context.put("paginaAtual",new Integer(page + 1));
			context.put("totalPaginas",new Integer(infoStatusLista.size()/pagesize + 1));
			context.put("tamanho", new Integer(infoStatusLista.size()));
	
			
			context.put("teste",Util.getPagedItem(page,pagesize,infoStatusLista));
		    //recupera collection de paginas


	
			
			
			/*for (Iterator i = infoStatusLista.iterator(); i.hasNext();)
			{
				AssinanteServicoBloqueado infoStatus = (AssinanteServicoBloqueado)i.next();
				context.put("idMsisdn", infoStatus.getIdMsisdn());
				context.put("datAtualizacao", infoStatus.getDatAtualizacao());
				context.put("descMotivo", infoStatus.getDescMotivo());
				context.put("descServico", infoStatus.getDescServico());
				context.put("descStatus", infoStatus.getDescStatus());
			} */
		}
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
