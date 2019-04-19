package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.AssinanteCampanha;
import br.com.brasiltelecom.ppp.portal.entity.Campanha;
import br.com.brasiltelecom.ppp.portal.entity.ParamIncentivoRecargas;
import br.com.brasiltelecom.ppp.portal.entity.CondIncentivoRecargas;

/**
 * Classe responsavel pela atualizacao dos objetos relacionados a campanha
 * promocionais no banco de dados
 * 
 * @author Joao Carlos
 * @since 16/02/2006
 */
public class CampanhaHome
{
	/**
	 * Metodo....:insereCampanha
	 * Descricao.:Realiza a insercao do objeto campanha e suas classes dependentes
	 * @param campanha - Campanha a ser inserida
	 * @param db - Objeto de banco de dados
	 * @throws PersistenceException
	 */
	private static void insereCampanha(Campanha campanha, Database db) throws PersistenceException
	{
		// Realiza a criacao do objeto Campanha pois devido a este gerar o objeto sequence
		// entao suas dependencias (Colecoes) serao gravadas posteriormente
		db.create(campanha);
	}

	/**
	 * Metodo....:atualizaCampanha
	 * Descricao.:Realiza a alteracao dos dados cadastrados da campanha
	 * @param campanha - Campanha a ser alterada
	 * @param db - Objeto de banco de dados
	 * @throws PersistenceException
	 */
	private static void atualizaCampanha(Campanha campanha, Database db) throws PersistenceException
	{
		// Realiza a alteracao das informacoes inseridas no objeto campanha
		db.update(campanha);
	}
	
	/**
	 * Metodo....:insereIncentivoRecargas
	 * Descricao.:Este metodo realiza a insercao dos dados da campanha promocional
	 *            e depois insere os dados relativos a campanha especifica de incentivo
	 *             de recargas
	 * @param campanha - Campanha a ser inserida
	 * @param parametros - Parametros de Inscricao a serem inseridos para a campanha de incentivo
	 * @param condicoes  - Condicoes de Concessao a serem inseridos para a campanha de incentivo
	 * @param db - Objeto de banco de dados
	 * @throws PersistenceException
	 */
	public static void insereIncentivoRecargas(Campanha campanha, Collection parametros, Collection condicoes, Database db) throws PersistenceException
	{
		Logger logger = Logger.getLogger(CampanhaHome.class);
		db.begin();
		try
		{
			// Realiza a criacao da campanha para posteriormente realizar o cadastramento
			// das classes relativas a campanha de incentivo de recargas (recarga em dobro)
			CampanhaHome.insereCampanha(campanha,db);
			// Realiza a iteracao buscando todos os parametros de inscricao cadastrados
			// para a campanha. Para cada objeto o Castor eh acionado para o registro no BD
			for (Iterator i=parametros.iterator(); i.hasNext();)
			{
				ParamIncentivoRecargas param = (ParamIncentivoRecargas)i.next();
				db.create(param);
			}
			// Realiza agora a iteracao da mesma forma que os parametros para as Condicoes
			// de concessao sendo que para cada um o Castor eh acionado para registro no BD
			for (Iterator i=condicoes.iterator(); i.hasNext();)
			{
				CondIncentivoRecargas cond = (CondIncentivoRecargas)i.next();
				db.create(cond);
			}
			db.commit();
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao inserir campanha de incentivo de recargas. Erro:"+e.getMessage());
			db.rollback();
			throw e;
		}
		finally
		{
			db.close();
		}
	}
	
