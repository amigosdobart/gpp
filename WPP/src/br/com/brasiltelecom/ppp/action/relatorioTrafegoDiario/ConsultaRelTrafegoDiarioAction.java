/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioTrafegoDiario;

import java.text.SimpleDateFormat;
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
 * Gera o relatório de tráfego diario
 * 
 * @author Renato Picanço
 * @since 21/06/2004
 */
public class ConsultaRelTrafegoDiarioAction extends ActionPortal
{
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
									   ActionForm actionForm,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   Database db) throws Exception
	{
		db.begin();
		ActionForward result = null;
		
		logger.info("Consulta por relatório de trafego diario");
		
		Date dataInicial = sdf.parse(request.getParameter("dataInicial"));
		Date dataFinal = sdf.parse(request.getParameter("dataFinal"));
		String formato = request.getParameter("DESFORMAT");
		String plano   = request.getParameter("PLANO");
		String csp 	   = request.getParameter("CSP");
		String ddd	   = request.getParameter("DDD");
		String endereco = "";
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de trafego diario realizada com sucesso!");
		this.codOperacao = Constantes.COD_CONSULTA_RECEITA_PLANO_PROD;
		
		if (formato.equalsIgnoreCase("DELIMITED"))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=TrafegoDiarioDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Trafego.xml+DDD="+ddd+"+PLANO="+plano+"+CSP="+csp+"+DATAINI="+sdf.format(dataInicial)+"+DATAFIM="+sdf.format(dataFinal);
		}
		else
		{
			endereco = "../reports/rwservlet?ppp+Perfil_trafego_diario.rdf+"+formato+"+DATAINI="+sdf.format(dataInicial)+"+DATAFIM="+sdf.format(dataFinal)+"+PLANO="+plano+"+CSP="+csp+"+DDD="+ddd;
		}
		request.setAttribute("endereco", endereco);
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
