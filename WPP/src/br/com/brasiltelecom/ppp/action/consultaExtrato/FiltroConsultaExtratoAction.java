/*
 * Created on 15/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaExtrato;

//import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.AdministrativoTipoExtratoHome;
import br.com.brasiltelecom.ppp.home.ConsultaHistoricoExtratoHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.portal.entity.TipoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.DecimalFormatFactory;

/**
 * Valida as informa��es se o extrato do cliente pode ser gerado
 * 
 * @author Andr� Gon�alves
 * @since 21/05/2004
 */
public class FiltroConsultaExtratoAction extends ActionPortal {

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
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		//Validar campos de entrada
		String msisdn = request.getParameter("MS");
		String dataInicial = request.getParameter("DI");
		String dataFinal = request.getParameter("DF");
		String canalSaida = request.getParameter("CS");
		
		if (msisdn == null || msisdn.length() != 12){
			request.setAttribute("mensagem","N�mero de Acesso (MSISDN) inv�lido.");
			request.setAttribute("erro","S");
			return actionMapping.findForward("success");
		}
		if (dataInicial == null || dataInicial.length() != 10){
			request.setAttribute("mensagem","Formato da data inicial inv�lido (DD/MM/AAAA).");
			request.setAttribute("erro","S");
			return actionMapping.findForward("success");
		}
		if (dataFinal == null || dataFinal.length() != 10){
			request.setAttribute("mensagem","Formato da data final inv�lido (DD/MM/AAAA).");
			request.setAttribute("erro","S");
			return actionMapping.findForward("success");
		}
		if (canalSaida != null && !(canalSaida.equals("01") || canalSaida.equals("02") || canalSaida.equals("03"))){
			request.setAttribute("mensagem","Canal de saida inv�lido.");
			request.setAttribute("erro","S");
			return actionMapping.findForward("success");
		}
		try {
			if (sdf.parse(dataInicial).getTime() > sdf.parse(dataFinal).getTime()){
				request.setAttribute("mensagem","Informe data inicial menor que data final.");
				request.setAttribute("erro","S");
				return actionMapping.findForward("success");
			}
			if (sdf.parse(dataInicial).getTime() > (new Date()).getTime() || 
				sdf.parse(dataFinal).getTime() > (new Date()).getTime()) {
				request.setAttribute("mensagem","Data n�o pode ser superior a data atual.");
				request.setAttribute("erro","S");
				return actionMapping.findForward("success");
			}
			if (Integer.parseInt(dataInicial.substring(3,5)) > 12){
				request.setAttribute("mensagem","Data inicial inv�lida (Informe m�s correto).");
				request.setAttribute("erro","S");
				return actionMapping.findForward("success");
			}
			if (Integer.parseInt(dataFinal.substring(3,5)) > 12){
				request.setAttribute("mensagem","Data final inv�lida (Informe m�s correto).");
				request.setAttribute("erro","S");
				return actionMapping.findForward("success");
			}
			if (Integer.parseInt(dataInicial.substring(0,2)) > 31){
				request.setAttribute("mensagem","Data inicial inv�lida (Informe dia correto).");
				request.setAttribute("erro","S");
				return actionMapping.findForward("success");
			}
			if (Integer.parseInt(dataFinal.substring(0,2)) > 31){
				request.setAttribute("mensagem","Data final inv�lida (Informe dia correto).");
				request.setAttribute("erro","S");
				return actionMapping.findForward("success");
			}
		} catch(Exception e) {
			request.setAttribute("mensagem","Data no formato inv�lido (DD/MM/AAAA).");
			return actionMapping.findForward("success");
		}
		
		
		// Verifica valor do extrato e se � o primeiro extrato do m�s
				
		int canal = Integer.parseInt(request.getParameter("CS").toString());
		TipoExtrato te = AdministrativoTipoExtratoHome.findByID(db,canal);
		
		Calendar calendar = Calendar.getInstance();
		int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.DAY_OF_MONTH,-(calendar.get(Calendar.DAY_OF_MONTH)-1));
		Date di = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH,ultimo-1);
		Date df = calendar.getTime();

		Map map = new HashMap();
		map.put("msisdn",request.getParameter("MS"));
		map.put("dataInicial",sdf.format(di));
		map.put("dataFinal",sdf.format(df));

		Collection c = ConsultaHistoricoExtratoHome.findByFilter(db,map);
		
		String servidor = servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR).toString();
		String porta = servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR).toString();
		
		Assinante assinante = ConsultaStatusAssinanteGPP.getAssinante(request.getParameter("MS"),servidor,porta);
		
