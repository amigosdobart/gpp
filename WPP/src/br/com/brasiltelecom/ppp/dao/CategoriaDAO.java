package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;

/**
 * Interface de acesso ao cadastro de <code>Categoria</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 12/03/2007
 */
public class CategoriaDAO 
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>Categoria</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.Categoria c ORDER BY c.idCategoria");	
		return query.list(); 
	}
	
	/**
	 * Consulta todos os dados cadastrados ordenados 
	 * pela descricao da categoria.
	 * @param session Sessao do hibernate.
	 * @return Colecao de <code>Categoria</code>.
	 */
	public static List findAllOrderedByDesc(Session session)
	{
		Query query = session.createQuery(
			"FROM com.brt.gpp.comum.mapeamentos.entidade.Categoria c ORDER BY c.desCategoria");
		
			return query.list(); 
	}
	
	/**
	 * Consulta uma Categoria a partir do código.
	 * 
	 * @param sessiojn 				Sessão do Hibernate.
	 * @param codigo				Código da Categoria.
	 * @return 						Instancia de <code>Categoria</code>.
	 */
	public static Categoria findByCodigo(Session session, int codigo)
	{		
		return (Categoria)session.get(com.brt.gpp.comum.mapeamentos.entidade.Categoria.class, new Integer(codigo));
	}
}