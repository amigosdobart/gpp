package br.com.brasiltelecom.ppp.dao;

import java.io.File;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateHelper 
{
	private static SessionFactory sessionFactory = null;

	private static Logger logger = Logger.getLogger(HibernateHelper.class
			.getClass());

	public static void load(String configFilename)
	{
		try 
		{
			HibernateHelper.sessionFactory = new Configuration().configure(
					new File(configFilename)).buildSessionFactory();

		} catch (Exception e) 
		{
			logger.error("Erro ao iniciar Hibernate", e);
		}
	}

	public static void begin() throws Exception 
	{
		getSession().beginTransaction();
	}

	public static void commit() throws Exception 
	{
		getSession().getTransaction().commit();
	}

	public static void rollback() 
	{
		try 
		{
			getSession().getTransaction().rollback();
		} catch (Exception ignore) {}

	}

	public static Session getSession() throws Exception 
	{
		Session session = getSessionFactory().getCurrentSession();
		
		if (session == null)
			throw new Exception("Erro ao iniciar conexão com o banco de dados");
		
		return session;
	}
	
	public static SessionFactory getSessionFactory()
	{
		if (sessionFactory == null)
			throw new RuntimeException("Banco de dados não iniciado");
		
		return sessionFactory;
	}
}
