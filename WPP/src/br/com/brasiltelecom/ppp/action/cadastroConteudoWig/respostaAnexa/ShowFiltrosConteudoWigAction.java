package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.respostaAnexa;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.ConteudoWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de cadastro das informacoes de conteudo WIG
 * 
 * @author Joao Carlos
 * @since 17/11/2005
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 09/03/2007
 */
public class ShowFiltrosConteudoWigAction extends ShowActionHibernate
{
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela()
	{
		return "filtrosConteudoWig.vm";
	}
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		int codConteudo = 0;
		
		try 
		{
			codConteudo = request.getParameter("codConteudo") != null ? Integer.parseInt(request.getParameter("codConteudo")) : 0;

			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        // Realiza a pesquisa do conteudo quando o usuario pesquisa
			// informacoes de filtro de um conteudo em especifico.
			// Caso o parametro esteja vazio entao a tela serah visualizada
			// sem nenhuma informacao.
	        ConteudoWig conteudoWig = null;
			if (codConteudo > 0)
				conteudoWig = ConteudoWigDAO.findByCodigo(session, codConteudo);
			context.put("conteudoWig", conteudoWig);
	        
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao pesquisar informações de filtros. <br><br>" + e);
			logger.error("Erro ao pesquisar informações de filtros do conteúdo " + codConteudo + "." + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
