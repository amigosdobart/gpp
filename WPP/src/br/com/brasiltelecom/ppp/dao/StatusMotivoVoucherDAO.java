package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.StatusMotivoVoucher;

/**
 * Interface de acesso ao cadastro de <code>StatusMotivoVoucher</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 11/07/2007
 */
public class StatusMotivoVoucherDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>StatusMotivoVoucher</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.StatusMotivoVoucher c ORDER BY c.idStatusVoucher");	
		return query.list(); 
	}

	/**
	 * Consulta um StatusMotivoVoucher por ID. Esse método retorna a primeira ocorrencia.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param id 					ID do LAC.
	 * @return 						Instancia de <code>StatusMotivoVoucher</code>.
	 */
	public static StatusMotivoVoucher findPrimeiroByIdStatusVoucher(Session session, int idStatusVoucher)
	{	
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.StatusMotivoVoucher a " +
				"WHERE  (a.idStatusVoucher = ?) ");
				
		query.setInteger(0, idStatusVoucher);
		List lista = query.list();
		
		if (lista.size() > 0)
		{
			return (StatusMotivoVoucher)lista.get(0);
		}
		else
			return null;
		
	}
}