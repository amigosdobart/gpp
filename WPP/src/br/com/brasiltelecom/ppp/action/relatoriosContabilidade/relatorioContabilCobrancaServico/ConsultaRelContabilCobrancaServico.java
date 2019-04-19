package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilCobrancaServico;

import java.text.ParseException;
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
 * Classe responsavel por realizar a consulta ao Banco
 * de Dados e gerar o Relatório de Cobrança Serviço 102, de acordo
 * com a Pesquisa selecionada pelo usuario
 * 
 * @author Geraldo Palmeira
 * @since  15/05/2007
 */
public class ConsultaRelContabilCobrancaServico extends ActionPortal
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
		
		this.codOperacao = Constantes.COD_CONSULTA_COBRANCA_SERVICO_102;
		
		logger.info("Consulta Relatório de Cobrança Serviço 102");
		
		
		String dataInicial = null;
		String dataFinal   = null;
		String formatoData = null;
		String endereco	   = null;
		String tipoPeriodo = request.getParameter("tipoPeriodo");
		String cn      = Uteis.arrayParaString(request.getParameterValues("CN"),",");
		String plano   = Uteis.arrayParaString(request.getParameterValues("PLANO"),",");
		String formato = request.getParameter("formato");
		
		if ( tipoPeriodo != null)
		{
			if (tipoPeriodo.equals("D"))
			{
				dataInicial = request.getParameter("dataInicialDia");
				dataFinal = request.getParameter("dataFinalDia");
				formatoData = "dd/MM/yyyy";
			} 
			else if (tipoPeriodo.equals("M"))
			{
				dataInicial = request.getParameter("dataInicialMes");
				dataFinal = request.getParameter("dataInicialMes");
				formatoData = "MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(formatoData);
				Calendar data = Calendar.getInstance();
				data.setTime(sdf.parse(dataFinal));
				data.add(Calendar.MONTH,1);
				dataFinal = sdf.format(data.getTime());
			}
		}
		

		
		if ("DELIMITED".equals(formato))
		{			
			endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilCobrancaServicoDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml" +
					   "+DATA_INICIAL="+dataInicial+
					   "+DATA_FINAL="+dataFinal+
					   "+FORMATO_DATA="+formatoData+
					   "+PLANO="+plano+
					   "+CN="+cn;
		}
		else 
		{
			endereco = 	"../reports/rwservlet?ppp+ContabilCobrancaServico.rdf+"+formato+
						"+DATA_INICIAL="+dataInicial+
						"+DATA_FINAL="+dataFinal+
						"+FORMATO_DATA="+formatoData+
						"+PLANO="+plano+
						"+CN="+cn;
		}
		
		request.setAttribute("endereco",endereco);
		request.setAttribute(Constantes.MENSAGEM, "Relatório de Cobrança Serviço 102 gerado com sucesso!");
		
		result = actionMapping.findForward("redirect");
		
		return result;
	}
}