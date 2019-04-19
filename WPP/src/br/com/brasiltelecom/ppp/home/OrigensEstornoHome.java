package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.OrigemEstorno;

/**
 * 
 *
 * Classe com métodos estáticos para tratamento das Origens de Requisicao de Lotes
 * do Estorno PulaPula.
 * 
 * @author Bernardo Vergne Dias
 * @since 20/12/2006
 */
public class OrigensEstornoHome 
{
	
	/**  
	 *   Método estático responsável pela recuperação de todos os cadastros.
	 * 	 @param db Conexão com o Banco de Dados.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result ArrayList com todos os cadastros
	 */
	
	public static ArrayList findAll(Database db) throws PersistenceException 
	{
	
			OQLQuery query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.OrigemEstorno a" +
				" order by a.origem ");
			QueryResults rs = query.execute(Database.ReadOnly);
	
			ArrayList result = new ArrayList();
	
			while(rs.hasMore())
			{
				OrigemEstorno origem = (OrigemEstorno) rs.next();
				result.add(origem);		
			}
	
			if(rs != null) rs.close();
			query.close();
			return result; 
	}

	/**  
	 *   Método estático responsável pela recuperação de todos os cadastros.
	 * 	 @param db Conexão com o Banco de Dados.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result ArrayList com todos os cadastros
	 */
	public static OrigemEstorno findByOrigem(Database db, String origem) throws PersistenceException 
	{
			return (OrigemEstorno)(db.load(OrigemEstorno.class, origem));
	}
	
	/**
	 * Método estático responsável pela exclusao de origem
	 * 
	 * @param db Conexão com o Banco de Dados.
	 * @param oriem Instancia de OrigemEstorno.
	 * @throws PersistenceException  Exceção lançada para possíveis erros de consulta dos dados.
	 */
	public static void excluirOrigem(Database db, OrigemEstorno origem) throws PersistenceException 
	{	
		db.remove(origem);
	}
	
	/**
	 * Método estático responsável pelo cadastro de origem
	 * 
	 * @param db Conexão com o Banco de Dados.
	 * @param oriem Instancia de OrigemEstorno.
	 * @throws PersistenceException  Exceção lançada para possíveis erros de consulta dos dados.
	 */
	public static void cadastrarOrigem(Database db, OrigemEstorno origem) throws PersistenceException 
	{
		db.create(origem);
	}
}
