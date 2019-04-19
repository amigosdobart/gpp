/*
 * Created on 23/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 * Ainda pendente de ajustes
 * 
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Categoria;

/**
 * Atualizado por Lucas Mindêllo de Andrade 13/03/2008
 * 
 * @author Luciano Vilela
 * @since 01/10/2004
 */
public class CategoriaHome 
{
	/**
	 * Recupera categoria por Id.
	 * 
	 * @param db conexao a base de dados
	 * @param id id da categoria
	 * @return objeto Categoria 
	 * @throws PersistenceException
	 */
	public static com.brt.gpp.comum.mapeamentos.entidade.Categoria findByID(Database db, int id)
			throws PersistenceException
	{

		OQLQuery query = db.getOQLQuery("select a from "
				+ "com.brt.gpp.comum.mapeamentos.entidade.Categoria a "
				+ " where a.idCategoria = $1");

		query.bind(id);

		QueryResults rs = query.execute();

		com.brt.gpp.comum.mapeamentos.entidade.Categoria result = null;

		if (rs.hasMore())
		{

			result = (com.brt.gpp.comum.mapeamentos.entidade.Categoria) rs.next();

		}

		if (rs != null)
			rs.close();

		if (query != null)
		{

			query.close();
		}
		return result;
	}

	/**
	 * Recupera lista de categorias
	 * 
	 * @param db conexao a base de dados
	 * @return collection de categorias
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException
	{
		OQLQuery query = null;
		Collection result = new java.util.ArrayList();
		QueryResults rs = null;
		try
		{
			db.begin();
			query = db
					.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.Categoria a order by a.idCategoria");
			rs = query.execute(Database.ReadOnly);

			while (rs.hasMore())
				result.add((Categoria) rs.next());

			db.commit();
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
			}

			if (query != null)
			{
				query.close();
			}
		}
		return result;
	}

	/**
	 * Recupera categorias em um request
	 * 
	 * @param db conexao a base de dados
	 * @param request mapeamento dos parametros do request
	 * @return collection de categorias
	 * @throws Exception
	 */
	public static Collection getCategorias(Database db, Map request)
			throws Exception
	{

		Collection result = new ArrayList();

		for (Iterator it = request.keySet().iterator(); it.hasNext();)
		{

			String key = (String) it.next();

			if (key.startsWith("categoria_"))
			{

				int id = Integer.parseInt((String) request.get(key));
				com.brt.gpp.comum.mapeamentos.entidade.Categoria cat = CategoriaHome.findByID(db, id);
				result.add(cat);

			}
		}

		return result;
	}
}