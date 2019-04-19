package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.Modelo;

/**
 * Interface de acesso ao cadastro de Modelos.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 08/03/2007
 */
public class ModeloDAO
{
	
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sess�o do Hibernate.
	 * @return 						Cole��o de <code>Modelo</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.Modelo a ORDER BY a.nomeModelo");	
		return query.list();
	}

	/**
	 * Consulta uma lista de Modelos a partir do trecho do nome.
	 * 
	 * @param session 				Sess�o do Hibernate.
	 * @param nome 					Nome do Modelo (ou parte do nome).
	 * @return 						Cole��o de <code>Modelo</code>.
	 */
	public static List findByNome(Session session, String nome)
	{
		Query query = session.createQuery(
			"FROM 	br.com.brasiltelecom.ppp.portal.entity.Modelo a " +
			"WHERE 	a.nomeModelo like ?" +
			"ORDER 	BY a.nomeModelo");	
		query.setString(0, nome);
		return query.list();
	}
	
	/**
	 * Consulta um Modelo a partir do c�digo.
	 * 
	 * @param session 				Sess�o do Hibernate.
	 * @param codigo 				C�digo do Modelo.
	 * @return 						Instancia de <code>Modelo</code>.
	 */
	public static Modelo findByCodigo(Session session, int codigo)
	{
		return (Modelo)session.get(br.com.brasiltelecom.ppp.portal.entity.Modelo.class, new Integer(codigo));
	}
}