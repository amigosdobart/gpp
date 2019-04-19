package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTSumarizacaoCampanha;

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
	public class ConsultaRelMKTSumarizacaoCampanhaAction extends ActionPortal
	{
		private Logger logger = Logger.getLogger(this.getClass());
		
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
		
		String dataInicial = null;
		String dataFinal = null;
		
		// Busca o parametro definido para a campanha
		dataInicial = request.getParameter("dataInicial");
		dataFinal = request.getParameter("dataFinal");
		String tipoFormato = request.getParameter("DESFORMAT");
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta por relatório de assinantes inscritos em uma determinada campanha realizado com sucesso!");
		
		// Realiza a chamada da servlet que irah gerar o relatorio
		String endereco = null;
		
		if (tipoFormato.equals("DELIMITED"))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=MKTSumarizacaoCampanhaDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml" +
					   "+DATA_INICIAL="+dataInicial+
					   "+DATA_FINAL="+dataFinal;
		}
		else
		{
			endereco = "../reports/rwservlet?ppp+MKTSumarizacaoCampanha.rdf+"+request.getParameter("DESFORMAT")+
					   "+DATA_INICIAL="+dataInicial+
			           "+DATA_FINAL="+dataFinal;
		}

		request.setAttribute("endereco",endereco);
		result = actionMapping.findForward("redirect");
		return result;
	}


	public String getOperacao()
	{
		return Constantes.COD_CONSULTA_SUMARIZACAO_CAMPANHA;
	}
}