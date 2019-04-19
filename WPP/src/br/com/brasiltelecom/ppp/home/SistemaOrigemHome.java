/*
 * Criado em 05/07/2005
 *
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.SistemaOrigem;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas ao sistema de origem da recarga
 * 
 * @author Marcelo Alves Araujo
 * @since  05/07/2005
 */
public class SistemaOrigemHome 
{
	/**
	 * Obtém o sistema de origem de recarga de acordo com a ID do sistema
	 * 
	 * @param db Conexão com o banco de dados
	 * @param idSistemaOrigem id do sistema de origem a se buscar
	 * @return objeto do tipo SistemaOrigem
	 * @throws PersistenceException
	 */
	public static SistemaOrigem findByID(Database db, String idSistemaOrigem) throws PersistenceException 
	{
		OQLQuery query = null;
		SistemaOrigem result = null;
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery( "select a from br.com.brasiltelecom.ppp.portal.entity.SistemaOrigem a "+
									"where a.idSistemaOrigem = " + idSistemaOrigem);
			
			rs = query.execute();
			
			if(rs.hasMore())
				result = (SistemaOrigem) rs.next();
		}
		finally
		{
			if(rs != null) 
				rs.close();
			if(query!=null)
				query.close();
		}

		return result;
	}
		
	/**
	 * Obtém os sistemas de origem de recarga
	 * 
	 * @param db Conexão com o banco de dados
	 * @return Coleção de objetos do tipo SistemaOrigem
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.SistemaOrigem a ");
			
			rs = query.execute();
			
			while(rs.hasMore())
			{
				SistemaOrigem sistema = (SistemaOrigem) rs.next();
				result.add(sistema);
			}
		}
		finally
		{
			if(rs != null) 
				rs.close();
			if(query!=null)
				query.close();
		}

		return result;				
	}
}