package br.com.brasiltelecom.ppp.action.enviaSMS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.interfacegpp.EnviaSMSTeste;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Carrega a lista de destinatários para a mensagem de sms 
 * 
 * @author Marcelo ALves Araujo
 * @since 14/07/2005
 */
public class EnviaMensagemAction extends ActionPortal 
{	
	private String codOperacao = Constantes.COD_BROADCAST_SMS;
	
	/** 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db) throws Exception 
	{			
		// Recebe a mensegem a ser enviada
		String resultado = "success";
		
		// Parâmetros para envio de SMS
		String arquivoDestinatarios = servlet.getServletContext().getRealPath("/WEB-INF")+java.io.File.separator+"smsDestino.txt";		
		String mensagem = (String)request.getAttribute("sms");
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta 	= (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		
		// Envia o SMS para uma lista de assinantes 
		resultado = EnviaSMSTeste.enviaSMS(arquivoDestinatarios,mensagem,servidor,porta,login.getNome());
		
		if (resultado.equals("error"))
			request.setAttribute(Constantes.MENSAGEM, "Arquivo Vazio!");
		else
		{
			request.setAttribute(Constantes.MENSAGEM, "Mensagem Enviada com Sucesso!");		
			request.setAttribute("imagem", "img/tit_broadcast_sms.gif");
		}
				
		return actionMapping.findForward(resultado);
	}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
}
