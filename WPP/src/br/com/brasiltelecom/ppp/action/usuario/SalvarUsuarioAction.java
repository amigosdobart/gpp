package br.com.brasiltelecom.ppp.action.usuario;

import java.util.Collection;
import java.util.Iterator;
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
import br.com.brasiltelecom.ppp.home.LogOperacaoHome;
import br.com.brasiltelecom.ppp.home.OperacaoHome;
import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.portal.entity.LogOperacao;
import br.com.brasiltelecom.ppp.portal.entity.Operacao;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe responsável por salvar e editar Usuários Internos e Externos, de acordo com os parâmetros passados via requisição WEB.
 * 
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 */
public class SalvarUsuarioAction extends ActionPortal {


	/********Atributos da Classe*******/
	 private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	/**********************************/

	/**
 	 * Método para buscar todos os códigos das operações do tipo "ACAO"
	 * @return String Retorna as operacoes do tipo ACAO.
	 */
	private String buscaCodigosAcao(Database db) throws Exception {
		
		/********Variáveis que serão utilizadas no próprio método*******/
         int contador = 0;		
		 Collection tpOperacao = null;
		/**************************************************************/

		tpOperacao = OperacaoHome.findByTipo(db, Constantes.ACAO);

		StringBuffer retorno = new StringBuffer();

		for (Iterator it = tpOperacao.iterator();it.hasNext();) {
			Operacao operacao = (Operacao) it.next();
			if (contador == 0)	{
				retorno.append(String.valueOf(operacao.getId()));
			} else	{
				retorno.append(",").append(String.valueOf(operacao.getId()));	
			}
			contador++;
		}
		return retorno.toString();

	}

	/**
	 * Método principal da classe, reponsável por salvar, excluir , alterar, reinicializar senha e desbloquear um Usuário Interno ou Externo.
	 * 
	 * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db parâmetro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */

