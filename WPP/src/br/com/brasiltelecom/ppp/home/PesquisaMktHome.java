package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Classe responsavel por realizar a consulta
 * no Banco de Dados das pesquisas Mobile Mkt realizadas
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  12/01/2006
 */
public class PesquisaMktHome
{
	public static Collection findAll(Database db) throws PersistenceException
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.PesquisaMkt a " +
								   "order by a.idPesquisa");
			
			rs = query.execute(Database.ReadOnly);
			
			while(rs.hasMore())
			{
				result.add(rs.next());
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