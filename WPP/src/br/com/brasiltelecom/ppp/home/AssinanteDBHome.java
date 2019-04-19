/*
 * Created on 25/03/2004
 *
 */
package br.com.brasiltelecom.ppp.home;


import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.AssinanteDB;

/**
 * 
 * @author Luciano Vilela
 * @since 14/04/2005
 */
public class AssinanteDBHome {
	
	/**
	 * Obtém o canal através do seu id
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @param id id do canal a ser buscado
	 * @return objeto do tipo Canal
	 * @throws PersistenceException
	 */
	public static AssinanteDB findByID(Database db, String id) throws PersistenceException {

		OQLQuery query = null;
		AssinanteDB result = null;
		QueryResults rs = null; 

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.AssinanteDB a "+
				"where a.msisdn = \"" + id + "\"");

			
			rs = query.execute();
			if(rs.hasMore()){

				result = (AssinanteDB) rs.next();
			}
		}
		finally{
			if(rs != null){
				rs.close();
			}

			if(query!=null){

				query.close();
			}
		}

		return result;
	}
	

}
