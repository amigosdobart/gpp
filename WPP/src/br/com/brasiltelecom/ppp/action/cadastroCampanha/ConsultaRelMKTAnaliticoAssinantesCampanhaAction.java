package br.com.brasiltelecom.ppp.action.cadastroCampanha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * @author Geraldo Palmeira
 * @since 16/03/2006
 *
 */
public class ConsultaRelMKTAnaliticoAssinantesCampanhaAction extends ActionPortal
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
		String idCampanha  = request.getParameter("idCampanha");
		String tipoFormato = request.getParameter("DESFORMAT");
		
		// Realiza a chamada da servlet que irah gerar o relatorio
		
		String endereco = null;
		
		if (tipoFormato.equals("DELIMITED"))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=MKTAnaliticoAssinantesCampanhaDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml" +
					   "+ID_CAMPANHA="+idCampanha;
		}
		else
		{
			endereco = "../reports/rwservlet?ppp+MKTAnaliticoAssinantesCampanha.rdf+"+request.getParameter("DESFORMAT")+
					   "+ID_CAMPANHA="+idCampanha;
		}
		request.setAttribute(Constantes.MENSAGEM, "Consulta por relatório de assinantes inscritos em uma determinada campanha realizado com sucesso!");
		request.setAttribute("endereco",endereco);
		result = actionMapping.findForward("redirect");
		return result;
	}

	public String getOperacao() 
	{
		return Constantes.COD_CONSULTA_ANALITICO_ASS_CAMPANHA;
	}
}