package br.com.brasiltelecom.ppp.home;

import java.util.Map;
import java.util.TreeMap;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.FabricaRelatorio;

/**
 * 
 *
 * Classe com m�todos est�ticos para recupera��o dos relatorios da Fabrica de Relatorios do GPP.
 * 
 * @author Bernardo Vergne Dias
 * @since 07/12/2006
 */
public class FabricaRelatorioHome 
{
	
	/**  
	 *   M�todo est�tico respons�vel pela recupera��o de todas os relatorios cadastrados.
	 * 	 @param db Conex�o com o Banco de Dados.
	 *   @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Map com todos os relatorios
	 */
	
	public static Map findAll(Database db) throws PersistenceException 
	{
	
			OQLQuery query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.FabricaRelatorio a" +
				" order by a.nome ");
	
			QueryResults rs = query.execute();
	
			Map result = new TreeMap();
	
			while(rs.hasMore())
			{
	
				FabricaRelatorio rel = (FabricaRelatorio) rs.next();
				result.put(rel.getNome() + " " + rel.getPasta(), rel);		
	
			}
	
			if(rs != null) rs.close();
			query.close();
			return result; 
	}
	
	/**  
	 *   M�todo est�tico respons�vel pela recupera��o de um relatorio, a partir de seu nome.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @param nome Nome do relatorio.
	 *   @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Objeto com os dados do relatorio.
	 */

	public static FabricaRelatorio findByNome(Database db, String nome) throws PersistenceException 
	{
	
			OQLQuery query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.FabricaRelatorio a "+
				" where a.nome = $1");
	
			query.bind(nome);
	
			QueryResults rs = query.execute();
	
			FabricaRelatorio result = null;
	
			if(rs.hasMore())
			{
	
				result = (FabricaRelatorio) rs.next();
	
			}
			if(rs != null) rs.close();
			if(query!=null)
			{

				query.close();
			}
			return result;
	}

}
