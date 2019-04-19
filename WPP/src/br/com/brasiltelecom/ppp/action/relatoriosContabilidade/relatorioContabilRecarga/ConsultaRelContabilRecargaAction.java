package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilRecarga;

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
 * Gera o relatório contabil/recargas
 * 
 * @author Alberto Magno
 * @since 01/07/2004
 */
public class ConsultaRelContabilRecargaAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório contabil para recargas");
	
				this.codOperacao = Constantes.COD_CONSULTAR_CONTABIL_RECARG;

				String desFormat = request.getParameter("DESFORMAT");
				String periodo = request.getParameter("MES") + "/" + request.getParameter("ANO");
				String codigosNacionais = request.getParameter("codigos_nacionais");
				String unidadesFederacao = request.getParameter("unidades_federacao");
				
				String endereco = null;
				
				if (desFormat.equals("DELIMITED")) 
				{
					endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilRecargasDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+PERIODO=" + periodo + "+CNS=" + codigosNacionais+"+UFS="+unidadesFederacao;
				}
				else
				{
					endereco = "../reports/rwservlet?ppp+ContabilRecargas.rdf+"+desFormat+"+PERIODO="+periodo+"+CNS="+codigosNacionais+"+UFS="+unidadesFederacao;
				}
								
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/recargas realizada com sucesso!");
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
