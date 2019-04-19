package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Classe de acesso a tabela da base de dados <code>TBL_GER_CONFIG_ASSINANTE</code>
 * 
 * @since 29/04/2008
 * @author Lucas Mindêllo de Andrade
 * @version 1.0
 */
public class ConfiguracaoAssinanteDAO 
{
	/**
	 * Busca todas as ocorrencias da <code>TBL_GER_CONFIG_ASSINANTE</code>
	 * @param session Sessao do Hibernate
	 * @return List de objetos <code>com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoAssinante</code>
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"  FROM com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoAssinante a " +
				" ORDER BY a.desConfiguracao "
				);
		return query.list();
	}
	
	/**
	 * Busca as ocorrencias baseando-se no <code>TIP_CONFIGURACAO</code>
	 * @param session Sessao do Hibernate
	 * @param tipConfiguracao TIP_CONFIGURACAO
	 * @return List de objetos <code>com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoAssinante</code>
	 */
	public static List findByTipConfiguracao(Session session, String tipConfiguracao)
	{
		Query query = session.createQuery(
				"  FROM com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoAssinante a " +
				" WHERE a.tipConfiguracao LIKE ? " +
				" ORDER BY a.desConfiguracao "
				);
		query.setString(0, tipConfiguracao);
		
		return query.list();
	}
}
