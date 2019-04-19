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
 * Chama a execucao do relatorio de consulta de Adesao a Oferta de Natal por Msisdn do portal.
 * 
 * @author Jorge Abreu
 * @since 15/12/2007
 */
public class ShowConsultaAdesaoOfertaNatalMsisdnAction extends ActionPortalHibernate 
{
	
	private String codOperacao = Constantes.ACAO_ADES_OFERTA_NATAL_M;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
		logger.info("Executando a Consulta ao relatório de Adesao a Oferta de Natal - Detalhada por Msisdn.");

		String datInicio = request.getParameter("DATA_INICIAL");
		String datFim    = request.getParameter("DATA_FINAL");
		String promo_antiga = request.getParameter("promo_antiga");
		String promo_nova = request.getParameter("promo_nova");
		
		if(!datInicio.matches("^\\d{2}/\\d{2}/\\d{4}$") || !datFim.matches("^\\d{2}/\\d{2}/\\d{4}$"))
			return actionMapping.findForward("error");
			
		String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRelAdesaoOfertaNatalMsisdn+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DAT_INICIO="+datInicio+"+DAT_FIM="+datFim+"+PROMO_ANTIGA="+promo_antiga+"+PROMO_NOVA="+promo_nova;
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