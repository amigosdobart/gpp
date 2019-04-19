package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTStatusAssinantesOptIn;

import java.util.Date;
import java.text.SimpleDateFormat;

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
 * Gera o relatório de marketing Status dos assiantes do Opt-ins
 * 
 * @author Geraldo Palmeira
 * @since 02/05/2006
 */
public class ConsultaRelMKTStatusAssinantesOptIn extends ActionPortal {

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
				
				logger.info("Consulta relatório de markting Status dos assiantes do Opt-in");
	
				this.codOperacao = Constantes.COD_CONSULTAR_STATUS_ASS_OPT_IN;
								
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
				String dataEmissao = sdf.format(new Date());
				
				String	endereco = "./relatoriosWPP?NOME_RELATORIO=MKTStatusAssinantesOptInDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+IND_ATIVO="+request.getParameter("ativo")+"+DATA_EMISSAO="+dataEmissao;
			
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de status de assinante opt-in!");
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
