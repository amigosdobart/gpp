package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;


/**
 * Classe responsável pelas consultas/atualizações no banco de dados 
 * relativas as tabelas de categoria opt-in
 * 
 * @author Geraldo Palmeira
 * @since 05/09/2006
 */
public class CategoriaOptIn 
{
	
	/**
	 * Obtem Categorias de opt-in do assinante.
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos OptIn
	 * @throws PersistenceException
	 */
	public static Collection findByMsisdn(Database db, String msisdn) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery(" select a from "+
								   " br.com.brasiltelecom.ppp.portal.entity.CategoriaOptIn a " +
								   " where nu_msisdn = " + msisdn);

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