package br.com.brasiltelecom.ppp.action.relatorioORTarifavelTrafegado;

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
public class ConsultaRelORTarifavelTrafegadoAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório ORTarifavelTrafegado");
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório ORTarifavelTrafegado realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_OR_MINUTOS;
				String endereco = null;
				
				if (request.getParameter("DESFORMAT").equals("TXT"))
					endereco = "../reports/rwservlet?ppp+ORTarifavelTrafegado.rdf+DELIMITED+DI="+request.getParameter("dataInicial")+"+DF="+request.getParameter("dataFinal")+"+CNS="+request.getParameter("codigos_nacionais")+"+TARIFAVEL=S,"+request.getParameter("incluiNaoTarifaveis")+"+rate_names="+request.getParameter("rate_names")+"+DELIMITED_HDR=no+DESNAME=MINUTOS_TRA_TAR.txt";
				else
					endereco = "../reports/rwservlet?ppp+ORTarifavelTrafegado.rdf+"+request.getParameter("DESFORMAT")+"+DI="+request.getParameter("dataInicial")+"+DF="+request.getParameter("dataFinal")+"+CNS="+request.getParameter("codigos_nacionais")+"+TARIFAVEL=S,"+request.getParameter("incluiNaoTarifaveis")+"+rate_names="+request.getParameter("rate_names");
				
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = "./rep2excel?relatorioXSL=./dtd/ORTarifavelTrafegado.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
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
