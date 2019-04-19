package br.com.brasiltelecom.ppp.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Classe de consulta de status de recargas recorrentes.
 * Efetua consultas à tabela recebendo como parâmetros o
 * período e o msisdn.
 * 
 * @author 	Marcelo Alves Araujo
 * @since	12/04/2006
 *
 */
public class InterfaceRecargaRecorrenteHome
{
	/**
	 * Consulta os registros de recargas do assinante no período especificado 
	 * @param 	db			-	Conexão com o banco de dados
	 * @param 	msisdn		-	Msisdn do assinante
	 * @param 	dataInicial	-	Data inicial de consulta
	 * @param 	dataFinal	-	Data final da consulta
	 * @return	Collection	-	Conjunto de objetos InterfaceRecargaRecorrente 
	 * @throws PersistenceException	-	Erro ao executar consulta
	 */
	public static Collection findByMsisdnPeriodo (Database db, String msisdn, Date dataInicial, Date dataFinal) throws PersistenceException
	{
		OQLQuery query = null;
		QueryResults rs = null;
		Collection result = new ArrayList();
		
		try
		{
			// Consulta todos os registros de um assinante em um período especificado
			String consulta = 	"select a " +
								"from br.com.brasiltelecom.ppp.portal.entity.InterfaceRecargaRecorrente a " +
								"where a.msisdn = $1 " +
								"and a.dataRecarga >= $2 " +
								"and a.dataRecarga <= $3 " +
								"order by a.dataRecarga";
			
			// Gera um OQL e anexa os parâmetros
			query = db.getOQLQuery(consulta);
			query.bind(msisdn);
			query.bind(dataInicial);
			query.bind(dataFinal);
			
			// Executa a consulta
			rs = query.execute();			
			
			// Adiciona os registros à Collection
			while(rs.hasMore())
				result.add(rs.next());
		}
		finally
		{
			// Fecha o ResultSet e o OQL
			if(rs != null)
				rs.close();
			if(query!=null)
				query.close();
		}
		
		return result;
	}
	
	/**
	 * Consulta a filial do assinante 
	 * @param 	db		-	Conexão com o banco de dados
	 * @param 	msisdn	-	Msisdn do assinante
	 * @return	String	-	Filial do assinante 
	 * @throws PersistenceException	-	Erro ao executar consulta
	 */
	public static String findFilial (Database db, String msisdn) throws PersistenceException
	{
		OQLQuery query = null;
		QueryResults rs = null;
		String result = null;
		
		try
		{
			// Consulta a filial do assinante
			String consulta = 	"select a.filial " +
								"from br.com.brasiltelecom.ppp.portal.entity.InterfaceRecargaRecorrente a " +
								"where a.msisdn = $1 " +
								"and a.dataRecarga >= $2";
			
			// Gera um OQL e anexa os parâmetros
			query = db.getOQLQuery(consulta);
			query.bind(msisdn);
			query.bind(findUltimaData(db,msisdn));
			
			// Executa a consulta
			rs = query.execute();			
			
			// Atribui a filial
			if(rs.hasMore())
				result = (String)rs.next();
		}
		finally
		{
			// Fecha o ResultSet e o OQL
			if(rs != null)
				rs.close();
			if(query!=null)
				query.close();
		}
		
		return result;
	}
	
	/**
	 * Consulta o número do contrato do assinante  
	 * @param 	db		-	Conexão com o banco de dados
	 * @param 	msisdn	-	Msisdn do assinante
	 * @return	long	-	Número do contrato 
	 * @throws PersistenceException	-	Erro ao executar consulta
	 */
	public static long findContrato (Database db, String msisdn) throws PersistenceException
	{
		OQLQuery query = null;
		QueryResults rs = null;
		long result = 0;
		
		try
		{
			// Consulta o número do contrato associado ao assinante
			String consulta = 	"select a.contrato " +
								"from br.com.brasiltelecom.ppp.portal.entity.InterfaceRecargaRecorrente a " +
								"where a.msisdn = $1 " +
								"and a.dataRecarga >= $2";
			
			// Gera um OQL e anexa os parâmetros
			query = db.getOQLQuery(consulta);
			query.bind(msisdn);
			query.bind(findUltimaData(db,msisdn));
			
			// Executa a consulta
			rs = query.execute();			
			
			// Atribui o número do contrato
			if(rs.hasMore())
				result = ((BigDecimal)rs.next()).longValue();
		}
		finally
		{
			// Fecha o ResultSet e o OQL
			if(rs != null)
				rs.close();
			if(query!=null)
				query.close();
		}
		
		return result;
	}

	/**
	 * Consulta a data do último registro do assinante  
	 * @param 	db		-	Conexão com o banco de dados
	 * @param 	msisdn	-	Msisdn do assinante
	 * @return	Date	-	Date da última recarga
	 * @throws PersistenceException	-	Erro ao executar consulta
	 */
	public static Date findUltimaData (Database db, String msisdn) throws PersistenceException
	{
		OQLQuery query = null;
		QueryResults rs = null;
		Date result = null;
		
		try
		{
			// Consulta o número do contrato associado ao assinante
			String consulta = 	"select max(a.dataRecarga) " +
								"from br.com.brasiltelecom.ppp.portal.entity.InterfaceRecargaRecorrente a " +
								"where a.msisdn = $1";
			
			// Gera um OQL e anexa os parâmetros
			query = db.getOQLQuery(consulta);
			query.bind(msisdn);
			
			// Executa a consulta
			rs = query.execute();			
			
			// Atribui o número do contrato
			if(rs.hasMore())
				result = (Date)rs.next();
		}
		finally
		{
			// Fecha o ResultSet e o OQL
			if(rs != null)
				rs.close();
			if(query!=null)
				query.close();
		}
		
		if(result == null)
			return new Date();
		return result;
	}
}
