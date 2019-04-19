package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;


/**
 * Classe responsavel pelas consultas/atualizacoes no banco de dados relativas a tabela dos Codigos de Retorno
 * 
 * @author Daniel Ferreira
 * @since 28/03/2005
 */
public class CodigosRetornoHome 
{
	
	/**
	 * Obtem os codigos de retorno
	 * 
	 * @param db Conexao com o Banco de Dados
	 * @return Colecao de objetos CodigosRetorno
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno a " +
								   "order by a.vlrRetorno");

			rs = query.execute();
	
			while(rs.hasMore())
			{
				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null){
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
	 * Obtem os codigos de retorno
	 * 
	 * @param db Conexao com o Banco de Dados
	 * @return Mapeamento de objetos CodigosRetorno
	 * @throws PersistenceException
	 */
	public static HashMap findAllHashMap(Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		HashMap result = new HashMap();
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno a ");

			rs = query.execute();
	
			while(rs.hasMore())
			{
				CodigosRetorno codigoRetorno = (CodigosRetorno)rs.next();
				result.put(codigoRetorno.getVlrRetorno(), codigoRetorno);
			}
		}
		finally
		{
			if(rs != null){
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
	 * Obtem o codigo de retorno do identificador passado por parametro
	 * 
	 * @param	idRetorno				Identificador do Codigo de Retorno
	 * @param	db 						Conexao com o Banco de Dados
	 * @return							Colecao de objetos CodigosRetorno
	 * @throws	PersistenceException
	 */
	public static CodigosRetorno findByIdRetorno(String idRetorno, Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		CodigosRetorno result = new CodigosRetorno();
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno a " +
								   "where a.idRetorno = $1 ");
			
			query.bind(idRetorno);

			rs = query.execute();
	
			if(rs.hasMore())
			{
				result = (CodigosRetorno)rs.next();
			}
		}
		finally
		{
			if(rs != null){
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
	 * Obtem o codigo de retorno do valor passado por parametro
	 * 
	 * @param	vlrRetorno				Valor do Codigo de Retorno
	 * @param	db 						Conexao com o Banco de Dados
	 * @return							Colecao de objetos CodigosRetorno
	 * @throws	PersistenceException
	 */
	public static CodigosRetorno findByVlrRetorno(String vlrRetorno, Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		CodigosRetorno result = new CodigosRetorno();
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno a " +
								   "where a.vlrRetorno = $1 ");
			
			query.bind(vlrRetorno);

			rs = query.execute();
	
			if(rs.hasMore())
			{
				result = (CodigosRetorno)rs.next();
			}
		}
		finally
		{
			if(rs != null){
				rs.close();
			}

			if(query!=null)
			{
				query.close();
			}
		}

		return result;
	}
	
}