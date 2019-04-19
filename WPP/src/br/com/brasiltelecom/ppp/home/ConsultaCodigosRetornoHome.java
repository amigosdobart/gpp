/*
 * Created on 27/09/2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;

/**
 * @author Henrique Canto
 */
public class ConsultaCodigosRetornoHome {

	public static CodigosRetorno findByValor(Database db, String valor)throws PersistenceException{
		CodigosRetorno 	result 	= null;
		OQLQuery 	query 	= null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno a where vlrRetorno = \"" + valor + "\"");
			rs = query.execute();
			if (rs.hasMore())
				result = (CodigosRetorno) rs.next();
		}
		finally
		{
			if(rs != null){
				rs.close();
			}

			if (query != null)
				query.close();
		}
		return result;		
		
		
	}
}
