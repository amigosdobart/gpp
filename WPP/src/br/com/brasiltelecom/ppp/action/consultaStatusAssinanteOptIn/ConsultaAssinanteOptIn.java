package br.com.brasiltelecom.ppp.action.consultaStatusAssinanteOptIn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.OptInHome;
import br.com.brasiltelecom.ppp.portal.entity.OptIn;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Efetua a consulta de Opt-in na tabela
 * @author	Geraldo Palmeira
 * @since	04/09/2006
 */
public class ConsultaAssinanteOptIn extends ActionPortal
{
	Logger logger = Logger.getLogger(this.getClass());
	
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception 
	{
		ActionForward result = null;
		
		logger.info("Consulta status do Assinante em opt-in");
		
		try
		{			
			// Consulta as informações dos assinantes em opt-in
			db.begin();
						
			// Pega o parâmetro passado pela tela do portal
			String msisdn = request.getParameter("msisdn");
			
			// Monta o objeto opt-in
			OptIn optIn = OptInHome.findByMsisdn(db, msisdn);
			
			// Verifica se o assinante tem optIn ativo
			if (optIn == null)
			{
				result = actionMapping.findForward("error");
				request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado para este assinante");
			}
			else
			{
				request.setAttribute("optIn", optIn);
				result = actionMapping.findForward("success");
			}
			
		} 
		catch (PersistenceException e)
		{
			logger.error("Erro ao consultar status do Assinante em opt-in (" +
							e.getMessage() + ")");
			
			result = actionMapping.findForward("error");
			
			request.setAttribute(Constantes.MENSAGEM, "Erro ao acessar o Banco de Dados");
		}
		
		return result; 
	}
	
	/**
	 * Retorna o código da operação
	 * @return String
	 */
	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_ASS_OPTIN;
	}
}