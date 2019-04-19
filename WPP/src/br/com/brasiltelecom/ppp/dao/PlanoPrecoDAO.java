package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;

/**
 * Interface de acesso ao cadastro de <code>PlanoPreco</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 24/05/2007
 */
public class PlanoPrecoDAO 
{
 
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>PlanoPreco</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco a " +
				"ORDER BY to_number(a.idtPlanoPreco) ").list();

		return result; 
	}
	
	/**  
	 * Consulta os dados cadastrados por Categoria.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @param categoria				Instancia de <code>Categoria</code>.
	 * @return 						Coleção de <code>PlanoPreco</code>.
	 */
	public static List findByCategoria(Session session, Categoria categoria)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco a " +
				"WHERE a.categoria = ? " +
				"ORDER BY to_number(a.idtPlanoPreco) ");
		query.setEntity(0, categoria);
		
		return query.list(); 
	}
	
	/**
	 * Consulta um PlanoPreco por ID.
	 * 
	 * @param sessiojn 				Sessão do Hibernate.
	 * @param id 					ID do PlanoPreco.
	 * @return 						Instancia de <codePlanoPreco</code>.
	 */
	public static PlanoPreco findById(Session session, String id)
	{		
		return (PlanoPreco)session.get(com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco.class, id);
	}
	 
	/**
	 * Remove um PlanoPreco.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param planoPreco 		Entidade <code>PlanoPreco</code>.
	 */
	public static void removerPlanoPreco(Session session, PlanoPreco planoPreco) 
	{	
		session.delete(planoPreco);
	}
	
	/**
	 * Cadastra um PlanoPreco.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param planoPreco 		Entidade <code>PlanoPreco</code>.
	 */
	public static void incluirPlanoPreco(Session session, PlanoPreco planoPreco)
	{
		session.save(planoPreco);
	}
}
 
