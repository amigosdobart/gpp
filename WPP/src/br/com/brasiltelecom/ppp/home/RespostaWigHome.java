package br.com.brasiltelecom.ppp.home;

/*
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
*/

/**
 * ------------------------------------------------------------------------
 * 
 * DEPRECATED  
 * 
 * O mapeamento de RespostaWig foi movido para o Hibernate.
 * Utilize a classe <code>br.com.brasiltelecom.ppp.dao.RespostaWigDAO</code>
 * 
 * Bernardo Dias, 08/03/2007
 * 
 * ------------------------------------------------------------------------
 */
public class RespostaWigHome
{
	/**
	 * Metodo....:findByCodigo
	 * Descricao.:Encontra a resposta wig desejado pelo codigo deste
	 * @param codResposta
	 * @param db
	 * @return
	 * @throws PersistenceException
	 *
	public static RespostaWig findByCodigo(int codResposta, Database db, boolean readOnly) throws PersistenceException
	{
		Logger logger = Logger.getLogger(RespostaWigHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		RespostaWig resposta = null;
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.RespostaWig a "+
								   "where a.codResposta = $1");
			
			query.bind(codResposta);
			rs = query.execute(readOnly ? Database.ReadOnly : Database.Shared);
			if (rs.hasMore())
				resposta = (RespostaWig)rs.next();
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao pesquisar informacoes de RespostaWig. Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return resposta;
	}
	*/
}
