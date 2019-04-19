package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.EstornoExpurgoPulaPulaHome;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * Mostra a página de aprovação de previas do Estorno/Expurgo de Bonus Pula-Pula.
 * 
 * @author Bernardo Vergne Dias
 * @since 19/12/2006
 */
public class ShowFiltroAprovacaoPreviaAction extends ShowAction 
{	
	
	private String codOperacao = Constantes.MENU_APROVACAO_PREVIAS;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Método para pegar a Tela Principal do Usuário.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroAprovacaoPrevia.vm";
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
			// Lista os lotes carregados em TBL_INT_ESTORNO_PULA_PULA
			db.begin();
			ArrayList lotes = EstornoExpurgoPulaPulaHome.findAllGroupByLote(db);
			db.commit();
			
			// Captura as acoes permitidas ao usuario
			Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));	
			
			boolean permissaoAprovar = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_APROVAR_PREVIA) != null);
			boolean permissaoRejeitar = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_REJEITAR_PREVIA) != null);
			
			context.put("permissaoAprovar", (permissaoAprovar == true) ? "true" : "false");
			context.put("permissaoRejeitar", (permissaoRejeitar == true) ? "true" : "false");
			
			context.put("lotes", lotes);
	
		}
		catch (Exception e)
		{
			e.printStackTrace();
			context.put("mensagem", "Erro ao consultar os lotes na base de dados.");
			logger.error("Erro ao consultar os lotes.");	
		}
		finally
		{
			db.close();
		}
		
	}
	
	/**
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
