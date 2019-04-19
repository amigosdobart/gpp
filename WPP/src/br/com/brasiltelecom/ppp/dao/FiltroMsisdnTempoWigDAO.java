package br.com.brasiltelecom.ppp.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import br.com.brasiltelecom.ppp.portal.entity.FiltroMsisdnTempoWig;

/**
 * Interface de acesso ao cadastro do Filtro de MSISDN/TEMPO
 * 
 * @author João Paulo Galvagni
 * @since  06/09/2007
 */
public class FiltroMsisdnTempoWigDAO
{
	/**
	 * Metodo....: findByMsisdnResposta
	 * Descricao.: Consulta o registro do assinante na tabela
	 * 			   do Filtro de MSISDN/Tempo
	 * 
	 * @param  session		- Sessao do Hibernate
	 * @param  msisdn		- Numero do assinante
	 * @param  codResposta	- Codigo de resposta a ser procurada
	 * @return filtro		- Entidade preenchida, caso o assinante exista
	 */
	public static FiltroMsisdnTempoWig findByMsisdnResposta(Session session, String msisdn, int codResposta)
	{
		Query query = session.createQuery(" FROM br.com.brasiltelecom.ppp.portal.entity.FiltroMsisdnTempoWig a " +
										  "WHERE (a.codResposta = ?) 										   " +
										  "  AND (a.msisdn 	    = ?) 										   " );
		
		query.setInteger(0, codResposta);
		query.setString (1, msisdn);
		
		return (FiltroMsisdnTempoWig)query.uniqueResult();
	}
	
	/**
	 * Metodo....: incluiFiltroMsisdnTempoWig
	 * Descricao.: Realiza a inclusao no Banco de Dados das
	 * 			   informacoes do Assinante/Filtro
	 * 
	 * @param session	- Sessao do Hibernate
	 * @param filtro	- Entidade com as informacoes do filtro
	 */
	public static void incluiFiltroMsisdnTempoWig(Session session, FiltroMsisdnTempoWig filtro)
	{
		session.save(filtro);
	}
	
	/**
	 * Metodo....: atualizaFiltroMsisdnTempoWig
	 * Descricao.: Realiza atualizacao no Banco de Dados das
	 * 			   informacoes do Assinante/Filtro
	 * 
	 * @param session	- Sessao do Hibernate
	 * @param filtro	- Entidade com as informacoes do filtro
	 */
	public static void atualizaFiltroMsisdnTempoWig(Session session, FiltroMsisdnTempoWig filtro)
	{
		session.saveOrUpdate(filtro);
	}
}