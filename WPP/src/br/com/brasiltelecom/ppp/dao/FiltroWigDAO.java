package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.FiltroWig;

/**
 * Interface de acesso ao cadastro de FiltroWig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 12/03/2007
 */
public class FiltroWigDAO 
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>FiltroWig</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.FiltroWig a ORDER BY a.nomeClasse");	
		return query.list(); 
	}
	
	/**
	 * Consulta um FiltroWig a partir do nome da classe.
	 * 
	 * @param sessiojn 				Sessão do Hibernate.
	 * @param nome 					Nome da classe.
	 * @return 						Instancia de <code>FiltroWig</code>.
	 */
	public static FiltroWig findByNomeClasse(Session session, String nome)
	{		
		return (FiltroWig)session.get(br.com.brasiltelecom.ppp.portal.entity.FiltroWig.class, nome);
	}
}
