package br.com.brasiltelecom.ppp.action.relatorioContabilConsumo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatório de ajuste Cotabil/consumo
 * 
 * @author Alberto Magno
 * @since 27/05/2004
 */
public class ConsultaRelContabilConsumoAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório contabil/consumo");
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/consumo realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_CONTABIL_CONSUMO;

				
				String endereco = "../reports/rwservlet?ppp+ContabilConsumo.rdf+"+request.getParameter("DESFORMAT")+"+PERIODO="+request.getParameter("MES")+"/"+request.getParameter("ANO")+"+CNS="+request.getParameter("codigos_nacionais")+"+CNSO="+request.getParameter("codigos_nacionais_origem")+"+GRUPOS="+request.getParameter("grupos_de_servicos")+"+CLASSE="+Constantes.CLASSE_SFA_CONSUMO;
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Contabil_Consumo.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
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
