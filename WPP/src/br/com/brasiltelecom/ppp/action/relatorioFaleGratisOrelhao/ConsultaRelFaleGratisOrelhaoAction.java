/*
 * Created on 21/08/2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioFaleGratisOrelhao;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o Relatório Fale de Graca no Orelhao
 * 
 * @author Jorge Abreu
 * @since 16/11/2007
 */
public class ConsultaRelFaleGratisOrelhaoAction extends ActionPortalHibernate 
{

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String codOperacao = Constantes.COD_CONSULTA_FALE_GRATIS_ORELHAO;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
	
				ActionForward result = null;
				
				logger.info("Consulta ao relatório de Chamadas Fale de Graca no Orelhao");
	
				Date dataInicial = sdf.parse(request.getParameter("dataInicial"));
				Date dataFinal 	 = sdf.parse(request.getParameter("dataFinal"));
				String endereco = "./relatoriosWPP?NOME_RELATORIO=RelatorioFaleGratisOrelhao+ARQUIVO_PROPRIEDADES=/relatorio/Trafego.xml+DATAINICIAL="+sdf.format(dataInicial)+"+DATAFINAL="+sdf.format(dataFinal);
			
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de Chamadas Fale de Graca no Orelhao realizada com sucesso!");
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
