package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.Canal;

/**
 * Interface de acesso ao cadastro de Canal.
 * 
 * @author Geraldo Palmeira
 * Criado em: 19/06/2007
 * 
 * Alterado por Bernardo Dias
 * Data: 29/09/2007
 */
public class CanalDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>Canal</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.Canal a " +
				"ORDER BY a.idCanal");	
		return query.list();
	}
	
	/**
	 * Consulta uma instância de Canal a partir do idCanal
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param String 				idCanal
	 * @return 						Instância de <code>Canal</code>.
	 */
	public static Canal findByIdCanal(Session session, String idCanal)
	{
		return (Canal)session.get(com.brt.gpp.comum.mapeamentos.entidade.Canal.class, idCanal);
	}
	
	/**
	 * Inclui um canal.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param canal 			Entidade <code>Canal</code>.
	 */
	public static void incluirCanal(Session session, Canal canal) 
	{	
		session.save(canal);
	}
}