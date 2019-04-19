/*
 * Created on 25/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.ajusteCredito;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.OrigemHome;
import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.interfacegpp.EfetuaAjusteGPP;
import br.com.brasiltelecom.ppp.model.Ajuste;
import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.entity.Origem;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Efetua um ajuste de crédito
 * 
 * @author André Gonçalves
 * @since 20/05/2004
 */
public class SalvarAjusteCreditoAction extends ActionPortal{

	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception {
		
		db.begin();
		logger.info("Ajuste de crédito solicitado");
		
		Usuario u;
		Origem origem;
		
		try
		{
			origem = OrigemHome.findByID(db,
							request.getParameter("obr_idOrigem").substring(0,2),
							(String)request.getParameter("obr_idOrigem").substring(2));
			u = UsuarioHome.findByID(db,((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula());
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar o ajuste de crédito, problemas na conexão com o banco de dados (" +
						pe.getMessage() + ")");
			throw pe;
		}		
		Collection col = u.getGrupos();
		double maxValor = 0;
		
		for(Iterator it = col.iterator();it.hasNext();){
			Grupo g = (Grupo)it.next();
			if (g.getMaxValorCobranca() > maxValor){
				maxValor = g.getMaxValorCobranca();
			}
		}

		String msisdn = request.getParameter("obr_msisdn"); 
		if (msisdn.length() == 13)
			msisdn = msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
		
		Ajuste ajuste = new Ajuste();
		ajuste.setMsisdn("55" + msisdn);
		ajuste.setUsuario(((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula());
		ajuste.setOrigem(origem);
		
		String valor = request.getParameter("obr_valor");
		String valorAjustado = "";
		for (int i=0; i < valor.length();i++){
			if (valor.charAt(i) != '.'){
				valorAjustado += valor.charAt(i);
			}
		}
		valorAjustado = valorAjustado.replace(',','.');
		ajuste.setValor(Double.parseDouble(valorAjustado));
		
		ajuste.setTipoAjuste("C");

		// Define o tipo de credito do ajuste. 
		// O tipo de credito para multiplos saldos define para qual saldo
		// o credito sera executado. Sao eles: 00-Principal, 01-Bonus, 02-SM, 03-Dados
		// Caso o parametro seja nulo entao utiliza-se o saldo principal como credito
		String tipoCredito = request.getParameter("saldoCredito");
		ajuste.setTipoCredito(tipoCredito != null ? tipoCredito : Constantes.TIPO_CREDITO_PRINCIPAL);

		// O numero de dias de expiracao nao mais e definido pela origem do lancamento
		// e sim por um combo box na pagina onde o usuario ira escolher o numero de
		// dias da expiracao para o ajuste de credito
		Calendar c = Calendar.getInstance();
		//int dias = origem.getNumDiasExpiracao();
		int dias = Integer.parseInt(request.getParameter("numDiasExpiracao"));
		c.add(Calendar.DAY_OF_MONTH,dias);
		ajuste.setDataExpiracao(c.getTime());
				
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		if (maxValor >= ajuste.getValor()){
		
			short ret;
			try
			{
				ret = EfetuaAjusteGPP.doAjuste(ajuste,servidor,porta);
			}
			catch (Exception e)
			{
				logger.error("Não foi possível realizar o ajuste de crédito, problemas com o GPP (" +
							e.getMessage() + ")");
				throw e;
			}
			
			if (ret == 0){
				this.codOperacao = Constantes.COD_AJUSTE_CREDITO;
				request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito de R$ " + 
						request.getParameter("obr_valor") + " para o número de acesso '" + 
						request.getParameter("obr_msisdn") + "' efetuado com sucesso!");
			} else if(ret == 2) {
				request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito de R$ " + 
						request.getParameter("obr_valor") + " para o número de acesso '" + 
						request.getParameter("obr_msisdn") + "' não pode ser efetuado! (Número de acesso inválido)");
			} else if(ret == 10) {
				request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito de R$ " + 
						request.getParameter("obr_valor") + " para o número de acesso '" + 
						request.getParameter("obr_msisdn") + "' não pode ser efetuado! (Status do número de acesso inválido)");
			} else if(ret == 11) {
				request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito de R$ " + 
						request.getParameter("obr_valor") + " para o número de acesso '" + 
						request.getParameter("obr_msisdn") + "' não pode ser efetuado! (Tipo de transação inválida)");
			} else if(ret == 12) {
				request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito de R$ " + 
						request.getParameter("obr_valor") + " para o número de acesso '" + 
						request.getParameter("obr_msisdn") + "' não pode ser efetuado! (Limite de créditos ultrapassado)");
			} else if(ret == 5) {
				request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito de R$ " + 
						request.getParameter("obr_valor") + " para o número de acesso '" + 
						request.getParameter("obr_msisdn") + "' não pode ser efetuado! (Saldo Insuficiente)");
		    } else {
				request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito de R$ " + 
						request.getParameter("obr_valor") + " para o número de acesso '" + 
						request.getParameter("obr_msisdn") + "' não pode ser efetuado!");
			}
		} else {
			request.setAttribute(Constantes.MENSAGEM, "Ajuste de crédito de R$ " + 
					request.getParameter("obr_valor") + " para o número de acesso '" + 
					request.getParameter("obr_msisdn") + "' não pode ser efetuado! (Usuario excedeu a cota de ajuste)");
		}
		
		return actionMapping.findForward("success");
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
