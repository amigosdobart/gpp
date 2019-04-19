/*
 * Created on 02/03/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.FilaRecargas;
/**
 * Classe responsavel pelas consultas/atualizações no banco de dados relativas aos registros da Fila de Recargas
 * 
 * @author Daniel Ferreira
 * @since 02/03/2005
 */
public class FilaRecargasHome 
{
	/**  
	 *   Metodo estatico responsavel pela localizacao dos registros na fila de recargas, de acordo com os parametros 
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto FilaRecargas com todas suas propriedades e funcionalidades.
	 */

	public static FilaRecargas findByParam(Database db, String idtMsisdn, String tipTransacao, Integer idtStatusProcessamento) 
	  throws PersistenceException 
	{

			OQLQuery query = null;
			FilaRecargas result = null;
			QueryResults rs = null;
			
			try
			{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.FilaRecargas a "+
					"where a.idtMsisdn = $1 and a.tipTransacao = $2 and a.idtStatusProcessamento = $3 ");
				
				query.bind(idtMsisdn);
				query.bind(tipTransacao);
				query.bind(idtStatusProcessamento);
				rs = query.execute();
				
				if(rs.hasMore())
				{
					result = (FilaRecargas)rs.next();
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
					"br.com.brasiltelecom.ppp.portal.entity.FilaRecargas a " +
					"order by a.idtMsisdn, a.datCadastro");
				rs = query.execute();
				
				while(rs.hasMore())
				{
					result.add((FilaRecargas) rs.next());
				}
			}
			finally
			{
				if(rs != null) rs.close();
				if(query!=null)
				{
					query.close();
				}
			}
			
			return result;	
	}

}