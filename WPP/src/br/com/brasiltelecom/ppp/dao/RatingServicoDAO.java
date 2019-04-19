package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoServicoSFA;
import com.brt.gpp.comum.mapeamentos.entidade.Modulacao;
import com.brt.gpp.comum.mapeamentos.entidade.Rating;
import com.brt.gpp.comum.mapeamentos.entidade.RatingServico;

/**
 * Interface de acesso ao cadastro de RatingServico.
 * 
 * @author Lucas Andrade
 * Data: 15/04/2008
 */
public class RatingServicoDAO
{
	
	/**  
	 * Consulta uma lista de RatingServico de acordo com o rating.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>RatingServico</code>.
	 */
	public static List findByRate(Session session, Rating rating)
	{
		if (rating == null)
			return null;
		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.RatingServico a " +
				"WHERE a.tipChamada.rateName = ? ");	
		
		query.setString(0, rating.getRateName());
		
		return query.list();
	}
	
	/**
	 * Consulta um RatingServico por caracteristicas.
	 * @param session Sessão do hibernate
	 * @param csp OperadoraLD
	 * @param modulacao Modulação da chamada
	 * @param tipChamada Tipo da Chamada
	 * @param planoPreco Plano de preços
	 * @param codigoServicoSFA Código SFA
	 * @return RatingServico
	 */
	public static List findByCaracteristicas(Session session, String csp, Modulacao modulacao,
			Rating tipChamada, Categoria categoria, CodigoServicoSFA codigoServicoSFA)
	{
		Query query = session.createQuery(
			" FROM com.brt.gpp.comum.mapeamentos.entidade.RatingServico a" +
			" WHERE a.csp = ? " +
			" AND a.modulacao = ? " +
			" AND a.tipChamada = ? " +
			" AND a.planoPreco.categoria = ? " + 
			" AND a.idtTipoServico = ? ");
		
		query.setString(0, csp);
		query.setEntity(1, modulacao);
		query.setEntity(2, tipChamada);
		query.setEntity(3, categoria);
		query.setString(4, codigoServicoSFA.getIdtTipoServico());
		
		return query.list();
	}
	
	/**
	 * Inclui um RatingServico.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param RatingServico 	Entidade <code>RatingServico</code>.
	 */
	public static void incluirRecargaServico(Session session, RatingServico ratingServico) 
	{	
		session.save(ratingServico);
	}
	
	/**
	 * Remove uma RatingServico.
	 * 
	 * @param session 			Session session
	 * @param RatingServico 	Entidade <code>RatingServico</code>.
	 */
	public static void removerRecargaServico(Session session, RatingServico ratingServico) 
	{	
		session.delete(ratingServico);
	}

}