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

import java.util.Collection;
import java.util.Map;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;


import br.com.brasiltelecom.ppp.portal.entity.Rating;


/**
 * 
 * @author Luciano Vilela
 * @since 01/10/2004
 */
public class RatingHome 
{
	public static Collection findAll(Database db) throws PersistenceException {

			OQLQuery query = null;
			Collection result = new java.util.ArrayList();
			QueryResults rs = null;

			try{

				query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.Rating a ");
				rs = query.execute();
				
				while(rs.hasMore()){
					result.add((Rating) rs.next());
				}
			}
			finally{
				if(rs != null) rs.close();
				if(query != null){
					query.close();	
				}	
			}
			return result;
	}
	public static Collection findbyMAP(Database db, Map map) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new java.util.ArrayList();
		QueryResults rs = null;

		String tarifavel = (String)map.get("tarifavel");
		
		try{

			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.Rating a " +
					"where a.idtTarifavel= \"" + tarifavel + " \"");
			rs = query.execute();
			
			while(rs.hasMore()){
				result.add((Rating) rs.next());
			}
		}
		finally{
			if(rs != null) rs.close();
			if(query != null){
				query.close();	
			}	
		}
		return result;
}
	
}