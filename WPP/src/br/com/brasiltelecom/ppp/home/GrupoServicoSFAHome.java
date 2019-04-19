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


/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas à tabela dos grupos de servico do SFA
 * 
 * @author Alberto Magno
 * @since 30/07/2004
 */
public class GrupoServicoSFAHome {
	
	/**
	 * Obtem os grupos de servico do SFA
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos GrupoServicoSFA
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.GrupoServicoSFA a " +
				"order by a.idtClasseServico");

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
	
	/**
	 * Obtém os grupos de servico do SFA através do parâmetro passado
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @param idtClasseServico identificador da classe de servico
	 * @return objeto do tipo CanalOrigemBS
	 * @throws PersistenceException
	 */
	public static Collection findById(Database db, String idtClasseServico) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.GrupoServicoSFA a " +
				"where a.idtClasseServico = \"" + idtClasseServico + "\"");

			rs = query.execute();
	        
			while(rs.hasMore()){
				result.add(rs.next());
			}
		}
		finally{

			if(rs!=null) rs.close();
			
			if(query!=null){

				query.close();
			}
		}

		return result;
	}

}
