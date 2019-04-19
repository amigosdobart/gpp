package br.com.brasiltelecom.ppp.action.cadastroServicoSasc;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.SascServicoHome;
import br.com.brasiltelecom.ppp.portal.entity.SascGrupo;
import br.com.brasiltelecom.ppp.portal.entity.SascPerfil;
import br.com.brasiltelecom.ppp.portal.entity.SascServico;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exolab.castor.jdo.Database;

public class CadastroServicoSascAction extends ActionPortal
{ 
	private String codOperacao;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * (non-Javadoc)
	 *	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 *	 
	 */
	public ActionForward performPortal(ActionMapping actionMapping, 
									   ActionForm actionForm, 
									   HttpServletRequest request, 
									   HttpServletResponse response, 
									   Database db) throws Exception
	{
		codOperacao = Constantes.COD_CADASTRAR_NOVO_SERV_SASC;
		logger.info("Inclusao de novo servico no SASC");
		
		try
		{
			db.begin();
			
			// Seleciona o perfil o qual o servico pertence
			SascPerfil perfil = SascServicoHome.findPerfilById(db, Integer.parseInt(request.getParameter("codPerfil")), false);
			
			// Seleciona o grupo o qual o servico pertence
			SascGrupo grupo = SascServicoHome.findGrupoById(db, Integer.parseInt(request.getParameter("codGrupo")));
			
			// Seta os parametros do servico a ser inserido no perfil
			SascServico servico = new SascServico();
			servico.setPerfil(perfil);
			servico.setGrupo(grupo);
			servico.setCodServico(Integer.parseInt(request.getParameter("codServico")));
			servico.setNomeServico(request.getParameter("nomeServico"));
			servico.setBlackList(true);
			
			// Adiciona o servico na lista de servicos do Perfil
			SascServicoHome.insereNovoServico(db, servico);
			request.setAttribute("perfil", perfil);
			
			db.commit();
		}
		catch (Exception e)
		{
			if (db.isActive())
				db.rollback();
			
			logger.error("Erro na inlcusao de novo servico do SASC. Erro: " + e.getMessage());
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
	 *	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 *	 
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}