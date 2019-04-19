package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Chamadas;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas a números correlatos
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class ConsultaNumerosCorrelatosHome
{
	/**
	 * Obtém as chamadas de acordo com o parâmetro passado
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @param param objeto Map contendo as informações de busca
	 * @return Coleção de objetos Chamadas
	 * @throws PersistenceException
	 */
	public static Collection findByFilter(Database db, Map param)  throws PersistenceException
	{
		OQLQuery query =null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		
		try
		{
			StringBuffer consulta = new StringBuffer("");
			if(param.get("msisdnOrigem") != null && param.get("msisdnDestino") != null)
			{
				String msisdnOrigTemp = "55" + param.get("msisdnOrigem").toString().substring(2);
				String msisdnDestTemp = "%" + param.get("msisdnDestino").toString().substring(1);
				consulta.append("select a from br.com.brasiltelecom.ppp.portal.entity.Chamadas a where a.msisdnOrigem = ");
				consulta.append("\"" + msisdnOrigTemp + "\"");
				consulta.append(" and a.msisdnDestino like $1");
				query = db.getOQLQuery(consulta.toString());
				query.bind(msisdnDestTemp);
				for(rs = query.execute(); rs.hasMore(); result.add((Chamadas)rs.next()));
			    consulta = new StringBuffer();
				msisdnOrigTemp = "55" + param.get("msisdnDestino").toString().substring(1);
				msisdnDestTemp = "%" + param.get("msisdnOrigem").toString().substring(2);
				consulta.append("select a from br.com.brasiltelecom.ppp.portal.entity.Chamadas a where a.msisdnOrigem = ");
				consulta.append("\"" + msisdnOrigTemp + "\"");
				consulta.append(" and a.msisdnDestino like $1");
				query = db.getOQLQuery(consulta.toString());
				query.bind(msisdnDestTemp);
				for(rs = query.execute(); rs.hasMore(); result.add((Chamadas)rs.next()));
			}
	
		} catch(Exception e) {
				e.printStackTrace();
		} finally {
			if(rs != null){
				rs.close();
			}

			if(query!=null){
				query.close();
			}
		}
			return result;	
		}			    
	
}