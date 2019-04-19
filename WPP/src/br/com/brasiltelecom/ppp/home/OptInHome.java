package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.OptIn;


/**
 * Classe responsável pelas consultas/atualizações no banco de dados 
 * relativas as tabelas de opt-in
 * 
 * @author Geraldo Palmeira
 * @since 01/09/2006
 */
public class OptInHome 
{
	
	/**
	 * Obtem informçaoes de opt-in do assinante.
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos OptIn
	 * @throws PersistenceException
	 */
	public static OptIn findByMsisdn(Database db, String msisdn) throws PersistenceException {

		OQLQuery query = null;
		OptIn result = null;
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery(" select a from "+
								   " br.com.brasiltelecom.ppp.portal.entity.OptIn a " +
								   " where a.msisdn = " + msisdn );

			rs = query.execute(Database.ReadOnly);
			if(rs.hasMore())
			{
				result = (OptIn) (rs.next());
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