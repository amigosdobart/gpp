package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.MotivoContestacao;

/**
 * Interface de acesso ao cadastro de Motivos de Contestacao.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 08/02/2008
 */
public class MotivoContestacaoDAO 
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>MotivoContestacao</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.MotivoContestacao c ORDER BY c.idMotivoContestacao");	
		return query.list(); 
	}
	
	/**
	 * Consulta um MotivoContestacao a partir do ID.
	 * 
	 * @param sessiojn 				Sessão do Hibernate.
	 * @param id				    ID.
	 * @return 						Instancia de <code>MotivoContestacao</code>.
	 */
	public static MotivoContestacao findById(Session session, long id)
	{		
		return (MotivoContestacao)session.get(com.brt.gpp.comum.mapeamentos.entidade.MotivoContestacao.class, new Long(id));
	}
}