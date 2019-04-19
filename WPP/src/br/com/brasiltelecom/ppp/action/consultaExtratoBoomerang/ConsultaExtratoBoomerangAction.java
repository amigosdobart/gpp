/*
 * Created on 03/03/2005
 *
 */
package br.com.brasiltelecom.ppp.action.consultaExtratoBoomerang;

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
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaExtratoBoomerangGPP;
import br.com.brasiltelecom.ppp.model.RetornoExtratoBoomerang;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Henrique Canto
 *
 */
public class ConsultaExtratoBoomerangAction extends ActionPortal {

	//private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		//codOperacao = "CONSULTAR_EXTRATO_BOO";
		ActionForward result = null;
		
		//busca msisdn inserido pelo atendente
		String msisdn = request.getParameter("msisdn");
		
		logger.info("Consulta extrato para promoção do Boomerang: " + msisdn);
		
		// formata o periodo da consulta
		//String tipoPeriodo=request.getParameter("tipoPeriodo");
		
		// busca o mês da pesquisa
		int mes = Integer.parseInt(request.getParameter("mes"));				
			
		// Inicia formatação de horario da pesquisa
		Calendar periodo = Calendar.getInstance();
		periodo.add(Calendar.MONTH,-mes);
		
		// Insere 23:59:59 h para horario do fim da pesquisa
		periodo.set(periodo.get(Calendar.YEAR),periodo.get(Calendar.MONTH),Constantes.DIA_FIM_BOOMERANG,23,59,59);
		Date dataFinal = periodo.getTime();
		
		// Reduz a data em 1 mes para a consulta a partir do dia 28 do mês anterior (inicio da contabilização do boomerang) 
		periodo.add(Calendar.MONTH,-1);
		
		// Insere 0:0:0h para horario do inicio da pesquisa
		periodo.set(periodo.get(Calendar.YEAR),periodo.get(Calendar.MONTH),Constantes.DIA_INICIO_BOOMERANG,0,0,0);	
		Date dataInicial = periodo.getTime();
		

		
		// Busca informações de porta e servidor do GPP
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		
		// Busca o extrato do boomerang (ligacoes recebidas) do GPP
		RetornoExtratoBoomerang resultSet = ConsultaExtratoBoomerangGPP.getExtratos(msisdn,sdf.format(dataInicial),sdf.format(dataFinal),servidor,porta);
		
		//se há resultados, passa-os para a action de exposição
	
		request.setAttribute(Constantes.RESULT, resultSet);				
		request.setAttribute(Constantes.MENSAGEM, "Consulta Extrato Boomerang para o número de acesso '" + request.getParameter("obr_msisdn") + "' realizada com sucesso!");
						
		request.setAttribute("page"				, request.getParameter("page")) ;
		request.setAttribute("msisdn"			, request.getParameter("msisdn"));
		request.setAttribute("obr_msisdn"		, request.getParameter("obr_msisdn"));
		request.setAttribute("mes"				, request.getParameter("mes"));
		
		// retorna sucesso
		result = actionMapping.findForward("success");

		return result;
	}

	public String getOperacao() 
	{
		return null;
	}
	

}
