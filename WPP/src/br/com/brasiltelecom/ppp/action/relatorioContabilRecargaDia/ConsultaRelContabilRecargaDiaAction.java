/*
 * Criado em 05/07/2005
 *
 */
package br.com.brasiltelecom.ppp.action.relatorioContabilRecargaDia;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.Uteis;

/**
 * Gera o relatório diário de recargas
 * 
 * @author Marcelo Alves Araujo
 * @since  05/07/2005
 */
public class ConsultaRelContabilRecargaDiaAction extends ActionPortal 
{
    private String codOperacao = Constantes.COD_CONSULTAR_RECARGA_DIA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception 
	{					
		ActionForward result = null;
				
		logger.info("Consulta por Relatorio de Recargas por Dia");
		
		String plano = Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),",");
		String endereco = "../reports/rwservlet?ppp+ContabilRecargasDiario.rdf+"+request.getParameter("DESFORMAT")+
						  "+SISTEMA=" + request.getParameter("sistema_recarga")+
						  "+CANAL=" + request.getParameter("canal_recarga")+
						  "+DATA_INICIAL="+request.getParameter("dataInicial")+
						  "+DATA_FINAL="+request.getParameter("dataFinal")+
						  "+PLANO="+plano;
		
		if(request.getParameter("DESFORMAT").equals("EXCEL"))
			endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Contabil_Recarga_Dia.xsl&relatorioXML="+endereco.replaceFirst("EXCEL","XML");
		/*else if (request.getParameter("DESFORMAT").equals("DELIMITED"))
			endereco = endereco.concat("+DELIMITED_HDR=NO+DELIMITER=;+DES_TYPE=LOCALFILE+DES_NAME=.txt");
	     */
				
		// Se for delimitado
		else if(request.getParameter("DESFORMAT").equals("DELIMITED"))
			endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilRecargaDiarioDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+CANAL="+request.getParameter("canal_recarga")+"+SISTEMA="+request.getParameter("sistema_recarga")+"+DATAINICIAL="+request.getParameter("dataInicial")+"+DATAFINAL="+request.getParameter("dataFinal")+"+PLANO="+plano;

		request.setAttribute(Constantes.MENSAGEM, "Consulta de Relatorio de Recargas Diário realizada com sucesso!");
		request.setAttribute("endereco", endereco);
		result = actionMapping.findForward("redirect");
		return result;
	}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
}