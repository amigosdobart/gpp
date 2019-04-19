package br.com.brasiltelecom.ppp.action.cadastroPrecoAparelho;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Apresenta a tela de cadastro de preços de aparelho
 * @author Marcelo Alves Araujo
 * @since 13/11/2006
 */
public class ShowCadastroPrecoAparelhoAction extends ShowAction
{	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "cadastroPrecoAparelho.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		// Não há implementações		
	}
	
	/** 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return Constantes.COD_CADASTRAR_PRECO_APARELHO;
	}	
}