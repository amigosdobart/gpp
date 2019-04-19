package br.com.brasiltelecom.ppp.action.consultaListaFabricaRelatorios;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.FabricaRelatorioHome;
import br.com.brasiltelecom.ppp.portal.entity.FabricaRelatorio;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Lista os relatórios disponíveis na Fábrica de Relatórios do GPP.
 * 
 * É aplicado um filtro automático de acordo com as pastas nas 
 * quais o usuário logado tem permissão de acessar.
 * 
 * @author Bernardo Vergne Dias
 * @since 04/12/2006
 *
 */
public class ShowConsultaListaRelatoriosAction extends ShowAction 
{

	private String codOperacao = Constantes.MENU_FABRICA_RELATORIOS;
	
	Logger logger = Logger.getLogger(this.getClass());
		
	/**
	 * Método para pegar a Tela Principal do Usuário.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaListaFabricaRelatorios.vm";
	}

	/**
	 * Método principal da Classe, é o corpo da Classe.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, Database db) throws Exception
	{
		try 
		{
			// Lista os relatorios cadastrados em TBL_REL_FABRICA_RELATORIO
			db.begin();
			Map rels = FabricaRelatorioHome.findAll(db);
			db.commit();
			db.close();
			
			// Busca o usuário do portal
			Usuario login = (Usuario) request.getSession().getAttribute(Constantes.LOGIN);
			
			// Lista todas as operacoes do usuario
			HashMap oprs = (HashMap)(login.getOperacoesPermitidas(null));	
			
			
			// Filtra os relatorios
			HashMap relsFiltrados = new HashMap();
			Iterator itRel = ((TreeMap)rels).values().iterator();
			
			while(itRel.hasNext())
			{
				FabricaRelatorio rel = (FabricaRelatorio)(itRel.next());
				if (rel.getOperacao() != null) {
					if (oprs.containsKey(rel.getOperacao().getNome()) == true) {
						relsFiltrados.put(rel.getNome(), rel);
					}
				}
			}
			
			context.put("relatorios", relsFiltrados.values());
		}
		catch (Exception e)
		{
			context.put("mensagem", "Erro ao consultar os relatórios.");
			context.put("relatorios", "");
			logger.error("Erro ao consultar os relatórios.");	
		}
	}
    
    /**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
