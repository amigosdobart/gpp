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
 * Classe responsável pelas consultas/atualizações no banco de dados relativas às configurações
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class ConfiguracaoHome {
	
	/**
	 * Obtém a configuração de acordo com o parâmetro passado
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @param id id da configuração
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
