package br.com.brasiltelecom.ppp.action.grupo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.home.GrupoHome;
import br.com.brasiltelecom.ppp.home.OperacaoHome;
import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * Classe para mostrar a página de criação e alteração de Grupos.
 * 
 * @author Luciano Vilela
 * @since 22/05/2003
 * 
 * Atualizado por Bernardo Vergne Data: 07/02/2007
 */
public class ShowEditarGrupoAction extends ShowAction 
{

	private String codOperacao = Constantes.MENU_CADASTRO_PERFIL;

	/**
	 * Método para pegar a Tela de Edição de Grupos.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "editGrupo.vm";
	}

	/**
	 * Método principal da Classe, é o corpo da Classe.<BR>
	 * Responsável pela ação no formulário de edição do Grupo.
	 * 
	 * @param context
	 *            parâmetro do tipo VelocityContext.
	 * @param request
	 *            parâmetro do tipo HttpServletRequest.
	 * @param db
	 *            parâmetro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception
	{

		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaGrupo.setPermissao(login, context);

		String acao = request.getParameter("acao");
		Grupo grupo = null;
		Map operacoes = null;
		Collection categorias = CategoriaHome.findAll(db);
		db.begin();

		if (acao.equals("editar"))
		{
			grupo = GrupoHome.findByID(db, Integer.parseInt(request
					.getParameter("id")));
		}
		else if (acao.equals("salvar"))
		{
			grupo = GrupoHome.findByID(db, Integer.parseInt((String)request
					.getAttribute("id")));
		}
		else if (acao.equals("novo"))
		{
			grupo = new Grupo();
			grupo.setId(-1);
		}

		Collection categoriasAtuais = new ArrayList();
		if (grupo.getCategorias() != null)
		{
			for (Iterator i = grupo.getCategorias().iterator(); i.hasNext();)
			{
				categoriasAtuais
						.add(String
								.valueOf(((com.brt.gpp.comum.mapeamentos.entidade.Categoria) i
										.next()).getIdCategoria()));
			}
		}
		context.put("grupo", grupo);
		context.put("categoriasAtuais", categoriasAtuais);
		operacoes = OperacaoHome.findAll(db);
		context.put("operacoesGrupo", operacoes);
		context.put("categoriasGrupo", categorias);
		context.put("acao", acao);
		context.put(Constantes.MENSAGEM, request
				.getAttribute(Constantes.MENSAGEM));

		db.commit();

	}

	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}

}
