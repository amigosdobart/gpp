package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoServicoSFA;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.RecargaServico;
import com.brt.gpp.comum.mapeamentos.entidade.SistemaOrigem;

/**
 * Interface de acesso ao cadastro de RecargaServico.
 * 
 * @author Bernardo Dias
 * Data: 29/09/2007
 */
public class RecargaServicoDAO
{
	
	/**  
	 * Consulta uma lista de RecargaServico de acordo com a origem de recarga.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>RecargaServico</code>.
	 */
	public static List findByOrigem(Session session, OrigemRecarga origem)
	{
		if (origem == null)
			return null;
		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.RecargaServico a " +
				"WHERE a.origem.idCanal = ? AND a.origem.idOrigem = ? " +
				"ORDER BY a.sistemaOrigem.idSistemaOrigem ");	
		
		query.setString(0, origem.getIdCanal());
		query.setString(1, origem.getIdOrigem());
		
		return query.list();
	}
	
	/**
	 * Consulta um RecargaServico por caracteristicas
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param sistemaOrigem 		Instancia de <code>SistemaOrigem<code>.
	 * @param origem 				Instancia de <code>OrigemRecarga<code>.
	 * @param categoria 			Instancia de <code>Categoria<code>.
	 * @param codigoServicoSFA 		Instancia de <code>CodigoServicoSFA<code>.
	 * @return 						Coleção de <code>RecargaServico</code>.
	 */
	public static List findByCaracteristicas(Session session, OrigemRecarga origem, 
			SistemaOrigem sistemaOrigem, Categoria categoria, CodigoServicoSFA codigoServicoSFA)
	{
		if (origem == null)
			return null;
		
		Query query = session.createQuery(
			" FROM com.brt.gpp.comum.mapeamentos.entidade.RecargaServico a" +
			" WHERE a.origem.idCanal = ? AND a.origem.idOrigem = ? AND " +
			"       a.sistemaOrigem = ?  AND a.codigoServicoSFA = ?" +
			"       AND a.planoPreco.categoria = ?  " );
		
		query.setString(0, origem.getIdCanal());
		query.setString(1, origem.getIdOrigem());
		query.setEntity(2, sistemaOrigem);
		query.setEntity(3, codigoServicoSFA);
		query.setEntity(4, categoria);
		
		return query.list();
	}
	
	/**
	 * Inclui uma RecargaServico.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param recargaServico 	Entidade <code>RecargaServico</code>.
	 */
	public static void incluirRecargaServico(Session session, RecargaServico recargaServico) 
	{	
		session.save(recargaServico);
	}
	
	/**
	 * Remove uma RecargaServico.
	 * 
	 * @param session 			Session session
	 * @param recargaServico 	Entidade <code>RecargaServico</code>.
	 */
	public static void removerRecargaServico(Session session, RecargaServico recargaServico) 
	{	
		session.delete(recargaServico);
	}

}