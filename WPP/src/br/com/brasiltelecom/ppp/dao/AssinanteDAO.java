package br.com.brasiltelecom.ppp.dao;

import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.Assinante;

/**
 * Interface de acesso ao cadastro de <code>Assinante</code>.
 * 
 * @author Bernardo Dias
 * Criado em: 21/02/2008
 */
public class AssinanteDAO 
{
	/**  
	 * Consulta um <code>Assinante</code> pelo msisdn
	 *   
	 * @param session 				Sessão do Hibernate.
     * @param msisnd                MSISDN.
	 * @return 						Instancia de <code>Assinante</code>.
	 */
	public static Assinante findByMsisdn(Session session, String msisdn)
	{
		return (Assinante)session.get(com.brt.gpp.comum.mapeamentos.entidade.Assinante.class, msisdn);
	}
}