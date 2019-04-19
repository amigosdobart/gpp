package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilChamadasAmigosTodaHora;

import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.Uteis;
import br.com.brasiltelecom.ppp.util._TratamentoDeCamposDeVM;

/**
 * Gera o relatório Contábil Amigos toda hora
 * 
 * @author Magno Batista Corrêa
 * @since 2006/08/09 (yyyy/mm/dd)
 */
public class ConsultaRelContabilChamadasAmigosTodaHoraAction extends ActionPortal
{
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	String separador  	 = Constantes.URL_SEPARADOR;
	String separadorMais = Constantes.URL_SEPARADOR_MAIS;
	String atribuidor	 = Constantes.URL_ATRIBUIDOR;
	
	/**
	 * @throws ParseException 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception
	{
		ArrayList parametros = new ArrayList();
		ArrayList valores = new ArrayList();
		ActionForward result = null;
		
		logger.info("Consulta do Relatório Contábil Amigos Toda Hora");

		String nomeDoRelatorio = "ContabilChamadasAmigosTodaHoraNormal.rdf";
		this.codOperacao = Constantes.CONSULTA_AMIGOS_TD_H;
		
		String categoria = Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),",");
		String cn 		 = Uteis.arrayParaString(request.getParameterValues("CN"),",");
		
		_TratamentoDeCamposDeVM tratamento = new _TratamentoDeCamposDeVM();
		tratamento.getCampoIntervaloDataPorMes(request, parametros, valores);

		request.setAttribute(Constantes.MENSAGEM, "Consulta ao Relatório Contábil Amigos Toda Hora realizada com sucesso!");
		StringBuffer endereco = new StringBuffer();
		String desFormat = request.getParameter("DESFORMAT");
		
		if ("DELIMITED".equals(desFormat))
		{
			nomeDoRelatorio = "ContabilChamadasAmigosTodaHoraNormalDelimited";
			endereco.append("./relatoriosWPP?NOME_RELATORIO=");
			endereco.append(nomeDoRelatorio);
			endereco.append(this.separadorMais);
			endereco.append("ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml");
			endereco.append(this.separadorMais);
			endereco.append("CATEGORIA="+categoria);
			endereco.append(this.separadorMais);
			endereco.append("CN="+cn);
			
			int size = parametros.size();
			for (int i = 0 ; i < size ; i++)
			{
				endereco.append(this.separadorMais);
				endereco.append(parametros.get(i));
				endereco.append(this.atribuidor);
				endereco.append(valores.get(i));
			}
		}
		else
		{
			endereco.append("../reports/rwservlet?ppp");
			endereco.append(this.separador);
			endereco.append(nomeDoRelatorio);
			endereco.append(this.separador);
			endereco.append(desFormat);
			endereco.append(this.separadorMais);
			endereco.append("CATEGORIA="+categoria);
			endereco.append(this.separadorMais);
			endereco.append("CN="+cn);
		
			int size = parametros.size();
			for (int i = 0 ; i < size ; i++)
			{
				endereco.append(this.separador);
				endereco.append(parametros.get(i));
				endereco.append(this.atribuidor);
				endereco.append(valores.get(i));
			}
			
		}
		request.setAttribute("endereco",endereco.toString());
		result = actionMapping.findForward("redirect");
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return this.codOperacao;
	}
}
