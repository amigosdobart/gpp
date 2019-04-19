package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTBonusPorPromocaoEFaixa;

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

/**
 * Gera o relatório de marketing bonus por promocao e faixa de valores
 * 
 * @author Marcos C. Magalhaes
 * @since 06/03/2006
 */
public class ConsultaRelMKTBonusPorPromocaoEFAixaAction extends ActionPortal {

	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception {
					
				db.begin();
	
				ActionForward result = null;
				
				logger.info("Consulta relatório de marketing bonus por promocao e faixa de valores");
	
				this.codOperacao = Constantes.COD_CONSULTAR_TROCA_PROM_X_ST;
								
				String desFormat = request.getParameter("DESFORMAT");
				String periodoRel = request.getParameter("MES") + "/" + request.getParameter("ANO");
				
		//int periodo = Integer.parseInt(request.getParameter("MES"));
		Calendar periodo = Calendar.getInstance();
		periodo.set(Calendar.DAY_OF_MONTH, 1);
		periodo.set(Calendar.YEAR, Integer.parseInt(request.getParameter("ANO"))); 
		periodo.set(Calendar.MONTH, Integer.parseInt(request.getParameter("MES")));
		periodo.add(Calendar.MONTH, -1);
		
		SimpleDateFormat conversorMes = new SimpleDateFormat("dd/MM/yyyy");	
		String dataInicial = conversorMes.format(periodo.getTime());
		String dataFinalAdiantamento = conversorMes.format(periodo.getTime());
		
		periodo.add(Calendar.MONTH, 1);
		String dataFinal = conversorMes.format(periodo.getTime());
		
		periodo.add(Calendar.MONTH, -2);
		String dataInicialAdiantamento = conversorMes.format(periodo.getTime());
				
				String endereco = null;
				
				if (desFormat.equals("DELIMITED")) 
				{
					endereco = "./relatoriosWPP?NOME_RELATORIO=MKTBonusPorPromocaoEFaixaDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DATAINICIAL="+dataInicial+"+DATAFINAL="+dataFinal+"+DATAINICIALADIANTAMENTO="+dataInicialAdiantamento+"+DATAFINALADIANTAMENTO="+dataFinalAdiantamento;
				}
				else
				{
					endereco = "../reports/rwservlet?ppp+MKTBonusPromocaoFaixa.rdf+"+desFormat+"+DATAINICIAL="+dataInicial+"+DATAFINAL="+dataFinal+"+PERIODOREL="+periodoRel+"+DATAINICIALADIANTAMENTO="+dataInicialAdiantamento+"+DATAFINALADIANTAMENTO="+dataFinalAdiantamento;
				}
								
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de marketing de troca de promocao por status!");
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
