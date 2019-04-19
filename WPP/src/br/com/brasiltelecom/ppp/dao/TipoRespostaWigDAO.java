package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.TipoRespostaWig;

/**
 * Interface de acesso ao cadastro de Tipos de Resposta Wig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/04/2007
 */
public class TipoRespostaWigDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>TipoRespostaWig</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.TipoRespostaWig a " +
				"ORDER BY a.desTipoResposta").list();

		return result; 
	}
	
	/**
	 * Consulta um TipoRespostaWig a partir do código.
	 * 
	 * @param sessiojn 				Sessão do Hibernate.
	 * @param codigo 				Código da Resposta.
	 * @return 						Instancia de <code>RespostaWig</code>.
	 */
	public static TipoRespostaWig findByCodigo(Session session, int codigo)
	{
		return (TipoRespostaWig)session.get(br.com.brasiltelecom.ppp.portal.entity.TipoRespostaWig.class, new Integer(codigo));
	}
	
}
