package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.DadosCadastraisCrm;

/**
 * Esta classe realiza a fabricacao de objetos entity DadosCadastraisCrm
 */
public class DadosCadastraisCrmHome {

	public static DadosCadastraisCrm findByAtividade(Database db, long idAtividade) throws PersistenceException
	{
		OQLQuery query = null;
		DadosCadastraisCrm result = null;
		QueryResults rs = null;

		try{
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.DadosCadastraisCrm a where idAtividade = "+idAtividade);

			rs = query.execute();
			
			if(rs.hasMore())
				result = (DadosCadastraisCrm) rs.next();
		}
		finally
		{
			if(rs != null){
				rs.close();
			}

			if(query!=null)
				query.close();
		}
		return result;
	}
}
