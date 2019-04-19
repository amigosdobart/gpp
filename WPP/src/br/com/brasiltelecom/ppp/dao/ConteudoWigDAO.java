package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;

/**
 * Interface de acesso ao cadastro de Conteudos Wig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 09/03/2007
 */
public class ConteudoWigDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sess�o do Hibernate.
	 * @return 						Cole��o de <code>ConteudoWig</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.ConteudoWig a " +
				"ORDER BY a.descricaoConteudo").list();

		return result; 
	}
	
	/**  
	 * Captura o c�digo para uma nova inser��o de registro (sequence manual)
	 *   
	 * @param session 				Sess�o do Hibernate.
	 * @return 						N�mero.
	 */
	public static int findNextCondigo(Session session)
	{
		List result = session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.ConteudoWig a " +
				"ORDER BY a.codConteudo DESC").list();

		if (result.size() == 0) return 1;
		else return ((ConteudoWig)(result.get(0))).getCodConteudo() + 1; 
	}
	
	/**
	 * Consulta um ConteudoWig.
	 * 
	 * @param session 				Sess�o do Hibernate.
	 * @param codConteudo			C�digo do conte�do.
	 * @return 						Instancia de <code>ConteudoWig</code>.
	 */
	public static ConteudoWig findByCodigo(Session session, int codConteudo)
	{
		return (ConteudoWig)session.get(br.com.brasiltelecom.ppp.portal.entity.ConteudoWig.class, 
				new Integer(codConteudo));
	}
	
	/**
	 * Retorna todos os dados de conteudo existentes na tabela desde que o
	 * wml do conteudo possua a tag <select> a ser utilizada
	 * 
	 * @param session 				Sess�o do Hibernate.
	 * @return 						Cole��o de <code>ConteudoWig</code>.
	 */
	public static List findByMenuOpcao(Session session)
	{
		Query query = session.createQuery(
				"FROM 	br.com.brasiltelecom.ppp.portal.entity.ConteudoWig a " +
				"WHERE 	a.menuOpcoes = ? " +
				"ORDER 	BY a.descricaoConteudo");	
		query.setBoolean(0, true);
		return query.list();
	}
	
	/**
	 * Remove um conte�do.
	 * 
	 * @param session 			Sess�o do Hibernate.
	 * @param conteudo 			Entidade <code>ConteudoWig</code>.
	 */
	public static void removerConteudo(Session session, ConteudoWig conteudo) 
	{	
		session.delete(conteudo);
	}
	
	/**
	 * Inclui um conte�do.
	 * 
	 * @param session 			Sess�o do Hibernate.
	 * @param conteudo 			Entidade <code>ConteudoWig</code>.
	 */
	public static void incluirConteudo(Session session, ConteudoWig conteudo) 
	{	
		conteudo.setCodConteudo(findNextCondigo(session));
		session.save(conteudo);
	}
}
