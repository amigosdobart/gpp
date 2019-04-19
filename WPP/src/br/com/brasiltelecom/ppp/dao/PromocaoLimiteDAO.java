package br.com.brasiltelecom.ppp.dao;


/**
 * DEPRECATED
 * 
 * Interface de acesso ao cadastro de <code>PromocaoLimite</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 14/02/2008
 */
public class PromocaoLimiteDAO
{
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>PromocaoLimite</code>.
	 *
	public static List findAll(Session session)
	{
		List result = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimite a " +
				"ORDER BY a.idtPromocao").list();

		return result; 
	}
    
    /**
     * Consulta uma PromocaoLimite vigente de acordo com o ID.
     * 
     * @param session               Sessão do Hibernate.
     * @param idtPromocao           ID da promocao
     * @return                      Instancia de <code>PromocaoLimite</code>.
     *
    public static PromocaoLimite findVigenteById(Session session, int idtPromocao)
    {
        Calendar c = Calendar.getInstance();
        
        Query query = session.createQuery(
                "FROM com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimite a " +
                "WHERE a.intPromocao = ? AND " +
                "      a.datIniPeriodo >= ? AND (a.datFimPeriodo < ? OR a.datFimPeriodo is null)");
        
        query.setInteger(0, idtPromocao);
        query.setDate(1, c.getTime());
        query.setDate(2, c.getTime());
        
        List result = query.list();

        if (result.size() == 1)
            return (PromocaoLimite)result.get(0);
        
        return null;
    }
    */
}
