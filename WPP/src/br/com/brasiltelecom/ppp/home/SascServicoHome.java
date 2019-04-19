package br.com.brasiltelecom.ppp.home;

import br.com.brasiltelecom.ppp.portal.entity.SascGrupo;
import br.com.brasiltelecom.ppp.portal.entity.SascPerfil;
import br.com.brasiltelecom.ppp.portal.entity.SascServico;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public class SascServicoHome
{
	/**
	 * Metodo....: getAllPerfis
	 * Descricao.: Consulta todos os perfis disponiveis
	 * 
	 * @param  db			 - Instancia do Banco de Dados
	 * @return listaDePerfis - Lista dos perfis disponiveis
	 * @throws PersistenceException 
	 */
	public static Collection findAllPerfis(Database db) throws PersistenceException
	{
		Collection listaDePerfis = new ArrayList();
		Logger logger = Logger.getLogger(SascServicoHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		
		try
		{
			query = db.getOQLQuery(" select a from " +
								   " br.com.brasiltelecom.ppp.portal.entity.SascPerfil a " +
								   " order by a.codPerfil ");
			
			rs = query.execute(Database.ReadOnly);
			
			while (rs.hasMore())
				listaDePerfis.add((SascPerfil)rs.next());
		}
		catch (PersistenceException e)
		{
			logger.error("Erro ao pesquisar informacoes de perfis do SASC. Erro: "+ e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		
		return listaDePerfis;
	}
	
	/**
	 * Metodo....: findPerfilById
	 * Descricao.: Consulta o perfil a partir do ID
	 * 
	 * @param  db		 - Instancia do  Banco de Dados
	 * @param  codPerfil - Codigo do perfil
	 * @return perfil	 - Objeto populado com as informacoes de Banco
	 * @throws PersistenceException
	 */
	public static SascPerfil findPerfilById(Database db, int codPerfil, boolean isReadyOnly) throws PersistenceException
	{
		Logger logger = Logger.getLogger(SascServicoHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		SascPerfil perfil = null;
		
		try
		{
			query = db.getOQLQuery(" select a from " +
								   " br.com.brasiltelecom.ppp.portal.entity.SascPerfil a " +
								   " where a.codPerfil = $1 ");
			
			query.bind(codPerfil);
			rs = query.execute(isReadyOnly ? Database.ReadOnly : Database.Shared);
			
			if (rs.hasMore())
				perfil = (SascPerfil)rs.next();
		}
		catch (PersistenceException e)
		{
			logger.error("Erro ao pesquisas o nome do perfil by id. Erro: " + e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		
		return perfil;
	}
	
	/**
	 * Metodo....: findGrupoById
	 * Descricao.: Seleciona o Grupo a partir do ID
	 * 
	 * @param  db		- Instancia do Banco de Dados
	 * @param  codGrupo	- ID do grupo a ser consultado
	 * @return grupo	- Objeto devidamente populado
	 * @throws PersistenceException
	 */
	public static SascGrupo findGrupoById(Database db, int codGrupo) throws PersistenceException
	{
		Logger logger = Logger.getLogger(SascServicoHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		SascGrupo grupo = null;
		
		try
		{
			query = db.getOQLQuery(" select a from " +
								   " br.com.brasiltelecom.ppp.portal.entity.SascGrupo a " +
								   " where a.codGrupo = $1 ");
			
			query.bind(codGrupo);
			rs = query.execute(Database.ReadOnly);
			
			if (rs.hasMore())
				grupo = (SascGrupo)rs.next();
		}
		catch (PersistenceException e)
		{
			logger.error("Erro ao pesquisas o nome do perfil by id. Erro: " + e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		
		return grupo;
	}
	
	/**
	 * Metodo....: insereNovoServico
	 * Descricao.: Insere um novo servico na tabela de servicos
	 * 
	 * @param  db		- Instancia do Banco de Dados
	 * @param  servico	- Objeto contendo os dados para insercao
	 * @throws PersistenceException
	 */
	public static void insereNovoServico(Database db, SascServico servico) throws PersistenceException
	{
		db.create(servico);
	}
	
	/**
	 * Metodo....: insereServicoBlackList
	 * Descricao.: Insere um novo servico na BlackList
	 * 
	 * @param db	  - Instancia do Banco de Dados
	 * @param perfil - Objeto contendo os dados para insercao
	 * @throws PersistenceException 
	 */
	public static void insereServicoBlackList(Database db, SascPerfil perfil) throws PersistenceException
	{
		db.update(perfil);
	}
	
	/**
	 * Metodo....: findAllGrupos
	 * Descricao.: Seleciona todos os grupos disponiveis
	 * 
	 * @param  db	  - Instancia do Banco de Dados
	 * @return grupos - Lista dos grupos existentes no Banco de Dados
	 * @throws PersistenceException
	 */
	public static Collection findAllGrupos(Database db) throws PersistenceException
	{
		Collection grupos = new ArrayList();
		Logger logger = Logger.getLogger(SascServicoHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		
		try
		{
			query = db.getOQLQuery(" select a from " +
								   " br.com.brasiltelecom.ppp.portal.entity.SascGrupo a " +
								   " order by a.nomeGrupo ");
			
			rs = query.execute(Database.ReadOnly);
			
			while (rs.hasMore())
				grupos.add((SascGrupo)rs.next());
		}
		catch (PersistenceException e)
		{
			logger.error("Erro ao pesquisar informacoes de perfis do SASC. Erro: "+ e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		
		return grupos;
	}
}