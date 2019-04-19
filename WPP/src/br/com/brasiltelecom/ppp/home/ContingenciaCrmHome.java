package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.BloqueioStatus;
import br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Esta classe realiza a fabricacao de objetos entity ContingenciaCrm
 */
public class ContingenciaCrmHome {

	public static ContingenciaCrm findByAtividade(Database db, long idAtividade) throws PersistenceException
	{
		OQLQuery query = null;
		ContingenciaCrm result = null;
		QueryResults rs = null;

		try{
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm a where idAtividade = "+idAtividade);

			rs = query.execute();
			if(rs.hasMore())
			{
				result = (ContingenciaCrm) rs.next();
					
				// Realiza a busca dos dados Cadastrais e do BO para serem inseridas na atividade de contingencia CRM
				result.setDadosCadastrais(DadosCadastraisCrmHome.findByAtividade(db,idAtividade));
				result.setDadosBo        (DadosBoHome.findByAtividade(db,idAtividade));
				result.setMotivoBloqueio (MotivoBloqueioCrmHome.findByIdOperacao(db,result.getIdOperacao()));
			}
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
	
	public static BloqueioStatus findByBloqueioStatusId(Database db, String id)throws PersistenceException{
		BloqueioStatus 	result 	= null;
		OQLQuery 	query 	= null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.BloqueioStatus a where idStatusBloqueio = \"" + id + "\"");
			rs = query.execute();
			if (rs.hasMore())
				result = (BloqueioStatus) rs.next();
		}
		finally
		{

			if(rs != null){
				rs.close();
			}

			if (query != null)
				query.close();
		}
		return result;		
		
		
	}
	
	public static ContingenciaCrm findAssinanteOperacao(Database db, String msisdn, String operacao)throws PersistenceException{
		ContingenciaCrm 	result 	= null;
		OQLQuery 	query 	= null;
		QueryResults rs = null;
			try
			{
				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm a where idOperacao = \""+operacao+"\" and " +
					"msisdn = \""+msisdn+"\"");
				rs = query.execute();
				if (rs.hasMore())
					result = (ContingenciaCrm) rs.next();
			}
			finally
			{
				if(rs != null){
					rs.close();
				}

				if (query != null)
					query.close();
			}
			return result;		
	}
	
	/*public static Collection findByMotivoEData(Database db, Date datInicial, Date datFinal, String idMotBloqueio, String idAtendente) throws PersistenceException
	{
		Collection 	result 	= new LinkedList();
		OQLQuery 	query 	= null;
		try
		{
			String paramBloqueio="";
			String paramAtendente="";
			
			if (idMotBloqueio != null && !idMotBloqueio.equals("todos"))
				paramBloqueio = " and idOperacao=$3";
			
			if (idAtendente != null && !idAtendente.equals(""))
				if(!paramBloqueio.equals(""))
				paramAtendente = " and idAtendente=$4";
				else
				paramAtendente = " and idAtendente=$3";
			
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm a where datAtividade between $1 and $2"+paramBloqueio+paramAtendente);

			query.bind(datInicial);
			query.bind(datFinal);
			if (idMotBloqueio != null && !idMotBloqueio.equals("todos"))
				query.bind(idMotBloqueio);
			if (idAtendente !=null && !idAtendente.equals(""))
				query.bind(idAtendente);
			QueryResults rs = query.execute();
			while (rs.hasMore())
				result.add(rs.next());
		}
		finally
		{
			if (query != null)
				query.close();
		}
		return result;
	}*/
    public static Collection findByMotivoEData(Database db, String datInicial, String datFinal, String idMotBloqueio, String idAtendente, String msisdn)
    throws PersistenceException
{
    Collection result;
    result = new LinkedList();
    OQLQuery query = null;
	QueryResults rs = null;
    try
    {
        StringBuffer filtro = new StringBuffer();
        if(idMotBloqueio != null && !idMotBloqueio.equals("todos"))
        {
            if (filtro.length() > 0) filtro.append(" and ");
            filtro.append("a.idOperacao= \"").append(idMotBloqueio).append("\"");
        }
        if(idAtendente != null && !idAtendente.equals("") )
        {
            if (filtro.length() > 0) filtro.append(" and ");
            filtro.append("a.idAtendente= \"").append(idAtendente).append("\"");
        }
        if(msisdn!=null && !msisdn.equals("") ) 
        {
            if (filtro.length() > 0) filtro.append(" and ");
            filtro.append("a.msisdn = \"55").append(msisdn + "\"");
        }
        if(datInicial != null && !datInicial.equals(""))
        {
            if (filtro.length() > 0) filtro.append(" and ");
            filtro.append(" a.datAtividade between to_date(\"").append(datInicial).append("\",\"DD/MM/YYYY hh24:mi:ss\") and to_date(\"").append(datFinal).append("\",\"DD/MM/YYYY hh24:mi:ss\")");
        }
        StringBuffer consulta = new StringBuffer("select a from ");
        consulta.append("br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm a ");
        
        if(filtro.length() > 0)
            consulta.append("where ").append(filtro.toString());
        
        consulta.append(" order by a.datAtividade");
        
        query = db.getOQLQuery(consulta.toString());
        
        for(rs = query.execute(); rs.hasMore(); result.add(rs.next()));
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
