package br.com.brasiltelecom.ppp.action.relatorioMKTCallMeBack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;

/**
 * Relatório do Call Me Back.
 * 
 * @author Lucas Mindêllo de Andrade Data: 11-03-2008
 */
public class ConsultaRelCallMeBack extends ActionPortalHibernate
{
	private String codOperacao = "CONSULT_REL_MKT_CALLBACK";
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse,
	 *      org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory)
	{
		logger.info("Consulta as informacoes de Call Me Back");

		String msisdn = request.getParameter("msisdn");
		String dataInicio = request.getParameter("dataInicial");
		String dataFinal = request.getParameter("dataFinal");
		boolean top50 = request.getParameter("top50") != null;

		String endereco;
		if (msisdn != null && !msisdn.equals(""))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRelCallMeBackMsisdn+"
					+ "ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+"
					+ "DAT_INICIO=" + dataInicio
					+ "+DAT_FIM=" + dataFinal
					+ "+MSISDN=" + msisdn;
		}
		else
		{
			if (top50)
			{
				endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRelCallMeBackTop50+"
					+ "ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+"
					+ "DAT_INICIO=" + dataInicio
					+ "+DAT_FIM=" + dataFinal;
			}
			else
			{
				endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRelCallMeBack+"
					+ "ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+"
					+ "DAT_INICIO=" + dataInicio
					+ "+DAT_FIM=" + dataFinal;
			}
		}

		request.setAttribute("endereco", endereco);

		return actionMapping.findForward("redirect");
	}

	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}
