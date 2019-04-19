package br.com.brasiltelecom.ppp.action.consultaBlackList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.DescartaAssinanteHome;
import br.com.brasiltelecom.ppp.portal.entity.DescartaAssinante;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Manda a requisi��o para cadastrar novo n�mero na Descata Assinante (Black List)
 * @author	Geraldo Palmeira
 * @since	04/01/2007
 */

public class CadastraBlackListAction extends ActionPortal
{
	Logger logger = Logger.getLogger(this.getClass());
	private ActionForward result;

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
	{
		try
		{
			// Recebe os par�metros da tela do portal
			String msisdn  = request.getParameter("msisdn");
			String masc	   = request.getParameter("mascara");
			int mascara = Integer.parseInt(masc);
			
			DescartaAssinante descartaAssinante = null;
			
			db.begin();
			
			try
			{
				// Monta o objeto BlackList
				descartaAssinante = DescartaAssinanteHome.findByMsisdn(db, msisdn);
			}
			catch(Exception e)
			{
				logger.error("N�o foi poss�vel inserir o numero na Black List, problemas na conex�o com o banco de dados (" +
							e.getMessage() + ")");
				throw e;
			}
			
			// Verifica se existe registro para o msisdn pesquisado
			if (descartaAssinante == null)
			{	
				// Seta os par�metros no objeto
				DescartaAssinante blackList = new DescartaAssinante();
				blackList.setIdtMsisdn(msisdn);
				blackList.setIndMascara(mascara);
				
				try
				{
					// Insere registro na tabela 
					DescartaAssinanteHome.setDescartaAssinante(db, blackList);
					
					// Monta a mensagem a ser mostrada no portal
					request.setAttribute(Constantes.MENSAGEM,"N�mero de acesso inserido com sucesso.");
				}
				catch(Exception e)
				{
					logger.error("N�o foi poss�vel inserir o gerente feliz,(" +
								e.getMessage() + ")");
					
					// Monta a mensagem a ser mostrada no portal
					request.setAttribute(Constantes.MENSAGEM,"N�o foi poss�vel inserir o n�mero de acesso, tente novamente mais tarde.");
				}
			}
			else
			{
				// Se o msisdn j� existe na tabela monta a mensagem que ser� mostrada no portal
				request.setAttribute(Constantes.MENSAGEM,"Este n�mero de acesso j� est� cadastrado.");							
			}
			
			result = actionMapping.findForward("mensagem");

		}
		catch(Exception e)
		{
			logger.error("Erro ao inserir novo Gerente Feliz (" +
					e.getMessage() + ")");
		}
		
		return result;
	}
	
	/**
	 * Retorna o c�digo da opera��o
	 * @return String
	 */
	public String getOperacao()
	{
		return Constantes.COD_ATUALIZACAO_BLACK_LIST;
	}
}
