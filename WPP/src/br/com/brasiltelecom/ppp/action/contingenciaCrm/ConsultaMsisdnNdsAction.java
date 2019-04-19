package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConfiguracaoHome;
import br.com.brasiltelecom.ppp.home.MotivoBloqueioCrmHome;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.entity.DadosAssinanteNDS;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.LDAP.ConsultaAssinanteNDS;

/**
 * @author Henrique Canto
 */
/**
 * Realiza consulta de assinantes na base NDS via LDAP.
 * @author ex352341
 * @return Informações Cadastrais de Assinantes da telefonia móvel
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConsultaMsisdnNdsAction extends ActionPortal {

	//private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	ActionForward result = null;

	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		
		//this.codOperacao = Constantes.COD_BLOQUEIO_CONTINGENCIA;
		String msisdn = null;
		db.begin();
		Configuracao contingenciaCrm = ConfiguracaoHome.findByID(db,"CONTINGENCIA_CRM");
		request.setAttribute("contingenciaCrm", contingenciaCrm);

				
		if(request.getParameter("msisdn") != null && !request.getParameter("msisdn").equals("") )
		{
			   msisdn = (String) request.getParameter("msisdn");
			   if (msisdn.length() == 13){
				  msisdn = "55" + msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
			   } else
			   if (msisdn.length() == 12){
				  msisdn = "55" + msisdn.substring(1,3) + msisdn.substring(4,7) + msisdn.substring(8,12);
			   }
		}

		ServletContext context = getServlet().getServletContext();
		String 	nomeServidor   = (String)context.getAttribute(Constantes.LDAP_NOME_SERVIDOR);
		String 	domainName	   = (String)context.getAttribute(Constantes.LDAP_NOME_DOMAIN);
		String 	password	   = (String)context.getAttribute(Constantes.LDAP_DOMAIN_PASSWORD);
		String  baseSearch	   = (String)context.getAttribute(Constantes.LDAP_BASE_SEARCH);
		String  arqCertificado = context.getRealPath((String)context.getAttribute(Constantes.LDAP_NOME_ARQUIVO_CERTIFICADO));
		int		portaServidor  = Integer.parseInt( (String)context.getAttribute(Constantes.LDAP_PORTA_SERVIDOR) );

		ConsultaAssinanteNDS consulta = new ConsultaAssinanteNDS(nomeServidor,portaServidor,domainName,password,baseSearch,arqCertificado);
		DadosAssinanteNDS assinante;
		try 
		{
			
			assinante = consulta.consultaAssinante(msisdn);

		} 	catch (Exception e) 
			{
				request.setAttribute(Constantes.MENSAGEM,"Erro ao conectar com o servidor NDS");
				result = actionMapping.findForward("error");
				return result;
			}
			
			request.setAttribute("assinante", assinante);
			if (assinante!=null)
				request.setAttribute("tamanho","1");
			else
				request.setAttribute("tamanho","0");
			// Fim do código para consulta no NDS
		
		
		// Início do Código para consulta de tipos de bloqueio
		
		logger.info("Consulta de tipos de bloqueio solicitada");

		
		Collection resultset;
		
		try
		{
			resultset = MotivoBloqueioCrmHome.findAll(db);
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível salvar o registro de bloqueio no banco de dados (" +
								pe.getMessage() + ")");
			throw pe;
		}
		
		request.setAttribute(Constantes.RESULT,resultset);	
		
		result = actionMapping.findForward("success");
		return result;
	}


	
	public String getOperacao() {
		return null;
	}

}
