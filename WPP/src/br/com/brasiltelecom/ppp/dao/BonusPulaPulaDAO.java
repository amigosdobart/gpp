package br.com.brasiltelecom.ppp.dao;


/**
 * DEPRECATED 
 * 
 * Interface de acesso ao cadastro de <code>BonusPulaPula</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 14/02/2008
 */
public class BonusPulaPulaDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>BonusPulaPula</code>.
	 *
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula a " +
				"ORDER BY a.datIniPeriodo, a.codigoNacional.idtCodigoNacional").list();

		return result; 
	}
	
	/**
	 * Consulta um BonusPulaPula vigente de acordo com os dados de chave para 
     * o período corrente (codigo nacional e plano de preco).
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param codNacional			Instancia de <code>CodigoNacional</code>
	 * @param planoPreco			Instancia de <code>PlanoPreco</code>
	 * @return 						Instancia de <code>BonusPulaPula</code>.
	 *
	public static BonusPulaPula findVigenteById(Session session, CodigoNacional codNacional, PlanoPreco planoPreco)
	{
        Calendar c = Calendar.getInstance();
        
		Query query = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula a " +
				"WHERE a.codigoNacionao = ? AND a.planoPreco = ? AND " +
                "      a.datIniPeriodo >= ? AND (a.datFimPeriodo < ? OR a.datFimPeriodo is null)");
        
		query.setEntity(0, codNacional);
        query.setEntity(1, planoPreco);
		query.setDate(2, c.getTime());
		query.setDate(3, c.getTime());
		
		List result = query.list();

		if (result.size() == 1)
			return (BonusPulaPula)result.get(0);
		
		return null;
	}
    */
}
