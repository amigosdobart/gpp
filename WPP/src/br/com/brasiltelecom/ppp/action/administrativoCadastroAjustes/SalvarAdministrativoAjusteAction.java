/*
 * Created on 22/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.administrativoCadastroAjustes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.AdministrativoAjusteHome;
import br.com.brasiltelecom.ppp.portal.entity.Origem;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * Persiste os motivos de ajuste
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class SalvarAdministrativoAjusteAction extends ActionPortal {

	/********Atributos da Classe*******/
	private String codOperacao;
	/*
	 * Inicializa o log 
	 */
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
		
		int idOrigem = 0;
		int idCanal = 0;
		if (request.getParameter("idOrigem") != null && !request.getParameter("idOrigem").toString().equals("")){
			idOrigem = Integer.parseInt(request.getParameter("idOrigem"));
		}
		if (request.getParameter("idCanal") != null && !request.getParameter("idCanal").toString().equals("")){
			idCanal = Integer.parseInt(request.getParameter("idCanal"));
		}
		
		try
		{
			Origem origem = AdministrativoAjusteHome.findByID(db, idOrigem, idCanal);
		
			if("salvar".equals(acao)){
	
				boolean novo = false;
	
				if(idOrigem == 0) {
					origem = new Origem();
					novo = true;
				}
	
				AdministrativoAjusteHome.setByRequest(origem, request, db);	
	
				if(novo) {
					logger.info("Inclusão de motivo de ajuste solicitada");
					AdministrativoAjusteHome.criarOrigem(origem,db);
					this.codOperacao = Constantes.COD_GRAVAR_MOTIVO_AJUSTE;
					request.setAttribute(Constantes.MENSAGEM, "Motivo de ajuste \'"+ origem.getDescOrigem() +"\' criado com sucesso");
				}
				else {
					logger.info("Alteração de motivo de ajuste solicitada");
					this.codOperacao = Constantes.COD_ALTERAR_MOTIVO_AJUSTE;	
					request.setAttribute(Constantes.MENSAGEM, "Motivo de ajuste \'"+ origem.getDescOrigem() +"\' alterado com sucesso");
				}	
			
				result = actionMapping.findForward("success");

			} else if ("excluir".equals(acao)){
				logger.info("Exclusão de motivo de ajuste solicitada");
				db.remove(origem);
				request.setAttribute(Constantes.MENSAGEM, "Motivo de ajuste \'"+ origem.getDescOrigem() +"\' excluído com sucesso");			
				result = actionMapping.findForward("successDelete");
				this.codOperacao = Constantes.COD_EXCLUIR_MOTIVO_AJUSTE;
			}
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a inclusão/alteração/exclusão de motivo ajuste, problemas na conexão com o banco de dados (" +
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
