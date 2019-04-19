package br.com.brasiltelecom.ppp.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 * Interface de acesso ao cadastro de Tipos Saldo.
 * 
 * @author Geraldo Palmeira
 * Criado em: 24/05/2007
 */
public class TipoSaldoDAO
{
	// Cache da lista de tipos de saldo (mapeamento por idtTipoSaldo)
	private static HashMap tiposSaldo = null;
	
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>TipoSaldos</code>.
	 */
	public static Collection findAll(Session session)
	{
		return getTiposSaldo(session).values();
	}
	
	/**
	 * Consulta um TipoSaldo a partir do idtTipoSaldo.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param idtTipoSaldo 			Id do Tipo de Saldo.
	 * @return 						Instancia de <code>TipoSaldo</code>.
	 */
	public static TipoSaldo findByIdTipoSaldo(Session session, short idtTipoSaldo)
	{
		return (TipoSaldo)(getTiposSaldo(session).get(new Short(idtTipoSaldo)));
	}
		
	/**
	 * Carrega o cache de tipos saldo
	 */ 
	private synchronized static HashMap getTiposSaldo(Session session)
	{
		// Consulta no banco apenas uma vez
		if (tiposSaldo == null)
		{
			tiposSaldo = new HashMap();
			Query query = session.createQuery("FROM com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo " +
					"a ORDER BY a.idtTipoSaldo");
	
			for (Iterator it = query.list().iterator(); it.hasNext();)
			{
				TipoSaldo saldo = (TipoSaldo)it.next();
				tiposSaldo.put(new Short(saldo.getIdtTipoSaldo()), saldo);
			}
		}
		
		return tiposSaldo;
	}
}