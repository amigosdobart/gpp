package br.com.brasiltelecom.ppp.dao;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Interface de acesso as tabelas de Servico de Assinante.
 * 
 * @author Jorge Abreu
 * Criado em: 17/12/2007
 */
public class ServicoAssinanteDAO
{
	
	/**  
	 * Consulta todos os servicos cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>Modelo</code>.
	 */
	public static Collection findAll(Session session) throws Exception
	{		
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.ServicoAssinante a");
		
		return query.list();
	}
		
}