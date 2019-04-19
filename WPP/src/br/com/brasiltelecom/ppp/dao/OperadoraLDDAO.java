package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.OperadoraLD;

/**
 * Interface de acesso ao cadastro de <code>OperadoraLD</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 22/03/2007
 */
public class OperadoraLDDAO {
 
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>OperadoraLD</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.OperadoraLD a " +
				"ORDER BY a.nomeOperadora").list();

		return result; 
	}
	
	/**  
	 * Consulta todos os dados cadastrados ordenados por id.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>OperadoraLD</code>.
	 */
	public static List findAllOrderedById(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.OperadoraLD a " +
				"ORDER BY a.csp").list();

		return result;
	}
	
	/**
	 * Consulta uma OperadoraLD por ID.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param id 					ID Operadora LD.
	 * @return 						Instancia de <code>OperadoraLD</code>.
	 */
	public static OperadoraLD findByCsp(Session session, int id)
	{		
		return (OperadoraLD)session.get(com.brt.gpp.comum.mapeamentos.entidade.OperadoraLD.class, new Integer(id));
	}
	
	/**
	 * Remove uma OperadoraLD.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param OperadoraLD 		Entidade <code>OperadoraLD</code>.
	 */
	public static void removerOperadoraGSM(Session session, OperadoraLD operadoraLD) 
	{	
		session.delete(operadoraLD);
	}
	
	/**
	 * Cadastra uma OperadoraLD
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param OperadoraLD		Entidade <code>OperadoraLD</code>.
	 */
	public static void incluirOperadoraGSM(Session session, OperadoraLD operadoraLD)
	{
		session.save(operadoraLD);
	}
	 
}
 
