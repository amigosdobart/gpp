package br.com.brasiltelecom.ppp.home;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import br.com.brasiltelecom.ppp.home.CodigosRetornoHome;
import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;
import br.com.brasiltelecom.ppp.portal.entity.HistoricoPulaPula;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;


/**
 * Classe responsavel pelas consultas/atualizacoes no banco de dados relativas a tabela de Historico do Pula-Pula
 * 
 * @author Daniel Ferreira
 * @since 28/03/2005
 */
public class HistoricoPulaPulaHome 
{
	
	/**
	 * Obtem o Historico de execucao do Pula-Pula para o assinante passado por parametro
	 * 
	 * @param	idtMsisdn				MSISDN do assinante
	 * @param	db 						Conexao com o Banco de Dados
	 * @return							Colecao de objetos CodigosRetorno
	 * @throws	PersistenceException
	 */
	public static Collection findByIdtMsisdn(String idtMsisdn, Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		
		//Obtendo os codigos de retorno para insercao no historico
		HashMap codigosRetorno =  CodigosRetornoHome.findAllHashMap(db);

		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.HistoricoPulaPula a " +
								   "where a.idtMsisdn = $1 order by a.datExecucao desc ");
			
			query.bind(idtMsisdn);

			rs = query.execute();
	
			while(rs.hasMore())
			{
				//Obtendo o objeto HistoricoPulaPula
				HistoricoPulaPula historico = (HistoricoPulaPula)rs.next();
				//Atribuindo a descricao do codigo de retorno
				CodigosRetorno codigoRetorno = (CodigosRetorno)codigosRetorno.get(historico.getIdtCodigoRetorno());
				historico.setDesCodigoRetorno((codigoRetorno == null) ? null : codigoRetorno.getDescRetorno());
				//Adicionando ao resultado
				result.add(historico);
			}
		}
		finally
		{
			if(rs != null) rs.close();
			if(query!=null)
			{
				query.close();
			}
		}

		return result;
	}
	
	/**
	 *	Obtem o Historico de execucao do Pula-Pula para o assinante no periodo passado por parametro 
	 *	(Strings no formato "mm/yyyy")
	 * 
	 * @param	idtMsisdn				MSISDN do assinante
	 * @param	mesInicial				Mes inicial da pesquisa
	 * @param	mesFinal				Mes final da pesquisa (inclusive)
	 * @param	db 						Conexao com o Banco de Dados
	 * @return							Colecao de objetos CodigosRetorno
	 * @throws	PersistenceException
	 */
	public static Collection findByIdtMsisdnAndMonths(String idtMsisdn, String mesInicial, String mesFinal, Database db) 
		throws PersistenceException, ParseException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		SimpleDateFormat conversorData = new SimpleDateFormat("MM/yyyy");
		Calendar calMesFinal = Calendar.getInstance();
		QueryResults rs = null;
		
		//Obtendo o mes final (nao inclusive)
		calMesFinal.setTime(conversorData.parse(mesFinal));
		calMesFinal.add(Calendar.MONTH, 1);
		
		//Obtendo os codigos de retorno para insercao no historico
		HashMap codigosRetorno =  CodigosRetornoHome.findAllHashMap(db);

		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.HistoricoPulaPula a " +
								   "where a.idtMsisdn = $1 and a.datExecucao >= $2 and a.datExecucao < $3 " +
								   "order by a.datExecucao desc ");
			
			query.bind(idtMsisdn);
			query.bind(new Date(conversorData.parse(mesInicial).getTime()));
			query.bind(new Date(calMesFinal.getTimeInMillis()));

			rs = query.execute();
	
			while(rs.hasMore())
			{
				//Obtendo o objeto HistoricoPulaPula
				HistoricoPulaPula historico = (HistoricoPulaPula)rs.next();
				//Atribuindo a descricao do codigo de retorno
				CodigosRetorno codigoRetorno = (CodigosRetorno)codigosRetorno.get(historico.getIdtCodigoRetorno());
				historico.setDesCodigoRetorno((codigoRetorno == null) ? null : codigoRetorno.getDescRetorno());
				//Adicionando ao resultado
				result.add(historico);
			}
		}
		finally
		{
			if(rs != null) rs.close();
			if(query!=null)
			{
				query.close();
			}
		}

		return result;
	}
	
}