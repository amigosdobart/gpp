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

import br.com.brasiltelecom.ppp.portal.entity.MotivoEvento;

/**
 * 
 * @author 	Marcos C. Magalhaes
 * @since 	10/10/2005
 */
public class MotivoEventoHome 
{
	/**  
	 *   Método estático responsável pela localização de uma Promocao, atraves de seu identificador único.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto Promocao com todas suas propriedades e funcionalidades.
	 */

	public static MotivoEvento findByID(Database db, int idtMotivo) throws PersistenceException 
	{

			OQLQuery query = null;
			MotivoEvento result = null;
			QueryResults rs = null;
			
			try
			{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.MotivoEvento a "+
					"where a.idtMotivo = $1");

				query.bind(idtMotivo);
				rs = query.execute();
				
				if(rs.hasMore())
				{
					result = (MotivoEvento) rs.next();
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
					"br.com.brasiltelecom.ppp.portal.entity.MotivoEvento a order by a.idtMotivo");
				rs = query.execute();
				
				while(rs.hasMore())
				{
					result.add((MotivoEvento) rs.next());
				}
			}
			finally
			{
				if(rs != null) rs.close();
				if(query!=null) query.close();
			}
			
			return result;	
	}

	public static Collection findAllAtivo(Database db) throws PersistenceException
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		
		try
		{
			query = db.getOQLQuery("select a " +
								   "from br.com.brasiltelecom.ppp.portal.entity.MotivoEvento a " +
								   "where a.indDisponivel = 1 " +
								   "order by a.idtMotivo");
			rs = query.execute();
			
			while(rs.hasMore())
			{
				result.add((MotivoEvento)rs.next());
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