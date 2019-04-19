package br.com.brasiltelecom.ppp.action.consultaEstornoPulaPula;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaEstornoPulaPulaGPP;
import br.com.brasiltelecom.ppp.model.RetornoEstornoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 *	Consulta pelos estornos de bonus Pula-Pula por fraude na pagina informada pelo cliente.
 * 
 *	@author	Daniel Ferreira
 *	@since	07/03/2006
 */
public class ConsultaPaginaAction extends ActionPortal 
{

	private	String	codOperacao = Constantes.COD_CONSULTA_ESTORNO_PULA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception 
	{	
		ActionForward result = null;
		db.begin();

		//Obtendo os parametros para obtencao do extrato.
		String msisdn			= request.getParameter("msisdn");
		String dataInicio		= request.getParameter("dataInicio");
		String dataFim			= request.getParameter("dataFim");
		String pagina			= request.getParameter("page");
		
		//Obtendo o servidor e a porta do GPP.
		String servidor			= (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta			= (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		//Obtendo o diretorio e o nome para a criacao do arquivo que ira conter o XML do comprovante.
		Collection diretorios	= (Collection)servlet.getServletContext().getAttribute(Constantes.DIRETORIO_COMPROVANTES);
		String sessionId		= request.getSession().getId();
		
		logger.info("Consulta de estorno de bonus Pula-Pula para o MSISDN: " + msisdn + " na pagina: " + pagina);
		
		//Executando a consulta pelo extrato.
		RetornoEstornoPulaPula resultSet = ConsultaEstornoPulaPulaGPP.getEstornosPulaPulaFromFile(msisdn, dataInicio, dataFim, servidor, porta, diretorios, sessionId);
		
		//Inserindo o resultado no request
		request.setAttribute(Constantes.RESULT	, resultSet);				
		request.setAttribute("page"				, pagina);
										
		result = actionMapping.findForward("success");

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
