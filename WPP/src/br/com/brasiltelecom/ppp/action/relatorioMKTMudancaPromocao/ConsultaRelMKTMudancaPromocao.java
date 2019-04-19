package br.com.brasiltelecom.ppp.action.relatorioMKTMudancaPromocao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;

/**
 * Classe de consulta do relatorio MKTMudancaPromocao
 * 
 * @author Lucas Mind�llo de Andrade
 * @since 07/05/2008
 */
public class ConsultaRelMKTMudancaPromocao extends ActionPortalHibernate
{
	private String operacao = "CONSULT_REL_MKT_MUD_PROMO";
	
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
			throws Exception 
	{
		Logger logger = Logger.getLogger(this.getClass());
		
		logger.info("Consulta o relat�rio de Mudan�a de Promo��o.");
		
		String dataInicio = request.getParameter("dataInicial");
		String dataFinal = request.getParameter("dataFinal");
		String promocao = request.getParameter("promocao");
		String categoria = request.getParameter("categoria");

		String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRelMudancaPromocao+"
			+ "ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+"
			+ "DAT_INICIO=" + dataInicio
			+ "+DAT_FIM=" + dataFinal
			+ "+IDT_PROMOCAO=" + promocao
			+ "+IDT_CATEGORIA=" + categoria;
		
		request.setAttribute("endereco", endereco);
		return actionMapping.findForward("redirect");
	}

	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return operacao;
	}
}