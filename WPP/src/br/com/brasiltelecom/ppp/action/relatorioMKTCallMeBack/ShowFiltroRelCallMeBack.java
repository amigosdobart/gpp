
package br.com.brasiltelecom.ppp.action.relatorioMKTCallMeBack;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;

/**
 * @author Lucas Mindêllo de Andrade
 * Data: 11-03-2008
 */
public class ShowFiltroRelCallMeBack extends ShowActionHibernate 
{
	private String codOperacao = "MENU_REL_MKT_CALLBACK";
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "filtroRelatorioCallMeBack.vm";
	}
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		// Apenas mostra a tela de filtro		
	}

	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}
