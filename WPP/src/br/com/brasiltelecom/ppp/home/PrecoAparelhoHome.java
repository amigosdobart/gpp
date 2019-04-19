package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.PrecoAparelho;

/**
 * Efetua busca, inser��o e atualiza��o de pre�os
 * de aparelhos na tabela HSID_PRECO_APARELHO
 * 
 * @author	Marcelo Alves Araujo
 * @since	10/11/2006
 */
public class PrecoAparelhoHome
{	
	/**  
	 * M�todo para consulta do pre�o de um aparelho a partir do c�digo SAP.
	 * @param db 				Conex�o com o Banco de Dados.
	 * @param codigoSAP			C�digo do modelo	
	 * @param codigoNacional	C�digo nacional
	 * @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * @return result Objeto PrecoAparelho com informa��es de pre�o do aparelho.
	 */
	public static PrecoAparelho findByCodigoSAP(Database db, int codigoSAP, int codigoNacional) throws PersistenceException 
	{
		OQLQuery query = null;
		PrecoAparelho result = null;
		QueryResults rs = null;

		try
		{
			
			query = db.getOQLQuery(	"select a " +
									"from br.com.brasiltelecom.ppp.portal.entity.PrecoAparelho a " +
									"where a.codigoSAP = $1 " +
									"and a.codigoNacional = $2 " +
									"order by a.dataInicioValidade desc");

			query.bind(codigoSAP);
			query.bind(codigoNacional);
			
			rs = query.execute();
				
			if(rs.hasMore())
				result = (PrecoAparelho) rs.next();
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
	 * Insere novo registro na HSID_PRECO_APARELHO
	 * @param db 			Conex�o com o Banco de Dados.
	 * @param precoAparelho	Pre�o do aparelho a ser inseerido
	 * @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 */
	public static void inserirPrecoAparelho(Database db, PrecoAparelho precoAparelho) throws PersistenceException 
	{
		db.begin();
		db.create(precoAparelho);
		db.commit();
	}	
	
	/**  
	 * Atualiza registro na HSID_PRECO_APARELHO
	 * @param db 			Conex�o com o Banco de Dados.
	 * @param precoAparelho	Pre�o do aparelho a ser inseerido
	 * @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 */
	public static void atualizaPrecoAparelho(Database db, PrecoAparelho precoAparelho) throws PersistenceException 
	{
		db.begin();
		db.update(precoAparelho);
		db.commit();
	}
}
