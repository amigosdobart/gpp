/*
 * Created on 12/05/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTRecargaValorFace;
 
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.Uteis;

/**
 * Gera o relatório de recargas por valor de face
 * 
 * @author Marcelo Alves Araujo
 * @since 12/05/2005
 */
public class ConsultaRelMKTRecargaValorFaceAction extends ActionPortal 
{
    private String codOperacao = Constantes.COD_CONSULTAR_RECARGAS_VF;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
				                       HttpServletResponse response, Database db)
		throws Exception 
	{
		ActionForward result = null;
		
				
		logger.info("Consulta por Relatorio de Recargas Mensais por Valor de Face");
		
		String dataInicial = null;
		String dataFinal = null;
		String formatoData = null;
		String categoria = Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),",");
		String tipoPeriodo = request.getParameter("tipoPeriodo");
		
		if ( tipoPeriodo != null)
		{
			if (tipoPeriodo.equals("D"))
			{
				dataInicial = request.getParameter("dataInicialDia");
				dataFinal = request.getParameter("dataFinalDia");
				formatoData = "dd/MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(formatoData);
				Calendar data = Calendar.getInstance();
				data.setTime(sdf.parse(dataFinal));
				data.add(Calendar.DAY_OF_YEAR,1);
				dataFinal = sdf.format(data.getTime());
			} 
			else if (tipoPeriodo.equals("M"))
			{
				dataInicial = request.getParameter("dataInicialMes");
				dataFinal = request.getParameter("dataFinalMes");
				formatoData = "MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(formatoData);
				Calendar data = Calendar.getInstance();
				data.setTime(sdf.parse(dataFinal));
				data.add(Calendar.MONTH,1);
				dataFinal = sdf.format(data.getTime());
			}
		}
				
		String endereco = 	"../reports/rwservlet?ppp+MKTRecargaValorFace.rdf+"+request.getParameter("DESFORMAT")+
							"+DATA_INICIAL="+dataInicial+
							"+DATA_FINAL="+dataFinal+
							"+FORMATO_DATA="+formatoData+
							"+CATEGORIA="+categoria;
		
		if(request.getParameter("DESFORMAT").equals("EXCEL"))
			endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Marketing_Recarga_Face.xsl&relatorioXML="+endereco.replaceFirst("EXCEL","XML");
		//else if (request.getParameter("DESFORMAT").equals("DELIMITED"))
		//	endereco = endereco.concat("+DELIMITED_HDR=NO+DELIMITER=;+DES_TYPE=LOCALFILE+DES_NAME=.txt");
		//Se for delimitado
		else if(request.getParameter("DESFORMAT").equals("DELIMITED"))
			{
				endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRecargaValorFaceDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DATAINICIAL="+dataInicial+"+DATAFINAL="+dataFinal+"+FORMATODATA="+formatoData+"+CATEGORIA="+categoria;
			}
						
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao Relatorio Recargas Mensais por Valor de Face realizada com sucesso!");
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