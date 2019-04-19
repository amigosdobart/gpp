package br.com.brasiltelecom.ppp.dao;

import org.hibernate.Session;

import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;

/**
 * Dao para a classe com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula
 *
 * @author Lucas Mindêllo de Andrade
 * @since  1.0, 30/03/2008
 */
public class DescontoPulaPulaDao
{
	/**
	 * Retorna o DescontoPulaPula pelo identificador.
	 *
	 * @param session Sessão do Hibernate
	 * @param idDesconto Identificador do DescontoPulaPula
	 * @return DescontoPulaPula
	 */
	public static DescontoPulaPula findByIdDesconto(Session session, short idDesconto)
	{
		return (DescontoPulaPula)session.get(DescontoPulaPula.class, new Short(idDesconto));
	}
}
