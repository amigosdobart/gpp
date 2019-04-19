package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.TanNotificacao;
import com.brt.gpp.comum.mapeamentos.entidade.TanRequisicao;

/**
 * Interface de acesso ao cadastro de Categorias.
 * 
 * @author Jorge Abreu
 * Criado em: 23/10/2007
 */
public class ConsultaSMSviaTangramDAO 
{
	/**  
	 * Consulta todas as Requisicoes de SMS enviados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>TanRequisicao</code>.
	 */
	public static List findAllRequisicao(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.TanRequisicao c ORDER BY c.idRequisicao");	
		return query.list(); 
	}
	
	/**
	 * Consulta todos os possíveis código de retorno da Requisicao.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @return 						Instancia de <code>TanRetornoRequisicao</code>.
	 */
	public static List findAllRetornoRequisicao(Session session)
	{		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.TanRetornoRequisicao c ORDER BY c.codRetorno");	
		return query.list(); 
	}
	
	/**
	 * Consulta todos os possíveis status de notificacao.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @return 						Instancia de <code>TanStatusNotificacao</code>.
	 */
	public static List findAllStatusNotificacao(Session session)
	{		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.TanStatusNotificacao c ORDER BY c.idtStatus");	
		return query.list(); 
	}
	
	/**
	 * Consulta todos os Conteudos de Mensagens.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @return 						Instancia de <code>TanStatusNotificacao</code>.
	 */
	public static List findAllConteudoMensagem(Session session)
	{		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.TanConteudoMensagem c ORDER BY c.idConteudo");	
		return query.list(); 
	}
	
	/**
	 * Consulta todos os Destinos de Mensagens.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @return 						Instancia de <code>TanStatusNotificacao</code>.
	 */
	public static List findAllDestinoMensagem(Session session)
	{		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.TanDestinoMensagem c ORDER BY c.idDestinoMensagem");	
		return query.list(); 
	}
	
	/**
	 * Consulta todas as Notificacoes.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @return 						Instancia de <code>TanStatusNotificacao</code>.
	 */
	public static List findAllNotificacao(Session session)
	{		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.TanNotificacao c ORDER BY c.idNotificacao");	
		return query.list(); 
	}
	
	/**
	 * Consulta o histórico das Mensagens SMS enviadas via Tangram.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @return 						Instancia de <code>TanStatusNotificacao</code>.
	 */
	public static List findAllHistorico(Session session)
	{		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.TanNotificacao a         " +
				"WHERE                                                                " +
				"a.getDestinoMensagem().getRequisicao().getDataRequisicao() = ##      " +
				
				"and a.getDestinoMensagem().getIdtMsisdnDestino() = ##                " +
				"and a.getDestinoMensagem().getRequisicao().getIdtOrigem() = ##       " +
				"and a.getIdtStatusNotificacao() = ##                                 " +
				" ORDER BY a.idNotificacao");
		
				 
				
				
				
				
		
		return query.list(); 
	}
	
	
}