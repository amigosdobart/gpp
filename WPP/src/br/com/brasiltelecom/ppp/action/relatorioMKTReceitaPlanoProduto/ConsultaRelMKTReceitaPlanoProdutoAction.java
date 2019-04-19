/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioMKTReceitaPlanoProduto;

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
 * Gera o relatório de receita por plano de serviço e produto
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ConsultaRelMKTReceitaPlanoProdutoAction extends ActionPortal {

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
					
				db.begin();
	
				ActionForward result = null;
				
				logger.info("Consulta por relatório de receita por plano de serviço e produto solicitada");
	
				String tipoPeriodo=request.getParameter("tipoPeriodo");
				
				Date dataInicial = null;
				Date dataFinal = new Date();
				if (tipoPeriodo != null){
					if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo"))){
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(request.getParameter("periodo")));
						dataInicial = c.getTime();
					} else if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("dataInicial")) && !"".equals(request.getParameter("dataFinal"))){
						dataInicial = sdf.parse(request.getParameter("dataInicial"));
						dataFinal = sdf.parse(request.getParameter("dataFinal"));
					}
				}
				
					request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de receita por plano de serviço e produto realizada com sucesso!");
					this.codOperacao = Constantes.COD_CONSULTA_RECEITA_PLANO_PROD;

				
					String endereco = "../reports/rwservlet?ppp+MKTReceitaPlanoProduto.rdf+"+request.getParameter("DESFORMAT")+"+datai="+sdf.format(dataInicial)+"+dataf="+sdf.format(dataFinal);
					if (request.getParameter("DESFORMAT").equals("DELIMITED"))
						endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Marketing_Receita_Produto.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
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
