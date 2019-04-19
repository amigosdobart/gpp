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
 * Classe respons�vel por salvar e editar Usu�rios Internos e Externos, de acordo com os par�metros passados via requisi��o WEB.
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
 	 * M�todo para buscar todos os c�digos das opera��es do tipo "ACAO"
	 * @return String Retorna as operacoes do tipo ACAO.
	 */
	private String buscaCodigosAcao(Database db) throws Exception {
		
		/********Vari�veis que ser�o utilizadas no pr�prio m�todo*******/
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
	 * M�todo principal da classe, repons�vel por salvar, excluir , alterar, reinicializar senha e desbloquear um Usu�rio Interno ou Externo.
	 * 
	 * @param actionMapping par�metro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm par�metro do tipo org.apache.struts.action.ActionForm.
	 * @param request  par�metro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response par�metro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db par�metro do tipo org.exolab.castor.jdo.Database.
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
				//VERIFICA SE TEM PERMISS�O DE SALVAR USUARIOS
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
							request.setAttribute("mensagem", "J� existe um usu�rio utilizando a matr�cula\'"+ request.getParameter("obr_matricula") +"\'.");
							return actionMapping.findForward("erro");
					}
	
					UsuarioHome.setByRequest(usuario, request, db);	
	
					if(novo) {
						logger.info("Inclus�o de usu�rio solicitada");
						// UsuarioHome.criarUsuario(usuario,db);
						request.setAttribute(Constantes.MENSAGEM, "Usu�rio \'"+ usuario.getMatricula() +"\' salvo com sucesso!");
						this.codOperacao = Constantes.COD_GRAVAR_USUARIO;
				
					} 
					 else {
						logger.info("Altera��o de usu�rio solicitada");
						request.setAttribute(Constantes.MENSAGEM, "Usu�rio \'"+ usuario.getMatricula() +"\' alterado com sucesso!");
						this.codOperacao = Constantes.COD_ALTERAR_USUARIO;	
				
					}
					
					result = actionMapping.findForward("success");

				}
				 else {

					request.setAttribute("mensagem", "Voc� n�o possui permiss�o de alterar/gravar Usu�rios!");
					return actionMapping.findForward("erro");	
				}		  

			}
			else if ("excluir".equals(acao)){
			
				//VERIFICA SE TEM PERMISS�O DE EXCLUIR USUARIOS
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
						logger.info("Exclus�o de usu�rio solicitada");
						usuario  = UsuarioHome.findByID(db, matricula);
						db.remove(usuario);
						request.setAttribute(Constantes.MENSAGEM, "Usu�rio \'"+ usuario.getMatricula() +"\' exclu�do com sucesso");				
						this.codOperacao = Constantes.COD_EXCLUIR_USUARIO;
						result = actionMapping.findForward("successDelete");
	
					} else	{
					
						request.setAttribute("mensagem", "Usu�rio n�o pode ser exclu�do!<br> Ele j� realizou opera��o no sistema.");
						return actionMapping.findForward("erro");	
				
					}

				}
				 else {

					request.setAttribute("mensagem", "Voc� n�o possui permiss�o de excluir Usu�rios!");
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
					 Senha inicial � igual aos primeiros 4 digitos do cpf 
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
					request.setAttribute(Constantes.MENSAGEM, "Usu�rio copiado com sucesso, consulte o usuario ID=\""+
					"c "+usuario.getMatricula()+"\"");
				
					this.codOperacao = Constantes.COD_GRAVAR_USUARIO;
					result =  actionMapping.findForward("mensagemSistema");
						
				}

			}
			else if ("Reset Senha".equalsIgnoreCase(acao)) {
			
				//VERIFICA SE TEM PERMISS�O DE INICIALIZAR SENHA DOS USUARIOS
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
					/*	Senha inicial � igual aos primeiros 4 digitos do cpf  */
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
					request.setAttribute(Constantes.MENSAGEM, "Senha do usu�rio \'"+ usuario.getMatricula() +"\' foi reiniciada com sucesso");
					this.codOperacao = Constantes.COD_INICIALIZAR_SENHA;
					result = actionMapping.findForward("success");
				
				}
				else {
				
					request.setAttribute("mensagem", "Voc� n�o possui permiss�o de inicializar as senhas dos Usu�rios!");
					return actionMapping.findForward("erro");
				
				}			
			} 
			else if ("desbloquear".equalsIgnoreCase(acao)) {
			
				//VERIFICA SE TEM PERMISS�O DE DESBLOQUEAR USUARIOS
				if(operacoesUsuario.get(Constantes.COD_DESBLOQUEAR_USUARIO) != null) {

					String matricula = request.getParameter("obr_matricula");
					usuario  = UsuarioHome.findByID(db, matricula);
					usuario.setTentativas(0);
					usuario.setDataValidadeStr("");
					request.setAttribute(Constantes.MENSAGEM, "Usu�rio \'"+ usuario.getMatricula() +"\' desbloqueado com sucesso");
					this.codOperacao = Constantes.COD_DESBLOQUEAR_USUARIO;
					result = actionMapping.findForward("success");
				} 
				else {
			 	
					request.setAttribute("mensagem", "Voc� n�o possui permiss�o de desbloquear os Usu�rios!");
					return actionMapping.findForward("erro");
				}
			}
		}
		catch (Exception e)
		{
			logger.error("N�o foi poss�vel realizar a inclus�o/altera��o/exclus�o de usu�rio, problemas na conex�o com o banco de dados (" +
								e.getMessage() + ")");
			throw e;
		}
		
		return result;
	}

	/**
	 * M�todo para pegar a Opera��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Opera��o realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

}
