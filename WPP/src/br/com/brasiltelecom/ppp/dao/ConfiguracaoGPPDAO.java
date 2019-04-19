package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoGPP;

/**
 * Interface de acesso ao cadastro de Configurações GPP.
 * 
 * @author Geraldo Palmeira
 * Criado em: 15/06/2007
 */
public class ConfiguracaoGPPDAO
{
	
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>Configuracao</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.Configuracao a");	
		return query.list();
	}
	
	/**
	 * Consulta uma Configuracao a partir do Configuracao
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param String 				idSistemaOrigem
	 * @return 						Instancia de <code>Configuracao</code>.
	 */
	public static ConfiguracaoGPP findByIdConfiguracao(Session session, String idConfiguracao)
	{
		return (ConfiguracaoGPP)session.load(com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoGPP.class, idConfiguracao);
	}
}