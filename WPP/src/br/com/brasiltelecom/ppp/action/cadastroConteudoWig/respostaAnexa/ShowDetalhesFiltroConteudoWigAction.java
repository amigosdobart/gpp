package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.respostaAnexa;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.ConteudoWigDAO;
import br.com.brasiltelecom.ppp.dao.FiltroConteudoWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de cadastro das informacoes de conteudo WIG
 * 
 * @author Joao Carlos
 * @since 17/11/2005
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 12/03/2007
 */
public class ShowDetalhesFiltroConteudoWigAction extends ShowActionHibernate
{
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela()
	{
		return "detalhesfiltroConteudoWig.vm";
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
		int codConteudo = request.getParameter("codConteudo") != null ? Integer.parseInt(request.getParameter("codConteudo")) : 0;
		int codResposta = request.getParameter("codResposta") != null ? Integer.parseInt(request.getParameter("codResposta")) : 0;
		
		try 
		{
			if (codConteudo > 0)
			{
				session = sessionFactory.getCurrentSession();
		        session.beginTransaction();
		        
		        // Realiza a pesquisa do conteudo sendo detalhado. O objeto ConteudoWig
				// possui as informacoes a respeito dos filtros existentes. 
				ConteudoWig conteudo = ConteudoWigDAO.findByCodigo(session, codConteudo);
				
				// Busca no ConteudoWig todos os filtros existentes para a Resposta
				// que estah sendo pesquisada pelo usuario. Para a comparacao eh utilizado
				// um objeto RespostaWig preenchido somente o codResposta, atributo utilizado
				// para a comparacao.
				RespostaWig resposta = new RespostaWig();
				resposta.setCodResposta(codResposta);
				Collection filtros = conteudo.getFiltrosPorResposta(resposta);
				
				// Para os filtros correspondentes, realiza a pesquisa dos valores
				// existentes para serem adicionados ao filtro e utilizado na tela
				// que exibirah os detalhes.
				for (Iterator i = filtros.iterator(); i.hasNext();)
				{
					FiltroConteudoWig filtroConteudoWig = (FiltroConteudoWig)i.next();
					filtroConteudoWig.addFiltroResposta(
							FiltroConteudoWigDAO.findFiltrosResposta(
									session, filtroConteudoWig.getResposta().getCodResposta(), filtroConteudoWig.getFiltro().getNomeClasse()));
				}
		        
		        session.getTransaction().commit();
		        context.put("filtrosConteudo",filtros);
			}
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao pesquisar detalhes do filtro de conteudo. <br><br>" + e);
			logger.error("Erro ao pesquisar detalhes do filtro. Conteudo:" + codConteudo + " Resposta:" + codResposta + e.getMessage());	
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
