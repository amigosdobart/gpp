package br.com.brasiltelecom.ppp.action.promocaoLancamento;

import java.text.ParseException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.xml.sax.SAXException;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.MotivoEventoDao;
import br.com.brasiltelecom.ppp.dao.PromocaoDAO;
import br.com.brasiltelecom.ppp.dao.PromocaoStatusAssinanteDao;
import br.com.brasiltelecom.ppp.dao.TipoDocumentoDao;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaPromocaoPulaPulaGPP;
import br.com.brasiltelecom.ppp.model.RetornoPulaPula;
import br.com.brasiltelecom.ppp.portal.entity.PromocaoStatusAssinante;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;



/**
 * Classe que recupera os Usuarios de acordo com os parametros passados via filtro na WEB.
 *
 * @author Luciano Vilela
 *
 */
public class ConsultaPromocaoAssinanteAction extends ActionPortalHibernate
{

	private String codOperacao = Constantes.COD_CONSULTA_PULA_PULA;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Metodo para pegar a Operacao.
	 *
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return this.codOperacao;
	}

	/**
	 * Método principal da classe, reponsável pela recuperação dos Usuários.
	 *
	 * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param sessionFactory parâmetro do tipo Session.
	 * @throws java.lang.Exception,
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, SessionFactory sessionFactory) throws Exception
	{
		ActionForward result = null;
		String msisdn = null;
		Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));

		Session session = null;
		try
		{
			session= sessionFactory.getCurrentSession();
			session.beginTransaction();

			logger.info("Consulta por Promocao de Assinante solicitada");

			RetornoPulaPula assinante = null;
			//FilaRecargas recargaAgendada = null;
			Collection listaPromocoes = null;
			Collection listaTipoDocumento = null;
			Collection listaMotivoTrocaProm = null;

	        //String tipTransacao = Constantes.TIP_TRANSACAO_PULA_PULA;

			try
			{
				msisdn = request.getParameter("msisdn");

				// Busca informações de porta e servidor do GPP
				String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
				String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);

				// Carrega Promoção Pula Pula para Assinante.
				assinante = ConsultaPromocaoPulaPulaGPP.getPromocao(msisdn, servidor, porta, session);
				
			}
			catch (SAXException e)
			{
				logger.error("Erro na consulta do assinante.", e);
				request.setAttribute(Constantes.MENSAGEM, "Não foi possível realizar a consulta do assinante, problemas internos do portal.");
				
				return(actionMapping.findForward("error"));
			}
			catch (ParseException e)
			{
				logger.error("Erro na consulta do assinante.", e);
				request.setAttribute(Constantes.MENSAGEM, "Não foi possível realizar a consulta do assinante, problemas internos do portal.");
				
				return(actionMapping.findForward("error"));
			}
			catch (GPPInternalErrorException e)
			{
				logger.error("Erro na consulta do assinante via GPP.", e);
				request.setAttribute(Constantes.MENSAGEM, "Não foi possível realizar a consulta do assinante, erro de conexão ao GPP.");
				
				return(actionMapping.findForward("error"));
			}
			catch (Exception e)
			{
				logger.error("Erro inexperado.", e);
				request.setAttribute(Constantes.MENSAGEM, "Erro técnico do portal.");
				
				return(actionMapping.findForward("error"));
			}
			
			if(assinante.getRetorno().equals("0099"))
			{
				logger.error("Nao foi possivel realizar a consulta por usuario, problemas ao consultar promocao no GPP.");
				request.setAttribute(Constantes.MENSAGEM, "Não foi possível realizar a consulta por usuário, problemas ao consultar promocao no GPP.");
				return(actionMapping.findForward("error"));
			}

	        if((assinante.getRetorno().equals("0063")) || (assinante.getRetorno().equals("0065")))
	        {
	        	//Nao tem nada para esse assinante referente as promocoes Pula-Pula.
	    		logger.warn("Consulta Promocao Pula-Pula - Usuario nao pertence a promocao Pula-Pula.");
	    		request.setAttribute(Constantes.MENSAGEM, "Consulta Promoção Pula-Pula - Usuário nao pertence a promoção Pula-Pula.");
				return(actionMapping.findForward("error"));
			}

	        if(assinante.getRetorno().equals("0082"))
	        {
	        	//Nao tem nada para esse assinante referente as promocoes Pula-Pula.
	    		logger.warn("Consulta Promocao Pula-Pula - O bônus do assinante não é calculado pela quantidade de chamadas.");
	    		request.setAttribute(Constantes.MENSAGEM, "Consulta Promoção Pula-Pula - O bônus do assinante não é calculado pela quantidade de chamadas.");
				return(actionMapping.findForward("error"));
			}

	        //Obtendo a descricao do status da promocao do assinante
	        int idStatus = Integer.parseInt(assinante.getStatus());
	        PromocaoStatusAssinante status = PromocaoStatusAssinanteDao.findById(idStatus, session);
	        String descricaoStatus = (status != null) ? status.getDesStatus() : null;

			// Atualiza o resultado com os registros das promocoes Pula-Pula

			//Verificando se o usuario tem permissao para trocar a promocao do assinante
			boolean podeTrocarPromocao = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_SALVAR_TROCA_PULA_PULA) != null);
			//Verificando se o usuario tem permissao para trocar a isencao do limite de bonus
			boolean podeTrocarIsencaoLimite = ((usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_SALVAR_ISENCAO_LIM_PULA) != null));
			//Caso o usuario tenha permissao de trocar de promocao, obter a lista de promocoes Pula-Pula
			if(podeTrocarPromocao)
			{
				listaPromocoes = PromocaoDAO.findAllPulaPulaAndPendenteRecarga(session);
				listaTipoDocumento = TipoDocumentoDao.findAllAtivo(session);
				listaMotivoTrocaProm = MotivoEventoDao.findAllAtivo(session);
			}

			request.setAttribute("assinante"      	      , assinante);
			request.setAttribute("descricaoStatus"		  , descricaoStatus);
			request.setAttribute("podeTrocarPromocao"     , new Boolean(podeTrocarPromocao));
			request.setAttribute("podeTrocarIsencaoLimite", new Boolean(podeTrocarIsencaoLimite));
			request.setAttribute("listaPromocoes"         , listaPromocoes);
			request.setAttribute("listaTipoDocumento"     , listaTipoDocumento);
			request.setAttribute("listaMotivoTrocaProm"   , listaMotivoTrocaProm);
			request.setAttribute(Constantes.MENSAGEM      , request.getAttribute(Constantes.MENSAGEM));

			result = actionMapping.findForward("success");
		}
		catch(Exception naoLanca)
		{			
		}
		finally
		{
			if(session!=null)
				session.getTransaction().rollback();
		}
		
		return result;
	}

}
