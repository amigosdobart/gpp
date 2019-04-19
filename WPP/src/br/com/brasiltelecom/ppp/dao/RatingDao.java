package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.Rating;

/**
 * Cria��o: 09/04/2008
 * @author Lucas Mind�llo de Andrade
 */
public class RatingDao 
{
	/**
	 * Retorna rating pelo nome
	 * @param session Sess�o do Hibernate
	 * @param rateName Nome da rating
	 * @return Rating
	 */
	public static Rating findByRateName(Session session, String rateName)
	{
		return (Rating)session.get(Rating.class, rateName);
	}
	
	/**
	 * Retorna lista completa de ratings
	 * @param session Sess�o do hibernate
	 * @return List
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery("from com.brt.gpp.comum.mapeamentos.entidade.Rating a ORDER BY a.rateName");
		
		return query.list();
	}
	
	/**
	 * Retorna lista filtrada pelo RateName 
	 * @param session Sess�o do Hibernate
	 * @param filtro Filtro de rateName 
	 * @return List
	 */
	public static List findByRateNamePart(Session session, String filtro)
	{
		Query query = session.createQuery("from com.brt.gpp.comum.mapeamentos.entidade.Rating a WHERE upper(a.rateName) LIKE upper(?)");
		query.setString(0, "%"+filtro+"%");
		
		return query.list();
	}
	
	/**
	 * Inclui rating na base de dados
	 * @param session Sess�o do hibernate
	 * @param rating Rating
	 */
	public static void incluirRating(Session session, Rating rating)
	{
		session.save(rating);
	}
}
