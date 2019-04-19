package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTMigracoesPulaPula;

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
 * Gera o relatório de clientes em shutdown
 * 
 * @author Marcos C. Magalhães
 * @since 27/10/2005
 */
public class ConsultaRelMKTMigracoesPulaPulaAction extends ActionPortal 
{
	private String codOperacao = Constantes.COD_CONSULTAR_MIGRACOES_PP;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db) throws Exception 
	{					
		logger.info("Consulta por Relatório de Migrações Pula-Pula");

				
		String dataInicial	= request.getParameter("dataInicial");
		String dataFinal	= request.getParameter("dataFinal");
		
		// Monta o endereço e passa os parâmetros do relatório de migrações pula-pula
		String endereco = "../reports/rwservlet?ppp+MKTMigracaoPulaPula.rdf+"
						+ request.getParameter("DESFORMAT")
						+ "+DATAINICIAL="+dataInicial
						+ "+DATAFINAL="+dataFinal;
		
		// Se for delimitado
		if(request.getParameter("DESFORMAT").equals("DELIMITED"))
			endereco = "./relatoriosWPP?NOME_RELATORIO=MKTMigracaoPulaPulaDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DATAINICIAL="+request.getParameter("dataInicial")+"+DATAFINAL="+request.getParameter("dataFinal");
	
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório gerencial realizada com sucesso!");
		request.setAttribute("endereco",endereco);
		
		return actionMapping.findForward("redirect");
	}
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
}
