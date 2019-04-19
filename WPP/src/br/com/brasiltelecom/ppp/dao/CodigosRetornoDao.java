package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.action.consultaPromocaoPulaPula.ConsultaPaginaAction;
import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;

public class CodigosRetornoDao
{
	public static CodigosRetorno findBiId(Session session, String idRetorno)
	{
		return (CodigosRetorno)session.get(CodigosRetorno.class, idRetorno);
	}

	public static CodigosRetorno findByVlr(Session session, String vlrRetorno)
	{
		Query query = session.createQuery(
				"from br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno a " +
				" where a.vlrRetorno = ? "
				);

		query.setString(0, vlrRetorno);

		List resultados = query.list();

		if(resultados.size()>0)
		{
			return (CodigosRetorno)resultados.get(0);
		}

		return null;
	}
}
