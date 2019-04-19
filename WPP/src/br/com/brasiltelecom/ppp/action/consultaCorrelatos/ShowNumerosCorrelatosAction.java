/*
 * Created on 24/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.consultaCorrelatos;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de números correlatos
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowNumerosCorrelatosAction extends ShowAction 
{
	private String codOperacao = Constantes.MENU_CONTESTACAO_DEB;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "numerosCorrelatos.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,Database db) 
	{
		if (request.getParameter("msisdnDestino") != null && !request.getParameter("msisdnDestino").equals(""))
		{
			try
			{
				db.begin();
			}
			catch (PersistenceException pe) {}
			logger.info("Consulta ao número correlato solicitada");
			this.codOperacao = Constantes.COD_CONSULTA_NUMS_CORRELATOS;
			request.setAttribute(Constantes.MENSAGEM, "Consulta ao número correlato de origem '" +
					"(" + request.getParameter("msisdnOrigem").substring(2,4) + ")" +
					request.getParameter("msisdnOrigem").substring(4,8) + "-" + 
					request.getParameter("msisdnOrigem").substring(8,12) +
					"', destino '" + request.getParameter("msisdnDestino") +
					"' realizada com sucesso!");
		}
		
		//HttpSession session = request.getSession();
		Collection result = (Collection) request.getAttribute(Constantes.RESULT);
		
		if(request.getParameter("msisdnOrigem") != null && !request.getParameter("msisdnOrigem").equals("") ){
		   context.put("msisdnOrigem", request.getParameter("msisdnOrigem") );
		   String msisdnAux = (String) request.getParameter("msisdnOrigem");		   
		   context.put("msisdnOrigemF", "(" + msisdnAux.substring(2,4) + ") " + msisdnAux.substring(4,8) + "-" + msisdnAux.substring(8,12) );		  			
		}		
		
		if(request.getParameter("numeroCartao") != null && !request.getParameter("numeroCartao").equals("") ){
		   context.put("numeroCartao", request.getParameter("numeroCartao") );
		}
		
		if(request.getParameter("msisdnDestino") != null && !request.getParameter("msisdnDestino").equals("") ){
			context.put("msisdnDestino", request.getParameter("msisdnDestino") );		    			
		}
		
		context.put("dadosChamadas", result);
		
		if (result != null) context.put("tamanho",String.valueOf(result.size()));
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}