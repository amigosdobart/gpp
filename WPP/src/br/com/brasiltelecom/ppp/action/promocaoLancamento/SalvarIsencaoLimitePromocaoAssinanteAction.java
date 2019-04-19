package br.com.brasiltelecom.ppp.action.promocaoLancamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.PromocaoAssinanteHome;
import br.com.brasiltelecom.ppp.portal.entity.PromocaoAssinante;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe responsavel por salvar as informacoes da Promocao Pula-Pula do assinante 
 * 
 * @author 	Daniel Ferreira
 * @since	20/04/2005
 * 
 */
public class SalvarIsencaoLimitePromocaoAssinanteAction extends ActionPortal 
{

	 private String codOperacao = Constantes.COD_SALVAR_ISENCAO_LIM_PULA;
	 Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Metodo principal da classe, reponsavel por salvar as informacoes referentes a Promocao Pula-Pula do Assinante 
	 * 
	 * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db parâmetro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */

	public ActionForward performPortal(ActionMapping actionMapping,
		ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,
		Database db) throws Exception 
	{
		
		ActionForward result = null;
			
		//HttpSession session = request.getSession();

		logger.info("Alteracao de Isenção de Limite de Bônus de Promocao de Assinante solicitada");
		db.begin();
		
		String msisdn = request.getParameter("msisdn");
		int promocaoAntiga = (new Integer(request.getParameter("promocaoAntiga"))).intValue();
		Integer isentoLimite = (request.getParameter("isentoLimite") != null) ? 
			new Integer(request.getParameter("isentoLimite")) : null;
		//String operador = request.getParameter("matricula");
		
		//String plano = null;
		//String servidor = null;
		//String porta = null;
		String observacao = "";
		
		try
		{
			PromocaoAssinante assinante = PromocaoAssinanteHome.findByID(db, msisdn, promocaoAntiga);
			
			if(assinante != null)
			{
				//Salvando a isencao de limite, caso exista
				if(isentoLimite != null)
				{
					assinante.setIndIsentoLimite(isentoLimite.intValue());
				}
				else
				{
					throw new Exception("Não foi possível atualizar a Isenção do Limite de Bônus da Promoção do Assinante"); 
				}
			}
			else
			{
				//Promocao do assinante nao encontrada
				throw new Exception("Promocao do assinante nao encontrada.");
			}
			
			request.setAttribute(Constantes.MENSAGEM, "Atualização da Isenção de Limite de Bônus da Promoção do Assinante realizada com sucesso!" + observacao);
			result = actionMapping.findForward("success");
		}
		catch (Exception e)
		{
			logger.error("Nao foi possivel realizar a alteracao da isencao do limite da promocao do assinante, problemas na conexao " +
					     "com o banco de dados (" + e.getMessage() + ")");
			throw e;
		}
		
		return result;
	}

	/**
	 * Metodo para pegar a Operacao.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}

}
