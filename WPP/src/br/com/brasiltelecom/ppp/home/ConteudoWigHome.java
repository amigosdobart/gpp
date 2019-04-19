
package br.com.brasiltelecom.ppp.home;

/*
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
*/

/**
 * ------------------------------------------------------------------------
 * 
 * DEPRECATED  
 * 
 * O mapeamento de ConteudoWig foi movido para o Hibernate.
 * Utilize a classe <code>br.com.brasiltelecom.ppp.dao.ConteudoWigDAO</code>
 * 
 * Bernardo Dias, 08/03/2007
 * 
 * ------------------------------------------------------------------------
 */
public class ConteudoWigHome
{
	/**
	 * Metodo....:findAll
	 * Descricao.:Retorna todos os dados de conteudo existentes na tabela
	 * @param db
	 * @return
	 * @throws PersistenceException
	 *
	public static Collection findAll(Database db) throws PersistenceException
	{
		Logger logger = Logger.getLogger(ConteudoWigHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		Collection listaConteudo = new ArrayList();
		try
		{
			db.begin();
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.ConteudoWig a "+
								   "order by a.descricaoConteudo");
			
			rs = query.execute(Database.ReadOnly);
			while (rs.hasMore())
				listaConteudo.add((ConteudoWig)rs.next());
			
			db.commit();
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao pesquisar informacoes de ConteudoWig. Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return listaConteudo;
	}
	
	/**
	 * Metodo....:findByCodigo
	 * Descricao.:Encontra o conteudo wig desejado pelo codigo deste
	 * @param codConteudo
	 * @param db
	 * @return
	 * @throws PersistenceException
	 *
	public static ConteudoWig findByCodigo(int codConteudo, Database db, boolean readOnly) throws PersistenceException
	{
		Logger logger = Logger.getLogger(ConteudoWigHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		ConteudoWig conteudo = null;
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.ConteudoWig a "+
								   "where a.codConteudo = $1");
			
			query.bind(codConteudo);
			rs = query.execute(readOnly ? Database.ReadOnly : Database.Shared);
			if (rs.hasMore())
				conteudo = (ConteudoWig)rs.next();
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao pesquisar informacoes de ConteudoWig. Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return conteudo;
	}
	
	/**
	 * Metodo....:findByMenuOpcao
	 * Descricao.:Retorna todos os dados de conteudo existentes na tabela desde que o
	 *            wml do conteudo possua a tag <select> a ser utilizada
	 * @param db
	 * @return
	 * @throws PersistenceException
	 *
	public static Collection findByMenuOpcao(Database db) throws PersistenceException
	{
		Logger logger = Logger.getLogger(ConteudoWigHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		Collection listaConteudo = new ArrayList();
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.ConteudoWig a "+
								   "where a.menuOpcoes = $1 "+
								   "order by a.descricaoConteudo");
			query.bind(true);
			rs = query.execute(Database.ReadOnly);
			while (rs.hasMore())
				listaConteudo.add((ConteudoWig)rs.next());
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao pesquisar informacoes de ConteudoWig. Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return listaConteudo;
	}
	*/
}
