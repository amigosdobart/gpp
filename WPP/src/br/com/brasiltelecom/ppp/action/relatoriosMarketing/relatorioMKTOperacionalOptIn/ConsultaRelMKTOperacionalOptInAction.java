package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTOperacionalOptIn;

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
 * Gera o relatório Operacional de Opt-in
 * 
 * @author Geraldo Palmeira	
 * @since 14/07/2006
 */
public class ConsultaRelMKTOperacionalOptInAction extends ActionPortal {

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
				throws Exception 
			{
	
				ActionForward result = null;
				
				logger.info("Consulta relatório operacional de opt-in");
	
				this.codOperacao = Constantes.COD_CONSULTAR_REL_OPERACIONAL_OPT_IN;
				
				String codigosModelo[] = request.getParameterValues("checkboxModelo");
				String preferencia[]   = request.getParameterValues("checkbox");
				String cn 			   = request.getParameter("cn");
				String lac 		       = request.getParameter("lac");
				String cellId		   = request.getParameter("cellId");
				String idtCodConteudo  = request.getParameter("codConteudo");
				String tipoPesquisa	   = request.getParameter("tipoPesquisa");
				String tipoResultado   = request.getParameter("resultado");
				StringBuffer codigosModeloParam = new StringBuffer("0");
				
				if (request.getParameterValues("checkboxModelo") != null)
				{
					codigosModeloParam = new StringBuffer(codigosModelo[0]);
					int tamanho = codigosModelo.length;
					for (int i = 1; i < tamanho; i++)
					{
						codigosModeloParam.append(","+codigosModelo[i]);
					}
				}			
				
				StringBuffer PrefParam = new StringBuffer(preferencia[0]);
				int tamanho2 = preferencia.length;
				for (int i = 1; i < tamanho2; i++)
				{
					PrefParam.append(","+preferencia[i]);
				}
				

				String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTOperacionalOptInDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+CN="+cn+
							   "+MODELO="+codigosModeloParam+"+LAC="+lac+"+CELLID="+cellId+"+PREF="+PrefParam+"+IDTCODCONTEUDO="+idtCodConteudo+
							   "+TIPOPESQUISA="+tipoPesquisa;
				
				if (tipoResultado.equals("1"))
				{
					endereco = endereco.replaceFirst("OptIn","OptInListaAssinantes");
				}
								
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de Operacional Opt-in!");
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
