package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.CanalOrigemBS;

/**
 * Interface de acesso ao cadastro de CanalOrigemBS.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 31/01/2008
 */
public class CanalOrigemBSDAO 
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sess�o do Hibernate.
	 * @return 						Cole��o de <code>CanalOrigemBS</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.CanalOrigemBS c ORDER BY c.idCanalOrigemBS");	
		return query.list(); 
	}
    
    /**
     * Consulta um CanalOrigemBS a partir do c�digo.
     * 
     * @param session               Sess�o do Hibernate.
     * @param codigo                C�digo do CanalOrigemBS.
     * @return                      Instancia de <code>CanalOrigemBS</code>.
     */
    public static CanalOrigemBS findById(Session session, int id)
    {       
        return (CanalOrigemBS)session.get(com.brt.gpp.comum.mapeamentos.entidade.CanalOrigemBS.class, new Integer(id));
    }
}