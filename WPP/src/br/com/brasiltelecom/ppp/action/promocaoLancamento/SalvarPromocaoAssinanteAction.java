package br.com.brasiltelecom.ppp.action.promocaoLancamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.CodigosRetornoHome;
import br.com.brasiltelecom.ppp.interfacegpp.AprovisionamentoGPP;
import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe responsavel por salvar as informacoes da Promocao Pula-Pula do assinante 
 * 
 * @author 	Daniel Ferreira
 * @since	20/04/2005
 * 
 */
public class SalvarPromocaoAssinanteAction extends ActionPortal 
{

	 private String codOperacao = Constantes.COD_SALVAR_TROCA_PULA_PULA;
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

		logger.info("Alteracao de Promocao de Assinante solicitada");
		db.begin();
		
		String msisdn = request.getParameter("msisdn");
		int promocao = (new Integer(request.getParameter("promocao"))).intValue();
		String operador = request.getParameter("matricula");
		int tipoDocumento = Integer.parseInt(request.getParameter("listaTipoDocumento"));
		String numDocumento = request.getParameter("descricaoDocumento");
		int motivo = Integer.parseInt(request.getParameter("listaMotivoTrocaProm"));
		
		short ret = -1;
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
				
		try
		{
			ret = AprovisionamentoGPP.trocaPulaPulaPPP(msisdn, promocao, operador, motivo, tipoDocumento, numDocumento, servidor, porta);
			
			if(ret == Constantes.GPP_RET_OPERACAO_OK)
			{
				request.setAttribute(Constantes.MENSAGEM, "Atualização da Promoção do Assinante realizada com sucesso!");
			}
			else
			{
			    CodigosRetorno descricao = CodigosRetornoHome.findByVlrRetorno(String.valueOf(ret), db);
			    String mensagem = "Atualização da Promoção do Assinante realizada com erro: ";
			    mensagem += ((descricao != null) && (descricao.getDescRetorno() != null)) ? descricao.getDescRetorno() : "Erro técnico.";
			    request.setAttribute(Constantes.MENSAGEM, mensagem);
			}
			
			result = actionMapping.findForward("success");
		}
		
		catch (Exception e)
		{
			logger.error("Nao foi possivel realizar a alteracao da promocao do assinante, problemas na conexao " +
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
