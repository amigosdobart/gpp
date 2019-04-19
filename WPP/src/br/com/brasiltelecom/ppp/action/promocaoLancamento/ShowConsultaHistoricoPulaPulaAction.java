/*
 *	Created on 28/03/2005
 */
package br.com.brasiltelecom.ppp.action.promocaoLancamento;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 *	Mostra a tela com o historico de execucao da Promocao Pula-Pula gerado
 * 
 *	@author	Daniel Ferreira
 *	@since	28/03/2005
 */
public class ShowConsultaHistoricoPulaPulaAction extends ShowAction 
{
	
	private String codOperacao = Constantes.MENU_HIS_PULA_PULA;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaHistoricoPulaPula.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db)
		throws Exception 
	{
		//HttpSession session = request.getSession();
		
		int page = 0;
		int pageSize = Constantes.PAGE_SIZE;
		
		if((request.getAttribute("page") != null) && (((String)request.getAttribute("page")).length() != 0))
		{
			page = Integer.parseInt(request.getAttribute("page").toString());
		}
		
		ArrayList historico = (ArrayList)request.getAttribute(Constantes.RESULT);
		
		//Coloca o MSISDN em formato de apresentacao no contexto
		context.put("obr_msisdn", request.getAttribute("obr_msisdn"));
		//Coloca o MSISDN sem formatacao no contexto
		context.put("msisdn", request.getAttribute("msisdn"));
		//Coloca o historico no contexto
		context.put("historico",Util.getPagedItem(page, pageSize, historico));
		//Coloca o objeto Collection de paginas no contexto
		context.put("paginas",Util.getPages(historico.size(), pageSize)); 
		//Coloca a pagina atual no contexto
		context.put("page", new Integer(page)); 
		//Coloca o tamanho maximo da pagina no contexto
		context.put("pageSize", new Integer(Constantes.PAGE_SIZE));
		//Coloca o total de registros no contexto
		context.put("total", new Integer(historico.size()));
		//Coloca objeto SimpleDateFormat para formatacao da data
		context.put("conversorData", new SimpleDateFormat("dd/MM/yyyy"));
		//Coloca objeto DecimalFormat para formatacao do valor de credito
		context.put("conversorValor", new DecimalFormat("##,##0.00"));
		//Coloca a escolha do tipo de pesquisa no contexto
		context.put("choice", request.getAttribute("choice"));
		//Coloca o mes inicial de pesquisa no contexto
		context.put("mesInicial", request.getAttribute("choice"));
		//Coloca o mes final de pesquisa no contexto
		context.put("mesFinal", request.getAttribute("mesFinal"));
	}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
