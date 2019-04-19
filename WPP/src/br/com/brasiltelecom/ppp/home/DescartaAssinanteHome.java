package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.DescartaAssinante;


/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas à tabela de
 * assinantes descartados (blacklist)
 * 
 * @author Geraldo Palmeira
 * @since 03/01/2006
 */
public class DescartaAssinanteHome 
{
	
	/**
	 * Obtem os Assinantes descartados e Mascaras
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos DescartaAssinante
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.DescartaAssinante a");

			rs = query.execute(Database.ReadOnly);
	
			while(rs.hasMore())
			{

				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null)
			{
				rs.close();
			}
			if(query!=null)
			{
				query.close();
			}
		}
		return result;
	}
	
	/**
	 * Obtem o Assinante descartado 
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Objeta assinanteDescartado
	 * @throws PersistenceException
	 */
	public static DescartaAssinante findByMsisdn(Database db, String msisdn) throws PersistenceException 
	{
		OQLQuery query = null;
		DescartaAssinante result = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.DescartaAssinante a " +
								   "where a.idtMsisdn = $1");

			query.bind(msisdn);
			rs = query.execute(Database.ReadOnly);
			if(rs.hasMore())
			{
				result = (DescartaAssinante) (rs.next());
			}
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
	 * Obtem as Máscaras descartadas
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos DescartaAssinante
	 * @throws PersistenceException
	 */
	public static Collection findByMascara(Database db, String mascara) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.DescartaAssinante a " +
								   "where a.indMascara = $1");

			query.bind(mascara);
			rs = query.execute(Database.ReadOnly);
	
			while(rs.hasMore())
			{

				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null)
			{
				rs.close();
			}
			if(query!=null)
			{
				query.close();
			}
		}
		return result;
	}
	
	/**
	 * Insere O Assinante Descartado (Black List)
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @throws PersistenceException
	 */
	public static void setDescartaAssinante(Database db, DescartaAssinante descartaAssinante) 
	throws PersistenceException 
	{
		db.create(descartaAssinante);
	}
}