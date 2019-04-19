/*
 * Created on 23/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 * Ainda pendente de ajustes
 * 
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

//import br.com.brasiltelecom.ppp.portal.entity.PromocaoLancamento;
import br.com.brasiltelecom.ppp.portal.entity.Promocao;
import br.com.brasiltelecom.ppp.portal.entity.PromocaoAssinante;

/**
 * Classe responsavel pelas consultas/atualizacoes no banco de dados relativas as promocoes do assinante
 * 
 * @author Luciano Vilela
 * @since 24/05/2004
 */
public class PromocaoAssinanteHome 
{
	/**  
	 *   Metodo estatico responsavel pela localizacao de um Grupo, atraves de seu identificador unico.
	 * 	 @param db Conexao com o Banco de Dados.
	 *   @param msisdn Numero para consulta do assinante
	 * 	 @throws PersistenceException Excecao lançada para possiveis erros de consulta dos dados.
	 * 	 @return result Objeto Grupo com todas suas propriedades e funcionalidades.
	 */

	public static PromocaoAssinante findByID(Database db, String msisdn, int idPromocao) throws PersistenceException 
	{

			OQLQuery query = null;
			PromocaoAssinante result = null;
			QueryResults rs = null;
			
			try
			{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.PromocaoAssinante a "+
					" where  a.MSISDN = $1 and a.promocaoId = $2");

				query.bind(msisdn);
				query.bind(idPromocao);
				rs = query.execute();
				
				if(rs.hasMore())
				{
					result = (PromocaoAssinante)rs.next();					
					
					//Obtendo os dados da promocao associada ao assinante
					Promocao promocao = PromocaoHome.findByID(db, idPromocao);
					result.setPromocao(promocao);
				}
			}
			finally
			{
				if(rs != null) rs.close();
				if(query != null)
				{
					query.close();	
				}	
			}
			
			return result;
	}

	public static Collection findByMsisdn(Database db, String msisdn) throws PersistenceException 
	{

			OQLQuery query = null;
			Collection result = new ArrayList();
			QueryResults rs = null;
			
			try
			{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.PromocaoAssinante a "+
					" where  a.MSISDN = $1");

				query.bind(msisdn);
				rs = query.execute();
				
				while(rs.hasMore())
				{
					PromocaoAssinante ass = (PromocaoAssinante)rs.next();					
					
					//Obtendo os dados da promocao associada ao assinante
					//Promocao promocao = PromocaoHome.findByID(db, ass.getPromocaoId());
				//	ass.setPromocao(promocao);
					
					result.add(ass);
				}
			}
			finally
			{
				if(rs != null) rs.close();
				if(query != null)
				{
					query.close();	
				}	
			}
			
			return result;
	}
	
}