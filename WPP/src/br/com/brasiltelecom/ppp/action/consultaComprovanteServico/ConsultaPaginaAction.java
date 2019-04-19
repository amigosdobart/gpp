package br.com.brasiltelecom.ppp.action.consultaComprovanteServico;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaExtratoGPP;
import br.com.brasiltelecom.ppp.model.RetornoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 *	Consulta pelo Comprovante de Servico na pagina informada pelo cliente.
 * 
 *	@author	Daniel Ferreira
 *	@since	10/08/2005
 */
public class ConsultaPaginaAction extends ActionPortal 
{

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String codOperacao = Constantes.COD_CONSULTA_COMPROVANTE;
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
		String msisdnFormatado	= request.getParameter("msisdnFormatado");
		String dataInicial		= request.getParameter("dataInicial");
		String dataFinal		= request.getParameter("dataFinal");
		
		//Obtendo o servidor e a porta do GPP.
		String servidor			= (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta			= (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		//Obtendo o diretorio e o nome para a criacao do arquivo que ira conter o XML do comprovante.
		ArrayList diretorios	= (ArrayList)servlet.getServletContext().getAttribute(Constantes.DIRETORIO_COMPROVANTES);
		String sessionId		= request.getSession().getId();
		
		logger.info("Consulta a um comprovante de servico solicitada: " + msisdn);
		
		//Executando a consulta pelo extrato.
		RetornoExtrato extrato = ConsultaExtratoGPP.getExtratosFromFile(msisdn, dataInicial, dataFinal, servidor, porta, diretorios, sessionId);
		
		//Inserindo o resultado no request
		request.setAttribute(Constantes.RESULT	, extrato);				
		request.setAttribute("msisdn"			, msisdn);
		request.setAttribute("msisdnFormatado"	, msisdnFormatado);
		request.setAttribute("dataInicial"		, request.getParameter("dataInicial"));
		request.setAttribute("dataFinal"		, request.getParameter("dataFinal"));
		request.setAttribute("page"				, request.getParameter("page"));
										
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
