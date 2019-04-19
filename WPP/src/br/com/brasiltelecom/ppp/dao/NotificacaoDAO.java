package br.com.brasiltelecom.ppp.dao;

import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.brt.gpp.comum.conexoes.tangram.entidade.Notificacao;

/**
 * Interface de acesso ao cadastro de <code>Notificacao</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 21/09/2007
 */
public class NotificacaoDAO
{
	/**  
	 * Registra uma notificacao
	 *   
	 * @param session 				Sessão do Hibernate
	 * @param notificacao			Notificacao
	 */
	public static void incluirNotificacao(Session session, Notificacao notificacao)
	{
		// Busca o ID_DESTINO_MENSAGEM
		
		Integer idDestinoMensagem = null;
		
		SQLQuery query = session.createSQLQuery(
				"SELECT a.ID_DESTINO_MENSAGEM id " +
				"FROM TBL_TAN_DESTINO_MENSAGEM a " +
				"WHERE a.IDT_MSISDN_DESTINO = ? and " +
				"      a.ID_MENSAGEM = ? and " +
				"      rownum = 1");
		
		query.addScalar("id", Hibernate.INTEGER);
		query.setString(0, notificacao.getIdtMsisdnDestino());
		query.setString(1, notificacao.getIdMensagem());		
		
		Iterator it = query.list().iterator();
		if (it.hasNext())
		{
			Object[] row = (Object[])it.next();
			idDestinoMensagem = (Integer)row[0];
		}
		else
		{
			throw new RuntimeException("Nao foi possivel encontrar um DestinoMensagem para essa notificacao. " +
					"IDT_MSISDN_DESTINO = " + notificacao.getIdtMsisdnDestino() + ", ID_MENSAGEM = " + 
					notificacao.getIdMensagem());
		}
		
		// Grava a notificacao
		
		query = session.createSQLQuery(
				"INSERT INTO TBL_TAN_NOTIFICACAO (ID_DESTINO_MENSAGEM, IDT_STATUS, DAT_NOTIFICACAO) " +
				"VALUES (?,?,?)");
		
		query.setInteger(0, idDestinoMensagem.intValue());
		query.setShort(1, notificacao.getStatusNotificacao().shortValue());
		query.setDate(2, new java.sql.Timestamp(notificacao.getDataNotificacao().getTime()));
		query.executeUpdate();
	}
	
}