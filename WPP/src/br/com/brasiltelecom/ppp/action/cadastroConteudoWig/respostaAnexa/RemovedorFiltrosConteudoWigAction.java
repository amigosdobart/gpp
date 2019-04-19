package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.respostaAnexa;

import java.util.ArrayList;
import java.util.Collection;

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
import br.com.brasiltelecom.ppp.dao.FiltroConteudoWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Joao Carlos
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 12/03/2007
 */
public class RemovedorFiltrosConteudoWigAction extends ActionPortalHibernate
{
	private String codOperacao = Constantes.COD_REMOVER_ANEXO_RESPOSTA;
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
		int codConteudo = request.getParameter("codConteudo") != null ? Integer.parseInt(request.getParameter("codConteudo")) : 0;
				
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        String flagExclusao[] = request.getParameterValues("exclusao");
			Collection filtrosARemover = new ArrayList();
			
			// Faz uma pesquisa do Conteudo atraves do codigo para identificar
			// todos os filtros existentes para este. Posteriormente serah realizado
			// uma pesquisa dos filtros por resposta (que estah sendo removida)
			// e entao adicionada em uma lista os objetos que serao efetivamente removidos
			ConteudoWig conteudo = ConteudoWigDAO.findByCodigo(session, codConteudo);
			for (int i = 0; i < flagExclusao.length; i++)
			{
				// Busca os filtros para a resposta sendo processada
				// Esta resposta que eh criada para comparacao eh preenchida
				// somente com o codigo (chave para comparacao)
				RespostaWig resposta = new RespostaWig();
				resposta.setCodResposta(Integer.parseInt(flagExclusao[i]));
				filtrosARemover.addAll(conteudo.getFiltrosPorResposta(resposta));
			}
			
			// Sabendo agora exatamente quais objetos devem ser removidos
			// passa esta lista como parametro para a classe Home que irah
			// implementar a remocao no banco de dados.
			FiltroConteudoWigDAO.removerFiltrosConteudo(session, filtrosARemover);	

	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Operação concluída com sucesso!");
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro tentar remover filtros do conteudo:" + codConteudo + ".<br><br>" + e);
			logger.error("Erro tentar remover filtros do conteudo:" + codConteudo + ". " + e.getMessage());		
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
