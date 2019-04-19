/*
 * Created on 28/01/2005
 *
 */
package br.com.brasiltelecom.ppp.action.ajusteTransfSaldo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.hibernate.Session;

import com.brt.gpp.comum.Definicoes;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.dao.HibernateHelper;
import br.com.brasiltelecom.ppp.home.OrigemHome;
import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.interfacegpp.EfetuaAjusteGPP;
import br.com.brasiltelecom.ppp.model.Ajuste;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.entity.Origem;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Efetua Transferencia de Crédito entre Saldos.
 * 
 * @author Daniel Ferreira
 * @since 28/01/2005
 */
public class SalvarTransfSaldoAction extends ActionPortal{

	private String codOperacao = Constantes.COD_TRANSF_SALDO;

	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception {
		
		//if (db.isClosed()||db==null) 
			db.begin();
		
		logger.info("Transferencia de Saldo solicitada.");
		
		Usuario u;
		Origem origemCredito;
		Origem origemDebito;
		
		try
		{
			//Determinando as origems dos Ajustes para Transferencia de Saldos.
			origemCredito = OrigemHome.findByID(db,Constantes.TRANSF_SALDO_CANAL_AJUSTE_CREDITO,
					                               Constantes.TRANSF_SALDO_ORIGEM);
			origemDebito = OrigemHome.findByID(db,Constantes.TRANSF_SALDO_CANAL_AJUSTE_DEBITO,
                                                  Constantes.TRANSF_SALDO_ORIGEM);
			
			//Buscando o usuario logado.
			u = UsuarioHome.findByID(db,((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula());
		}
		catch (PersistenceException pe)
		{
			logger.error("nao foi possível realizar a Transferencia de Saldo, problemas na conexão com o banco de dados (" +
						pe.getMessage() + ")");
			throw pe;
		}	

		//Verificando o valor máximo de Ajuste permitido para o Usuário.
		
		double maxValor = 0;

			Collection col = u.getGrupos();
	
			for(Iterator it = col.iterator();it.hasNext();)
			{
				Grupo g = (Grupo)it.next();
				if (g.getMaxValorCobranca() > maxValor)
				{
					maxValor = g.getMaxValorCobranca();
				}
			}

		//Recuperando o MSISDN
		String msisdn = "";
		if (request.getParameter("msisdn").length()==10)
			msisdn = "55";
		msisdn = msisdn+request.getParameter("msisdn"); 
		
		Ajuste ajusteDebito = new Ajuste();
		ajusteDebito.setMsisdn(msisdn);
		
		ajusteDebito.setUsuario(((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula());
		
		
		String valor = request.getParameter("valorTransferencia");

		//Ajustando o valor para formato Float.
		String valorAjustado = "";
		for (int i=0; i < valor.length();i++){
			if (valor.charAt(i) != '.'){
				valorAjustado += valor.charAt(i);
			}
		}
		valorAjustado = valorAjustado.replace(',','.');
		ajusteDebito.setValor(Double.parseDouble(valorAjustado));

		//Nao ha modificação na Data de Expiracao para Transferencia de Saldo.		
		ajusteDebito.setDataExpiracao(new Date(0));
		
		// Define a origem e o destino do Saldo da Transferencia. 
		// O tipo de credito para multiplos saldos define para qual saldo
		// o credito sera executado. Sao eles: 00-Principal, 01-Bonus, 02-SM, 03-Dados
		// Caso o parametro seja nulo entao utiliza-se o saldo principal como credito
		String tipoCreditoOrigem = request.getParameter("saldoOrigem");
		String tipoCreditoDestino = request.getParameter("saldoDestino");

		String servidor = null;
		String porta = null;
		
		if (request.getAttribute("contextoWIG")==null)
		{
			servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
			porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		}
		else
		{
			servidor = (String)((ServletContext)request.getAttribute("contextoWIG")).getAttribute(Constantes.GPP_NOME_SERVIDOR);
			porta = (String)((ServletContext)request.getAttribute("contextoWIG")).getAttribute(Constantes.GPP_PORTA_SERVIDOR);
			maxValor = Double.MAX_VALUE;
		}
		
		//Alteracao solicitada pelo usuario para nao criticar
		// limite de credito nessa operacao
		if(ajusteDebito.getValor() <= maxValor  ){
			Session session = null;
			short ret;
			try
			{
				session = HibernateHelper.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Assinante assinante = ConsultaStatusAssinanteGPP.getAssinanteSaldos(msisdn, servidor, porta, session);
				session.getTransaction().commit();
				session = null;
				
				if(assinante.getSaldoSmsDouble() + ajusteDebito.getValor() > Constantes.VLR_MAX_TRANSFERENCIA_SALDO_SMS && tipoCreditoDestino.equals(Definicoes.TIPO_CREDITO_SMS))
				{
					ret = Constantes.GPP_RET_LIMITE_CREDITO_ULTRAPASSADO;
				}
				else if(assinante.getSaldoPeriodicoDouble() >= Constantes.VLR_MAX_SALDO_OFFNET_TRANSFERENCIA_SALDO)
				{
					request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo nao pode ser efetuada!");
					
					if (actionMapping!=null)//Para consulta via portal
						return actionMapping.findForward("success");
					else 
						return null; //Para consulta via WIG SERVLET
				}
				else
				{
					//Realizando o ajuste de débito no processo de Transferencia.
					ajusteDebito.setOrigem(origemDebito);
					ajusteDebito.setTipoCredito(tipoCreditoOrigem != null ? tipoCreditoOrigem : Constantes.TIPO_CREDITO_BONUS);				
					ajusteDebito.setTipoAjuste("D");
					ret = EfetuaAjusteGPP.doAjuste(ajusteDebito,servidor,porta);
				}
				
				if(ret == 0)
				{
				  Ajuste ajusteCredito = new Ajuste();
				  
				  //Atribuindo a data/hora do ajuste de credito para 1 segundo apos o ajuste de debito
				  String data = null;
				  try
				  {
				  	SimpleDateFormat conversorData = new SimpleDateFormat("yyyyMMddHHmmss");
				  	Date dataAjusteCredito = conversorData.parse(ajusteCredito.getData());
				  	long millisAjusteCredito = dataAjusteCredito.getTime();
				  	millisAjusteCredito += 1000;
				  	dataAjusteCredito.setTime(millisAjusteCredito);
				  	data = conversorData.format(dataAjusteCredito);
				  }
				  catch(ParseException ignore){}
					
				  //Realizando o ajuste de credito no processo de Transferencia.
				  ajusteCredito.setDataExpiracao(ajusteDebito.getDataExpiracao());
				  ajusteCredito.setData(data);
				  ajusteCredito.setMsisdn(ajusteDebito.getMsisdn());
				  ajusteCredito.setOrigem(origemCredito);
				  ajusteCredito.setTipoCredito(tipoCreditoDestino != null ? tipoCreditoDestino : Constantes.TIPO_CREDITO_SMS);				
				  ajusteCredito.setTipoAjuste("C");
				  ajusteCredito.setUsuario(ajusteDebito.getUsuario());
				  ajusteCredito.setValor(ajusteDebito.getValor());
				  
   				  ret = EfetuaAjusteGPP.doAjuste(ajusteCredito,servidor,porta);
				}
			}
			catch (Exception e)
			{
				logger.error("nao foi possível realizar a Transferencia de Saldo, problemas com o GPP (" +
							e.getMessage() + ")");
				if(session != null)
				{
					session.getTransaction().commit();
				}
				throw e;
			}
			
			if (ret == 0){
				request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo efetuada com sucesso!");
			} else if(ret == 2) {
				request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo nao pode ser efetuada! (Numero de acesso invalido)");
			} else if(ret == 10) {
				request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo nao pode ser efetuada! (Status do numero de acesso invalido)");
			} else if(ret == 11) {
				request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo nao pode ser efetuada! (Tipo de transacaoo inválida)");
			} else if(ret == 12) {
				request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo nao pode ser efetuada! (Limite de creditos ultrapassado)");
			} else if(ret == 5) {
				request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo nao pode ser efetuada! (Saldo Insuficiente)");
			} else {
				request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo nao pode ser efetuada!");
			}
		} else {
			request.setAttribute(Constantes.MENSAGEM, "Transferencia de Saldo nao pode ser efetuada! (Usuario excedeu a cota de ajuste)");
		}
		
		if (actionMapping!=null)//Para consulta via portal
			return actionMapping.findForward("success");
		else 
			return null; //Para consulta via WIG SERVLET
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
