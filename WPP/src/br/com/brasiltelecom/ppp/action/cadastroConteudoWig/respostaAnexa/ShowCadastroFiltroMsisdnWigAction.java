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
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela()
	{
		return "cadastroFiltroMsisdnWig.vm";
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
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
