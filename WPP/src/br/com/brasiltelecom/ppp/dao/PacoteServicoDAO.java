package br.com.brasiltelecom.ppp.dao;

import java.util.Iterator;
import org.hibernate.Query;
import org.hibernate.Session;
import br.com.brasiltelecom.ppp.portal.entity.FiltroMsisdnTempoWig;
import com.brt.gpp.comum.mapeamentos.entidade.PacoteDados;

/**
 * Interface de acesso ao Pacote de Servicos
 * 
 * @author João Paulo Galvagni
 * @since  11/09/2007
 */
public class PacoteServicoDAO
{
	/**
	 * Metodo....: findById
	 * Descricao.: Realiza a consulta do Pacote de Dados atraves do ID
	 * 
	 * @param  session			- Sessao do Hibernate
	 * @param  idPacoteServico	- Id do Pacote de Servicos
	 * @return PacoteDados		- Objeto populado com as informacoes do Pacote
	 */
	public static PacoteDados findById (Session session, int idPacoteServico)
	{
		return (PacoteDados)session.load(com.brt.gpp.comum.mapeamentos.entidade.PacoteDados.class, new Integer(idPacoteServico));
	}
	
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
		
		Iterator it = query.list().iterator();
		
		if (it.hasNext())
			return (FiltroMsisdnTempoWig)it.next();
		
		return null;
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