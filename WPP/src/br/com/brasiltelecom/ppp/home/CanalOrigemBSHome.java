/*
 * Created on 04/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.CanalOrigemBS;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas à tabela de canal/origem de BS
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class CanalOrigemBSHome {
	
	/**
	 * Obtem os canais de origem de BS existentes
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos CanalOrigemBS
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.CanalOrigemBS a " +
				"order by a.idCanalOrigemBS");

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
	
	/**
	 * Obtém o canal de origem de BS através do parâmetro passado
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @param idCanalOrigemBS identificador do canal de origem da BS
	 * @return objeto do tipo CanalOrigemBS
	 * @throws PersistenceException
	 */
	public static CanalOrigemBS findById(Database db, String idCanalOrigemBS) throws PersistenceException {

		OQLQuery query = null;
		CanalOrigemBS canalOrigemBS = null;
		QueryResults rs = null;

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.CanalOrigemBS a " +
				"where a.idCanalOrigemBS = \"" + idCanalOrigemBS + "\"");

			rs = query.execute();
	        
			if(rs.hasMore()){
				canalOrigemBS = (CanalOrigemBS) rs.next();
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

		return canalOrigemBS;
	}

}
