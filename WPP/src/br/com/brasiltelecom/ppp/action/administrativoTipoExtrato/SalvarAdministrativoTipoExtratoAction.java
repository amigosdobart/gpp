/*
 * Created on 22/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.administrativoTipoExtrato;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.AdministrativoTipoExtratoHome;
import br.com.brasiltelecom.ppp.portal.entity.TipoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Edita e exclui comprovantes de serviço
 * 
 * @author André Gonçalves
 * @since 20/05/2004
 */
public class SalvarAdministrativoTipoExtratoAction extends ActionPortal {

	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
		ActionMapping actionMapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response,
		Database db)
		throws Exception {
		ActionForward result = null;
		String acao = request.getParameter("acao");
				
		//HttpSession session = request.getSession();
		
		db.begin();
	
		int idTipoExtrato = 0;
		if (request.getParameter("idTipoExtrato") != null && !request.getParameter("idTipoExtrato").toString().equals("")){
			idTipoExtrato = Integer.parseInt(request.getParameter("idTipoExtrato"));
		}
		
		try
		{
			TipoExtrato te = AdministrativoTipoExtratoHome.findByID(db, idTipoExtrato);
			
			if("salvar".equals(acao)){
				logger.info("Alteração de tipo de extrato solicitada");
			
				boolean novo = false;
		
				if(idTipoExtrato == 0) {
					te = new TipoExtrato();
					novo = true;
				}
		
				AdministrativoTipoExtratoHome.setByRequest(te, request, db);	
		
				if(novo) {
					AdministrativoTipoExtratoHome.criarTipoExtrato(te,db);
				}
				else {
					request.setAttribute(Constantes.MENSAGEM, "Motivo de ajuste \'" + te.getDescTipoExtrato() + 
									"\' alterado com sucesso!");
					this.codOperacao = Constantes.COD_ALTERAR_TIPO_EXTRATO;
				}	
	
				result = actionMapping.findForward("success");
	
			} else if ("excluir".equals(acao)){
				logger.info("Exclusão de tipo de extrato solicitada");
				te.setAtivo(0);
				request.setAttribute(Constantes.MENSAGEM, "Motivo de ajuste \'" + te.getDescTipoExtrato() + 
								"\' excluído com sucesso!");
				result = actionMapping.findForward("successDelete");
				this.codOperacao = Constantes.COD_EXCLUIR_TIPO_EXTRATO;
			}			
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a alteração/exclusão de tipo de extrato, problemas na conexão com o banco de dados (" +
								e.getMessage() + ")");
			throw e;
		}
						
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