	public ActionForward performPortal(ActionMapping actionMapping,
		ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,
		Database db) throws Exception {
		
		ActionForward result = null;
		String acao = request.getParameter("acao");
		Usuario usuario = null;
		this.codOperacao = null;
		
		
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		Map operacoesUsuario = login.getOperacoesPermitidas("ACAO");

		db.begin();
		
		try
		{
			if("salvar".equals(acao))
			{			
				//VERIFICA SE TEM PERMISSÃO DE SALVAR USUARIOS
				if ((operacoesUsuario.get(Constantes.COD_ALTERAR_USUARIO) != null) || 
					(operacoesUsuario.get(Constantes.COD_GRAVAR_USUARIO) != null))	{

					String matricula = request.getParameter("obr_matricula");
					boolean novo = false;

					usuario = UsuarioHome.findByID(db, matricula);
	
					if(usuario == null) {
						usuario = new Usuario();
						novo = true;
					}
	
					//VERIFICA SE O USUARIO JA EXISTE
					if ((!novo) && (request.getParameter("novoUser") != null) 
						&& (!"".equals(request.getParameter("novoUser"))))
					{
							request.setAttribute("mensagem", "Já existe um usuário utilizando a matrícula\'"+ request.getParameter("obr_matricula") +"\'.");
							return actionMapping.findForward("erro");
					}
	
					UsuarioHome.setByRequest(usuario, request, db);	
	
					if(novo) {
						logger.info("Inclusão de usuário solicitada");
						// UsuarioHome.criarUsuario(usuario,db);
						request.setAttribute(Constantes.MENSAGEM, "Usuário \'"+ usuario.getMatricula() +"\' salvo com sucesso!");
						this.codOperacao = Constantes.COD_GRAVAR_USUARIO;
				
					} 
					 else {
						logger.info("Alteração de usuário solicitada");
						request.setAttribute(Constantes.MENSAGEM, "Usuário \'"+ usuario.getMatricula() +"\' alterado com sucesso!");
						this.codOperacao = Constantes.COD_ALTERAR_USUARIO;	
				
					}
					
					result = actionMapping.findForward("success");

				}
				 else {

					request.setAttribute("mensagem", "Você não possui permissão de alterar/gravar Usuários!");
					return actionMapping.findForward("erro");	
				}		  

			}
			else if ("excluir".equals(acao)){
			
				//VERIFICA SE TEM PERMISSÃO DE EXCLUIR USUARIOS
				if(operacoesUsuario.get(Constantes.COD_EXCLUIR_USUARIO) != null) {
			
					//VERIFICA SE O USUARIO JA FEZ ALGUMA ACAO NO SISTEMA
					String codigos = buscaCodigosAcao(db);
					String matricula = request.getParameter("obr_matricula");
					boolean possuiRegistro = false;
				
					Collection resp = LogOperacaoHome.findByTipo(db,codigos,matricula);
				
					for (Iterator it = resp.iterator();it.hasNext();) {
						LogOperacao operacao = (LogOperacao) it.next();
						if (operacao.getId() > 0) {
							possuiRegistro = true;
							break;	
						}
					}
		
					if (!possuiRegistro) {
						logger.info("Exclusão de usuário solicitada");
						usuario  = UsuarioHome.findByID(db, matricula);
						db.remove(usuario);
						request.setAttribute(Constantes.MENSAGEM, "Usuário \'"+ usuario.getMatricula() +"\' excluído com sucesso");				
						this.codOperacao = Constantes.COD_EXCLUIR_USUARIO;
						result = actionMapping.findForward("successDelete");
	
					} else	{
					
						request.setAttribute("mensagem", "Usuário não pode ser excluído!<br> Ele já realizou operação no sistema.");
						return actionMapping.findForward("erro");	
				
					}

				}
				 else {

					request.setAttribute("mensagem", "Você não possui permissão de excluir Usuários!");
					return actionMapping.findForward("erro");
				}			

			}
			else if ("copiar".equals(acao)) {

				String matricula = request.getParameter("obr_matricula");
				usuario  = UsuarioHome.findByID(db, matricula);
			
				if(usuario!= null){
					Usuario novoUsuario = new Usuario();
					novoUsuario.setNome(usuario.getNome());
					novoUsuario.setCargo(usuario.getCargo());
					novoUsuario.setDataValidade(usuario.getDataValidade());
					novoUsuario.setEmail(usuario.getEmail());
					novoUsuario.setEmpresa(usuario.getEmpresa());
				
//					novoUsuario.setFilial(usuario.getFilial());

					novoUsuario.setTelefone(usuario.getTelefone());
					novoUsuario.setCpf(usuario.getCpf());
					novoUsuario.setGrupos(usuario.getGrupos());
					novoUsuario.setMatricula("c "+usuario.getMatricula());
	/*				Senha senhaInicial = new Senha();
					String senha = null;
					senhaInicial.setId(0);
					senhaInicial.setDataAtualizacao(new Date(0));
					senhaInicial.setUsuario(usuario);
					 Senha inicial é igual aos primeiros 4 digitos do cpf 
					if(novoUsuario.getCpf() != null){
						senha = novoUsuario.getCpf().length()>4 ? 
									 novoUsuario.getCpf().substring(0,4)
									:novoUsuario.getCpf();
					}
					else{
						senha = "senha";	
					}
					senhaInicial.setSenha(GeradorHash.CriptografarSenha(senha));
					novoUsuario.setTentativas(0);
					novoUsuario.setSenhas(new ArrayList());
					novoUsuario.getSenhas().add(senhaInicial);

					db.create(novoUsuario);
	*/
					request.setAttribute(Constantes.MENSAGEM, "Usuário copiado com sucesso, consulte o usuario ID=\""+
					"c "+usuario.getMatricula()+"\"");
				
					this.codOperacao = Constantes.COD_GRAVAR_USUARIO;
					result =  actionMapping.findForward("mensagemSistema");
						
				}

			}
			else if ("Reset Senha".equalsIgnoreCase(acao)) {
			
				//VERIFICA SE TEM PERMISSÃO DE INICIALIZAR SENHA DOS USUARIOS
				if(operacoesUsuario.get(Constantes.COD_INICIALIZAR_SENHA) != null)	{

					String matricula = request.getParameter("obr_matricula");
					usuario  = UsuarioHome.findByID(db, matricula);
				
					//Collection senhas = usuario.getSenhas();
	
					/* Atualiza o id das senhas antigas	*/
	/*
					for(Iterator it = senhas.iterator(); it.hasNext();) {
						Senha sen = (Senha) it.next();
						sen.setId(sen.getId() + 1);
					}
				
					Senha senhaInicial = new Senha();
					String senha = null;
					senhaInicial.setId(0);
					senhaInicial.setDataAtualizacao(new Date(0));
					senhaInicial.setUsuario(usuario);
	*/	
					/*	Senha inicial é igual aos primeiros 4 digitos do cpf  */
	/*				if(usuario.getCpf() != null){
						senha = usuario.getCpf().length()>4 ? 
									 usuario.getCpf().substring(0,4)
									:usuario.getCpf();
					}
					else { 
						senha = "senha";	
					}
				
					senhaInicial.setSenha(GeradorHash.CriptografarSenha(senha));
					usuario.setTentativas(0);
					usuario.getSenhas().add(senhaInicial);
	*/
					request.setAttribute(Constantes.MENSAGEM, "Senha do usuário \'"+ usuario.getMatricula() +"\' foi reiniciada com sucesso");
					this.codOperacao = Constantes.COD_INICIALIZAR_SENHA;
					result = actionMapping.findForward("success");
				
				}
				else {
				
					request.setAttribute("mensagem", "Você não possui permissão de inicializar as senhas dos Usuários!");
					return actionMapping.findForward("erro");
				
				}			
			} 
			else if ("desbloquear".equalsIgnoreCase(acao)) {
			
				//VERIFICA SE TEM PERMISSÃO DE DESBLOQUEAR USUARIOS
				if(operacoesUsuario.get(Constantes.COD_DESBLOQUEAR_USUARIO) != null) {

					String matricula = request.getParameter("obr_matricula");
					usuario  = UsuarioHome.findByID(db, matricula);
					usuario.setTentativas(0);
					usuario.setDataValidadeStr("");
					request.setAttribute(Constantes.MENSAGEM, "Usuário \'"+ usuario.getMatricula() +"\' desbloqueado com sucesso");
					this.codOperacao = Constantes.COD_DESBLOQUEAR_USUARIO;
					result = actionMapping.findForward("success");
				} 
				else {
			 	
					request.setAttribute("mensagem", "Você não possui permissão de desbloquear os Usuários!");
					return actionMapping.findForward("erro");
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a inclusão/alteração/exclusão de usuário, problemas na conexão com o banco de dados (" +
								e.getMessage() + ")");
			throw e;
		}
		
		return result;
	}

	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

}
