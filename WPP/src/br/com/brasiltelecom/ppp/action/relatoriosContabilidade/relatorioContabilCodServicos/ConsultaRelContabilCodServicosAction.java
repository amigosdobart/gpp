package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilCodServicos;
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
 * Gera o relatório de Códigos de Serviços Pré-Pago
 * 
 * @author Geraldo Palmeira
 * @since 26/04/2005
 */
public class ConsultaRelContabilCodServicosAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório de códigos de serviços pré-pago");
	
				this.codOperacao = Constantes.COD_CONSULTA_CONT_COD_SERVICO;

				String tipoPlanoPre = request.getParameter("prepago");
				String tipoPlanoHib = request.getParameter("hibrido");
				String tipoCodigo   = request.getParameter("tipoCodigo");
				String tipoCodigoBO = request.getParameter("bonus");
				String tipoCodigoRE = request.getParameter("recarga");
				String tipoServico  = request.getParameter("tipoServico");
				String plano    = "";
				String endereco = "";
				String servico  = "";
				
				if ("true".equals(tipoPlanoPre))
				{
					plano += "0";
				}
				
				if ("true".equals(tipoPlanoHib))
				{
					plano += "1";
				}
				
     			if (tipoCodigo.equals("recarga"))
     			{
	     			if ("true".equals(tipoCodigoRE))
	     			{
	     				servico += "RE";
	     			}
	     			if  ("true".equals(tipoCodigoBO))
	     			{
	     				servico += "BO";
	     			}

     			}
	     		else
	     		{
	     			if ("true".equals(tipoCodigoBO))
	     			{	
	     				if (tipoServico.equals("gsm"))
	     				{
	     					servico += "CBG";
	     				}
	     				if (tipoServico.equals("btsa"))
	     				{
	     					servico += "CBB";
	     				}
	     				if (tipoServico.equals("todos"))
	     				{
	     					servico += "CBBCBG";
	     				}
	     			}
	     			if ("true".equals(tipoCodigoRE))
	     			{
	     				if (tipoServico.equals("gsm"))
	     				{
	     					servico += "CRG";
	     				}
	     				if (tipoServico.equals("btsa"))
	     				{
	     					servico += "CRB";
	     				}
	     				if (tipoServico.equals("outras"))
	     				{
	     					servico += "CRO";
	     				}
	     				if (tipoServico.equals("todos"))
	     				{
	     					servico += "CRGCRBCRO";
	     				}
	     			}
	     		}
     			 		
     			endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilCodServicosDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+PLANO="+plano+"+SERVICO="+servico;
				
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
