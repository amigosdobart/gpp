/*
 * Created on 28/03/2005
 */

package br.com.brasiltelecom.ppp.action.promocaoLancamento;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.home.HistoricoPulaPulaHome;

/**
 *	Gera Historico de Execucao da Promocao Pula-Pula
 * 
 *	@author	Daniel Ferreira
 *	@since	28/03/2005
 */
public class ConsultaHistoricoPulaPulaAction extends ActionPortal 
{

	private String codOperacao = Constantes.COD_CONSULTA_HIS_PULA_PULA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 *	Executa a consulta de Historico de Execucao da Promocao Pula-Pula
	 *
	 *	@see	br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
									   HttpServletResponse response, Database db)
		throws Exception 
	{
		ActionForward result = null;
		db.begin();

		String msisdn = null;
		String choice = null;
		String mesInicial = null;
		String mesFinal = null;
		Collection historico = null;

		logger.info("Consulta ao historico de execucao da Promocao Pula-Pula solicitada: " + msisdn);
		
		//Executando a consulta
		try
		{
			msisdn = request.getParameter("msisdn");
			choice = request.getParameter("choice");
			if(choice.equals("H"))
			{
				historico = HistoricoPulaPulaHome.findByIdtMsisdn(msisdn, db);
			}
			else
			{
				mesInicial = request.getParameter("mesInicial");
				mesFinal = request.getParameter("mesFinal");
				historico = HistoricoPulaPulaHome.findByIdtMsisdnAndMonths(msisdn, mesInicial, mesFinal, db);
			}
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta ao historico da Promocao Pula-Pula, problemas na conexão com o banco de dados (" +e.getMessage() + ")");
			throw e;
		}
		
		if((historico != null) && (historico.size() > 0))
		{			
			request.setAttribute(Constantes.RESULT, historico);
			request.setAttribute("page", request.getParameter("page")) ;
			request.setAttribute("obr_msisdn", request.getParameter("obr_msisdn"));
			request.setAttribute("msisdn", msisdn);
			request.setAttribute("choice", choice);
			request.setAttribute("mesInicial", mesInicial);
			request.setAttribute("mesFinal", mesFinal);
					
			result = actionMapping.findForward("success");
		}
		else 
		{
			//request.setAttribute("imagem", "img/tit_ger_comp_serv.gif");
			request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado para o numero de acesso: " + request.getParameter("obr_msisdn"));
			result = actionMapping.findForward("nenhumRegistro");	
		}
		
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
