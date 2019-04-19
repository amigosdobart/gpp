/*
 * Created on 23/08/2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.home.CategoriaHome;


/**
 * @author Henrique Canto
 *
 */
public class ShowFiltroDesbloqueioAction extends ShowAction {

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		
		return "contingenciaDesbloqueio.vm";
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		
		
		if (request.getAttribute(Constantes.RESULT)!=null)
		{
			Collection result = (Collection) request.getAttribute(Constantes.RESULT);
			Configuracao contingencia = (Configuracao) request.getAttribute("valorContingenciaCrm");
			Collection Uf = (Collection)request.getAttribute("Uf");
			context.put("motivosBloqueio", result);
			context.put("contingenciaCrm", contingencia);
			context.put("Ufs",Uf);
			context.put("mensagem",request.getAttribute(Constantes.MENSAGEM));
			//context.put("planosPreco", (Collection) request.getAttribute("planosPreco"));
			//context.put("planoPos",Definicoes.XML_OS_CATEGORIA_POSPAGO);
			//context.put("planoPre",Definicoes.XML_OS_CATEGORIA_PREPAGO);
			//context.put("planoCon",Definicoes.XML_OS_CATEGORIA_HIBRIDO);
			
			db.begin();
			Collection produtos = CategoriaHome.findAll(db);
			context.put("produtos", produtos);
		}

	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
