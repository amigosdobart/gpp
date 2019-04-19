package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTRecargasPlanoControle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
 * Gera o relatório de Recargas Controle
 * 
 * @author Magno Batista Corrêa
 * @since 13/04/2006
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 29/01/2007
 * Código completamente reescrito
 */
public class ConsultaRelMKTRecargasControleAction extends ActionPortal {

	private String codOperacao = Constantes.COD_CONSULTA_REC_PLAN_CONTROL;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(	ActionMapping actionMapping,ActionForm actionForm,
				HttpServletRequest request,	HttpServletResponse response, Database db) throws Exception 
	{
		
		// Tratando os campos do Periodo da pesquisa
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dataInicial1 = null;
		Date dataFinal1 = null;
		String dataInicial = null;
		String dataFinal = null;

		Calendar cFim = Calendar.getInstance();
		
		dataInicial1 = sdf.parse(request.getParameter("dataInicial"));
		dataFinal1 = sdf.parse(request.getParameter("dataFinal"));

		cFim.setTime(dataFinal1);
		cFim.add(Calendar.DAY_OF_MONTH,1);

		dataInicial = sdf.format(dataInicial1);
		dataFinal = sdf.format(cFim.getTime());
		
		// Tratando os campos CN, Sistema de Recarga e Canais de origem

		String[] categoria 	  	= request.getParameterValues("CATEGORIA");
		String[] codigoNacional = request.getParameterValues("COD_NACIONAL");
		String[] sistemaRecarga = request.getParameterValues("SISTEMA");	
		String[] origem 		= request.getParameterValues("ORIGEM");
		
		// Monta a url do relatorio e passa os parâmetros necessários para a consulta
		
		String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRecargasControleDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+";
		
		endereco += "CATEGORIA=" 			+ java.net.URLEncoder.encode(Uteis.arrayParaString(categoria,","), "UTF-8")
				  + "+CN="                  + java.net.URLEncoder.encode(Uteis.arrayParaString(codigoNacional, ","), "UTF-8")
				  + "+SISTEMA_RECARGA="     + java.net.URLEncoder.encode(Uteis.arrayParaString(sistemaRecarga, ","), "UTF-8")
				  + "+ORIGEM="  			+ java.net.URLEncoder.encode(Uteis.arrayParaString(origem, ","), "UTF-8")
		  		  + "+DATA_INICIAL="        + dataInicial
				  + "+DATA_FINAL="          + dataFinal;

		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório gerencial realizada com sucesso!");
		request.setAttribute("endereco", endereco);
		
		logger.info("Consulta por relatório de Recarga Controle");
		return actionMapping.findForward("redirect");

	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
	
	
}
