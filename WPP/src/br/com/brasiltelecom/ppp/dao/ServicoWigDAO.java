package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.ServicoWig;

/**
 * Interface de acesso ao cadastro de Servi�os Wig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/04/2007
 */
public class ServicoWigDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sess�o do Hibernate.
	 * @return 						Cole��o de <code>ServicoWig</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.ServicoWig a " +
				"ORDER BY a.desServicoWig").list();

		return result; 
	}
	
	/**
	 * Consulta um ServicoWig a partir do c�digo.
	 * 
	 * @param sessiojn 				Sess�o do Hibernate.
	 * @param codigo 				C�digo da Resposta.
	 * @return 						Instancia de <code>ServicoWig</code>.
	 */
	public static ServicoWig findByCodigo(Session session, int codigo)
	{
		return (ServicoWig)session.get(br.com.brasiltelecom.ppp.portal.entity.ServicoWig.class, new Integer(codigo));
	}
	
}
