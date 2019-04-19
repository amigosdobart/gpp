package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Modelo;


/**
 * Classe responsável pelas consultas/atualizações no banco de dados 
 * relativas as tabelas de fabricante de aparelhos
 * 
 * @author Geraldo Palmeira
 * @since 17/04/2006
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 01/02/2007
 */
public class ModeloHome
{
	
	/**
	 * Obtem os Codigos e nomes de modelos e Fabricante das tabelas de hsid.
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos OptIn
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery(" select a from "+
								   " br.com.brasiltelecom.ppp.portal.entity.Modelo a " +
								   " order by a.nomeModelo");

			rs = query.execute();
			while(rs.hasMore())
			{
				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null)
				rs.close();

			if(query!=null)
				query.close();
		}
		return result;
	}

	public static Collection findByNome(Database db, String nome) throws PersistenceException
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery(" select a from "+
								   " br.com.brasiltelecom.ppp.portal.entity.Modelo a " +
								   " where a.nomeModelo like $1 " +
								   " order by a.nomeModelo");
			query.bind("%"+nome.trim().toUpperCase()+"%");
			rs = query.execute();
			while(rs.hasMore())
			{
				result.add(rs.next());
			}
		}
		finally
		{
			if(rs != null)
				rs.close();

			if(query!=null)
				query.close();
		}
		return result;
	}
	
	public static Modelo findByCodigo(Database db, int codigo) throws PersistenceException
	{
		Modelo modelo = null;
		OQLQuery query = null;
		QueryResults rs = null;
		
		try
		{
			query = db.getOQLQuery(" select a from "+
								   " br.com.brasiltelecom.ppp.portal.entity.Modelo a " +
								   " where a.codigoModelo = $1 ");
			query.bind(codigo);
			rs = query.execute();
			
			if (rs.hasMore())
			{
				modelo = (Modelo)rs.next();
			}
		}
		finally
		{
			if(rs != null)
				rs.close();

			if(query!=null)
				query.close();
		}
		
		return modelo;
	}
}