package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTGerencialOptIn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatório gerencial Opt-in
 * 
 * @author Magno Batista Corrêa
 * @since 2006/07/26 (yyyy/mm/dd)
 */
public class ConsultaRelMKTGerencialOptInAction extends ActionPortal
{
	SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATA_FORMATO);
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Coloca nos parâmetros a informação de Data inicial e Data final 
	 * @param request
	 * @param parametros 
	 * @param valores 
	 * @throws ParseException
	 */
	private void getDataOuPeriodo(HttpServletRequest request, ArrayList parametros, ArrayList valores) throws ParseException
	{
		String tipoPeriodo=request.getParameter("tipoPeriodo");
		String dataInicial = null;
		String dataFinal = sdf.format(new Date());
		if (tipoPeriodo != null)
		{
			if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo")))
			{
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(request.getParameter("periodo")));
				dataInicial = sdf.format(c.getTime());
			}
			else if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("dataInicial")) && !"".equals(request.getParameter("dataFinal")))
			{
				dataInicial = sdf.format(sdf.parse(request.getParameter("dataInicial")));
				dataFinal = sdf.format(sdf.parse(request.getParameter("dataFinal")));
			}
		}
		parametros.add("DATA_INICIAL");
		valores.add(dataInicial);
		parametros.add("DATA_FINAL");
		valores.add(dataFinal);
	}

	/**
	 * Coloca nos parâmetros a informação de Granulosidade 
	 * @param request
	 * @param parametros
	 * @param valores
	 */
	private void getGranulosidadeData(HttpServletRequest request, ArrayList parametros, ArrayList valores)
	{
		String granulosidadeData = request.getParameter("granulosidadeData");
		if (!"0".equals(granulosidadeData))
		{
			parametros.add("MASCARA_TEMPO");
			valores.add(granulosidadeData);
		}
	}
	
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

		String nomeDoRelatorio = "RelatorioGerencialOptIn.rdf";
		String separador = "&";
		String atribuidor = "=";
		ActionForward result = null;
		logger.info("Consulta pro relatório gerencial Opt-in");
		this.codOperacao = Constantes.COD_CONSULTA_GERENCIAL_OPTIN;

		getDataOuPeriodo(request, parametros, valores);
		getGranulosidadeData(request, parametros, valores);
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório gerencial Opt-in realizada com sucesso!");
		StringBuffer endereco = new StringBuffer();
		String desFormat = request.getParameter("DESFORMAT");
		
		endereco.append("../reports/rwservlet?ppp");
		endereco.append(separador);
		endereco.append(nomeDoRelatorio);
		endereco.append(separador);
		endereco.append(desFormat);
		
		int size = parametros.size();
		for (int i = 0 ; i < size ; i++)
		{
			endereco.append(separador);
			endereco.append(parametros.get(i));
			endereco.append(atribuidor);
			endereco.append(valores.get(i));
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
