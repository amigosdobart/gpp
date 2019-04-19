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

import br.com.brasiltelecom.ppp.portal.entity.PromocaoDiaExecucao;


/**
 * 
 * @author 	Daniel Ferreira
 * @since 	21/02/2005
 */
public class PromocaoDiaExecucaoHome 
{
	/**  
	 *   Método estático responsável pela localização do mapeamento do dia de execucao dos registros e sua 
	 *   consequente execucao na fila de recargas, de acordo com o dia de entrada do assinante na promocao, 
	 *   atraves de seu identificador único.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto PromocaoDiaExecucao com todas suas propriedades e funcionalidades.
	 */

	public static PromocaoDiaExecucao findByID(Database db, int idtPromocao, int numDiaEntrada) 
	  throws PersistenceException 
	{

			OQLQuery query = null;
			PromocaoDiaExecucao result = null;
			QueryResults rs = null;
			
			try
			{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.PromocaoDiaExecucao a "+
					"where a.idtPromocao = $1 and a.numDiaEntrada = $2");

				query.bind(idtPromocao);
				query.bind(numDiaEntrada);
				rs = query.execute();
				
				if(rs.hasMore())
				{
					result = (PromocaoDiaExecucao) rs.next();
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
					"br.com.brasiltelecom.ppp.portal.entity.PromocaoDiaExecucao a " +
					"order by a.idtPromocao, a.numDiaEntrada");
				rs = query.execute();
				
				while(rs.hasMore())
				{
					result.add((PromocaoDiaExecucao) rs.next());
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