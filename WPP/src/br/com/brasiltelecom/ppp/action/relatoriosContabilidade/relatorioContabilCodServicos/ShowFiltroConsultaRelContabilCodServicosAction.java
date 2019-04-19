package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilCodServicos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

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
 * Mostra a tela de consulta de relatorio Códigos de Serviços Pré-Pago
 * 
 * @author Geraldo Palmeira
 * @since 26/04/2005
 */
public class ShowFiltroConsultaRelContabilCodServicosAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CONT_COD_SERVICOS;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelContabilCodServicos.vm";
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
				Collection cns = CodigoNacionalHome.findAllBrt(db);
				context.put("cns",cns);
				Collection ufs = this.getUFs(cns);
				context.put("ufs",ufs);
				Collection cnso = CodigoNacionalHome.findAll(db);
				context.put("cnso",cnso);
			} 
			catch (PersistenceException pe) 
			{	
				logger.error("Não foi possível realizar a consulta de relatório Contabil/Codigos Servicos, problemas na conexão com o banco de dados (" +
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
