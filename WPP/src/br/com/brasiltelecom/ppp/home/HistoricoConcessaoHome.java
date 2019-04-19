package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.HistoricoConcessao;

/**
 * Classe de acesso aos dados de Histórico de Concessão de Bônus
 * 
 * @author Bernardo Dias
 * @since 03/05/2007
 */
public class HistoricoConcessaoHome
{
	/**
	 * Metodo....:findByAssinanteCampanha
	 * Descricao.:Realiza a pesquisa de historicos relativos à campanha e assinante informados
	 * @param msisdn - Msisdn do assinante a ser pesquisado
	 * @param idCampanha - Id a ser pesquisado
	 * @param db - Objeto de banco de dados
	 * @return Collection - Lista de objetos HistoricoConcessao 
	 * @throws Exception
	 */
	public static Collection findByAssinanteCampanha(String msisdn, long idCampanha, Database db) throws Exception
	{
		Logger logger = Logger.getLogger(HistoricoConcessaoHome.class);
		Collection historicos = new ArrayList();
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
			   					   "br.com.brasiltelecom.ppp.portal.entity.HistoricoConcessao a "+
					               "where a.msisdn = $1 and a.idCampanha = $2");
			query.bind(msisdn);
			query.bind(idCampanha);
			rs = query.execute(Database.ReadOnly);
			
			while (rs.hasMore())
				historicos.add((HistoricoConcessao)rs.next());
		}
		catch(Exception e)
		{
			logger.error("Erro ao pesquisar HistoricoConcessao para IdCampanha = "+idCampanha+" do assinante "+msisdn+". Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return historicos;
	}
}
