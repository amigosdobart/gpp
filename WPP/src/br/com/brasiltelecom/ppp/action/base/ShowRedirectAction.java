package br.com.brasiltelecom.ppp.action.base;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

/**
 * Classe Respons�vel por redirecionar aplica��o fora do Struts
 * 
 * @author Alberto Magno
 * @since 26/05/2003
 * 
 * Atualizado por: Bernardo Dias
 * Descri��o: Classe escrita novamente.
 * Data: 22/10/2007
 * 
 */
public class ShowRedirectAction extends ShowActionHibernate
{
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "redirect.vm";
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
		context.put("endereco", (String) request.getAttribute("endereco"));
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return null;
	}
}
