package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoFaleGratis;

/**
 * Interface de acesso ao cadastro de <code>TotalizacaoFaleGratis</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 31/05/2007
 */
public class TotalizacaoFaleGratisDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>TotalizacaoFaleGratis</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoFaleGratis a " +
				"ORDER BY a.msisdn, a.datMes").list();

		return result; 
	}
	
	/**
	 * Consulta uma TotalizacaoFaleGratis por MSISDN e Mês de referência.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param msisdn				MSISDN.
	 * @param mes					Mês de referência.
	 * @return 						Instancia de <code>TotalizacaoFaleGratis</code>.
	 */
	public static TotalizacaoFaleGratis findById(Session session, String msisdn, String mes)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoFaleGratis a " +
				"WHERE a.msisdn = ? AND a.datMes = ?");
		
		query.setString(0, msisdn);
		query.setString(1, mes);
		
		List result = query.list();

		if (result.size() == 1)
			return (TotalizacaoFaleGratis)result.get(0);
		
		return null;
	}

}
