package br.com.brasiltelecom.ppp.action.ajustaStatusServico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.interfacegpp.AprovisionamentoGPP;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe responsavel por salvar as informacoes da Promocao Pula-Pula do assinante 
 * 
 * @author 	Lawrence Josua
 * @since	18/05/2005
 * 
 */
public class TrocaStatusServicoAction extends ActionPortal 
{

	 private String codOperacao = Constantes.TROCA_STATUS_SERVICO;
	 Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Metodo principal da classe
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
			
		HttpSession session = request.getSession();

		logger.info("Alteracao de Status do Servico solicitada");
		db.begin();
		
		String msisdn = 55 + (String)session.getAttribute("msisdn");
		String operador = (String)session.getAttribute("matricula");
		Assinante assinante = (Assinante)session.getAttribute("assinante");
		int statusServico = (new Integer(request.getParameter("statusServico"))).intValue();
		
		String observacao = "";

		try
		{
			if(assinante != null)
			{
					//Efetuar commit para que as alteracoes no banco sejam visiveis no momento da modificacao do plano
					//do assinante. Para efeitos de log o objeto Database deve ser reativado
					//db.commit();
					//db.begin();
					
					String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
					String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);

					//Trocando o plano do assinante
					short retTrocaStatus = AprovisionamentoGPP.modificaDadoAssinante(msisdn, Constantes.TIPO_BLOQUEIO, String.valueOf(statusServico), operador, servidor, porta);
					if(retTrocaStatus != Constantes.GPP_RET_OPERACAO_OK)
					{
						observacao = "Não foi possível realizar a Troca de Status do Servico do assinante.";
					}
					else
					{
						observacao = "Atualização do Status de Servico do Assinante realizada com sucesso!";
					}
			}
			else
			{
				//Promocao do assinante nao encontrada
				throw new Exception("Assinante não encontrado.");
			}
			
			request.setAttribute(Constantes.MENSAGEM, observacao);
			result = actionMapping.findForward("success");
		}
		catch (Exception e)
		{
			logger.error("Nao foi possivel realizar a alteracao do Status de servico do assinante, problemas na conexao " +
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