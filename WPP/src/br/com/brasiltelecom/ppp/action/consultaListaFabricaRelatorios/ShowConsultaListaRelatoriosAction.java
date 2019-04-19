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
 * Lista os relat�rios dispon�veis na F�brica de Relat�rios do GPP.
 * 
 * � aplicado um filtro autom�tico de acordo com as pastas nas 
 * quais o usu�rio logado tem permiss�o de acessar.
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
	 * M�todo para pegar a Tela Principal do Usu�rio.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaListaFabricaRelatorios.vm";
	}

	/**
	 * M�todo principal da Classe, � o corpo da Classe.
	 * 
	 * @param context par�metro do tipo VelocityContext.
	 * @param request par�metro do tipo HttpServletRequest.
	 * @param db	   par�metro do tipo Database do Castor.
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
			
			// Busca o usu�rio do portal
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
			context.put("mensagem", "Erro ao consultar os relat�rios.");
			context.put("relatorios", "");
			logger.error("Erro ao consultar os relat�rios.");	
		}
	}
    
    /**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
