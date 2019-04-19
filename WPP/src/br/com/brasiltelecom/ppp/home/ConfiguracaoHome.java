/*
 * Created on 28/04/2004
 *
 */
package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Configuracao;

/**
 * Classe respons�vel pelas consultas/atualiza��es no banco de dados relativas �s configura��es
 * 
 * @author Andr� Gon�alves
 * @since 24/05/2004
 */
public class ConfiguracaoHome {
	
	/**
	 * Obt�m a configura��o de acordo com o par�metro passado
	 * 
	 * @param db Conex�o com o Banco de Dados
	 * @param id id da configura��o
	 * @return um objeto Configuracao
	 * @throws PersistenceException
	 */
	public static Configuracao findByID(Database db, String id) throws PersistenceException {

		OQLQuery query = null;
		Configuracao result = null;
		QueryResults rs = null;

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.Configuracao a "+
				"where a.idConfiguracao =\""+id+"\"");

			rs = query.execute();
		
			if(rs.hasMore()){

				result = (Configuracao) rs.next();
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
