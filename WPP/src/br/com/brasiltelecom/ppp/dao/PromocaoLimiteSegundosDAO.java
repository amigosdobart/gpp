package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteSegundos;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;

/**
 * Interface de acesso ao cadastro de <code>PromocaoLimiteSegundos</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 31/05/2007
 */
public class PromocaoLimiteSegundosDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>PromocaoLimiteSegundos</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteSegundos a " +
				"ORDER BY a.promocao.idtPromocao, numSegundos").list();

		return result; 
	}
	
	/**
	 * Consulta o PromocaoLimiteSegundos de maior quantidade de segundos para a promocao especificada.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param promocao				Instancia de <code>Promocao</code>.
	 * @return 						Instancia de <code>PromocaoLimiteSegundos</code>.
	 */
	public static PromocaoLimiteSegundos findLimiteMaximoByPromocao(Session session, Promocao promocao)
	{
		Query query = session.createQuery(
			"FROM com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteSegundos a " +
			"WHERE a.promocao = ? AND a.limiteMaximo = ?");
		
		query.setEntity(0, promocao);
		query.setBoolean(1, true);
		
		List result = query.list();

		if (result.size() == 1)
			return (PromocaoLimiteSegundos)result.get(0);
		
		return null;
	}

}
