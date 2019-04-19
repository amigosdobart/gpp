
/*
 * Created on 20/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.relatorioFINBaseUsuarios;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.ConfigAssinanteHome;

/*import br.com.brasiltelecom.ppp.home.CanalOrigemBSHome;*/
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de Relatório de Ativação de Cartões Físicos Virtuais
 * 
 * @author Marcos C. Magalhães
 * @since 10/05/2005
 */
public class ShowFiltroConsultaRelFINBaseUsuariosAction extends ShowAction {

	private String codOperacao = Constantes.MENU_FIN_BASE_USUARIOS;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelFINBaseUsuarios.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
			//HttpSession session = request.getSession();
			try
			{
				db.begin();
				//Pesquisa pelos Codigos Nacionais validos para geracao do filtro na pagina de apresentacao
				Collection codigosNacionais = CodigoNacionalHome.findAllBrt(db);
				Collection statusServico = ConfigAssinanteHome.findAllBloqueio(db);
				Collection statusAssinante = ConfigAssinanteHome.findAllBloqueioAss(db);
				context.put("codigosNacionais", codigosNacionais);
				context.put("statusServico", statusServico);
				context.put("statusAssinante", statusAssinante);
											
				
				/*Collection c = CanalOrigemBSHome.findAll(db);
				context.put("origens",c); */
			}
			catch (PersistenceException pe)
			{
				logger.error("Não foi possível realizar a consulta de relatório de Base de Usuários, problemas na conexão com o banco de dados (" +
								pe.getMessage() + ")");
				throw pe;
			}
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}

