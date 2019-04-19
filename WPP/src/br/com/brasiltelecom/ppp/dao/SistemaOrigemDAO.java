package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.SistemaOrigem;


/**
 * Interface de acesso ao cadastro de Sistemas de Origens.
 * 
 * @author Geraldo Palmeira
 * Criado em: 14/06/2007
 */
public class SistemaOrigemDAO
{
	
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>SistemaOrigem</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.SistemaOrigem a " +
				"ORDER BY a.idSistemaOrigem");	
		return query.list();
	}
	
	/**
	 * Consulta um SistemaOrigem a partir do IdSistemaOrigem
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param String 				idSistemaOrigem
	 * @return 						Instancia de <code>SistemaOrigemDAO</code>.
	 */
	public static SistemaOrigem findByIdSistemaOrigem(Session session, String idSistemaOrigem)
	{
		return (SistemaOrigem)session.load(com.brt.gpp.comum.mapeamentos.entidade.SistemaOrigem.class, idSistemaOrigem);
	}
}