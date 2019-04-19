package br.com.brasiltelecom.ppp.action.consultaPromocaoPulaPula;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import org.apache.log4j.Logger;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.CodigosRetornoDao;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaExtratoPulaPulaGPP;
import br.com.brasiltelecom.ppp.model.RetornoExtratoPulaPula;
import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 *	Executa a consulta pelo Extrato Pula-Pula.
 *
 *	@author	Daniel Ferreira
 *	@since	10/10/2005
 */
public class ConsultaPromocaoPulaPulaAction extends ActionPortalHibernate
{
	private	String	codOperacao = Constantes.COD_CONSULTA_EXTRATO_PULA;
	private	Logger	logger = Logger.getLogger(this.getClass());

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
		try
		{
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			String	msisdn			= request.getParameter("msisdn");
			String	msisdnFormatado	= request.getParameter("obr_msisdn");
			String	tipoPeriodo		= request.getParameter("tipoPeriodo");

			String dataInicial = null;
			String dataFinal = null;

			logger.info("Consulta extrato para promoção do Pula Pula: " + msisdn);

			//Obtendo o diretorio e o nome para a criacao do arquivo que ira conter o XML do comprovante.
			Collection	diretorios	= (Collection)servlet.getServletContext().getAttribute(Constantes.DIRETORIO_COMPROVANTES);
			String		sessionId	= request.getSession().getId();
			if("M".equalsIgnoreCase(tipoPeriodo)){
				int		mes				= Integer.parseInt(request.getParameter("mes"));
				SimpleDateFormat conversorData = new SimpleDateFormat(Constantes.DATA_FORMATO);
				Calendar calMes = Calendar.getInstance();
				calMes.set(Calendar.DAY_OF_MONTH, calMes.getActualMinimum(Calendar.DAY_OF_MONTH));
				calMes.add(Calendar.MONTH, -mes);
				dataInicial = conversorData.format(calMes.getTime());
				calMes.add(Calendar.MONTH, 1);
				dataFinal = conversorData.format(calMes.getTime());
			}
			else{
				dataInicial = request.getParameter("dataInicial");
				dataFinal = request.getParameter("dataFinal");
			}
			String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
			String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);

			boolean consultaCheia = new Boolean(request.getParameter("consultaCheia")).booleanValue();

			//Busca o extrato do pula-pula (ligacoes recebidas) do GPP
			RetornoExtratoPulaPula resultSet = ConsultaExtratoPulaPulaGPP.getExtratos(msisdn, dataInicial, dataFinal, servidor, porta, diretorios, sessionId, consultaCheia, session);

			//extrato retornado com sucesso
			if(resultSet.getExtratos().size() > 0)
			{
				String mensagem = "Consulta Extrato Pula-Pula para o número de acesso '" + msisdnFormatado + "' realizada com sucesso!";

				request.setAttribute("consultaCheia"	, request.getParameter("consultaCheia"));
				request.setAttribute(Constantes.RESULT	, resultSet);
				request.setAttribute("msisdn"			, msisdn);
				request.setAttribute("msisdnFormatado"	, msisdnFormatado);
				request.setAttribute("dataInicial"		, dataInicial);
				request.setAttribute("dataFinal"		, dataFinal);
				request.setAttribute(Constantes.MENSAGEM, mensagem);

				//retorna sucesso
				result = actionMapping.findForward("success");
			}
			else
			{
			    if(Integer.parseInt(resultSet.getRetorno()) != Constantes.GPP_RET_OPERACAO_OK)
			    {
			        //Assinante nao pertence a promocao Pula-Pula.
			        CodigosRetorno retorno = CodigosRetornoDao.findByVlr(session, resultSet.getRetorno());
			        String mensagem = (retorno != null) ? retorno.getDescRetorno() : "";
			        request.setAttribute(Constantes.MENSAGEM, mensagem);
			    }
			    else
			    {
					//Extrato nao enviado.
					request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado para o número de acesso: " + request.getParameter("obr_msisdn"));
			    }
			    request.setAttribute("imagem", "img/tit_consulta_ext_pulapula.gif");
				result = actionMapping.findForward("nenhumRegistro");
			}
		}
		catch(SAXException ex)
		{
			logger.error("Erro de parse do xml do extrato do assinante.",ex);
		}
		catch(ParseException ex)
		{
			logger.error("Erro de parse de objetos.",ex);
		}
		catch(GPPTecnomenException ex)
		{
			logger.error("Problema na consulta do assinante na tecnomen.", ex);
		}
		catch(DOMException ex)
		{
			logger.error("XML de consulta do extrato mal formado.", ex);
		}
		catch(GPPInternalErrorException ex)
		{
			logger.error("Erro de conexao com o GPP.",ex);
		}
		catch(Exception ex)
		{
			logger.error("Erro inexperado.",ex);
		}
		finally
		{
			if(session != null)
			{
				session.getTransaction().rollback();
			}
		}
		return result;
	}

}
