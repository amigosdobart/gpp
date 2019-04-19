package br.com.brasiltelecom.ppp.action.relatorioMKTHighlights;

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
 * Gera o relatório de Highlights TSD
 * 
 * @author Rafael Palladino
 * @since 14/12/2004
 */
public class ConsultaRelMKTHighlightsAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório Highlights TSD");
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório Marketing realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_HIGHLIGHT_TSD;
				
				String endereco = "../reports/rwservlet?hsid+MKTHighlights.rdf+"
								+ request.getParameter("DESFORMAT")
								+ "+dti="+request.getParameter("dataInicial")
								+ "+dtf="+request.getParameter("dataFinal")
								+ "+CNS="+request.getParameter("codigos_nacionais")
								+ "+PLANOS=";
				if (request.getParameter("PREPAGO")!=null&&request.getParameter("PREPAGO").equals("True"))
					endereco += "Pre-pago,";
				if (request.getParameter("POSPAGO")!=null&&request.getParameter("POSPAGO").equals("True"))
					endereco += "Pos-pago,";	
				if (request.getParameter("HIBRIDO")!=null&&request.getParameter("HIBRIDO").equals("True"))
					endereco += "Hibrido";

				endereco += "+ESTOQUE=";
				if (request.getParameter("ESTOQUE")!=null&&request.getParameter("ESTOQUE").equals("True"))
					endereco += "S,";
				if (request.getParameter("NAOESTOQUE")!=null&&request.getParameter("NAOESTOQUE").equals("True"))
					endereco += "N";
									
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Marketing_TSDHighlights.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
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
