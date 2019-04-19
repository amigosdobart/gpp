package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.PromocaoStatusAssinante;

/**
 *	Classe responsavel pela obtencao de informacoes referentes a status de assinantes.
 * 
 *	@author 	Daniel Ferreira
 *	@since 		12/06/2006
 */
public class PromocaoStatusAssinanteHome 
{
    
    //Constantes.
    
	/**  
	 *	Statement OQL para obtencao de registros de status de assinantes a partir do identificador unico.
	 */
    private static final String OQL_STATUS_ID = 
        "select a " +
        "  from br.com.brasiltelecom.ppp.portal.entity.PromocaoStatusAssinante a " +
        " where a.idtStatus = $1 ";
    
	/**  
	 *	Statement OQL para obtencao de todos os registros de status de assinantes.
	 */
    private static final String OQL_STATUS = 
        "select a " +
        "  from br.com.brasiltelecom.ppp.portal.entity.PromocaoStatusAssinante a " +
        " order by a.idtStatus";
    
    //Finders.
    
	/**  
	 *	Retorna um objeto representando um registro da tabela a partir de seu identificador unico.
	 *
	 *	@param		idStatus				Identificador do status da promocao do assinante.
	 * 	@param		db						Conexao com o banco de dados.
	 *	@return								Objeto representando o registro da tabela.
	 *	@throws		PersistenceException
	 */
	public static PromocaoStatusAssinante findById(int idStatus, Database db) throws PersistenceException 
	{
	    OQLQuery query = null;
		QueryResults registros = null;
		
		try
		{
			query = db.getOQLQuery(PromocaoStatusAssinanteHome.OQL_STATUS_ID);
			query.bind(idStatus);
			registros = query.execute();
			
			if(registros.hasMore())
				return (PromocaoStatusAssinante)registros.next();
		}
		finally
		{
			if(registros != null) 
			    registros.close();
			
			if(query != null) 
			    query.close();
		}
		
		return null;	
	}

	/**  
	 *	Retorna uma colecao contendo objetos que representam todos os status de assinantes possiveis.
	 *
	 * 	@param		db						Conexao com o banco de dados.
	 *	@return								Colecao com todos os status de assinantes.
	 *	@throws		PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException 
	{
	    OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults registros = null;
		
		try
		{
			query = db.getOQLQuery(PromocaoStatusAssinanteHome.OQL_STATUS);
			registros = query.execute();
			
			while(registros.hasMore())
				result.add(registros.next());
		}
		finally
		{
			if(registros != null) 
			    registros.close();
			
			if(query != null) 
			    query.close();
		}
		
		return result;	
	}

}