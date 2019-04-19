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

import br.com.brasiltelecom.ppp.portal.entity.Origem;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas às origens de recarga
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class OrigemHome {
	
	/**
	 * Obtém a origem de recarga de acordo com os parâmetros informados
	 * 
	 * @param db Conexão com o banco de dados
	 * @param idCanal id do canal a se buscar
	 * @param idOrigem id de origem a se buscar
	 * @return objeto do tipo Origem
	 * @throws PersistenceException
	 */
	public static Origem findByID(Database db, String idCanal, String idOrigem) throws PersistenceException {

				OQLQuery query = null;
				Origem result = null;
				QueryResults rs = null;

				try{
				 if( (idCanal != null && ! idCanal.equals("")) && (idOrigem != null && ! idOrigem.equals("") )){ 
					 query = db.getOQLQuery("select a from "+
						"br.com.brasiltelecom.ppp.portal.entity.Origem a "+
						"where a.idCanal = " + idCanal + 
						" and a.idOrigem = " + idOrigem);

					 rs = query.execute();
					
					if(rs.hasMore()){
						Origem origem = (Origem) rs.next();
						origem.setCanal(CanalHome.findByID(db,origem.getIdCanal())); 
						result = origem;
					}
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
		
	/**
	 * Obtém as origens de recarga pelo canal
	 * 
	 * @param db Conexão com o banco de dados
	 * @param idCanal id do canal a se buscar
	 * @return Coleção de objetos do tipo Origem
	 * @throws PersistenceException
	 */
	public static Collection findByCanal(Database db, String idCanal) throws PersistenceException {

				OQLQuery query = null;
				Collection result = new ArrayList();
				QueryResults rs = null;

				try{
				 if (idCanal != null && ! idCanal.equals("")){ 
					 query = db.getOQLQuery("select a from "+
						"br.com.brasiltelecom.ppp.portal.entity.Origem a "+
						"where a.idCanal = $1 " +
						"order by a.descOrigem");
					 
					 query.bind(idCanal);
					 rs = query.execute();
					
					while(rs.hasMore()){
						Origem origem = (Origem) rs.next();
						origem.setCanal(CanalHome.findByID(db,origem.getIdCanal())); 
						result.add(origem);
					}
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
