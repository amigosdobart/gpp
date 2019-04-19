/*
 * Created on 16/07/2004
 *
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.AssinanteBloqueado;
import br.com.brasiltelecom.ppp.portal.entity.BloqueioServico;
import br.com.brasiltelecom.ppp.portal.entity.StatusBloqueioAssinante;

/**
 * @author André Gonçalves
 * @since 16/07/2004
 */
public class ConsultaAssinantesBloqueados {
	
	/**
	 * Consulta se o msisdn está bloqueado
	 * 
	 * @param db Conexão com o banco de dados
	 * @param msisdn O msisdn a ser consultado
	 * @return Objeto do tipo AssinanteBloqueado
	 * @throws PersistenceException
	 */
	public static AssinanteBloqueado findByMsisdn(Database db, String msisdn) throws PersistenceException {

		OQLQuery query =null;
		AssinanteBloqueado temp = null;
		QueryResults rs = null;
		try
		{
			String consulta = "select a from br.com.brasiltelecom.ppp.portal.entity.AssinanteBloqueado a " 
				+ "where a.msisdn = \"" + msisdn + "\"";
			
			query = db.getOQLQuery(consulta);

			rs = query.execute();
			if (rs.hasMore())
				temp = (AssinanteBloqueado) rs.next();
		}
		finally{
			if(rs != null){
				rs.close();
			}

			if(query!=null){
				query.close();
			}
		}
		return temp;
   }
	
	/**
	 * Consulta o status de bloqueio do msisdn
	 * 
	 * @param db Conexão com o banco de dados
	 * @param msisdn O msisdn a ser consultado
	 * @return Objeto do tipo PedidoVoucher
	 * @throws PersistenceException
	 */
	public static StatusBloqueioAssinante findStatusByMsisdn(Database db, String msisdn) throws PersistenceException {

		OQLQuery query =null;
		StatusBloqueioAssinante temp = null;
		QueryResults rs = null;
		try
		{
			String consulta = "select a from br.com.brasiltelecom.ppp.portal.entity.StatusBloqueioAssinante a " 
				+ "where a.msisdn = \"" + msisdn + "\"";
			
			query = db.getOQLQuery(consulta);

			rs = query.execute();
			if (rs.hasMore())
				temp = (StatusBloqueioAssinante) rs.next();
		}
		finally{
			if(rs != null){
				rs.close();
			}

			if(query!=null){
				query.close();
			}
		}
		return temp;
   }
	
	/**
	 * Consulta os serviços para o qual o msisdn possui bloqueio automático
	 * 
	 * @param db Conexão com o banco de dados
	 * @param msisdn O msisdn a ser consultado
	 * @return Coleção de objetos do tipo BloqueioServico
	 * @throws PersistenceException
	 */
	public static Collection findBloqueiosAutomaticosByMsisdn(Database db, String msisdn) throws PersistenceException {

		OQLQuery query = null;
		Collection temp = new ArrayList();
		QueryResults rs = null;
		try
		{
			String consulta = "select a from br.com.brasiltelecom.ppp.portal.entity.BloqueioServico a " 
				+ "where a.msisdn = \"" + msisdn + "\"";
			
			query = db.getOQLQuery(consulta);

			rs = query.execute();
			while (rs.hasMore())
			{
				BloqueioServico bloqueio = (BloqueioServico) rs.next();				
				temp.add(bloqueio);
			}				
		}
		finally{
			if(rs != null){
				rs.close();
			}

			if(query!=null){
				query.close();
			}
		}
		return temp;
   }
	
	
	
	/**
	 * Insere o msisdn na tabela de msisdns bloqueados
	 * 
	 * @param db Conexão com o banco de dados
	 * @param assinante O objeto a ser inserido
	 * @throws PersistenceException
	 */
	public static void setAssinanteBloqueado(Database db, AssinanteBloqueado assinante)
		throws PersistenceException {
		db.create(assinante);
	}
	
	/**
	 * Insere na tabela de interfaces o status de bloqueio do assinante
	 * 
	 * @param db Conexão com o banco de dados
	 * @param status O objeto a ser inserido
	 * @throws PersistenceException
	 */
	public static void setStatusBloqueioAssinante(Database db, StatusBloqueioAssinante status)
		throws PersistenceException {
		db.create(status);
	}
	
	/**
	 * Exclui da tabela de interfaces o status de bloqueio do assinante
	 * 
	 * @param db Conexão com o banco de dados
	 * @param status O objeto a ser excluído
	 * @throws PersistenceException
	 */
	public static void deleteStatusBloqueioAssinante(Database db, StatusBloqueioAssinante status)
		throws PersistenceException {
		db.remove(status);
	}
	
	/**
	 * Exclui da tabela de interfaces o status de bloqueio do assinante
	 * 
	 * @param db Conexão com o banco de dados
	 * @param assinante O objeto a ser excluído
	 * @throws PersistenceException
	 */
	public static void deleteAssinanteBloqueado(Database db, AssinanteBloqueado assinante)
		throws PersistenceException {
		db.remove(assinante);
	}
	
	/**
	 * Exclui o assinante da tabela de bloqueios automáticos
	 * 
	 * @param db Conexão com o banco de dados
	 * @param assinante O objeto a ser excluído
	 * @throws PersistenceException
	 */
	public static void deleteAssinanteBloqueioAutomatico(Database db, BloqueioServico bloqueio)
		throws PersistenceException {
		db.remove(bloqueio);
	}
	
	/**
	 * Insere um assinante na tabela de bloqueio automático
	 * 
	 * @param db Conexão com o banco de dados
	 * @param status O objeto a ser inserido
	 * @throws PersistenceException
	 */
	public static void setBloqueioAutomatico(Database db, BloqueioServico bloqueio)
		throws PersistenceException {
		db.create(bloqueio);
	}

}
