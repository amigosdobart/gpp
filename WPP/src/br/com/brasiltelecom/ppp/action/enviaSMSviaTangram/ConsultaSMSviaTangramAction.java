package br.com.brasiltelecom.ppp.action.enviaSMSviaTangram;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.comum.mapeamentos.entidade.TanConteudoMensagem;
import com.brt.gpp.comum.mapeamentos.entidade.TanNotificacao;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.ConsultaSMSviaTangramDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela com o resultado da pesquisa de SMS enviados via Tangram
 * 
 * @author Jorge Abreu
 * @since 10/10/2007
 */
public class ConsultaSMSviaTangramAction extends ShowActionHibernate 
{	    
	private String codOperacao = Constantes.MENU_ENVIA_SMS_TANGRAM;	
	Logger logger = Logger.getLogger(this.getClass());
	    
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "ConsultaSMSviaTangram.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception 
	{
		Session session = null;
		TanNotificacao entidade;
		
		try
		{
			
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			List resultado = ConsultaSMSviaTangramDAO.findAllNotificacao(session);
			
			System.out.println("Lista de Notificacao: \n");
			
			for(Iterator it = resultado.iterator(); it.hasNext(); )
			{
				entidade = (TanNotificacao)it.next();
				System.out.println("\n Registro: "+ 
						           entidade.getIdNotificacao() +" "+ 
						           entidade.getDestinoMensagem().getIdtMsisdnDestino() + " " +
						           entidade.getDestinoMensagem().getRequisicao().getIdRequisicao() + " " +
						           leConteudoMensagem(entidade.getDestinoMensagem().getRequisicao().getConteudosMensagem())+ " " +
						           entidade.getIdtStatusNotificacao());
	
			}	
			context.put("action", this);
			request.setAttribute("resultado", resultado);
			session.close();
			
		}
		catch(Exception e)
		{
			session.close(); 
			logger.error(e);
			
		}
							
		context.put("resultado", request.getAttribute("resultado"));
	}		

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
    
	/**
	 * Método para percorrer os elementos ConteudoMensagem concatenando as mensagens enviadas em uma mesma requisicao.
	 * @param conteudo - Lista com objetos ConteudoMensagem
	 * @return String - Todos os textoConteudo de cada objeto ConteudoMensagem concatenados.
	 */
	public String leConteudoMensagem(Collection conteudo)
	{
		StringBuffer msg = new StringBuffer();
		for(Iterator it = conteudo.iterator();it.hasNext();)
			msg.append(((TanConteudoMensagem)it.next()).getTextoConteudo() + " ");
		
		return msg.toString();
	}
}