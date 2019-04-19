package br.com.brasiltelecom.ppp.action.consultaPromocaoPulaPula;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.model.RetornoExtratoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;
import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

/**
 *	Mostra a tela com o Extrato Pula-Pula gerado.
 *
 *	@author	Daniel Ferreira
 *	@since	10/10/2005
 */
public class ShowConsultaPromocaoPulaPulaAction extends ShowAction
{

	private String codOperacao = Constantes.MENU_CONSULTA_EXT_PULA;

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "consultaExtratoPulaPula.vm";
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
		   page = Integer.parseInt(request.getAttribute("page").toString());
		
		RetornoExtratoPulaPula result = (RetornoExtratoPulaPula) request.getAttribute(Constantes.RESULT);
		context.put("msisdn"			, request.getAttribute("msisdn"));

		context.put("consultaCheia"			, request.getAttribute("consultaCheia"));
		context.put("msisdnFormatado"	, request.getAttribute("msisdnFormatado"));
		context.put("tipoPulaPula"		, result.getPromocao());
		context.put("promoShowValor" 	, new Boolean(result.getPromocao().getIdtPromocao()!=Promocao.FALE_GANHE));
		context.put("dataInicial"		, request.getAttribute("dataInicial"));
		context.put("dataFinal"			, request.getAttribute("dataFinal"));
		context.put("totalBonus"		, result.getTotalBonus());
		context.put("totalParcial"		, result.getTotalParcial());
		context.put("totalAReceber"		, result.getTotalAReceber());
		context.put("extratos"			, Util.getPagedItem(page, pageSize, result.getExtratos()));
		context.put("formatador"		, new SimpleDateFormat("HH:mm:ss"));
		context.put("formatTelefone" 	, new PhoneNumberFormat());
		context.put("calendar"			, Calendar.getInstance());
		context.put("conversorDouble", new DecimalFormat(Constantes.DOUBLE_FORMATO, new DecimalFormatSymbols(new Locale("pt", "BR", ""))));
		//coloca o tamanho da pagina.
		context.put("pageSize"			, new Integer(pageSize));
	    //coloca collection de paginas.
		context.put("paginas"			, Util.getPages(result.getExtratos().size(), pageSize));
	    //coloca a pagina atual.
		context.put("page"				, new Integer(page));
		//listagem.
		context.put("sumarizacao"		, result.getSumarizacao());
		context.put("bonificacoes"		, result.getBonificacoes());
	    //coloca o total.
		context.put("total"				, new Integer(result.getExtratos().size()));
		context.put("tamanho"			, new Integer(result.getExtratos().size()));
		context.put("retorno"			, result.getRetorno());

		//Se for a ultima pagina, mostrar os valores totais.
		if (page == Util.getPages(result.getExtratos().size(), pageSize).size() - 1)
			context.put("mostra"		, "S");
		else
			context.put("mostra"		, "N");
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}

}
