package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTRecargaTFPP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.portal.entity.CodigoNacional;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import org.apache.log4j.Logger;




/**
 * Mostra a tela de consulta de relatorio Contabilidade Recargas TFPP
 * 
 * @author Marcos C. Magalhaes
 * @since 12/09/2005
 */
public class ShowFiltroConsultaRelMKTRecargaTFPPAction extends ShowAction {

	private String codOperacao = Constantes.MENU_REL_MKT_REC_TFPP;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelMKTRecargaTFPP.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception 
		{
			try 
			{
				db.begin();
				Collection c = CodigoNacionalHome.findAllBrt(db);
				context.put("cns",c);
				Collection ufs = this.getUFs(c);
				context.put("ufs",ufs);
			} 
			catch (PersistenceException pe) 
			{
				logger.error("Não foi possível realizar a consulta de relatório contabil/recargas tfpp, problemas na conexão com o banco de dados (" +
							pe.getMessage() + ")");
				throw pe;
			}
			
	}

	private Collection getUFs(Collection cns)
	{
		HashMap control = new HashMap();
		ArrayList result = new ArrayList();
		
		Iterator iterator = cns.iterator();
		while(iterator.hasNext())
		{
			Object value = iterator.next();
			
			if(!control.containsKey(((CodigoNacional)value).getIdtUF())) 
			{
				control.put(((CodigoNacional)value).getIdtUF(), value);
				result.add(value);
			}
		}
		
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}
