/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTPulaPula2004Detalhado;

import java.text.SimpleDateFormat;
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
 * Gera o relatório detalhado do Bonus PulaPula 2004
 * 
 * @author Magno Batista Corrêa
 * @since 2006/06/02
 */
public class ConsultaRelMKTPulaPula2004DetalhadoAction extends ActionPortal {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception {
	
				ActionForward result = null;
				
				logger.info("Consulta por relatório detalhado do Bonus PulaPula 2004");
	
				String tipoPeriodo=request.getParameter("tipoPeriodo");
				
				String dataInicial = null;
				String dataFinal = sdf.format(new Date());
				if (tipoPeriodo != null){
					if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo"))){
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(request.getParameter("periodo")));
						dataInicial = sdf.format(c.getTime());
					} else if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("dataInicial")) && !"".equals(request.getParameter("dataFinal"))){
						dataInicial = sdf.format(sdf.parse(request.getParameter("dataInicial")));
						dataFinal = sdf.format(sdf.parse(request.getParameter("dataFinal")));
					}
				}
					request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório detalhado do Bonus PulaPula 2004 realizada com sucesso!");
					this.codOperacao = Constantes.COD_CONSULTA_PULA_PULA_2004_DETALHADO;
					String endereco = null;
					String desFormat = request.getParameter("DESFORMAT");
					if (desFormat.equals("DELIMITED")) 
					{
						endereco = "./relatoriosWPP?NOME_RELATORIO=MKTBonusPulaPula2004DetalhadoDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DATAINICIAL="+dataInicial+"+DATAFINAL="+dataFinal;
					}
					else
					{
						endereco = "../reports/rwservlet?ppp+MKTPulaPula2004Detalhado.rdf+"+request.getParameter("DESFORMAT")+"+datai="+dataInicial+"+dataf="+dataFinal;
					}
					request.setAttribute("endereco",endereco);
					result = actionMapping.findForward("redirect");
				return result;
			}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}
