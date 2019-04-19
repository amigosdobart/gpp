package br.com.brasiltelecom.ppp.dao;

import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;


/**
 * Interface de acesso ao cadastro de <code>ParametrosNotificacao</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 21/09/2007
 */
public class ParametrosNotificacaoDAO
{
	/**  
	 * Consulta uma URL de notificacao para uma data requisica.
	 *   
	 * @param session 				Sessão do Hibernate
	 * @param idtMsisdnDestino 		Destinatario
	 * @param idMensagem 			Identificacao da mensagem
	  */
	public static String findEnderecoByDestinoMensagem(Session session,
			String idtMsisdnDestino, String idMensagem)
	{
		SQLQuery query = session.createSQLQuery(
				"SELECT a.URL_NOTIFICACAO url " +
				"FROM TBL_TAN_REQUISICAO a, TBL_TAN_DESTINO_MENSAGEM b " +
				"WHERE a.ID_REQUISICAO = b.ID_REQUISICAO and  " +
				"      b.IDT_MSISDN_DESTINO = '" + idtMsisdnDestino+ "' and " +
				"      b.ID_MENSAGEM = '" + idMensagem + "' and " +
				"      rownum = 1");
		
		query.addScalar("url", Hibernate.STRING);
		
		Iterator it = query.list().iterator();
		if (it.hasNext())
		{
			Object[] row = (Object[])it.next();
	        return (String)row[0];
		}
			
		return null;
	}
	
}