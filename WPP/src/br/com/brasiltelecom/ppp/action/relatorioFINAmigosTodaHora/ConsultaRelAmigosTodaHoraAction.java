/*
 * Created on 12/05/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioFINAmigosTodaHora;

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
 * Gera o relatório de chamadas Amigos Toda Hora por CN
 * 
 * @author Daniel Ferreira
 * @since 12/05/2005
 */
public class ConsultaRelAmigosTodaHoraAction extends ActionPortal 
{

	private String codOperacao = Constantes.COD_CONSULTAR_AMIGOS_TH;
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
				
		logger.info("Consulta por Relatorio de Chamadas Amigos Toda Hora por CN");
		
		//Determinando o mes de pesquisa
		String desFormat = request.getParameter("DESFORMAT");
		String cn = request.getParameter("P_CN");
		int periodo = Integer.parseInt(request.getParameter("periodo"));
		Calendar calMes = Calendar.getInstance();
		calMes.set(Calendar.DAY_OF_MONTH, 1);
		for(int i = 0; i < periodo; i++)
		{
			calMes.add(Calendar.MONTH, -1);
		}
		
		SimpleDateFormat conversorMes = new SimpleDateFormat("dd/MM/yyyy");	
		String dataInicial = conversorMes.format(calMes.getTime());
		
		calMes.add(Calendar.MONTH, 1);
		String dataFinal = conversorMes.format(calMes.getTime());
		
		String endereco = "../reports/rwservlet?ppp+FINAmigosTodaHora.rdf+"+desFormat+"+DATA_INICIAL="+dataInicial+"+DATA_FINAL="+dataFinal+"+P_CN="+cn;
		if(desFormat.equals("DELIMITED"))
		{
		    endereco = endereco.concat("+DELIMITED_HDR=NO+DELIMITER=;+DES_TYPE=LOCALFILE+DES_NAME=.xls");
		}
						
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao Relatorio Amigos Toda Hora realizada com sucesso!");
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