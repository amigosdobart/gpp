package br.com.brasiltelecom.ppp.action.recargaMassa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.interfacegpp.AprovacaoRecargaMassaGPP;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Dispara o processo batch GPP para aprova��o ou rejeicao de lote
 * de recarga em massa
 * 
 * @author Bernardo Vergne Dias
 * Data: 09/08/2007
 */
public class ConcessaoRecargaMassa extends ActionPortalHibernate 
{
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());
	private boolean emProcessamento = false;	
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param response			Resposta HTTP a ser encaminhada ao usu�rio.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
		if (emProcessamento)
		{
			request.setAttribute("mensagem", "Existe uma aprova��o/rejei��o em andamento. Tente novamente" +
					"em alguns instantes.");
		}
		else
		{
			emProcessamento = true;
			try
			{
				//	Inicializa 
				
				String servGPP 	= (String)(servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR));
				String portGPP 	= (String)(servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR));
				String lote 	= request.getParameter("lote");
				String usuario 	= ((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula();	
				
				//	Requisicao CORBA ao GPP
				if (request.getParameter("rejeitar") != null && 
						request.getParameter("rejeitar").equals("true"))
				{
					AprovacaoRecargaMassaGPP.rejeitarLoteRecarga(servGPP, portGPP, lote, usuario);
					request.setAttribute("mensagem", "Lote rejeitado com sucesso!");
					
				}
				else
				{
					AprovacaoRecargaMassaGPP.aprovarLoteRecarga(servGPP, portGPP, lote, usuario);
					request.setAttribute("mensagem", "Lote aprovado com sucesso!");
					
				}
			}
			catch (Exception e)
			{
				request.setAttribute("mensagem", "[erro]Erro ao realizar aprova��o/rejei��o de lote recarga em massa.");
				logger.error("Erro ao iniciar concess�o de recarga em massa.");	
			}
			emProcessamento = false;
		}
		
		return actionMapping.findForward("success");
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
