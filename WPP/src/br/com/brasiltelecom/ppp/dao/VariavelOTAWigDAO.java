package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Interface de acesso ao cadastro de Vari�veis OTA do Wig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 08/03/2007
 */
public class VariavelOTAWigDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sess�o do Hibernate.
	 * @return 						Cole��o de <code>VariavelOTAWig</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
			"FROM br.com.brasiltelecom.ppp.portal.entity.VariavelOTAWig a ORDER BY a.descricaoVariavel");	
		return query.list(); 
	}
}
