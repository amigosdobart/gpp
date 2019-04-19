package br.com.brasiltelecom.ppp.dao;

import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.PromocaoStatusAssinante;

public class PromocaoStatusAssinanteDao
{
	public static PromocaoStatusAssinante findById(int idStatus, Session session)
	{
		return (PromocaoStatusAssinante)session.get(PromocaoStatusAssinante.class, new Integer(idStatus));
	}
}
