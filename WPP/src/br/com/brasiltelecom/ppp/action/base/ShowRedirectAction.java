package br.com.brasiltelecom.ppp.action.base;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

/**
 * Classe Responsável por redirecionar aplicação fora do Struts
 * 
 * @author Alberto Magno
 * @since 26/05/2003
 * 
 * Atualizado por: Bernardo Dias
 * Descrição: Classe escrita novamente.
 * Data: 22/10/2007
 * 
 */
public class ShowRedirectAction extends ShowActionHibernate
{
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "redirect.vm";
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
		context.put("endereco", (String) request.getAttribute("endereco"));
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return null;
	}
}