	/**
	 * Metodo....:atualizaIncentivoRecargas
	 * Descricao.:Atualiza as informacoes da campanha de incentivo de recargas
	 * @param campanha - Campanha associada
	 * @param parametros - Parametros de inscricao da campanha
	 * @param condicoes  - Condicoes de concessao da campanha
	 * @param db - Objeto de banco de dados
	 * @throws PersistenceException
	 */
	public static void atualizaIncentivoRecargas(Campanha campanha, Collection parametros, Collection condicoes, Database db) throws PersistenceException
	{
		Logger logger = Logger.getLogger(CampanhaHome.class);
		db.begin();
		try
		{
			CampanhaHome.atualizaCampanha(campanha,db);
			// Realiza a iteracao buscando todos os parametros de inscricao cadastrados
			// para a campanha. Para cada objeto o Castor eh atualizado para o registro no BD
			for (Iterator i=parametros.iterator(); i.hasNext();)
			{
				ParamIncentivoRecargas param = (ParamIncentivoRecargas)i.next();
				// Verifica se o ID do parametro estah preenchido. Como nao eh possivel
				// alterar o parametro, entao caso o id serah realizado uma insercao
				// caso contrario nao faz nada
				if (param.getId() == 0)
					db.create(param);
				else if(param.deveRemover())
						db.remove(param);
			}
			// Realiza agora a iteracao da mesma forma que os parametros para as Condicoes
			// de concessao sendo que para cada um o Castor eh acionado para registro no BD
			for (Iterator i=condicoes.iterator(); i.hasNext();)
			{
				CondIncentivoRecargas cond = (CondIncentivoRecargas)i.next();
				// Verifica se o ID do parametro estah preenchido. Como nao eh possivel
				// alterar a condicao, entao caso o id serah realizado uma insercao
				// caso contrario nao faz nada
				if (cond.getId() == 0)
					db.create(cond);
				else if(cond.deveRemover())
						db.remove(cond);
			}
			db.commit();
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao atualizar campanha de incentivo de recargas. Erro:"+e.getMessage());
			db.rollback();
			throw e;
		}
		finally
		{
			db.close();
		}
	}
	
