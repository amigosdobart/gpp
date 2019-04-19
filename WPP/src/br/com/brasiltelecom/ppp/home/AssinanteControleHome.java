package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.AssinanteControle;

/**
 * Classe de consulta dos assinantes híbridos.
 * Consulta detalhes dos assinantes
 * 
 * @author 	Marcelo Alves Araujo
 * @since	27/07/2006
 *
 */
public class AssinanteControleHome
{
	/**
	 * Retorna o registro do assinante híbrido 
	 * @param 	db					-	Conexão com o banco de dados
	 * @param 	msisdn				-	Msisdn do assinante
	 * @return	AssinanteHibrido	-	Conjunto de objetos InterfaceRecargaRecorrente 
	 * @throws PersistenceException	-	Erro ao executar consulta
	 */
	public static AssinanteControle findByMsisdn (Database db, String msisdn) throws PersistenceException
	{
		OQLQuery query = null;
		QueryResults rs = null;
		AssinanteControle result = null;
		
		try
		{
			// Consulta todos os registros de um assinante em um período especificado
			String consulta = 	"select a " +
								"from br.com.brasiltelecom.ppp.portal.entity.AssinanteControle a " +
								"where a.msisdn = $1";
			
			// Gera um OQL e anexa os parâmetros
			query = db.getOQLQuery(consulta);
			query.bind(msisdn);
			
			// Executa a consulta
			rs = query.execute();			
			
			// Adiciona os registros à Collection
			if(rs.hasMore())
				result = (AssinanteControle)rs.next();
		}
		finally
		{
			// Fecha o ResultSet e o OQL
			if(rs != null)
				rs.close();
			if(query!=null)
				query.close();
		}
		
		return result;
	}
}
