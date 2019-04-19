package br.com.brasiltelecom.ppp.action.consultaStatusGerenteFeliz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.HappyManagerHome;
import br.com.brasiltelecom.ppp.portal.entity.HappyManager;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Efetua a consulta Gerente Feliz na tabela
 * @author	Geraldo Palmeira
 * @since	27/09/2006
 */
public class ConsultaStatusGerenteFeliz extends ActionPortal
{
	Logger logger = Logger.getLogger(this.getClass());
	
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception 
	{
		ActionForward result = null;
		
		logger.info("Consulta status Gerente Feliz");
		
		try
		{			
			// Consulta as informações de Gerente Feliz
			db.begin();
						
			// Pega o parâmetro passado pela tela do portal
			String msisdn = request.getParameter("msisdn");
			
			// Monta o objeto HappyManager
			HappyManager happyManager = HappyManagerHome.findByMsisdn(db, msisdn);
			
			// Verifica se existe registro para o msisdn pesquisado
			if (happyManager == null)
			{
				result = actionMapping.findForward("error");
				request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado para este número de acesso");
			}
			else
			{
				request.setAttribute("happyManager", happyManager);
				result = actionMapping.findForward("success");
			}
			
		} 
		catch (PersistenceException e)
		{
			logger.error("Erro ao consultar status Gerente Feliz (" +
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
		return Constantes.COD_CONSULTAR_GERENTE_FELIZ;
	}
}