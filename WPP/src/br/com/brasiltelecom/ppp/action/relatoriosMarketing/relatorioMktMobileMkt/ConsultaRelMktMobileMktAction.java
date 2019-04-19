package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMktMobileMkt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

/**
 * Classe responsavel por realizar a consulta ao Banco
 * de Dados e gerar o relatorio Mobile Marketing, de acordo
 * com a Pesquisa selecionada pelo usuario
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  15/01/2006
 */
public class ConsultaRelMktMobileMktAction extends ActionPortal
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
		
		logger.info("Consulta relatorio Mobile Marketing");
		
		// Seta a operacao
		this.codOperacao = Constantes.COD_CONSULTA_MOBILE_MARKETING;
		
		String endereco = "";
		String formato = request.getParameter("Formato");
		String idPesquisa = request.getParameter("Pesquisa");
		
		if (formato.equalsIgnoreCase("DELIMITED"))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRelMobileMarketing+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+IDPESQUISA="+idPesquisa;
		}
		else
		{
			// Opcao para geracao de relatorio no formato PDF: Oracle Reports
			endereco = "";
		}
		
		request.setAttribute("endereco",endereco);
		request.setAttribute(Constantes.MENSAGEM, "Relatório Mobile Marketing gerado com sucesso!");
		
		result = actionMapping.findForward("redirect");
		
		return result;
	}
}