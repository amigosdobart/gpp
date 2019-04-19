package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;


/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas à tabela dos Períodos Contábeis
 * 
 * @author Geraldo Palmeira
 * @since 12/12/2006
 */
public class PeriodoContabilHome 
{
	
	/**
	 * Obtem os Périodos Contábeis e as Datas de Fechamentos
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos CodigoNacional
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.PeriodoContabil a " +
					               "order by a.datInicioPeriodo");

			rs = query.execute(Database.ReadOnly);
	
			while(rs.hasMore())
			{

				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null)
			{
				rs.close();
			}
			if(query!=null)
			{
				query.close();
			}
		}
		return result;
	}

	public static Collection findPeriodoContabilEspecifico(Database db, int qtdMesesAtras, int qtdMesesAFrente) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try{

			query = db.getOQLQuery("select a from " +
								   "br.com.brasiltelecom.ppp.portal.entity.PeriodoContabil a " +
								   "where a.datInicioPeriodo >= $1 " +
								   "and a.datFinalPeriodo    <= $2 " +
								   "order by a.datInicioPeriodo");
			
			Calendar calendarAtras = Calendar.getInstance();
			calendarAtras.add(Calendar.MONTH, (qtdMesesAtras * -1));
			Calendar calendarAFrente = Calendar.getInstance();
			calendarAFrente.add(Calendar.MONTH, qtdMesesAFrente);
			query.bind(calendarAtras.getTime());
			query.bind(calendarAFrente.getTime());
			rs = query.execute(Database.ReadOnly);
	
			while(rs.hasMore()){

				result.add(rs.next());
			}
		}
		finally{
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