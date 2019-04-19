package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.conteudo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.ConteudoWigDAO;
import br.com.brasiltelecom.ppp.dao.RespostaWigDAO;
import br.com.brasiltelecom.ppp.dao.ServicoWigDAO;
import br.com.brasiltelecom.ppp.dao.TipoRespostaWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.entity.ServicoWig;
import br.com.brasiltelecom.ppp.portal.entity.TipoRespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Inclui um conteúdo Wig.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/04/2007
 */
public class InclusaoConteudoWigAction extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisição HTTP que originou a execução dessa ação.
	 * @param response			Resposta HTTP a ser encaminhada ao usuário.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
		ActionForward forward = actionMapping.findForward("success");
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	
	        ConteudoWig conteudoWig = new ConteudoWig();
				        
	        RespostaWig respostaWig = RespostaWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("resposta_cod")));
	        ServicoWig servicoWig = ServicoWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("servico")));
	        TipoRespostaWig tipoRespostaWig = TipoRespostaWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("tipo_resposta")));
	        
	        conteudoWig.setResposta(respostaWig);
	        conteudoWig.setServico(servicoWig);
	        conteudoWig.setTipoResposta(tipoRespostaWig);
	        if (request.getParameter("billing") != null && !(request.getParameter("billing").equals("")))
	        	conteudoWig.setBillingCode(Long.parseLong(request.getParameter("billing")));
	        else
	        	throw new Exception("Especifique um valor de Billing Code.");
	        conteudoWig.setDescricaoConteudo(request.getParameter("descricao"));
	        conteudoWig.setRegistraAcesso((request.getParameter("registra_acesso") == null) ? false : true);
	        conteudoWig.setMenuOpcoes((request.getParameter("menu_opcoes") == null) ? false : true);
	        
	        ConteudoWigDAO.incluirConteudo(session, conteudoWig);
			
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Conteúdo WIG cadastrado com sucesso!");
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro ao cadastrar Conteúdo WIG. <br><br>" + e);
			logger.error("Erro ao cadastrar Conteúdo WIG. " + e.getMessage());	
			forward = actionMapping.findForward("error");
			if (session != null) session.getTransaction().rollback();
		}
		
		return forward;
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
 
