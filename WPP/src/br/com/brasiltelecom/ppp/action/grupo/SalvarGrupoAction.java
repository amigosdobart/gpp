package br.com.brasiltelecom.ppp.action.grupo;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.home.GrupoHome;
import br.com.brasiltelecom.ppp.home.GrupoUsuarioHome;
import br.com.brasiltelecom.ppp.home.OperacaoHome;
import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.entity.GrupoUsuario;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * Classe que realiza as ações de cadastrar, alterar e excluir grupos.
 * 
 * @author Luciano Vilela
 * @since 01/09/2003
 * 
 * Atualizado por Bernardo Vergne Data: 12/02/2007
 * Atualizado por Lucas Mindêllo de Andrade 13/03/2008
 * 
 */
public class SalvarGrupoAction extends ActionPortal 
{

	private String codOperacao = Constantes.MENU_CADASTRO_PERFIL;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Método principal da classe, reponsável por salvar, excluir e alterar um
	 * Grupo.
	 * 
	 * Atualizado por Lucas Mindêllo de Andrade 13/03/2008
	 * 
	 * @param actionMapping
	 *            parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm
	 *            parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request
	 *            parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response
	 *            parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db
	 *            parâmetro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception,
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping,
	 *      ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception
	{

		ActionForward result = null;
		String acao = request.getParameter("acao");

		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		Map operacoesUsuario = login.getOperacoesPermitidas("ACAO");

		try
		{
			db.begin();

			if ("salvar".equals(acao))
			{
				// VERIFICA SE TEM PERMISSÃO DE ALTERAR OU GRAVAR CONTRATOS

				if ((operacoesUsuario.get(Constantes.COD_ALTERAR_GRUPO) != null)
						|| (operacoesUsuario.get(Constantes.COD_GRAVAR_GRUPO) != null))
				{

					int id = Integer.parseInt(request.getParameter("id"));
					String nome = request.getParameter("nome");

					// VERIFICA SE POSSUI O MESMO NOME

					Grupo grupo1 = GrupoHome.findByNome(db, nome);

					if (grupo1 != null && grupo1.getId() != id)
					{
						request
								.setAttribute(
										"mensagem",
										"O nome \'"
												+ nome
												+ "\' já está cadastrado, favor escolher outro nome!");
						result = actionMapping.findForward("erro");
					}
					else
					{

						Grupo grupo = GrupoHome.findByID(db, id);

						if (grupo == null)
						{
							grupo = new Grupo();
							db.create(grupo);
							logger
									.info("Inclusão de grupo de perfil solicitada");
							request.setAttribute(Constantes.MENSAGEM, "Grupo '"
									+ request.getParameter("nome")
									+ "' criado com sucesso!");
							this.codOperacao = Constantes.COD_GRAVAR_GRUPO;
						}
						else
						{
							logger
									.info("Alteração de grupo de perfil solicitada");
							request.setAttribute(Constantes.MENSAGEM, "Grupo '"
									+ request.getParameter("nome")
									+ "' alterado com sucesso!");
							this.codOperacao = Constantes.COD_ALTERAR_GRUPO;
						}

						// SALVA O GRUPO

						DecimalFormat df = new DecimalFormat("#,##0.00");
						Map parametros = Util.parameterToMap(request);
						Collection operacoes = OperacaoHome.getOperacoes(db,
								parametros);
						Collection categorias = CategoriaHome.getCategorias(db,
								parametros);

						grupo.setNome((String) request.getParameter("nome"));
						grupo.setMaxValorCobranca(df.parse(
								request.getParameter("maxValorCobranca"))
								.doubleValue());
						grupo.setAtivo(1);
						grupo.setOperacoes(operacoes);
						grupo.setCategorias(categorias);

						request.setAttribute("id", String
								.valueOf(grupo.getId()));
						result = actionMapping.findForward("success");
					}
				}
				else
				{
					request
							.setAttribute("mensagem",
									"Você não possui permissão de alterar/gravar Grupo!");
					result = actionMapping.findForward("erro");
				}
			}
			else if ("excluir".equals(acao))
			{
				// VERIFICA SE TEM PERMISSÃO DE EXCLUIR EMPRESA

				if (operacoesUsuario.get(Constantes.COD_EXCLUIR_GRUPO) != null)
				{

					int id = Integer.parseInt((String) request
							.getParameter("id"));

					logger.info("Exclusão de grupo de perfil solicitada");
					GrupoUsuario grupoUsuario = GrupoUsuarioHome.findByGrupo(
							db, id);

					if (grupoUsuario != null)
					{
						request
								.setAttribute(
										"mensagem",
										"O Grupo não pode ser Excluído,<br> pois existe(m) Usuário(s) associado(s) a este Grupo.<br>Favor associar este(s) Usuário(s) a outro Grupo ou excluí-lo(s)!");
						result = actionMapping.findForward("erro");
					}
					else
					{
						Grupo grupo = GrupoHome.findByID(db, id);
						grupo.setAtivo(0);
						request.setAttribute(Constantes.MENSAGEM, "Grupo '"
								+ grupo.getNome() + "' excluído com sucesso!");
						this.codOperacao = Constantes.COD_EXCLUIR_GRUPO;
						result = actionMapping.findForward("successDelete");
					}
				}
				else
				{
					request.setAttribute("mensagem",
							"Você não possui permissão de excluir Grupo!");
					result = actionMapping.findForward("erro");
				}
			}
		}
		catch (Exception e)
		{
			logger
					.error("Não foi possível realizar a inclusão/alteração/exclusão de grupo de perfil, "
							+ "problemas na conexão com o banco de dados ("
							+ e.getMessage() + ")");
			throw e;
		}
		finally
		{
			db.commit();
		}

		return result;
	}

	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return this.codOperacao;
	}

}
