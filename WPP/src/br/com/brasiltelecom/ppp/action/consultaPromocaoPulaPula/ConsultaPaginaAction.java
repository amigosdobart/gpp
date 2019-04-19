package br.com.brasiltelecom.ppp.action.consultaPromocaoPulaPula;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaExtratoPulaPulaGPP;
import br.com.brasiltelecom.ppp.model.RetornoExtratoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 *	Consulta pelo Extrato Pula-Pula na pagina informada pelo cliente.
 *
 *	@author	Daniel Ferreira
 *	@since	13/10/2005
 */
public class ConsultaPaginaAction extends ActionPortalHibernate
{

	private	String	codOperacao = Constantes.COD_CONSULTA_EXTRATO_PULA;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return this.codOperacao;
	}

	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, SessionFactory sessionFactory) throws Exception
	{
		ActionForward result = null;

		Session session = null;
		String msisdn			= request.getParameter("msisdn");
		try
		{
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			//Obtendo os parametros para obtencao do extrato.
			String msisdnFormatado	= request.getParameter("msisdnFormatado");
			String dataInicial		= request.getParameter("dataInicial");
			String dataFinal		= request.getParameter("dataFinal");
			String pagina			= request.getParameter("page");

			//Obtendo o servidor e a porta do GPP.
			String servidor			= (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
			String porta			= (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);

			//Obtendo o diretorio e o nome para a criacao do arquivo que ira conter o XML do comprovante.
			Collection diretorios	= (Collection)servlet.getServletContext().getAttribute(Constantes.DIRETORIO_COMPROVANTES);
			String sessionId		= request.getSession().getId();

			logger.info("Consulta ao extrato Pula-Pula solicitada: " + msisdnFormatado + " para a pagina: " + pagina);

			boolean consultaCheia = new Boolean(request.getParameter("consultaCheia")).booleanValue();

			//Executando a consulta pelo extrato.
			RetornoExtratoPulaPula extrato = ConsultaExtratoPulaPulaGPP.getExtratosPulaPulaFromFile(msisdn, dataInicial, dataFinal, servidor, porta, diretorios, sessionId, consultaCheia, session);

			//Inserindo o resultado no request
			request.setAttribute(Constantes.RESULT	, extrato);
			request.setAttribute("msisdn"			, msisdn);
			request.setAttribute("consultaCheia"	, String.valueOf(consultaCheia));
			request.setAttribute("msisdnFormatado"	, msisdnFormatado);
			request.setAttribute("dataInicial"		, dataInicial);
			request.setAttribute("dataFinal"		, dataFinal);
			request.setAttribute("page"				, pagina);

			result = actionMapping.findForward("success");
			
			if(session!=null)
			{
				session.getTransaction().commit();
			}
		}
		catch(Exception naoLanca)
		{
			logger.error("Erro na consulta do pula-pula do msisdn: " + msisdn, naoLanca);
			if(session!=null)
			{
				session.getTransaction().rollback();
			}
		}
		return result;
	}
}
