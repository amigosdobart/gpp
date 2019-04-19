package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.respostaAnexa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
import br.com.brasiltelecom.ppp.portal.entity.FiltroConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Bernardo Vergne Dias
 * Data: 23/04/2007
 */
public class AlteraOrdemFiltrosConteudoWigAction extends ActionPortalHibernate
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
	        
	        String listaOrdem[] = request.getParameterValues("ordem");
	        String listaRespostaCod[] = request.getParameterValues("respostaCod");
	        
			Collection filtrosParaAlterar = new ArrayList();
			FiltroConteudoWig filtro;
			
			ConteudoWig conteudo = ConteudoWigDAO.findByCodigo(session, codConteudo);
			for (int i = 0; i < listaRespostaCod.length; i++)
			{
				// Busca os filtros para a resposta sendo processada
				// Esta resposta que eh criada para comparacao eh preenchida
				// somente com o codigo (chave para comparacao)				
				RespostaWig resposta = new RespostaWig();
				resposta.setCodResposta(Integer.parseInt(listaRespostaCod[i]));
				filtrosParaAlterar = conteudo.getFiltrosPorResposta(resposta);
				
				// Percorre todos os filtros fazendo a alteração de ordem
				Iterator it = filtrosParaAlterar.iterator();
				while (it.hasNext())
				{
					filtro = (FiltroConteudoWig)it.next();
					if (listaOrdem[i] != null && !listaOrdem[i].equals(""))
						filtro.setOrdem(Integer.parseInt(listaOrdem[i]));
					else
						filtro.setOrdem(0);
				}
				
			}
			
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Operação concluída com sucesso!");
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro ao atualizar a ordem dos filtros do conteudo:" + codConteudo + ".<br><br>" + e);
			logger.error("Erro ao atualizar a ordem dos filtros do conteudo:" + codConteudo + ". " + e.getMessage());		
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
