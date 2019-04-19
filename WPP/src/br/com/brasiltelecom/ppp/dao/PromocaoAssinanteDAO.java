package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Interface de acesso ao cadastro de <code>PromocaoAssinante</code>.
 * 
 * @author Geraldo Palmeira
 * Criado em: 21/06/2007
 */
public class PromocaoAssinanteDAO
{

	/**
	 * Consulta as Promocoes do Assinante.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param id					ID da promoção.
	 * @return 						Coleção de <code>PromocaoAssinante</code>.
	 */
	public static List findByMsisdn(Session session, String msisdn)
	{
		Query query = session.createQuery(
				"SELECT a.promocao " +
				"FROM 	com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante a " +
				"WHERE 	a.idtMsisdn = ? ");	
			query.setString(0, msisdn);
			return query.list();
	}
}
