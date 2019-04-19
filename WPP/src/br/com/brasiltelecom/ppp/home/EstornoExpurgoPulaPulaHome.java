/*
 * Created on 19/12/2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 * 
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;

import org.apache.struts.action.ActionServlet;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.interfacegpp.AprovacaoPreviaGPP;
import br.com.brasiltelecom.ppp.portal.entity.CastorResultObject;
import br.com.brasiltelecom.ppp.portal.entity.LoteEstornoExpurgoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * @author Bernardo Vergne Dias
 * @since 19/12/2006
 */
public class EstornoExpurgoPulaPulaHome 
{

	/**
	 * Método estático responsável pela aquisição da lista de registros
	 * agrupados por lote
	 * 
	 * @param db Conexão com o Banco de Dados.
	 * @throws PersistenceException  Exceção lançada para possíveis erros de consulta dos dados.
	 * @return result ArrayList de objetos do tipo <code>LoteEstornoExpurgoPulaPula</code>.
	 */
	public static ArrayList findAllGroupByLote(Database db) throws PersistenceException 
	{

		OQLQuery query = null;
		CastorResultObject castorObj = null;
		QueryResults rs = null;
		ArrayList result = null;
		
		try 
		{
			query = db.getOQLQuery("CALL SQL SELECT "
							+ " idt_lote as id, "
							+ " count(*) as field00, "
							+ " null as field01, "
							+ " null as field02, "
							+ " null as field03, "
							+ " null as field04, "
							+ " null as field05, "
							+ " null as field06, "
							+ " null as field07, "
							+ " null as field08, "
							+ " null as field09, "
							+ " null as field10, "
							+ " null as field11, "
							+ " null as field12, "
							+ " null as field13, "
							+ " null as field14, "
							+ " null as field15, "
							+ " null as field16, "
							+ " null as field17, "
							+ " null as field18, "
							+ " null as field19 "
							+ " FROM tbl_int_estorno_pula_pula"
							+ " WHERE idt_status_processamento = 'V'"
							+ " GROUP BY idt_lote "
							+ " AS br.com.brasiltelecom.ppp.portal.entity.CastorResultObject ");

			// query.bind("");
			rs = query.execute(Database.ReadOnly);

			LoteEstornoExpurgoPulaPula lote;
			result = new ArrayList();

			while (rs.hasMore()) 
			{
				castorObj = (CastorResultObject) rs.next();
				lote = new LoteEstornoExpurgoPulaPula();
				
				lote.setId(castorObj.getId());
				lote.setNumRegistros(Integer.parseInt(castorObj.getField00()));

				result.add(lote);
			}

		} finally 
		{
			if (rs != null) 
			{
				rs.close();
			}
			if (query != null) 
			{
				query.close();
			}
		}

		return result;
	}
	
	/**
	 * Método estático responsável pela aprovacao de lote
	 * 
	 * @param db Conexão com o Banco de Dados.
	 * @param loteId Id do Lote.
	 * @throws PersistenceException  Exceção lançada para possíveis erros de consulta dos dados.
	 */
	public static void aprovarLote(ActionServlet servlet, String loteId) throws Exception
	{		
		try 
		{
			String serv = (String)(servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR));
			String port = (String)(servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR));
			AprovacaoPreviaGPP.aprovarLote(serv, port, loteId);
		}
		catch (Exception e)
		{
			throw new Exception(e.getMessage());
		} 
	}
	
	/**
	 * Método estático responsável pela rejeição de lote (remoção do banco)
	 * 
	 * @param db Conexão com o Banco de Dados.
	 * @param loteId Id do Lote.
	 * @throws PersistenceException  Exceção lançada para possíveis erros de consulta dos dados.
	 */
	public static void rejeitarLote(ActionServlet servlet, String loteId) throws Exception 
	{
		try 
		{
			String serv = (String)(servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR));
			String port = (String)(servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR));
			AprovacaoPreviaGPP.rejeitarLote(serv, port, loteId);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} 
	}
}