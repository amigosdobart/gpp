package br.com.brasiltelecom.ppp.action.relatorioAdesaoOfertaNatal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Chama a execucao do relatorio de consulta de Adesao a Oferta de Natal por Dia do portal.
 * 
 * @author Jorge Abreu
 * @since 15/12/2007
 */
public class ShowConsultaAdesaoOfertaNatalDiaAction extends ActionPortalHibernate 
{
	
	private String codOperacao = Constantes.ACAO_ADESAO_OFERTA_NATAL;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
	    logger.info("Executando a Consulta ao relatório de Adesao a Oferta de Natal por Dia.");
	
		String datInicial = request.getParameter("DATA_INICIAL");
		String datFinal   = request.getParameter("DATA_FINAL");
		String idServico  = request.getParameter("servicos");
		
		//Valida os parâmetros de DATA de entrada
		if(!datInicial.matches("^\\d{2}/\\d{2}/\\d{4}$") || !datFinal.matches("^\\d{2}/\\d{2}/\\d{4}$"))
			return actionMapping.findForward("error");
		
		String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRelAdesaoOfertaNatalDia+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DAT_INICIO="+datInicial+"+DAT_FIM="+datFinal+"+ID_SERVICO="+idServico;
		request.setAttribute("endereco",endereco);

		return actionMapping.findForward("redirect");
	}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
}
