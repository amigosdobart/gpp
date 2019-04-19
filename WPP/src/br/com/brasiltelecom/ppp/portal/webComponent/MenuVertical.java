package br.com.brasiltelecom.ppp.portal.webComponent;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;

import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Menu vertical do portal
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class MenuVertical 
{

	/**
	 * Verifica as operações que o usuário tem permissão e disponibiliza em tela
	 * @param request
	 * @param context
	 */
	public static void getMenu(HttpServletRequest request, VelocityContext context)
	{
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute(Constantes.USUARIO);
		
		if(usuario != null)
		{
			try{
				Map operacoes = usuario.getOperacoesPermitidas(null);
				context.put("operacoes", operacoes);

			} catch(Exception e)
			{
				System.err.println("Erro ao criar menu : " +e);	
			}

		}		
			   	
	}
}