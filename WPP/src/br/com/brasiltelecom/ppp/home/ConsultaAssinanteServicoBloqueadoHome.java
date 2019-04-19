/*
 * Created on 13/09/2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.AssinanteServicoBloqueado;

/**
 * @author Henrique Canto
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConsultaAssinanteServicoBloqueadoHome {
	
	/**
	 * Desc: Retorna todos os assinantes com servicos bloqueados.
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos AssinanteServicoBloqueado
	 * @throws PersistenceException
	 */
	
	public static Collection findAll(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try{
			db.begin();
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.AssinanteServicoBloqueado a " +
				"order by a.datAtualizacao");

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
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @param objeto Map contendo as informações de busca
	 * @return Coleção de objetos AssinanteServicoBloqueado
	 * @throws PersistenceException
	 */
	
public static Collection findByFilter(Database db, Map param)
    throws PersistenceException
{
    org.exolab.castor.jdo.OQLQuery query = null;
    Collection result = new ArrayList();
	QueryResults rs = null;
    try
    {
    	db.begin();
        StringBuffer filtro = new StringBuffer();
        if(param.get("datInicial") != null && !"".equals(param.get("datInicial")))
        {
            if(filtro.length() > 0)
                filtro.append(" and ");
            filtro.append(" a.datAtualizacao between to_date(\"").append(param.get("datInicial")).append("\",\"DD/MM/YYYY\") and to_date(\"").append(param.get("datFinal")).append("\",\"DD/MM/YYYY\")");
        }
        if(param.get("msisdn") != null && !"".equals(param.get("msisdn")))
        {
            if(filtro.length() > 0)
                filtro.append(" and ");
            filtro.append(" a.idMsisdn = \"").append(param.get("msisdn")+"\" ");
        }
        StringBuffer consulta = new StringBuffer("select a from ");
        consulta.append(" br.com.brasiltelecom.ppp.portal.entity.AssinanteServicoBloqueado a ");
        if(filtro.length() > 0)
            consulta.append(" where ").append(filtro.toString());
        query = db.getOQLQuery(consulta.toString() + " order by a.datAtualizacao ");
        //for(QueryResults rs = query.execute(); rs.hasMore(); result.add((AssinanteServicoBloqueado)rs.next()));
        rs = query.execute();
        while(rs.hasMore())
        	result.add((AssinanteServicoBloqueado)rs.next());
        	
    }
    finally
    {
		if(rs != null){
			rs.close();
		}

        if(query != null)
            query.close();
    }
    return result;
}
}
