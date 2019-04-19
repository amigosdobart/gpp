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

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.PlanoPreco;


/**
 * 
 * @author Luciano Vilela
 * @since 01/10/2004
 */
public class PlanoPrecoHome {
	/**  
	 *   Método estático responsável pela localização de um Grupo, através de seu identificador único.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto Grupo com todas suas propriedades e funcionalidades.
	 */

	public static PlanoPreco findByID(Database db, int id) throws PersistenceException {

			OQLQuery query = null;
			PlanoPreco result = null;
			QueryResults rs = null;

			try{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.PlanoPreco a "+
					" where  a.idPlano = $1");

				query.bind(id);
				rs = query.execute();
				
				if(rs.hasMore()){

					result = (PlanoPreco) rs.next();
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

	public static Collection findAll(Database db) throws PersistenceException {
			OQLQuery query =null;
			Collection result = new ArrayList();
			QueryResults rs = null;
			try{
				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.PlanoPreco a order by a.idPlano");
				rs = query.execute();
				
				while(rs.hasMore()){
					result.add((PlanoPreco) rs.next());
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