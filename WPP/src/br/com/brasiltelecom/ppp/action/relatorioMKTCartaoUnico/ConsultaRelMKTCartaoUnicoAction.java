/*
 * Created on 12/05/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioMKTCartaoUnico;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
 * Gera o relatório de tráfego por CSP
 * 
 * @author Daniel Ferreira
 * @since 12/05/2005
 */
public class ConsultaRelMKTCartaoUnicoAction extends ActionPortal 
{

	private String codOperacao = Constantes.COD_CONSULTAR_CARTAO_UNICO;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
				                       HttpServletResponse response, Database db)
		throws Exception 
	{
					
		db.begin();
	
		ActionForward result = null;
				
		logger.info("Consulta por Relatorio de Informacoes de Cartao Unico");
		
		//Determinando o mes de pesquisa
		String desFormat = request.getParameter("DESFORMAT");
		String cn = request.getParameter("CN");
		int periodo = Integer.parseInt(request.getParameter("periodo"));
		Calendar calMes = Calendar.getInstance();
		for(int i = 0; i < periodo; i++)
		{
			calMes.add(Calendar.MONTH, -1);
		}
		SimpleDateFormat conversorMes = new SimpleDateFormat("yyyyMM");
		String mes = conversorMes.format(calMes.getTime());
		
		String endereco = "../reports/rwservlet?ppp+MKTCartaoUnico.rdf+"+desFormat+"+MES="+mes+"+CN="+cn;;
		if(desFormat.equals("DELIMITED"))
		{
			endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Marketing_Cartao_Unico.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
		}
						
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao Relatorio de Cartao Unico realizada com sucesso!");
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
