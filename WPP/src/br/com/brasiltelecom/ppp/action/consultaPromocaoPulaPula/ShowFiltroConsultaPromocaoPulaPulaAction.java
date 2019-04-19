package br.com.brasiltelecom.ppp.action.consultaPromocaoPulaPula;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.entity.Operacao;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import java.util.Calendar;
import java.util.Iterator;
import java.text.SimpleDateFormat;

/**
 *	Classe responsavel por mostrar a tela de consulta de Extrato Pula-Pula
 * 
 *	@author	Daniel Ferreira
 *	@since	21/05/2004
 */

public class ShowFiltroConsultaPromocaoPulaPulaAction extends ShowAction 
{

	private String codOperacao = Constantes.MENU_CONSULTA_EXT_PULA;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaExtratoPulaPula.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db)
		throws Exception 
	{
		SimpleDateFormat conversorMes = new SimpleDateFormat(Constantes.MES_FORMATO);
		Calendar calMes = Calendar.getInstance();
		context.put("mes0", conversorMes.format(calMes.getTime()));
		for (int i = 1; i <= 5; i++)
		{
			calMes.add(Calendar.MONTH, -1);
			context.put("mes" + i, conversorMes.format(calMes.getTime()));
		}
		
		// Busca o usuário do portal para saber se ele pode ou não alterar
		// a lista de amigos toda hora
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		
		// Lista de grupos ao qual pertence o usuário
		Iterator itGrp = login.getGrupos().iterator();
		context.put("extratoCheio","false");
		
		// Sinaliza alteraFF indicando se o usuário do portal pode alterar a lista de msisdn
		while(itGrp.hasNext())
		{
			Iterator itOp = ((Grupo)itGrp.next()).getOperacoes().iterator();
			while(itOp.hasNext())
				if(((Operacao)itOp.next()).getNome().equals(Constantes.COD_CONSULTA_EXTRATO_CHEIO))
				{
					context.put("extratoCheio","true");
				}
		}
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}
