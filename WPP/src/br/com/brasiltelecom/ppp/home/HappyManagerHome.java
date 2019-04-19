package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.HappyManager;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados 
 * relativas as tabelas de happy manager (Gerente Feliz)
 * 
 * @author Geraldo Palmeira
 * @since 27/09/2006
 */
public class HappyManagerHome 
{
	
	/**
	 * Obtem informçaoes do assinante.
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos Gerente Feliz
	 * @throws PersistenceException
	 */
	public static HappyManager findByMsisdn(Database db, String msisdn) throws PersistenceException {

		OQLQuery query = null;
		HappyManager result = null;
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery(" select a from "+
								   " br.com.brasiltelecom.ppp.portal.entity.HappyManager a " +
								   " where a.msisdn = " + msisdn );

			rs = query.execute(Database.ReadOnly);
			if(rs.hasMore())
			{
				result = (HappyManager) (rs.next());
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
	


	public static void setHappyManager(Database db, HappyManager happyManager) 
	throws PersistenceException 
	{
		db.create(happyManager);
	}
}