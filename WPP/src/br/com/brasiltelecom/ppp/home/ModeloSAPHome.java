package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.ModeloSAP;

/**
 * Efetua busca de modelos de aparelhos
 * na tabela HSID_MODELO_SAP
 * 
 * @author	Marcelo Alves Araujo
 * @since	13/11/2006
 */
public class ModeloSAPHome
{	
	/**  
	 * M�todo para verificar exist�ncia do modelo.
	 * @param db 				Conex�o com o Banco de Dados.
	 * @param codigoSAP			C�digo do modelo	
	 * @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * @return result Objeto ModeloSAP com informa��es do aparelho.
	 */
	public static ModeloSAP findByCodigoSAP(Database db, int codigoSAP) throws PersistenceException 
	{
		OQLQuery query = null;
		ModeloSAP result = null;
		QueryResults rs = null;

		try
		{
			db.begin();
			query = db.getOQLQuery(	"select a " +
									"from br.com.brasiltelecom.ppp.portal.entity.ModeloSAP a " +
									"where a.codigoSAP = $1");

			query.bind(codigoSAP);
			
			rs = query.execute();
				
			if(rs.hasMore())
				result = (ModeloSAP) rs.next();
		}
		finally
		{
			db.commit();
			if(rs != null) 
				rs.close();
			if(query != null)
				query.close();	
		}
			
		return result;
	}
}