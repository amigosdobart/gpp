/*
 * Created on 29/03/2004
 *
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Classe respons�vel pelas consultas/atualiza��es no banco de dados relativas �s origens de recarga
 * 
 * @author Andr� Gon�alves
 * @since 24/05/2004
 */
public class TipoEventoHome {
	
		
	/**
	 * Obt�m as origens de recarga pelo canal
	 * 
	 * @param db Conex�o com o banco de dados
	 * @param idCanal id do canal a se buscar
	 * @return Cole��o de objetos do tipo Origem
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

				OQLQuery query = null;
				Collection result = new ArrayList();
				QueryResults rs = null;

				try{
					 query = db.getOQLQuery("select a from "+
						"br.com.brasiltelecom.ppp.portal.entity.TipoEvento a ");

					 rs = query.execute();
					
					while(rs.hasMore()){
						result.add(rs.next());
					}
				}
				finally{
					if(rs != null) rs.close();
					if(query!=null){

						query.close();
					}
				}

				return result;
		}

}
