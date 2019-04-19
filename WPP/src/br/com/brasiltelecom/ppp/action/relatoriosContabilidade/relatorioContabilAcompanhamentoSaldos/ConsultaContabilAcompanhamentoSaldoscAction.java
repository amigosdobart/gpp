package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilAcompanhamentoSaldos;

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
public class ConsultaContabilAcompanhamentoSaldoscAction extends ActionPortal
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
		
		logger.info("Consulta relatorio contábil de acompanhamento de saldos");
		
		// Seta a operacao
		this.codOperacao = Constantes.COD_CONSULTA_ACOMPANHAMENTO_SALDOS;
		
		String endereco 	= "";
		String dtInicio 	= request.getParameter("dtInicio");
		String dtFinal 		= request.getParameter("dtFinal");
		String relatorio	= request.getParameter("relatorio");
		
		if (relatorio.equals("saldoInicial"))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilAcompanhamentoSaldosSaldoInicial+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+DTINICIO="+dtInicio+"+DTFIM="+dtFinal;
			request.setAttribute(Constantes.MENSAGEM, "Relatório Contábil de Acompanhamento de Saldos para saldo inicial gerado com sucesso!");
		}
		if (relatorio.equals("receita"))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilAcompanhamentoSaldosReceita+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+DTINICIO="+dtInicio+"+DTFIM="+dtFinal;
			request.setAttribute(Constantes.MENSAGEM, "Relatório Contábil de Acompanhamento de Saldos para receita gerado com sucesso!");
		}
		if (relatorio.equals("consumo"))
		{
			endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilAcompanhamentoSaldosConsumo+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+DTINICIO="+dtInicio+"+DTFIM="+dtFinal;
			request.setAttribute(Constantes.MENSAGEM, "Relatório Contábil de Acompanhamento de Saldos para consumo gerado com sucesso!");
		}
		
		request.setAttribute("endereco",endereco);
		
		result = actionMapping.findForward("redirect");
		
		return result;
	}
}