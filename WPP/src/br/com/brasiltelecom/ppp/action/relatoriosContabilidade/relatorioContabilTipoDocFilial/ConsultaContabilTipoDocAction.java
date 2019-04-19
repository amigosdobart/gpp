package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilTipoDocFilial;

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

/**
 * Classe responsavel por realizar a consulta ao Banco
 * de Dados e gerar o relatorio contábil por tipo de documento
 * e filial de acordo com a Pesquisa selecionada pelo usuario
 * 
 * @author Bernardo Pina
 * @since  26/03/2007
 */
public class ConsultaContabilTipoDocAction extends ActionPortal
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
		
		logger.info("Consulta relatorio contábil por tipo de documento e filial");
		
		// Seta a operacao
		this.codOperacao = Constantes.COD_CONSULTA_CONT_TIPODOC_FILIAL;
		
		String endereco = "";
		String formato = request.getParameter("Formato");
		String idPeriodo = request.getParameter("periodoContabil");
		
		if (formato.equalsIgnoreCase("DELIMITED"))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilTipoDocFilial+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+IDPERIODO="+idPeriodo;
		}
		else
		{
			// Opcao para geracao de relatorio no formato PDF: Oracle Reports
			endereco = "";
		}
		
		request.setAttribute("endereco",endereco);
		request.setAttribute(Constantes.MENSAGEM, "Relatório Contábil por tipo de documento e filial gerado com sucesso!");
		
		result = actionMapping.findForward("redirect");
		
		return result;
	}
}