package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTBonificacaoCampanhas;

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
 * Gera o relatório de Bonificacao de Campanhas
 * 
 * @author Geraldo Palmeira
 * @since 13/11/2006
 */ 
public class ConsultaRelMKTBonificacaoCampanhas extends ActionPortal {

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
				
				logger.info("Consulta relatório de marketing Bonificação Campanhas");
	
				this.codOperacao = Constantes.COD_CONSULTA_BONIFICACAO_CAMPANHA;
				
				String dataInicial 	  = request.getParameter("dataInicial");
				String dataFinal 	  = request.getParameter("dataFinal");
				String codigoNacional = request.getParameter("cn");
				String origem 		  = request.getParameter("origens"); 
				String endereco       = "../reports/rwservlet?ppp+MKTBonificacaoCampanhas.rdf+"
										+request.getParameter("DESFORMAT")+
										"+DATAINICIAL="+dataInicial+
										"+DATAFINAL="+dataFinal+
										"+CN="+codigoNacional+
										"+ORIGEM="+origem;
				
				if ("DELIMITED".equals(request.getParameter("DESFORMAT")))
				{
					endereco = "./relatoriosWPP?NOME_RELATORIO=MKTBonificacaoCampanhasDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml"
								+"+DATAINICIAL="+dataInicial
								+"+DATAFINAL="+dataFinal
								+"+CN="+codigoNacional
								+"+ORIGEM="+origem;
				}
				
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao Consulta relatório de marketing Bonificação Campanhas");
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
