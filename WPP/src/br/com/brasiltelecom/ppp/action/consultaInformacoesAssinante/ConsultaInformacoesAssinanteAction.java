package br.com.brasiltelecom.ppp.action.consultaInformacoesAssinante;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.entity.Operacao;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Henrique Canto
 * 
 * Adaptado por: Bernardo Vergne (novo XML de consulta completa)
 * Data: 08/10/2007
 */
public class ConsultaInformacoesAssinanteAction extends ActionPortalHibernate 
{
	private String codOperacao = "CONSULTAR_INF_ASSINANTE";
	
	Logger logger = Logger.getLogger(this.getClass());

	public ActionForward performPortal(ActionMapping actionMapping, 
			ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, 
			SessionFactory sessionFactory) throws Exception
	{
		Assinante assinante = new Assinante();
		String msisdn = "55" + request.getParameter("msisdn");
		
		
		Session session = null;
		
		try 
		{
			//	Busca informações de porta e servidor do GPP
			String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
			String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
			
			// Consulta assinante com os saldos pertencentes a cada categoria
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        assinante = ConsultaStatusAssinanteGPP.getAssinanteSaldos(msisdn, servidor, porta, session);
	        session.getTransaction().rollback();
	        
			// Busca o usuário do portal para saber se ele pode ou não alterar a lista de amigos toda hora
			Usuario login = (Usuario) request.getSession().getAttribute(Constantes.LOGIN);
			
			// Verifica se o assinante pode alterar a lista de amigos toda hora
			for (Iterator itGrp = login.getGrupos().iterator(); itGrp.hasNext(); )
			{
				for (Iterator itOp = ((Grupo)itGrp.next()).getOperacoes().iterator(); itOp.hasNext();)
					if(((Operacao)itOp.next()).getNome().equals(Constantes.COD_INSERIR_FF))
					{
						request.setAttribute("alteraFF","true");
						assinante = formataFF(assinante);
					}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		    logger.error("Erro ao consultar assinante: " + e);
		    
		    if (session != null)
		    	session.getTransaction().rollback();
		    
			throw new Exception("Conexão com a plataforma não disponível. " +
					"Favor tentar novamente ou notificar este erro ao Service Desk da TI.");
		}
		
		request.setAttribute("cn", msisdn.substring(2,4));
		request.setAttribute("assinante",assinante);
		
		return actionMapping.findForward("success");
	}

	public String getOperacao() 
	{
		return codOperacao;
	}
	
	/**
	 * Altera a lista de amigos toda hora formatando-a.
	 * @param assinante - Assinante consultado.
	 * @return
	 */
	private Assinante formataFF(Assinante assinante)
	{		
		String listaFF = "";
		short limiteFF = Constantes.LIMITEFF_PREPAGO;
		
		if (assinante.getPlanoPreco().equals("4") || assinante.getPlanoPreco().equals("5"))
			limiteFF = Constantes.LIMITEFF_HIBRIDO;
		
		// Formata o número para ser apresentado no VM
		for(int i = 0; i < limiteFF; i++)
		{
			if(assinante.getFriendFamily().length > i)
				if(assinante.getFriendFamily()[i].length()==11)
					listaFF = listaFF + assinante.getFriendFamily()[i].substring(3,7)+"-"+assinante.getFriendFamily()[i].substring(7)+";";
				else
					listaFF += " ;";
			else
				listaFF += " ;";
		}
		assinante.setFriendFamily(listaFF);
		
		
		return assinante;
	}

}
