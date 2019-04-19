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

//import br.com.brasiltelecom.ppp.portal.entity.Promocao;
import br.com.brasiltelecom.ppp.portal.entity.TipoDocumento;
//import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * @author 	Marcos C. Magalhaes
 * @since 	10/10/2005
 */
public class TipoDocumentoHome 
{
	/**  
	 *   Método estático responsável pela localização de uma Promocao, atraves de seu identificador único.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto Promocao com todas suas propriedades e funcionalidades.
	 */

	public static TipoDocumento findByID(Database db, int idtDocumento) throws PersistenceException 
	{

			OQLQuery query = null;
			TipoDocumento result = null;
			QueryResults rs = null;
			
			try
			{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.TipoDocumento a "+
					"where a.idtDocumento = $1");

				query.bind(idtDocumento);
				rs = query.execute();
				
				if(rs.hasMore())
				{
					result = (TipoDocumento) rs.next();
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
					"br.com.brasiltelecom.ppp.portal.entity.TipoDocumento a order by a.idtDocumento");
				rs = query.execute();
				
				while(rs.hasMore())
				{
					result.add((TipoDocumento) rs.next());
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
								   "from br.com.brasiltelecom.ppp.portal.entity.TipoDocumento a " +
								   "where a.indAtivo = 1 " +
								   "order by a.tipDocumento");
			rs = query.execute();
			
			while(rs.hasMore())
			{
				result.add((TipoDocumento)rs.next());
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