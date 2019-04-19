package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;


/**
 * Classe responsável pelas consultas/atualizações no banco de dados 
 * relativas à tabela das Operadoras de Longa Distancia e seus CSP's
 * 
 * @author Daniel Ferreira
 * @since 10/02/2005
 */
public class OperadorasLDHome 
{
	
	/**
	 * Obtem os Codigos de Servicos de Prestadoras (CSP's)
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos OperadoraLD
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.OperadoraLD a " +
				"order by a.numCSP");

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