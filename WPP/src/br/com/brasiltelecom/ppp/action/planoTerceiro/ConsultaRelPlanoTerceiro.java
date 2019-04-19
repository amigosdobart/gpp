package br.com.brasiltelecom.ppp.action.planoTerceiro;

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
public class ConsultaRelPlanoTerceiro extends ActionPortalHibernate
{
	private String codOperacao = "CONS_REL_PLANO_TERCEIRO";
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

		String ddd = request.getParameter("ddd");
		
		if(ddd == null || ddd.equals(""))
			ddd = "%";
		
		String endereco = "./relatoriosWPP?NOME_RELATORIO=RelPlanoTerceiro+"
					+ "ARQUIVO_PROPRIEDADES=/relatorio/Logistica.xml+"
					+ "DDD=" + ddd;
		
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
