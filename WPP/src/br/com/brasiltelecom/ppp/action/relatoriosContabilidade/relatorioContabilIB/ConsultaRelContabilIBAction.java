package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilIB;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
//import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatório contabil/recargas
 * 
 * @author Alberto Magno
 * @since 01/07/2004
 */
public class ConsultaRelContabilIBAction extends ActionPortal {

	private String codOperacao = Constantes.COD_CONSULTAR_CONTABIL_IB;
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
	
				//Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));
				//String matricula = usuario.getMatricula();
				
				ActionForward result = null;
				String endereco = null;
				
				logger.info("Consulta por relatório contabil Indice Bonificacao");
				
				String desFormat = request.getParameter("DESFORMAT");
				String periodo = request.getParameter("MES") + "/" + request.getParameter("ANO");
				String codigosNacionais = request.getParameter("codigos_nacionais");
				String preHibrido = request.getParameter("prehibrido");
				
				if (desFormat.equals("DELIMITED")) 
				{
					endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilIBDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+PERIODO=" + periodo + "+CNS=" + codigosNacionais;
				}
				else
				{
					endereco = "../reports/rwservlet?ppp+ContabilIB.rdf+"+desFormat+"+PERIODO="+periodo+"+CNS="+codigosNacionais+"+PREHIBRIDO="+preHibrido;
					
				}
				
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/Indice Bonificacao realizada com sucesso!");
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
