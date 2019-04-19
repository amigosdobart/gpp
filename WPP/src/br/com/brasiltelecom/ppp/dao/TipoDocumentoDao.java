package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Session;

public class TipoDocumentoDao
{
	public static List findAllAtivo(Session session)
	{
		return session.createQuery(
				"FROM br.com.brasiltelecom.ppp.portal.entity.TipoDocumento a " +
				"WHERE a.indAtivo = 1 ").list();
	}
}
