package br.com.brasiltelecom.ppp.action.cadastroServicoSasc;

import java.util.Iterator;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.SascServicoHome;
import br.com.brasiltelecom.ppp.portal.entity.SascPerfil;
import br.com.brasiltelecom.ppp.portal.entity.SascServico;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.exolab.castor.jdo.Database;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtualizaBlackListSascAction extends ActionPortal
{
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, 
									   ActionForm actionForm, 
									   HttpServletRequest request, 
									   HttpServletResponse response, 
									   Database db) throws Exception
	{
		codOperacao = Constantes.COD_ATUALIZACAO_BLACK_LIST;
		logger.info("Atualizacao da BlackList do SASC");
		
		try
		{
			db.begin();
			SascPerfil  perfil  = SascServicoHome.findPerfilById (db, Integer.parseInt(request.getParameter("codPerfil")), false);
			String servicosBlackList[] = request.getParameterValues("blackList");
			
			for (Iterator i = perfil.getListaDeServicos().iterator(); i.hasNext(); )
			{
				SascServico servico = (SascServico) i.next();
				servico.setBlackList(false);
				
				for (int j = 0; j < servicosBlackList.length; j++)
				{
					if (Integer.parseInt(servicosBlackList[j]) == servico.getCodServico())
						servico.setBlackList(true);
				}
			}
			
			request.setAttribute("perfil", perfil);
			db.commit();
		}
		catch (Exception e)
		{
			if (db.isActive())
				db.rollback();
			
			logger.error("Erro na atualizacao da BlackList. Erro: " + e.getMessage());
		}
		finally
		{
			if (!db.isClosed())
				db.close();
		}
		
		return actionMapping.findForward("success");
	}
	
	/**
	 * (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}