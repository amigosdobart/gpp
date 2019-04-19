/*
 * Criado em 29/06/2005
 *
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.ResultadosTesteSMS;


/**
 * Classe respons�vel pelas consultas dos resultados dos teste do servi�o de sms
 *  
 * @author Marcelo Alves Araujo
 * @since  29/06/2005
 */
public class ResultadosTesteSMSHome 
{
	
	/**
	 * Obtem todos os resultados dos testes
	 * 
	 * @param 	db Conex�o com o Banco de Dados
	 * @return 	Cole��o de objetos ResultadosTesteSMS
	 * @throws 	PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.ResultadosTesteSMS a " +
				"order by a.msisdn,a.idMensagem,a.idMensagem,a.dataProcessamento, a.idtStatusProcessamento");

			rs = query.execute();
	
			while(rs.hasMore())
				result.add(rs.next());
		}
		finally
		{
			if(rs != null) 
				rs.close();

			if(query!=null)
				query.close();
		}

		return result;
	}
	
	/**
	 * Obt�m o hist�rico dos testes realizados para determinado msisdn
	 * 
	 * @param db Conex�o com o Banco de Dados
	 * @param msisdn N�mero do assinante
	 * @return Cole��o de objetos ResultadosTesteSMS
	 * @throws PersistenceException
	 */
	public static Collection findByMsisdn(Database db, String msisdn) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.ResultadosTesteSMS a " +
				"where a.msisdn = $1 " +
				"order by a.idMensagem,a.dataProcessamento, a.idtStatusProcessamento");

			query.bind(msisdn);
			rs = query.execute();
	        
			while(rs.hasMore())
				result.add((ResultadosTesteSMS)rs.next());
		}
		finally
		{
			if(rs!=null) 
				rs.close();
			
			if(query!=null)
				query.close();
		}

		return result;
	}
}
