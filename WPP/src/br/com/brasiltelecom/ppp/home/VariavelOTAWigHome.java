package br.com.brasiltelecom.ppp.home;

/*
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.VariavelOTAWig;
*/

/**
 * ------------------------------------------------------------------------
 * 
 * DEPRECATED  
 * 
 * O mapeamento de VariavelOTAWig foi movido para o Hibernate.
 * Utilize a classe <code>br.com.brasiltelecom.ppp.dao.VariavelOTAWigDAO</code>
 * 
 * Bernardo Dias, 08/03/2007
 * 
 * ------------------------------------------------------------------------
 */
public class VariavelOTAWigHome
{
	/**
	 * Metodo....:findAll
	 * Descricao.:Retorna todos os dados de variaveis OTA existentes na tabela
	 * @param db
	 * @return
	 * @throws PersistenceException
	 *
	public static Collection findAll(Database db) throws PersistenceException
	{
		Logger logger = Logger.getLogger(VariavelOTAWigHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		Collection listaVariaveis = new ArrayList();
		try
		{
			db.begin();
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.VariavelOTAWig a "+
								   "order by a.descricaoVariavel");
			
			rs = query.execute(Database.ReadOnly);
			while (rs.hasMore())
				listaVariaveis.add((VariavelOTAWig)rs.next());
			
			db.commit();
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao pesquisar informacoes de VariaveisOTAWig. Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return listaVariaveis;
	}
	*/
}
