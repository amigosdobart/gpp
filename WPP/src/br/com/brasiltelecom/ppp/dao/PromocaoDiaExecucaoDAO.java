package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;

/**
 * Interface de acesso ao cadastro de <code>PromocaoDiaExecucao</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 06/06/2007
 */
public class PromocaoDiaExecucaoDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>PromocaoDiaExecucao</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao a " +
				"ORDER BY a.tipExecucao, a.idtPromocao, a.numDiaEntrada").list();

		return result; 
	}
	
	/**
	 * Consulta PromocaoDiaExecucao por ID.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param idtPromocao			ID da promoção.
	 * @param numDiaEntrada			Dia de entrada na promoção.
	 * @param tipExecucao			Tipo de execução.
	 * @return 						Instancia de <code>PromocaoLimiteSegundos</code>.
	 */
	public static PromocaoDiaExecucao findById(Session session, 
			int idtPromocao, int numDiaEntrada, String tipExecucao)
	{
		Query query = session.createQuery(
			"FROM com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao a " +
			"WHERE a.idtPromocao = ? AND a.numDiaEntrada = ? AND a.tipExecucao = ?");
		
		query.setInteger(0, idtPromocao);
		query.setInteger(1, numDiaEntrada);
		query.setString(2, tipExecucao);

		List result = query.list();

		if (result.size() == 1)
			return (PromocaoDiaExecucao)result.get(0);
		
		return null;
	}

}
