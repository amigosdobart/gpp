package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTExpurgoEstornoBonusPulaPula;

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
 * @since 20/03/2006
 */
public class ConsultaRelMKTEstornoExpurgoPulaPulaAction extends ActionPortal {

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
	
				this.codOperacao = Constantes.COD_CONSULTAR_ESTORNO_EXPURGO;

				String dataUsada = "";

				// Verificar qual foi o tipo de data selecionado				
				if(request.getParameter("QUALDATA").equals("DATAREF"))
					dataUsada = "dataReferencia";
				else if (request.getParameter("QUALDATA").equals("DATAEXE"))
					dataUsada = "dataExecucao";
				
					
					//int periodo = Integer.parseInt(request.getParameter("MES"));
					Calendar periodo = Calendar.getInstance();
					periodo.set(Calendar.DAY_OF_MONTH, 1);
					periodo.set(Calendar.YEAR, Integer.parseInt(request.getParameter("ANO"))); 
					periodo.set(Calendar.MONTH, Integer.parseInt(request.getParameter("MES")));
					periodo.add(Calendar.MONTH, -1);
					
					SimpleDateFormat conversorMes = new SimpleDateFormat("dd/MM/yyyy");	
					String dataInicial = conversorMes.format(periodo.getTime());
							
					periodo.add(Calendar.MONTH, 1);
					String dataFinal = conversorMes.format(periodo.getTime());	
				
					periodo.set(Calendar.YEAR, Integer.parseInt("2005"));
					String dataInicialNaoUsada = conversorMes.format(periodo.getTime());
					String dataFinalNaoUsada = conversorMes.format(periodo.getTime());
		
				String endereco = null;

					if (dataUsada.equals("dataReferencia"))
					{
						endereco = "./relatoriosWPP?NOME_RELATORIO=MKTExpurgoEstornoBonusPulaPulaDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DATA_REFERENCIA_INICIAL="+dataInicial+"+DATA_REFERENCIA_FINAL="+dataFinal+"+DATA_PROCESSAMENTO_INICIAL="+dataInicialNaoUsada+"+DATA_PROCESSAMENTO_FINAL="+dataInicialNaoUsada;	
					}
					else if (dataUsada.equals("dataExecucao"))
					{
						endereco = "./relatoriosWPP?NOME_RELATORIO=MKTExpurgoEstornoBonusPulaPulaDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DATA_REFERENCIA_INICIAL="+dataInicialNaoUsada+"+DATA_REFERENCIA_FINAL="+dataFinalNaoUsada+"+DATA_PROCESSAMENTO_INICIAL="+dataInicial+"+DATA_PROCESSAMENTO_FINAL="+dataFinal;
					}
				
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de marketing de expurgo / estorno de bonus pula-pula");
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
