
package br.com.brasiltelecom.ppp.action.consultaFGN;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Bernardo Vergne Dias
 * Data: 30-05-2007
 */
public class ShowFiltroConsultaFGN extends ShowActionHibernate 
{
	private String codOperacao = Constantes.MENU_CONSULTA_FGN;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "filtroConsultaFGN.vm";
	}
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		// Apenas mostra a tela de filtro		
	}

	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}
