package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.PlanoTerceiro;

/**
 * Criação: 21/07/2008
 * @author Lucas Mindêllo de Andrade
 */
public class PlanoTerceiroDAO 
{
	/**
	 * Retorna PlanoTerceiro pelo msisdn do assinante
	 * @param session Sessão do Hibernate
	 * @param planoTerceiro Plano Terceiro
	 * @return PlanoTerceiro
	 */
	public static PlanoTerceiro findByAssinante(Session session, String planoTerceiro)
	{
		return (PlanoTerceiro)session.get(PlanoTerceiro.class, planoTerceiro);
	}
	
	/**
	 * Retorna lista completa de PlanoTerceiro
	 * @param session Sessão do hibernate
	 * @return List
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery("from br.com.brasiltelecom.ppp.portal.entity.PlanoTerceiro a ORDER BY a.descricao");
		
		return query.list();
	}
}
