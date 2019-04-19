package br.com.brasiltelecom.ppp.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.AssinanteOfertaPacoteDados;

/**
 * Interface de acesso ao cadastro dos Assinantes relacionados
 * a uma Oferta de Pacote de Dados
 * 
 * @author João Paulo Galvagni
 * @since  06/09/2007
 */
public class AssinanteOfertaPacoteDadosDAO
{
	/**
	 * Metodo....: findById
	 * Descricao.: Realiza a consulta do Pacote de Dados atraves do ID
	 * 
	 * @param  session			- Sessao do Hibernate
	 * @param  idPacoteDados	- Id do Pacote de Servicos
	 * @return PacoteDados		- Objeto populado com as informacoes do Pacote
	 */
	public static AssinanteOfertaPacoteDados findById (Session session, int idPacoteDados)
	{
		Query query = session.createQuery(" FROM com.brt.gpp.comum.mapeamentos.entidade.AssinanteOfertaPacoteDados a " +
										  "WHERE (a.idtPacoteDados = ?)									  " );
		
		// Seta o Id do Pacote de Servicos para a consulta
		query.setInteger(0, idPacoteDados);
		
		Iterator i = query.list().iterator();
		
		if (i.hasNext())
			return (AssinanteOfertaPacoteDados)i.next();
		
		return null;
	}

	/**
	 * Consulta por Msisdn. Considera as ofertas cadastradas a partir de determinada data.
	 * 
	 * @param session				Sessao do Hibernate
	 * @param msisdn				MSISDN
	 * @param dataInicioCadastro	Data inicial de cadastro da oferta
	 * @return Lista de <code>AssinanteOfertaPacoteDados</code>
	 */
	public static List findByMsisdnDataCadastro(Session session, String msisdn, Date dataInicioCadastro)
	{
		Query query = session.createQuery(" FROM com.brt.gpp.comum.mapeamentos.entidade.AssinanteOfertaPacoteDados a " +
		  								  "WHERE (a.msisdn = ?) AND (a.ofertaPacoteDados.dataCadastro >= ?)	         " +
		  								  "ORDER BY a.ofertaPacoteDados.dataCadastro" );
		
		query.setString(0, msisdn);
		query.setDate(1, dataInicioCadastro);

		return query.list();
	}
	
	/**
	 * Metodo....: insereAssinanteOferta
	 * Descricao.: Insere o assinante no Banco de Dados
	 * 
	 * @param session			- Sessao do Hibernate
	 * @param assinantesOferta	- Lita de Assinantes <code>AssinanteOfertaPacoteDados</code>
	 */
	public static void insereAssinanteOferta(Session session, HashSet assinantesOferta)
	{
		for (Iterator i = assinantesOferta.iterator(); i.hasNext(); )
			session.save((AssinanteOfertaPacoteDados)i.next());
	}
}