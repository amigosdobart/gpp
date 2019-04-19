package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.CastorResultObject;
import br.com.brasiltelecom.ppp.portal.entity.SolicitacoesRelatorio;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe respons�vel pelas consultas/atualiza��es no banco 
 * de dados na tabela de solicita��es de relat�rio
 * 
 * @author Marcelo Alves Araujo
 * @since  14/09/2005
 */

public class SolicitacoesRelatorioHome 
{
	/**
	 * <P><b>Metodo....:</b> findAll
	 * <P><b>Descricao.:</b> M�todo est�tico respons�vel pela busca de todas as 
	 * 						 solicita��es de relat�rios
	 * @param db - Inst�ncia do banco de dados
	 * @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * @return Collection com todas as consultas
	 */
	public static Collection findAll(Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.SolicitacoesRelatorio a");
								
			rs = query.execute();
			while (rs.hasMore())
				result.add((SolicitacoesRelatorio) rs.next());
		}
		finally
		{
			if(rs != null)
				rs.close();
			
			if(query != null)
				query.close();
		}
		
		return result;
	}
	
	/**
	 * <P><b>Metodo....:</b> findByID
	 * <P><b>Descricao.:</b> M�todo est�tico respons�vel pela busca da 
	 * 						 solicita��o de relat�rio, de acordo como a ID
	 * @param db 			- Inst�ncia do banco de dados
	 * @param idSolicitacao	- Identifica��o da solicita��o de relat�rio
	 * @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * @return Objeto de SolicitacoesRelatorio com a consulta
	 */
	public static SolicitacoesRelatorio findByID(Database db, int idSolicitacao) throws PersistenceException 
	{
		OQLQuery query = null;
		SolicitacoesRelatorio solRel = null;
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery(	"select a " +
									"from br.com.brasiltelecom.ppp.portal.entity.SolicitacoesRelatorio a " +
									"where a.solicitacao = " + 
									idSolicitacao);
								
			rs = query.execute();
			if (rs.hasMore())
				solRel = (SolicitacoesRelatorio)rs.next();
		}
		finally
		{
			if(rs != null)
				rs.close();
			
			if(query != null)
				query.close();
		}
		
		return solRel;
	}
	
	/**
	 * <P><b>Metodo....:</b> findByOperador
	 * <P><b>Descricao.:</b> M�todo est�tico respons�vel pela busca da 
	 * 						 solicita��o de relat�rio, de acordo como o operador
	 * @param db 			- Inst�ncia do banco de dados
	 * @param operador		- Operador que solicitou os relat�rios
	 * @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * @return Collection com a consulta
	 */
	public static Collection findByOperador(Database db, String operador) throws PersistenceException 
	{
		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery(	"select a " +
									"from br.com.brasiltelecom.ppp.portal.entity.SolicitacoesRelatorio a " +
									"where a.operador = $1 and " +
									"a.tipoSolicitacao = $2 " +
									"order by a.solicitacao");
			query.bind(operador);
			query.bind(Constantes.CONSULTA_TEMPORARIA);
								
			rs = query.execute();
			while (rs.hasMore())
				result.add((SolicitacoesRelatorio) rs.next());
		}
		finally
		{
			if(rs != null)
				rs.close();
			
			if(query != null)
				query.close();
		}
		
		return result;
	}
	
	
	/**
	 * <P><b>Metodo....:</b> findNextID
	 * <P><b>Descricao.:</b> M�todo est�tico respons�vel pela busca do 
	 * 						 pr�ximo ID das solicita��es
	 * @param db - Inst�ncia do banco de dados
	 * @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * @return Collection com a consulta
	 */
	public static int findNextID(Database db) throws PersistenceException 
	{
		OQLQuery query = null;
		CastorResultObject result = null;
		QueryResults rs = null;

		try
		{
			query = db.getOQLQuery("CALL SQL " +
								   "SELECT " +
								   "SEQ_ID_SOLICITACAO.NEXTVAL as ID," +
								   "null AS field00, " +
								   "null AS field01, " +
								   "null AS field02, " +
								   "null AS field03, " +
								   "null AS field04, " +
								   "null AS field05, " +
								   "null AS field06, " +
								   "null AS field07, " +
								   "null AS field08, " +
								   "null AS field09, " +
								   "null AS field10, " +
								   "null AS field11, " +
								   "null AS field12, " +
								   "null AS field13, " +
								   "null AS field14, " +
								   "null AS field15, " +
								   "null AS field16, " +
								   "null AS field17, " +
								   "null AS field18, " +
								   "null AS field19 " +
								   "FROM DUAL " +
								   "AS br.com.brasiltelecom.ppp.portal.entity.CastorResultObject");
								
			rs = query.execute();
			if(rs.hasMore())
				result = (CastorResultObject)(rs.next());
		}
		finally
		{
			if(rs != null)
				rs.close();
			
			if(query != null)
				query.close();
		}
		return new Integer(result.getId()).intValue();
	}
}
