package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;

/**
 * Interface de acesso ao cadastro de <code>CodigoNacional<code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 22/03/2007
 */
public class CodigoNacionalDAO 
{
 
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>CodigoNacional</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional a " +
				"ORDER BY a.idtCodigoNacional").list();

		return result; 
	}	 
	 
	/**
	 * Consulta um Código Nacional por ID.
	 * 
	 * @param sessiojn 				Sessão do Hibernate.
	 * @param id 					ID do código nacional.
	 * @return 						Instancia de <code>CodigoNacional</code>.
	 */
	public static CodigoNacional findById(Session session, int id)
	{		
		return (CodigoNacional)session.get(com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional.class, new Integer(id));
	}
	
	
	/**  
	 * Consulta todos os CN por região
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>CodigoNacional</code>.
	 */
	public static List findByRegiaoBrt(Session session, int idRegiaoBrt)
	{
		Query query = session.createQuery(
				" FROM com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional a " +
				" WHERE a.indRegiaoBrt = ? " +		
				" ORDER BY a.idtCodigoNacional");
		
		query.setInteger(0, idRegiaoBrt);

		return query.list(); 
	}
}
 
