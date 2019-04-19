package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTDemonstativoBonus;

import java.text.ParseException;

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
 * Classe responsavel por realizar a consulta ao Banco
 * de Dados e gerar o Demonstrativo de retirada de Bônus, de acordo
 * com a Pesquisa selecionada pelo usuario
 * 
 * @author Geraldo Palmeira
 * @since  24/01/2006
 */
public class ConsultaRelMKTDemonstativoBonusAction extends ActionPortal
{
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
	
	/**
	 * @throws ParseException 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
	{
		ActionForward result = null;
		
		logger.info("Consulta relatorio Demonstrativo de retirada de Bônus");
		
		// Seta a operacao
		this.codOperacao = Constantes.COD_CONSULTA_DEMONSTRATIVO_BONUS;
		
		String dataInicial 	  = request.getParameter("dataInicial");
		String dataFinal 	  = request.getParameter("dataFinal");
		String[] codigoNacional = request.getParameterValues("COD_NACIONAL");
		String[] promocao 	  = request.getParameterValues("PROMOCAO");	
		String[] categoria 	  = request.getParameterValues("CATEGORIA");
		String endereco       = "./relatoriosWPP?NOME_RELATORIO=MKTRelDemonstrativoBonus" +
								"+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml" +
								"+DATAINICIAL="+ dataInicial + "+DATAFINAL="+ dataFinal +
								"+CN=" + Uteis.arrayParaString(codigoNacional,",") + 
								"+CATEGORIA=" + Uteis.arrayParaString(categoria,",") +
								"+PROMOCAO=" + Uteis.arrayParaString(promocao,",");

		request.setAttribute("endereco",endereco);
		request.setAttribute(Constantes.MENSAGEM, "Relatório Demonstrativo de retirada de Bônus gerado com sucesso!");
		
		result = actionMapping.findForward("redirect");
		
		return result;
	}
}