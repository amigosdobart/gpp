package br.com.brasiltelecom.ppp.action.relatorioMKTClientesShutdown;

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
 * @author Marcelo Alves Araujo
 * @since 27/07/2005
 */
public class ConsultaRelMKTClientesSDAction extends ActionPortal 
{
	private String codOperacao = Constantes.COD_CONSULTAR_CLIENTES_SD;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db) throws Exception 
	{					
		logger.info("Consulta por Relatório de Clientes em Shutdown");

		// Monta o endereço e passa os parâmetros do relatório de clientes em shutdown
		String endereco = "../reports/rwservlet?ppp+MKTClientesShutDown.rdf+"
						+ request.getParameter("DESFORMAT")
						+ "+DATAINICIAL="+request.getParameter("dataInicial")
						+ "+DATAFINAL="+request.getParameter("dataFinal");
		// Se for do formato excel, gera um xml e transforma em excel
		if(request.getParameter("DESFORMAT").equals("EXCEL"))
			endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Marketing_Clientes_Shutdown.xsl&relatorioXML="+endereco.replaceFirst("EXCEL","XML");
		// Se for delimitado, outros parâmetros são passados
		else if(request.getParameter("DESFORMAT").equals("DELIMITED"))
			endereco = "./relatoriosWPP?NOME_RELATORIO=MKTClientesShutDownDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DATAINICIAL="+request.getParameter("dataInicial")+"+DATAFINAL="+request.getParameter("dataFinal");
				
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
