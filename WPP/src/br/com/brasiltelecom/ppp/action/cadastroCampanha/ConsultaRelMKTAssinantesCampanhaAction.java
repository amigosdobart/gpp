/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.cadastroCampanha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatório de assinantes por campanha
 * 
 * @author Joao Carlos
 * @since 13/03/2006
 */
public class ConsultaRelMKTAssinantesCampanhaAction extends ActionPortal
{
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
				throws Exception
	{
		ActionForward result = null;
		logger.debug("Consulta por relatório de assinantes inscritos em uma determinada campanha");
		// Busca o parametro definido para a campanha
		String idCampanha=request.getParameter("idCampanha");
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta por relatório de assinantes inscritos em uma determinada campanha realizado com sucesso!");
		// Realiza a chamada da servlet que irah gerar o relatorio
		String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTAssinantesCampanhaDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+ID_CAMPANHA="+idCampanha;
		request.setAttribute("endereco",endereco);
		result = actionMapping.findForward("redirect");
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return Constantes.COD_CADASTRAR_CAMPANHA;
	}
}
