package br.com.brasiltelecom.ppp.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.OfertaPacoteDados;

/**
 * Classe responsavel por realizar as consultas referentes as
 * Ofertas de Pacote de Dados
 * 
 * @author João Paulo Galvagni
 * @since  29/08/2007
 */
public class OfertaPacoteDadosDAO
{
	/**
	 * Metodo....: findAll
	 * Descricao.: Consulta todos os dados cadastrados.
	 *   
	 * @param  session	- Sessao do Hibernate.
	 * @return List		- Colecao de <code>OfertaPacoteDados</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery("FROM " +
					  "com.brt.gpp.comum.mapeamentos.entidade.OfertaPacoteDados a " +
					  "ORDER BY a.idtOferta");
		
		return query.list();
	}
	
	/**
	 * Metodo....: findAllPacoteDados
	 * Descricao.: Consulta todos os Pacotes de Dados
	 * 
	 * @param  session	- Sessao do Hibernate
	 * @return List		- Colecao de <code>PacoteDados</code>
	 */
	public static List findAllPacoteDados(Session session)
	{
		Query query = session.createQuery(" FROM com.brt.gpp.comum.mapeamentos.entidade.PacoteDados a " +
									  	  "ORDER BY a.numDias, a.desValorPacote						  " );
		
		return query.list();
	}
	
	/**
	 * Metodo....: findByPeriodoCadastro<br>
	 * Descricao.: Consulta as Ofertas de Pacote de Dados cadastrada
	 * 			   dentro do periodo informado
	 * 
	 * @param  session			  - Sessao do Hibernate
	 * @param  dataInicioCadastro - Data inicial do cadastro
	 * @param  dataFimCadastro	  - Data final do cadastro
	 * @return List				  - Lista contendo as Ofertas do periodo
	 */
	public static List findByPeriodoCadastro(Session session, Date dataInicioCadastro, Date dataFimCadastro)
	{
		Query query = session.createQuery(" FROM com.brt.gpp.comum.mapeamentos.entidade.OfertaPacoteDados a " +
		  								  "WHERE (a.dataCadastro >= ?)									    " +
		  								  "  AND (a.dataCadastro <= ?)									    " );
		
		// Seta o Id do Pacote de Servicos para a consulta
		query.setDate(0, dataInicioCadastro);
		query.setDate(1, dataFimCadastro);
		
		return query.list();
	}
	
	/**
	 * Metodo....: incluiOferta
	 * Descricao.: Inclui a Oferta de Pacote de Dados no Banco
	 * 
	 * @param session			- Sessao do Hibernante
	 * @param ofertaPacoteDados	- Instancia de <code>OfertaPacoteDados</code>
	 */
	public static void incluiOferta(Session session, OfertaPacoteDados ofertaPacoteDados)
	{
		session.save(ofertaPacoteDados);
	}
}