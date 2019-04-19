package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Session;

public class MotivoEventoDao
{
	public static List findAllAtivo(Session session)
	{
		return session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.MotivoEvento a " +
				"WHERE a.indDisponivel = 1 ").list();
	}
}
