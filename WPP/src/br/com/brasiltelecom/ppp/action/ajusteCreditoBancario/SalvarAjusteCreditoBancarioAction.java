/*
 * Created on 25/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.ajusteCreditoBancario;

import java.text.DateFormat;
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
import br.com.brasiltelecom.ppp.interfacegpp.RecargaGPP;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.aplicacoes.recarregar.DadosRecarga;

/**
 * Efetua um ajuste de crédito bancário
 * 
 * @author Anderson Jefferson Cerqueira
 * @since 12/02/2008
 */
public class SalvarAjusteCreditoBancarioAction extends ActionPortal{

	private String codOperacao = Constantes.COD_AJUSTE_CREDITO_BANCARIO;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, 
            HttpServletRequest request, HttpServletResponse response, Database db) throws Exception 
    {
		
        logger.info("Ajuste de crédito bancário solicitado");
		
		// Captura todos os dados da tela
		String msisdn = request.getParameter("msisdn");
		String valor = request.getParameter("obr_valor");
		Date data = Calendar.getInstance().getTime();
		String canal = request.getParameter("canalRecarga");
		String origem = request.getParameter("origemRecarga");
		String nsu = request.getParameter("obr_nsu");
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy" );
		SimpleDateFormat dataHora = new SimpleDateFormat("yyyyMMddHHmmss" );
		
		// Formata a data contábil para o padrão MMDD
		String dataContabil = df.format(data).substring(3, 5)+df.format(data).substring(0, 2);
		
		// Popula o objeto DadosRecarga com os valores digitados pelo usuário
		DadosRecarga dadosRecarga = new DadosRecarga();
		dadosRecarga.setMSISDN(msisdn);
		dadosRecarga.setTipoTransacao(canal+origem);
		dadosRecarga.setIdentificacaoRecarga(nsu);
		dadosRecarga.setNsuInstituicao(nsu);
		dadosRecarga.setCodLoja("");
		dadosRecarga.setTipoCredito("00");
		dadosRecarga.setValorPrincipal(Double.parseDouble(valor));
		dadosRecarga.setDataHora(dataHora.format(data));
		dadosRecarga.setDataContabil(dataContabil);
		dadosRecarga.setTerminal("");
		dadosRecarga.setTipoTerminal("");
		dadosRecarga.setSistemaOrigem("BCO");
		dadosRecarga.setOperador(((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula());
		
		// Carrega os dados para conexão com o servidor GPP
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		short ret;
		try
		{
			// Efetua o ajuste de crédito bancário no GPP e retorna um short
			ret = RecargaGPP.ajusteCreditoBancario(dadosRecarga, servidor, porta);
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar o ajuste de crédito, problemas com o GPP (" +
						e.getMessage() + ")");
			throw e;
		}
		
		if (ret == 0){
			this.codOperacao = Constantes.COD_AJUSTE_CREDITO_BANCARIO;
			request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito bancário de R$ " + 
					request.getParameter("obr_valor") + "0 para o número de acesso <br>'" + 
					request.getParameter("obr_msisdn") + "' efetuado com sucesso!");
		} else if(ret == 2) {
			request.setAttribute(Constantes.MENSAGEM, "[erro]Ajuste de crédito bancário de R$ " + 
					request.getParameter("obr_valor") + "0 para o número de acesso <br>'" + 
					request.getParameter("obr_msisdn") + "' não pode ser efetuado. (Número de acesso inválido)");
		} else if(ret == 10) {
			request.setAttribute(Constantes.MENSAGEM, "[erro]Ajuste de crédito bancário de R$ " + 
					request.getParameter("obr_valor") + "0 para o número de acesso <br>'" + 
					request.getParameter("obr_msisdn") + "' não pode ser efetuado. (Status do número de acesso inválido)");
		} else if(ret == 11) {
			request.setAttribute(Constantes.MENSAGEM, "[erro]Ajuste de crédito bancário de R$ " + 
					request.getParameter("obr_valor") + "0 para o número de acesso <br>'" + 
					request.getParameter("obr_msisdn") + "' não pode ser efetuado. (Tipo de transação inválida)");
		} else if(ret == 12) {
			request.setAttribute(Constantes.MENSAGEM, "[erro]Ajuste de crédito bancário de R$ " + 
					request.getParameter("obr_valor") + "0 para o número de acesso <br>'" + 
					request.getParameter("obr_msisdn") + "' não pode ser efetuado. (Limite de créditos ultrapassado)");
		} else if(ret == 5) {
			request.setAttribute(Constantes.MENSAGEM, "[erro]Ajuste de crédito bancário de R$ " + 
					request.getParameter("obr_valor") + "0 para o número de acesso <br>'" + 
					request.getParameter("obr_msisdn") + "' não pode ser efetuado. (Saldo Insuficiente)");
	    } else {
			request.setAttribute(Constantes.MENSAGEM, "[erro]Ajuste de crédito bancário de R$ " + 
					request.getParameter("obr_valor") + "0 para o número de acesso <br>'" + 
					request.getParameter("obr_msisdn") + "' não pode ser efetuado.");
		}
		
		return actionMapping.findForward("success");
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
    {
		return codOperacao;
	}

}
