package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.TipoBloqueioCrm;

public class MotivoBloqueioCrmHome
{
	public static Collection findAll(Database db) throws PersistenceException
	{
	    org.exolab.castor.jdo.OQLQuery query = null;
	    Collection result = new ArrayList();
	    QueryResults rs = null;
	    
	    try
	    {
	        query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.TipoBloqueioCrm a order by a.descOperacao");
	        for(rs = query.execute(); rs.hasMore(); result.add((TipoBloqueioCrm)rs.next()));
	    }
	    finally
	    {
	    	if(rs != null) rs.close();
	        if(query != null)
	            query.close();
	    }
    	return result;
	}
	
	public static TipoBloqueioCrm findByIdOperacao(Database db, String idOperacao) throws PersistenceException
	{
		org.exolab.castor.jdo.OQLQuery query = null;
		TipoBloqueioCrm result = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.TipoBloqueioCrm a where a.idOperacao="+idOperacao);
			for( rs = query.execute(); rs.hasMore(); result = (TipoBloqueioCrm)rs.next() );
		}
		finally
		{
			if(rs != null) rs.close();
			if(query != null)
				query.close();
		}
		return result;
	}
}