//		if (assinante.getStatusAssinante().equals("1") || 
//			assinante.getStatusAssinante().equals("4") ||
//			assinante.getStatusAssinante().equals("5")){
//				request.setAttribute("mensagem","Assinante n�o pode gerar extrato (status invalido)");
//				request.setAttribute("erro","S");
//				return actionMapping.findForward("success");
//		}

		if (te.getValor() > 0 && c.size() > 1){
			
			//DecimalFormat dformat = DecimalFormatFactory.getMoeda();
			
			if (assinante.getRetorno().equals("2")){
				request.setAttribute("mensagem","Assinante n�o pode gerar extrato (status servico invalido)");
				request.setAttribute("erro","S");
				return actionMapping.findForward("success");
			}
		
				/**
				 * TODO Verificar se realmente � para fazer a an�lise em cima do Saldo Principal
				 * ou da soma dos saldos.
				 */
			if (assinante.getSaldoPrincipalDouble() < te.getValor()){
				request.setAttribute("mensagem","O Assinante j� fez uma solicita��o de comprovante nos �ltimos 30 dias, <br>" +
					"e uma novo solicita��o ser� cobrada (R$ " + DecimalFormatFactory.formataMonetario(te.getValor()) + "). O cliente n�o possui saldo" +
						" suficiente para uma nova solicita��o. Solicite uma novo recarga.");
				request.setAttribute("erro","S");
				return actionMapping.findForward("success");
			}
			
			if (canal == 1){
				request.setAttribute("mensagem","O Assinante j� fez uma solicita��o de um comprovante nos �ltimos 30 dias e" +
					" ser� cobrado o valor de R$ " + DecimalFormatFactory.formataMonetario(te.getValor()) + " para nova solicita��o.<br> ");
			} else if (canal == 2){
				request.setAttribute("mensagem","O Assinante j� fez uma solicita��o de um comprovante nos �ltimos 30 dias e" +
					" ser� cobrado o valor de R$ " + DecimalFormatFactory.formataMonetario(te.getValor()) + " para nova solicita��o.<br> " +
					" Confirma o envio do Comprovante de Servi�o do n�mero de acesso " +
					msisdn + " do per�odo de " + dataInicial + " � " + dataFinal + " para o Email " + request.getParameter("EM"));
			} else if (canal == 3){
				request.setAttribute("mensagem","O Assinante j� fez uma solicita��o de um comprovante nos �ltimos 30 dias e" +
					" ser� cobrado o valor de R$ " + DecimalFormatFactory.formataMonetario(te.getValor()) + " para nova solicita��o.<br> " +
					"Confirma o envio do Comprovante de Servi�os do n�mero de acesso " +
					msisdn + " do per�odo de " + dataInicial + " � " + dataFinal + " para o Endere�o: <BR>" + 
					request.getParameter("LG") + " " + request.getParameter("CP") + "<br>" + request.getParameter("BR") + " " +
					request.getParameter("CD") + " - " + request.getParameter("UF") + "<br>" + request.getParameter("CE"));
			}

			request.setAttribute("erro","N");
		} else {
			if (canal == 1){
				String url = "MS=" + msisdn + 
					"+DI=" + dataInicial + 
					"+DF=" + dataFinal + 
					"+CS=" + request.getParameter("CS") + 
					"+NM=" + request.getParameter("NM") + 
					"+EM=" + request.getParameter("EM") + 
					"+LG=" + request.getParameter("LG") + 
					"+CP=" + request.getParameter("CP") + 
					"+BR=" + request.getParameter("BR") +
					"+CD=" + request.getParameter("CD") + 
					"+UF=" + request.getParameter("UF") +
					"+CE=" + request.getParameter("CE");
					request.setAttribute("endereco","../reports/rwservlet?CRM+"+url);
					return actionMapping.findForward("redirect");

			} else if (canal == 2){
				request.setAttribute("mensagem","Confirma o envio do Comprovante de Servi�o do n�mero de acesso " +
					msisdn + " do per�odo de " + dataInicial + " � " + dataFinal + " para o Email " + request.getParameter("EM"));
			} else if (canal == 3){
				request.setAttribute("mensagem","Confirma o envio do Comprovante de Servi�os do n�mero de acesso " +
					msisdn + " do per�odo de " + dataInicial + " � " + dataFinal + " para o Endere�o: <BR>" + 
					request.getParameter("LG") + " " + request.getParameter("CP") + "<br>" + request.getParameter("BR") + " " +
					request.getParameter("CD") + " - " + request.getParameter("UF") + "<br>" + request.getParameter("CE"));
			}
		}
		if (!response.isCommitted()) 
			{
				request.setAttribute("erro","N");
				return actionMapping.findForward("success");
			}
			
			
		return null;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
