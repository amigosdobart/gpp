/*
 * Created on 21/02/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 * 
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Promocao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * @author 	Daniel Ferreira
 * @since 	21/02/2005
 */
public class PromocaoHome 
{
	/**  
	 *   Método estático responsável pela localização de uma Promocao, atraves de seu identificador único.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto Promocao com todas suas propriedades e funcionalidades.
	 */

	public static Promocao findByID(Database db, int idtPromocao) throws PersistenceException 
	{

			OQLQuery query = null;
			Promocao result = null;
			QueryResults rs = null;
			
			try
			{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.Promocao a "+
					"where a.idtPromocao = $1");

				query.bind(idtPromocao);
				rs = query.execute();
				
				if(rs.hasMore())
				{
					result = (Promocao) rs.next();
				}
			
			}
			finally
			{
				if(rs != null) rs.close();
				if(query != null)
				{
					query.close();	
				}	
			}
			
			
			return result;
	}

	public static Collection findAll(Database db) throws PersistenceException 
	{
			OQLQuery query = null;
			Collection result = new ArrayList();
			QueryResults rs = null;
			try
			{
				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.Promocao a order by a.idtPromocao");
				rs = query.execute();
				
				while(rs.hasMore())
				{
					result.add(rs.next());
				}
			}
			finally
			{
				if(rs != null) rs.close();
				if(query!=null) query.close();
			}
			
			return result;	
	}

	public static Collection findAllPulaPula(Database db) throws PersistenceException
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		
		try
		{
			query = db.getOQLQuery("select a " +
								   "from br.com.brasiltelecom.ppp.portal.entity.Promocao a " +
								   "where a.idtCategoria = $1 " +
								   "order by a.idtPromocao");
			
			query.bind(Constantes.ID_CATEGORIA_PULAPULA);
			
			rs = query.execute();
			
			while(rs.hasMore())
			{
				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null) rs.close();
			if(query!=null) query.close();
		}
		
		return result;
	}
	
	public static Collection findAllPulaPulaPrePago(Database db) throws PersistenceException
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		
		try
		{
			query = db.getOQLQuery("select a " +
								   "from br.com.brasiltelecom.ppp.portal.entity.Promocao a " +
								   "where a.idtCategoria = $1" +
								   "and a.idtPromocao != $2 " +
								   "order by a.idtPromocao");
			
			query.bind(Constantes.ID_CATEGORIA_PULAPULA);
			query.bind(Constantes.ID_PROMOCAO_PULAPULA_CONTROLE);
			
			rs = query.execute();
			
			while(rs.hasMore())
			{
				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null) rs.close();
			if(query!=null) query.close();
		}
		
		return result;
	}

	public static Collection findAllPulaPulaAndPendenteRecarga(Database db) throws PersistenceException
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		
		try
		{
			query = db.getOQLQuery("select a " +
								   "from br.com.brasiltelecom.ppp.portal.entity.Promocao a " +
								   "where a.idtCategoria = $1 or " +
								   "a.idtPromocao = $2 " +
								   "order by a.idtPromocao");
			
			query.bind(Constantes.ID_CATEGORIA_PULAPULA);
			query.bind(Constantes.ID_PROMOCAO_PEND_REC);
			
			rs = query.execute();
			
			while(rs.hasMore())
			{
				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null) rs.close();
			if(query!=null) query.close();
		}
		
		return result;
	}

}