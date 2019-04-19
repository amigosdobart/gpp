package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilConsumo;

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
	
				this.codOperacao = Constantes.COD_CONSULTAR_CONTABIL_CONSUMO;

				String periodo = request.getParameter("MES") + "/" + request.getParameter("ANO");
				String codigosNacionais = request.getParameter("codigos_nacionais");
				String codigosNacionaisOrigem = request.getParameter("codigos_nacionais_origem");
				String unidadesFederacao = request.getParameter("unidades_federacao");
				String endereco = null;

				endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilConsumoDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+PERIODO="+periodo+"+CNS="+codigosNacionais+"+CNSO="+codigosNacionaisOrigem+"+UFS="+unidadesFederacao;
				
				request.setAttribute("endereco", endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/consumo realizada com sucesso!");
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
