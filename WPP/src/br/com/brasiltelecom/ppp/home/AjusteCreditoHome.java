
package br.com.brasiltelecom.ppp.home;

import br.com.brasiltelecom.ppp.portal.entity.Canal;
import br.com.brasiltelecom.ppp.portal.entity.Origem;
import java.util.ArrayList;
import java.util.Collection;
import org.exolab.castor.jdo.*;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas aos ajustes de crédito
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class AjusteCreditoHome
{
	/**
	 * Obtém as origens dos ajustes cujo id do canal seja 'Ajustes'
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos Origem
	 * @throws PersistenceException
	 */
    public static Collection findAll(Database db)
        throws PersistenceException
    {
        org.exolab.castor.jdo.OQLQuery query = null;
        Collection result = new ArrayList();
		QueryResults rs = null;
        try
        {
            query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.Origem a where a.idCanal=\"05\" and a.tipoLancamento = \'C\' and a.disponivel = 1 order by a.descOrigem");
            rs = query.execute();
            for(rs=query.execute(); rs.hasMore(); result.add((Origem)rs.next()));
        }
        finally
        {
			if(rs != null){
				rs.close();
			}
			
            if(query != null)
                query.close();
        }
        return result;
    }
    
    public static Collection findByPerfil(Database db, boolean perfilGSM, boolean perfilLigMix)
	    throws PersistenceException
	{
	    org.exolab.castor.jdo.OQLQuery query = null;
	    Collection result = new ArrayList();
		QueryResults rs = null;
		int indLigMix = 0;
		
	    try
	    {
	    	if (perfilGSM && perfilLigMix)
	    	{
	    		indLigMix = -1;
	    	}
	    	else if(perfilLigMix)
	    	{
	    		indLigMix = 0;
	    	}
	    	else
	    	{
	    		indLigMix = 1;
	    	}
	    		
	        query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.Origem a where a.idCanal=\"05\" and a.tipoLancamento = \'C\' and a.disponivel = 1 and (" + indLigMix + "= -1 or a.indLigMix = " +indLigMix+ ") order by a.descOrigem");

	        rs = query.execute();
	        for(rs=query.execute(); rs.hasMore(); result.add((Origem)rs.next()));
	    }
	    finally
	    {
			if(rs != null){
				rs.close();
			}
			
	        if(query != null)
	            query.close();
	    }
	    return result;
	}
}