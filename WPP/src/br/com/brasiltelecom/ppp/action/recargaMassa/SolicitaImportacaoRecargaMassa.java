package br.com.brasiltelecom.ppp.action.recargaMassa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Dispara o processo batch GPP para carga dos arquivos 
 * de lotes para recarga em massa
 * 
 * @author Bernardo Vergne Dias
 * Data: 09/08/2007
 */
public class SolicitaImportacaoRecargaMassa extends ActionPortalHibernate 
{
	private String codOperacao = null;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private static Thread thread 										= null;
	private static ImportacaoRecargaMassaThread importacaoRecargaMassa  = null;
		
	/**
	 * Implementa a lógica de negócio dessa ação.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisição HTTP que originou a execução dessa ação.
	 * @param response			Resposta HTTP a ser encaminhada ao usuário.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
		// Inicializa 
		
		String servGPP = (String)(servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR));
		String portGPP = (String)(servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR));
		String raizRecargaMassa = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_RECARGA_MASSA));
		importacaoRecargaMassa = new ImportacaoRecargaMassaThread(servGPP, portGPP, raizRecargaMassa, logger);
		
		// Se alguma thread estiver em execução o action apenas retorna um aviso para o usuario. 
		
		if (thread != null && thread.isAlive())
		{
			request.setAttribute("mensagem", "O processamento está em endamento. Aguarde o resultado.");	
		}
		else
		{
			thread = new Thread(importacaoRecargaMassa);
			thread.start();
			
			request.setAttribute("mensagem", "O processo de importação foi iniciado com sucesso. " +
							"O resultado será disponibilizado em breve. Aguarde.");
		}
		
		return actionMapping.findForward("success");
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
