/*
 * Created on 25/03/2004
 *
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Canal;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas aos tipos de canais de recarga 
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class CanalHome {
	
	/**
	 * Obtém o canal através do seu id
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @param id id do canal a ser buscado
	 * @return objeto do tipo Canal
	 * @throws PersistenceException
	 */
	public static Canal findByID(Database db, String id) throws PersistenceException {

		OQLQuery query = null;
		Canal result = null;
		QueryResults rs = null;

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.Canal a "+
				"where a.idCanal = " + id);

			rs = query.execute();
		
			if(rs.hasMore()){

				result = (Canal) rs.next();
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
	
	/**
	 * Obtém todos os canais
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos Canal
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.Canal a " +
				"order by a.descCanal");

			rs = query.execute();
	
			while(rs.hasMore()){

				result.add(rs.next());
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
