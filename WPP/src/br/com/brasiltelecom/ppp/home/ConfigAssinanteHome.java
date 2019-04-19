package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * Classe responsável pelas consultas/atualizações no banco de dados 
 * relativas à tabela de configuração de assinante
 * 
 * @author Marcos Magalhães
 * @since 18/05/2005
 */
public class ConfigAssinanteHome {
	
	/**
	 * Obtem as configurações do assinante
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos CodigoNacional
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.ConfigAssinante a");

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

	public static Collection findAllBloqueio(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.ConfigAssinante a where a.tipo = \""+ Constantes.STATUS_SERVICO +"\"" +
				"order by a.idConfig");

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


public static Collection findAllBloqueioAss(Database db) throws PersistenceException {
// obtem todos os tipos de status de assinante
	
	
	OQLQuery query = null;
	Collection result = new ArrayList();
	QueryResults rs = null;
	try{

		query = db.getOQLQuery("select a from "+
			"br.com.brasiltelecom.ppp.portal.entity.ConfigAssinante a where a.tipo = \""+ Constantes.STATUS_ASSINANTE +"\"" +
			"order by a.idConfig");

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
