package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.respostaAnexa;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;

/**
 * Mostra a tela de cadastro das informacoes de conteudo WIG
 * 
 * @author Joao Carlos
 * @since 17/11/2005
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 09/03/2007
 */
public class ShowCadastroFiltroMsisdnWigAction extends ShowActionHibernate
{
	private String codOperacao = null; //Constantes.MENU_PEDIDO_VOUCHER;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela()
	{
		return "cadastroFiltroMsisdnWig.vm";
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
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
