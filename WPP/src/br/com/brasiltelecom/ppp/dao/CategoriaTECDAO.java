package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.CategoriaTEC;

/**
 * Interface de acesso ao cadastro de <code>CategoriaTEC</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 12/07/2007
 */
public class CategoriaTECDAO 
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sess�o do Hibernate.
	 * @return 						Cole��o de <code>CategoriaTEC</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.CategoriaTEC c ORDER BY c.idtCategoria");	
		return query.list(); 
	}
	
	/**
	 * Consulta uma CategoriaTEC a partir do c�digo.
	 * 
	 * @param session 				Sess�o do Hibernate.
	 * @param codigo				C�digo da Categoria (serviceID).
	 * @return 						Instancia de <code>CategoriaTEC</code>.
	 */
	public static CategoriaTEC findById(Session session, short serviceID)
	{		
		return (CategoriaTEC)session.get(com.brt.gpp.comum.mapeamentos.entidade.CategoriaTEC.class, new Short(serviceID));
	}
}