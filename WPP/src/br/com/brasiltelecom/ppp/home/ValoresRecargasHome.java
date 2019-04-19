package br.com.brasiltelecom.ppp.home;

import java.util.Collection;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.ValoresRecargas;

/**
 * Classe responsavel pela atualizacao dos objetos relacionados aos valores de recarga
 * 
 * @author Joao Carlos
 * @since 06/03/2006
 */
public class ValoresRecargasHome
{
	public static Collection findValoresFace(Database db)
	{
		Logger logger = Logger.getLogger(ValoresRecargasHome.class);
		Collection resultado = new TreeSet();
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.ValoresRecargas a ");

			rs = query.execute();
			while (rs.hasMore())
			{
				ValoresRecargas valorRecarga = (ValoresRecargas)rs.next();
				if (valorRecarga.isValorFace())
					resultado.add(valorRecarga);
			}
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao pesquisar valores de recarga. Erro:"+e.getMessage());
		}
		finally
		{
			if(rs != null){rs.close();}
			if(query!=null){query.close();}
		}
		return resultado;
	}
}
