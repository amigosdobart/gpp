package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.OrigensEstornoHome;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * Mostra a página de cadastro de origens de requisição 
 * para os lotes de Estorno/Expurgo de Bonus Pula-Pula.
 * 
 * @author Bernardo Vergne Dias
 * @since 20/12/2006
 */
public class ShowCadastroOrigensEstornoAction extends ShowAction 
{	
	
	private String codOperacao = Constantes.MENU_ORIGENS_REQUISICAO;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Método para pegar a Tela Principal do Usuário.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaOrigensEstorno.vm";
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
			// Lista o cadastro de origens de estorno
			db.begin();
			ArrayList itens = OrigensEstornoHome.findAll(db);
			db.commit();
			
			
			// Captura as acoes permitidas ao usuario
			Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));	
			
			boolean permissaoCadastrar = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_CADASTRAR_ORIGEM) != null);
			boolean permissaoExcluir = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_EXCLUIR_ORIGEM) != null);
			boolean permissaoAtualizar = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_ATUALIZAR_ORIGEM) != null);
					
			context.put("permissaoCadastrar", (permissaoCadastrar == true) ? "true" : "false");
			context.put("permissaoExcluir", (permissaoExcluir == true) ? "true" : "false");
			context.put("permissaoAtualizar", (permissaoAtualizar == true) ? "true" : "false");
			
			context.put("itens", itens);
	
		}
		catch (Exception e)
		{
			context.put("mensagem", "Erro ao consultar o cadastro de origens de Estorno/Expurgo Pula-Pula.");
			logger.error("Erro ao consultar o cadastro de origens de Estorno/Expurgo Pula-Pula.");	
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