package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.AssinanteTerceiro;

/**
 * Criação: 21/07/2008
 * @author Lucas Mindêllo de Andrade
 */
public class AssinanteTerceiroDAO 
{
	/**
	 * Retorna AssinanteTerceiro pelo msisdn do assinante
	 * @param session Sessão do Hibernate
	 * @param msisdn MSISDN
	 * @return AssinanteTerceiro
	 */
	public static AssinanteTerceiro findByAssinante(Session session, String msisdn)
	{
		return (AssinanteTerceiro)session.get(AssinanteTerceiro.class, msisdn);
	}
	
	/**
	 * Retorna lista completa de AssinanteTerceiro
	 * @param session Sessão do hibernate
	 * @return List
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery("from br.com.brasiltelecom.ppp.portal.entity.AssinanteTerceiro a ORDER BY a.msisdn");
		
		return query.list();
	}
	
	/**
	 * Retorna lista filtrada pelo msisdn 
	 * @param session Sessão do Hibernate
	 * @param filtro Filtro de msisdn 
	 * @return List
	 */
	public static List findByMsidnPart(Session session, String filtro)
	{
		Query query = session.createQuery("from br.com.brasiltelecom.ppp.portal.entity.AssinanteTerceiro a WHERE a.msisdn LIKE ?");
		query.setString(0, "%"+filtro+"%");
		
		return query.list();
	}
	
	/**
	 * Inclui AssinanteTerceiro na base de dados
	 * @param session Sessão do hibernate
	 * @param assinanteTerceiro AssinanteTerceiro
	 */
	public static void incluirAssinanteTerceiro(Session session, AssinanteTerceiro assinanteTerceiro)
	{
		session.save(assinanteTerceiro);
	}
	
	/**
	 * Remove AssinanteTerceiro da base de dados
	 * @param session Sessão do hibernate
	 * @param assinanteTerceiro AssinanteTerceiro
	 */
	public static void removerAssinanteTerceiro(Session session, AssinanteTerceiro assinanteTerceiro)
	{
		session.delete(assinanteTerceiro);
	}
}
