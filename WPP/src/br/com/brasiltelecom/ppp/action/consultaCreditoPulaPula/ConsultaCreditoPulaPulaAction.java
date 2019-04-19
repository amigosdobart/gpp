/* * Created on 28/09/2005
 */
package br.com.brasiltelecom.ppp.action.consultaCreditoPulaPula;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaPromocaoPulaPulaGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.Definicoes;

/**
 * @author Marcos C. Magalhães
 */
public class ConsultaCreditoPulaPulaAction extends ActionPortal {
	
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		logger.info("Consulta credito pula-pula");
		codOperacao = Constantes.COD_CONSULTAR_CRED_PULA_PULA;
		
		//CastorResultObject assinante = new CastorResultObject();
		
		String msisdn = "55" + request.getParameter("msisdn");
		String mes = request.getParameter("MES");
		String ano = request.getParameter("ANO");
		String datMes = ano + mes;
		//int promo = Integer.parseInt(request.getParameter("promocao"));
//		 Busca informações de porta e servidor do GPP
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		String valor="";
		
		DecimalFormat format = new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
		try 
		{
			
			valor=format.format(ConsultaPromocaoPulaPulaGPP.getCreditoPulaPula(msisdn,datMes,servidor,porta));
			
			//db.begin();
			//assinante = CreditoPulaPulaHome.findByParam(db, datMes, msisdn, promo);
		}
		
		catch (Exception e)
		{
			request.setAttribute(Constantes.MENSAGEM,"Erro ao conectar ao GPP");
		}
		try
		{
			request.setAttribute("msisdn", msisdn);
			//request.setAttribute("promocao",assinante.getField00());
			//request.setAttribute("count",assinante.getField01());
			request.setAttribute("valor",valor);
			return actionMapping.findForward("success");	
		}
		catch (NullPointerException ne)
		{
			request.setAttribute("imagem", "img/tit_consulta_cred_pula_pula.gif");
			request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado.");
			return actionMapping.findForward("nenhumRegistro");
		}
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	
	public String getOperacao() 
	{
		return this.codOperacao;
	}
	
}
