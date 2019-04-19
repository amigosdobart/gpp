package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;

/**
 * Interface de acesso ao cadastro de Respostas Wig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 08/03/2007
 */
public class RespostaWigDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>RespostaWig</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.RespostaWig a " +
				"ORDER BY a.nomeResposta").list();

		return result; 
	}
	
	/**
	 * Consulta uma RespostaWig a partir do código.
	 * 
	 * @param sessiojn 				Sessão do Hibernate.
	 * @param codigo 				Código da Resposta.
	 * @return 						Instancia de <code>RespostaWig</code>.
	 */
	public static RespostaWig findByCodigo(Session session, int codigo)
	{
		return (RespostaWig)session.get(br.com.brasiltelecom.ppp.portal.entity.RespostaWig.class, new Integer(codigo));
	}
	
	/**
	 * Remove uma resposta.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param resposta 			Entidade <code>RespostaWig</code>.
	 */
	public static void removerResposta(Session session, RespostaWig resposta) 
	{	
		session.delete(resposta);
	}
	
	/**
	 * Inclui uma resposta.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param resposta 			Entidade <code>RespostaWig</code>.
	 */
	public static void incluirResposta(Session session, RespostaWig resposta) 
	{	
		session.save(resposta);
	}
}
