package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCategoria;

/**
 * Interface de acesso ao cadastro de <code>Promocao</code>.
 *
 * @author Bernardo Vergne Dias
 * Criado em: 31/05/2007
 * Modificado: 07/05/2008 pro Lucas Andrade
 */
public class PromocaoDAO
{
	/**
	 * Consulta todos os dados cadastrados.
	 *
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>Promocao</code>.
	 */
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.Promocao a " +
				"ORDER BY a.idtPromocao").list();

		return result;
	}

	public static List findAllPulaPulaAndPendenteRecarga(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.Promocao a " +
				"WHERE a.categoria = "+Constantes.ID_CATEGORIA_PULAPULA+" or " +
				"a.idtPromocao = "+Constantes.ID_PROMOCAO_PEND_REC+" ");

		//query.setInteger(0, Constantes.ID_CATEGORIA_PULAPULA);
		//query.setInteger(1, Constantes.ID_PROMOCAO_PEND_REC);
		return query.list();
	}
	
	/**
	 * Consulta os dados cadastrados por categoria de promocao.
	 * 
	 * @param session Sessao do hibernate
	 * @param categoria Categoria de promocao
	 * @return <code>List</code>
	 */
	public static List findAllByCategoria(Session session, PromocaoCategoria categoria)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.Promocao a " +
				"WHERE a.categoria = ? " + 
				"ORDER BY a.nomPromocao ");
		
		query.setEntity(0, categoria);
		
		return query.list();
	}

	/**
	 * Consulta uma Promocao.
	 *
	 * @param session 				Sessão do Hibernate.
	 * @param id					ID da promoção.
	 * @return 						Instancia de <code>Promocao</code>.
	 */
	public static Promocao findById(Session session, int id)
	{
		return (Promocao)session.get(com.brt.gpp.aplicacoes.promocao.entidade.Promocao.class,
				new Integer(id));
	}

}
