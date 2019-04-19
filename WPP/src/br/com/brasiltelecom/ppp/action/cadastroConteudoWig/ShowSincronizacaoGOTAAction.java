package br.com.brasiltelecom.ppp.action.cadastroConteudoWig;

import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de limpa ache
 * 
 * @author Bernardo Vergne Dias
 * Data: 17/04/2007
 */
public class ShowSincronizacaoGOTAAction extends ShowActionHibernate
{
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela()
	{
		return "sincronizacaoGota.vm";
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
		try 
		{
			context.put("limpaCacheURL", getLimpaCacheURL().toString());
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao gerar link para sincronização do G-OTA. <br><br>" + e);
			logger.error("Erro ao gerar link (limpaCache) para sincronização do G-OTA. " + e.getMessage());	
		}
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
	
	/**
	 * Retorna a URL do limpaCache
	 */
	private URL getLimpaCacheURL()
	{
		try
		{
			ServletContext context 	= getServlet().getServletContext();
			String servidor  		= (String)context.getAttribute(Constantes.WIG_NOME_SERVIDOR);
			String porta     		= (String)context.getAttribute(Constantes.WIG_PORTA_SERVIDOR);
			return new URL("http://"+servidor+":"+porta+"/wig/limpaCache");	
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
