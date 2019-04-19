package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.Modulacao;

/**
 * Interface de acesso ao cadastro de <code>Modulacao</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 27/06/2007
 */
public class ModulacaoDAO {
 
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>Modulacao</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.Modulacao a " +
				"ORDER BY a.desModulacao ").list();

		return result; 
	}
	
	/**
	 * Consulta uma Modulacao.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param id 					ID da Modulacao.
	 * @return 						Instancia de <code>Modulacao</code>.
	 */
	public static Modulacao findById(Session session, String id)
	{	
		return (Modulacao)session.get(com.brt.gpp.comum.mapeamentos.entidade.Modulacao.class, id);

	}
	
	/**
	 * Remove uma Modulacao.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param Modulacao 		Entidade <code>Modulacao</code>.
	 */
	public static void removerModulacao(Session session, Modulacao Modulacao) 
	{	
		session.delete(Modulacao);
	}
	
	/**
	 * Cadastra uma Modulacao
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param Modulacao		Entidade <code>Modulacao</code>.
	 */
	public static void incluirModulacao(Session session, Modulacao Modulacao)
	{
		session.save(Modulacao);
	}
}
 
