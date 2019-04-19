/*
 * Created on 21/08/2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioTrafego;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import org.apache.log4j.Logger;


import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.Uteis;

/**
 * Gera o relatório de tráfego de ligações
 * 
 * @author Jorge Abreu
 * @since 21/08/2007
 */
public class ConsultaRelTrafegoAction extends ActionPortalHibernate 
{

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
	
				ActionForward result = null;
				
				logger.info("Consulta ao relatório de trafego");
	
				Date dataInicial = sdf.parse(request.getParameter("dataInicial"));
				Date dataFinal 	 = sdf.parse(request.getParameter("dataFinal"));
				String categoria = Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),",");
				String cn 		 = Uteis.arrayParaString(request.getParameterValues("CN"),",");
				String csp 		 = Uteis.arrayParaString(request.getParameterValues("CSP"),",");
				String endereco = null;
							
				endereco = "./relatoriosWPP?NOME_RELATORIO=RelatorioTrafego+ARQUIVO_PROPRIEDADES=/relatorio/Trafego.xml+DATAINICIAL="+sdf.format(dataInicial)+"+DATAFINAL="+sdf.format(dataFinal)+"+CN="+cn+"+CATEGORIA="+categoria+"+CSP="+csp;
			
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de trafego realizada com sucesso!");
				result = actionMapping.findForward("redirect"); 
		   
		 		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de trafego realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_REL_TRAF;
				
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