	/**
	 * Metodo....:findById
	 * Descricao.:Retorna o objeto campanha baseado no id passado como parametro
	 * @param idCampanha - Id a ser pesquisado
	 * @param db - Objeto database
	 * @return Campanha - Objeto campanha pesquisado no banco de dados
	 * @throws Exception
	 */
	public static Campanha findById(long idCampanha, Database db) throws Exception
	{
		Logger logger = Logger.getLogger(CampanhaHome.class);
		Campanha campanha = null;
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
			   					   "br.com.brasiltelecom.ppp.portal.entity.Campanha a "+
					               "where a.id = $1");
			query.bind(idCampanha);
			rs = query.execute();
			if (rs.hasMore())
				campanha = (Campanha)rs.next();
		}
		catch(Exception e)
		{
			logger.error("Erro ao pesquisar campanha promocional. Id:"+idCampanha+". Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return campanha;
	}
	
	/**
	 * Metodo....:findParamsIncentivoRecargas
	 * Descricao.:Pesquisa a lista de parametros de incentivo de recargas
	 * @param campanha - Campanha a ser pesquisada
	 * @param db - objeto de banco de dados
	 * @return Collection - lista de parametros de incentivo de recargas
	 * @throws Exception
	 */
	public static Collection findParamsIncentivoRecargas(Campanha campanha, Database db) throws Exception
	{
		Logger logger = Logger.getLogger(CampanhaHome.class);
		Collection params = new ArrayList();
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a "+
					               "from br.com.brasiltelecom.ppp.portal.entity.ParamIncentivoRecargas a "+
					               "where a.campanha = $1");
			query.bind(campanha);
			rs = query.execute();
			while (rs.hasMore())
				params.add((ParamIncentivoRecargas)rs.next());
		}
		catch(Exception e)
		{
			logger.error("Erro ao pesquisar parametros de inscricao para a campanha. Id:"+campanha.getId()+". Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return params;
	}
	
	/**
	 * Metodo....:findCondsIncentivoRecargas
	 * Descricao.:Pesquisa a lista de condicoes de concessao para a campanha de incentivo de recargas
	 * @param campanha - Campanha a ser pesquisada
	 * @param db - objeto de banco de dados
	 * @return Collection - lista de condicoes de concessao
	 * @throws PersistenceException
	 */
	public static Collection findCondsIncentivoRecargas(Campanha campanha, Database db) throws Exception
	{
		Logger logger = Logger.getLogger(CampanhaHome.class);
		Collection params = new ArrayList();
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
			   					   "br.com.brasiltelecom.ppp.portal.entity.CondIncentivoRecargas a "+
					               "where a.campanha = $1");
			query.bind(campanha);
			rs = query.execute();
			while (rs.hasMore())
				params.add((CondIncentivoRecargas)rs.next());
		}
		catch(Exception e)
		{
			logger.error("Erro ao pesquisar condicoes de concessao para a campanha. Id:"+campanha.getId()+". Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return params;
	}
	
	/**
	 * Metodo....:findCampanhasVigentes
	 * Descricao.:Realiza a pesquisa de todas as campanhas promocionais vigentes
	 * @param dataPesquisa - Data a ser utilizada para pesquisa (dessa forma permite visualizacao de
	 *                       campanhas retroativas)
	 * @param db - Objeto de banco de dados
	 * @return Collection - Lista de campanhas vigentes na data de pesquisa
	 * @throws Exception
	 */
	public static Collection findCampanhasVigentes(Date dataPesquisa, Database db) throws Exception
	{
		Logger logger = Logger.getLogger(CampanhaHome.class);
		Collection campanhas = new ArrayList();
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
			   					   "br.com.brasiltelecom.ppp.portal.entity.Campanha a "+
					               "where $1 between a.validadeInicial and a.validadeFinal");
			query.bind(dataPesquisa);
			rs = query.execute();
			while (rs.hasMore())
				campanhas.add((Campanha)rs.next());
		}
		catch(Exception e)
		{
			logger.error("Erro ao pesquisar campanhas vigentes. Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return campanhas;
	}

	/**
	 * Metodo....:findByAssinante
	 * Descricao.:Realiza a pesquisa de campanhas na qual um assinante estah inscrito
	 * @param msisdn - Msisdn do assinante a ser pesquisado
	 * @param db - Objeto de banco de dados
	 * @return Collection - Lista de objetos AssinanteCampanha mostrando em quais
	 *                      campanhas o assinante estah inscrito
	 * @throws Exception
	 */
	public static Collection findByAssinante(String msisdn, Database db) throws Exception
	{
		Logger logger = Logger.getLogger(CampanhaHome.class);
		Collection campanhas = new ArrayList();
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
			   					   "br.com.brasiltelecom.ppp.portal.entity.AssinanteCampanha a "+
					               "where a.msisdn = $1");
			query.bind(msisdn);
			rs = query.execute();
			while (rs.hasMore())
				campanhas.add((AssinanteCampanha)rs.next());
		}
		catch(Exception e)
		{
			logger.error("Erro ao pesquisar campanhas do assinante "+msisdn+". Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return campanhas;
	}
	
	/**
	 * Metodo....:findByAssinante
	 * Descricao.:Realiza a pesquisa de campanhas na qual um assinante estah inscrito
	 * @param msisdn - Msisdn do assinante a ser pesquisado
	 * @param idCampanha - Id a ser pesquisado
	 * @param db - Objeto de banco de dados
	 * @return Collection - Lista de objetos AssinanteCampanha 
	 * @throws Exception
	 */
	public static AssinanteCampanha findByAssinanteCampanha(String msisdn, long idCampanha, Database db) throws Exception
	{
		Logger logger = Logger.getLogger(CampanhaHome.class);
		AssinanteCampanha result = 	null;
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery("select a from "+
			   					   "br.com.brasiltelecom.ppp.portal.entity.AssinanteCampanha a "+
					               "where a.msisdn = $1 and a.idCampanha = $2");
			query.bind(msisdn);
			query.bind(idCampanha);
			rs = query.execute(Database.ReadOnly);
			
			if(rs.hasMore())
			{
				result = (AssinanteCampanha) (rs.next());
			}
		}
		catch(Exception e)
		{
			logger.error("Erro ao pesquisar IdCampanha:"+idCampanha+" do assinante "+msisdn+". Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return result;
	}
}
