/*
 * Created on 08/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaExtrato;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import com.brt.gppEnviaMail.conexoes.EnviaMail;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.AdministrativoTipoExtratoHome;
import br.com.brasiltelecom.ppp.home.ConfiguracaoHome;
import br.com.brasiltelecom.ppp.home.ConsultaHistoricoExtratoHome;
import br.com.brasiltelecom.ppp.home.DadosComprovanteHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaExtratoGPP;
import br.com.brasiltelecom.ppp.interfacegpp.EfetuaAjusteGPP;
import br.com.brasiltelecom.ppp.model.Ajuste;
import br.com.brasiltelecom.ppp.model.Evento;
import br.com.brasiltelecom.ppp.model.Extrato;
import br.com.brasiltelecom.ppp.model.RetornoExtrato;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.entity.DadosComprovante;
import br.com.brasiltelecom.ppp.portal.entity.HistoricoExtrato;
import br.com.brasiltelecom.ppp.portal.entity.Origem;
import br.com.brasiltelecom.ppp.portal.entity.TipoExtrato;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Efetua o extrato para um número de acesso de um cliente 
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ConsultaExtratoAction extends ActionPortal {

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
		
		String msisdn = request.getParameter("MS");
		String dataInicial = request.getParameter("DI");
		String dataFinal = request.getParameter("DF");
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);

		int canal = Integer.parseInt(request.getParameter("CS"));
		TipoExtrato te = AdministrativoTipoExtratoHome.findByID(db,canal);
		
				
		//Debita o valor do extrato
		if (te.getValor() > 0){
			Ajuste a = new Ajuste();
			a.setDataExpiracao(new Date());
			a.setMsisdn(msisdn);
			Origem o = new Origem();
			o.setIdOrigem("01"+canal);
			o.setIdCanal("06");
			a.setOrigem(o);
			a.setTipoAjuste("D");
			a.setUsuario(((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula());
			a.setValor(te.getValor());
			EfetuaAjusteGPP.doAjuste(a,servidor,porta);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		RetornoExtrato resultSet = ConsultaExtratoGPP.getExtratos(msisdn,dataInicial,dataFinal,servidor,porta);
		
		//Envio por email
		if (canal == 2)
		{
			String srvReports   = (String)servlet.getServletContext().getAttribute(Constantes.REPORTS_NOME_SERVIDOR);
			String portaReports = (String)servlet.getServletContext().getAttribute(Constantes.REPORTS_PORTA_SERVIDOR);
			String enderecoOrig = (String)servlet.getServletContext().getAttribute(Constantes.CONTA_EMAIL);
			String servidorSmtp = (String)servlet.getServletContext().getAttribute(Constantes.SERVIDOR_SMTP);
			String enderecoDest = request.getParameter("EM");
			
			String url = "http://"+srvReports+":"+portaReports+"/reports/rwservlet";
			URL urlObj = new URL(buscarParamURLComprovante(request,url));
			
			EnviaMail email = new EnviaMail(enderecoOrig, servidorSmtp);
			email.setAssunto("Comprovante de servicos pre-pago");
			email.setMensagem("Ver anexo o comprovante de servicos pre-pago");
			email.addEnderecoDestino(enderecoDest);
			email.addUrlAnexo(urlObj, "extrato.pdf");
			email.enviaMail();
			
			logger.info("Enviado e-mail para "+enderecoDest+". URL extrato:"+urlObj.toExternalForm());
			request.setAttribute(Constantes.MENSAGEM,"O extrato do número:"+msisdn+" foi enviado por email.");
			
		//Envio impresso
		} 
		else if (canal == 3)
		{
			DadosComprovante dc = new DadosComprovante();
			dc.setDatPeriodoFinal(sdf.parse(request.getParameter("DF")));
			dc.setDatPeriodoInicial(sdf.parse(request.getParameter("DI")));
			dc.setDatRequisicao(new Date());
			dc.setDesBairro(request.getParameter("BR"));
			dc.setDesCep(request.getParameter("CE"));
			dc.setDesCidade(request.getParameter("CD"));
			dc.setDesComplemento(request.getParameter("CP"));
			dc.setDesEndereco(request.getParameter("LG"));
			dc.setDesUf(request.getParameter("UF"));
			dc.setIdtMsisdn(request.getParameter("MS"));
			dc.setNomCliente(request.getParameter("NM"));
			dc.setIdtStatusProcessamento("N");
			
			DadosComprovanteHome.criarDadosComprovante(dc,db);
			
			request.setAttribute(Constantes.MENSAGEM,"O extrato do número:"+msisdn+" foi enviado por correio.");
		}
				
		//Grava o historico do extrato
		HistoricoExtrato he = new HistoricoExtrato();
		he.setMsisdn(msisdn);
		if (te.getValor() > 0){
			he.setCobrado(1);
		} else {
			he.setCobrado(0);
		}
		he.setData(new Date());
		he.setEmissor("CRM");
		he.setUsuario(((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula());
		he.setTipoExtrato(te);
		
		ConsultaHistoricoExtratoHome.criarOrigem(he,db);

		request.setAttribute(Constantes.RESULT, resultSet);

		if (canal == 1) // Se for extrato para visualização em Tela - > Saida PDF via Oracle Reports
		{
			request.setAttribute("endereco",buscarParamURLComprovante(request,"../reports/rwservlet"));
			return actionMapping.findForward("redirect");			
		}
			
		return actionMapping.findForward("success");
	}

	private String buscarParamURLComprovante(HttpServletRequest request, String servlet) throws Exception
	{
		String url = servlet+"?CRM&MS=" + request.getParameter("MS") + 
		"&DI=" + request.getParameter("DI") + 
		"&DF=" + request.getParameter("DF") + 
		"&CS=" + request.getParameter("CS") + 
		"&NM=" + URLEncoder.encode(request.getParameter("NM"),"UTF-8") + 
		"&EM=" + request.getParameter("EM") + 
		"&LG=" + URLEncoder.encode(request.getParameter("LG"),"UTF-8") + 
		"&CP=" + URLEncoder.encode(request.getParameter("CP"),"UTF-8") + 
		"&BR=" + URLEncoder.encode(request.getParameter("BR"),"UTF-8") +
		"&CD=" + URLEncoder.encode(request.getParameter("CD"),"UTF-8") + 
		"&UF=" + request.getParameter("UF") +
		"&CE=" + request.getParameter("CE");

		return url;
	}
	
	/**
	 * Gera o HTML para envio de extrato por email
	 * 
	 * @param re objeto contendo as informações de extrato a serem geradas
	 * @param request parâmetro do tipo javax.servlet.http.HttpServletRequest 
	 * @param db parâmetro do tipo org.exolab.castor.jdo.Database
	 * @return página HTML para enviar extrato por email
	 * @throws PersistenceException
	 */
	private String getEmail(RetornoExtrato re, HttpServletRequest request, Database db) throws PersistenceException{
		StringBuffer retorno = new StringBuffer();
		Configuracao conf = ConfiguracaoHome.findByID(db,"EXTRATO_MENSAGEM_EMAIL");
		retorno.append("<link rel=\"STYLESHEET\" type=\"text/css\" href=\"dealer.css\">");
		retorno.append(conf.getVlrConfiguracao() + "<BR><BR>");
		retorno.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" bordercolor=\"#C0E0AF\">");
		retorno.append("  <!--<tr rowspan=\"10\">");
		retorno.append("	<td><img src=\"img/logo_brt.gif\" width=\"469\" height=\"44\" alt=\"\"></td>");
		retorno.append("  </tr>-->");
		retorno.append("  <tr>");
		retorno.append("	<td width=\"2%\"></td>");
		retorno.append("	<td class=\"fontBold\">Comprovante de Prestação de Serviço");
		retorno.append("	<div align=\"right\"></div></td>");
		retorno.append("	<td></td>");
		retorno.append("  </tr>");
		retorno.append("  <tr>");
		retorno.append("  <td></td>");
		retorno.append("  <td class=\"fontverde3\">Nome do Cliente:</td>");
		retorno.append("  <td class=\"fontverde\">"+request.getParameter("NM")+"</td>");
		retorno.append("  </tr>");
		retorno.append("  <tr>");
		retorno.append("  <td></td>");
		retorno.append("  <td class=\"fontverde3\">Endereço de Correspondência:</td>");
		retorno.append("  <td class=\"fontverde\">"+request.getParameter("LG") + request.getParameter("CP") + "</td>");
		retorno.append("  </tr>");
		retorno.append("  <tr>");
		retorno.append("  <td></td>");
		retorno.append("  <td class=\"fontverde3\">Número de Acesso:</td>");
		retorno.append("  <td class=\"fontverde\">"+request.getParameter("MS") + "</td>");
		retorno.append("  </tr>");
		retorno.append("  <tr>");
		retorno.append("  <td></td>");
		retorno.append("  <td class=\"fontverde3\">Data de Ativação:</td>");
		retorno.append("  <td class=\"fontverde\"></td>");
		retorno.append("  </tr>");
		retorno.append("  <tr>");
		retorno.append("  <td></td>");
		retorno.append("  <td class=\"fontverde3\">Período:</td>");
		retorno.append("  <td class=\"fontverde\">"+request.getParameter("DI") + " à " +request.getParameter("DF") + "</td>");
		retorno.append("  </tr>");
		retorno.append("</table>");
		retorno.append("<table border=\"1\" width=\"200%\" bordercolor=\"#C0E0AF\" cellspacing=\"0\" cellpadding=\"5\" style=\"border-collapse: collapse\" bgcolor=\"#FFFFFF\">");
		retorno.append("		<tr>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\"></td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Nro de Origem</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Data</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Hora</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Tipo</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Operação</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Região Origem</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Nro de Destino</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Duração</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Valor Debitado Normal</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Valor Debitado Bônus</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Valor Debitado SMS</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Valor Debitado Dados</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Valor Total R$</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Saldo Normal R$</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Saldo Bônus R$</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Saldo SMS R$</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Saldo Dados R$</td>");
		retorno.append("			<td bgcolor=\"#E1EA99\" class=\"fontverde3\">Saldo R$</td>");
		retorno.append("		</tr>");
		Iterator extratos = re.getExtratos().iterator();
		int i=0;
		while (extratos.hasNext()){
			Extrato e = (Extrato)extratos.next();
			retorno.append("			<tr>");
			retorno.append("				<td class=\"fontverde\">&nbsp;"+ ++i +"</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getNumeroOrigemString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getData() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getHoraChamada()+"</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getTipoTarifacao()+"</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getOperacao() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getRegiaoOrigem() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getNumeroDestinoString() + "</td>");				
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getDuracaoChamada() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getValorPrincipalString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getValorBonusString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getValorSMSString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getValorGPRSString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getValorTotalString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getSaldoPrincipalString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getSaldoBonusString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getSaldoSMSString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getSaldoGPRSString() + "</td>");
			retorno.append("				<td class=\"fontverde\">&nbsp;" + e.getSaldoTotalString() + "</td>");
			retorno.append("			</tr>");
		}
		retorno.append("	</table>");
		retorno.append(" <table width=\"100%\" border=\"1\" cellspacing=\"0\" bordercolor=\"#C0E0AF\">");
		retorno.append("		  <tr bgcolor=\"#E1EA99\">");
		retorno.append("			<td width=\"20%\"></td>");
		retorno.append("			<td width=\"8%\">Saldo Principal</td>");
		retorno.append("			<td width=\"8%\">Saldo de Bônus</td>");
		retorno.append("			<td width=\"8%\">Saldo de Torpedos</td>");
		retorno.append("			<td width=\"8%\">Saldo de Dados</td>");
		retorno.append("			<td width=\"8%\">Total</td>");
		retorno.append("		  </tr>");
		retorno.append("		  <tr bgcolor=\"#E1EA99\">");
		retorno.append("			<td>Saldo Inicial</td>");
		retorno.append("			<td>" + re.getSaldoInicialPrincipalString() + "</td>");
		retorno.append("			<td>" + re.getSaldoInicialBonusString() + "</td>");
		retorno.append("			<td>" + re.getSaldoInicialSMSString() + "</td>");
		retorno.append("			<td>" + re.getSaldoInicialGPRSString() + "</td>");
		retorno.append("			<td>" + re.getSaldoInicialTotalString() + "</td>");
		retorno.append("		  </tr>");
		retorno.append("		  <tr bgcolor=\"#E1EA99\">");
		retorno.append("			<td>Total de Cr&eacute;ditos</td>");
		retorno.append("			<td>" + re.getTotalCreditosPrincipalString() + "</td>");
		retorno.append("			<td>" + re.getTotalCreditosBonusString() + "</td>");
		retorno.append("			<td>" + re.getTotalCreditosSMSString() + "</td>");
		retorno.append("			<td>" + re.getTotalCreditosGPRSString() + "</td>");
		retorno.append("			<td>" + re.getTotalCreditosTotalString() + "</td>");
		retorno.append("		  </tr>");
		retorno.append("		  <tr bgcolor=\"#E1EA99\">");
		retorno.append("			<td>Total de D&eacute;bitos</td>");
		retorno.append("			<td>" + re.getTotalDebitosPrincipalString() + "</td>");
		retorno.append("			<td>" + re.getTotalDebitosBonusString() + "</td>");
		retorno.append("			<td>" + re.getTotalDebitosSMSString() + "</td>");
		retorno.append("			<td>" + re.getTotalDebitosGPRSString() + "</td>");
		retorno.append("			<td>" + re.getTotalDebitosTotalString() + "</td>");
		retorno.append("		  </tr>");
		retorno.append("		  <tr bgcolor=\"#E1EA99\">");
		retorno.append("			<td>Saldo Final</td>");
		retorno.append("			<td>" + re.getSaldoFinalPrincipalString() + "</td>");
		retorno.append("			<td>" + re.getSaldoFinalBonusString() + "</td>");
		retorno.append("			<td>" + re.getSaldoFinalSMSString() + "</td>");
		retorno.append("			<td>" + re.getSaldoFinalGPRSString() + "</td>");
		retorno.append("			<td>" + re.getSaldoFinalTotalString() + "</td>");
		retorno.append("		  </tr>");
		retorno.append("		</table>");
		retorno.append("		<table width=\"100%\" border=\"0\" cellspacing=\"0\">");
		retorno.append("		  <tr>");
		retorno.append("			<td>&nbsp;</td>");
		retorno.append("		  </tr>");
		retorno.append("		</table>");	    
		retorno.append("		<table width=\"100%\" border=\"1\" cellspacing=\"0\" bordercolor=\"#C0E0AF\">");
		retorno.append("		  <tr>");
		retorno.append("			<td colspan=\"13\" class=\"fontBold\">Demonstrativo de Eventos");
		retorno.append("			  <div align=\"right\"></div>");
		retorno.append("			</td>");
		retorno.append("		  </tr>");
		retorno.append("		  <tr>");
		retorno.append("			<td width=\"2%\" bgcolor=\"#E1EA99\">&nbsp;</td>");
		retorno.append("			<td width=\"6%\" bgcolor=\"#E1EA99\" class=\"fontverde3\">Evento</td>");
		retorno.append("			<td width=\"3%\" bgcolor=\"#E1EA99\" class=\"fontverde3\">&nbsp;</td>");
		retorno.append("			<td width=\"10%\" bgcolor=\"#E1EA99\" class=\"fontverde3\">&nbsp;</td>");
		retorno.append("			<td width=\"7%\" bgcolor=\"#E1EA99\" class=\"fontverde3\">&nbsp;</td>");
		retorno.append("			<td colspan=\"2\" bgcolor=\"#E1EA99\" class=\"fontverde3\">&nbsp;</td>");
		retorno.append("			<td width=\"20%\" bgcolor=\"#E1EA99\" class=\"fontverde3\">Data</td>");
		retorno.append("			<td width=\"20%\" bgcolor=\"#E1EA99\" class=\"fontverde3\">Hora</td>");
		retorno.append("			<td width=\"4%\" bgcolor=\"#E1EA99\">&nbsp;</td>");
		retorno.append("			<td width=\"4%\" bgcolor=\"#E1EA99\">&nbsp;</td>");
		retorno.append("			<td width=\"5%\" bgcolor=\"#E1EA99\">&nbsp;</td>");
		retorno.append("			<td width=\"5%\" bgcolor=\"#E1EA99\">&nbsp;</td>");
		retorno.append("		  </tr>");
		Iterator eventos = re.getEventos().iterator();
		i=0;
		while (eventos.hasNext()){
			Evento e = (Evento)eventos.next();
			retorno.append("		  <tr>");
			retorno.append("			<td>" + ++i + "</td>");
			retorno.append("			<td colspan=\"6\">" + e.getNome()+"</td>");
			retorno.append("			<td>" + e.getData() + "</td>");
			retorno.append("			<td>" + e.getHora() + "</td>");
			retorno.append("			<td>&nbsp;</td>");
			retorno.append("			<td>&nbsp;</td>");
			retorno.append("			<td>&nbsp;</td>");
			retorno.append("			<td>&nbsp;</td>");
			retorno.append("		  </tr>");
		}
		retorno.append("		</table>");
		return retorno.toString();
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
