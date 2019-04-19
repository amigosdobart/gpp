package br.com.brasiltelecom.ppp.action.base;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.home.GrupoHome;
import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.acesso.LogFacade;

import org.apache.log4j.Logger;



/**
 * Classe abstrata que deve ser extendida por todas as classes de ação.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 * Atualizardo por Bernardo Vergne
 * Data: 08/02/2007
 */
public abstract class ActionPortal extends Action 
{

    /**
     *
     * Método principal do Struts, é o corpo da Classe.<br>
     * 
     * Possui regra de negócio necessária para controle de conexão com o banco de dados,sessão do usuário e geração de Log.
     * 
     * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
     * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
     * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
     * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
     *
     * @throws java.io.IOException, javax.servlet.http.ServletException.
     * 
     * @see org.apache.struts.action.Action#execute(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response)
     */
    public final ActionForward execute(ActionMapping actionMapping,
          ActionForm actionForm,HttpServletRequest request,HttpServletResponse response) 
          throws IOException, ServletException 
    {
        
        HttpSession session = request.getSession(true);
        ActionForward result= null;
        Usuario usuario     = null;
        JDO jdo             = (JDO) servlet.getServletContext().getAttribute(Constantes.JDO);
        Database db         = null;
        Logger log          = Logger.getLogger(this.getClass());

        // CAPTURA CONEXAO COM O BANCO
        
        try
        {
            db = jdo.getDatabase();
        }
        catch(PersistenceException e)
        {
            request.setAttribute(Constantes.MENSAGEM, e.getMessage());
            log.error("Error ao abrir banco de dados", e);
            return actionMapping.findForward("error");  

        }
        
        // VALIDA LOGIN DO USUARIO
        // Tenta capturar o usuário da sessão ou do ICHAIN

        try 
        {
            usuario = validaUsuario(request, db);
        } 
        catch (PersistenceException e) 
        {
            log.error("Error ao validar usuario", e);
            request.setAttribute(Constantes.MENSAGEM, e.getMessage());
            return actionMapping.findForward("error");
        }
        
        // SALVA INFORMACOES DO TOOLBAR NA SESSAO
        // Identifica qual eh o nome do servidor ou ip do toolbar que estah realizando
        // a requisicao do portal. Este parametro deve ser adicionado a sessao do usuario
        // pois serah utilizado em outras telas do sistema
        String hostNameToolbar = request.getParameter("ip");
        if (hostNameToolbar != null)
            session.setAttribute("ipToolbar", hostNameToolbar);
        
        // SALVA USUÁRIO NA SESSAO
        
        if(usuario == null)
        {
            request.setAttribute(Constantes.MENSAGEM, "Não existe usuário logado ou as informações de autenticação são inválidas");
            return actionMapping.findForward("showLogon");
        }
        else
        {
            session.setAttribute(Constantes.USUARIO, usuario);
            session.setAttribute(Constantes.LOGIN, usuario);    
        }

        // VERIFICA PERMISSAO (OPERACAO) PARA CONTINUAR O ACTION

        Map operacoes = usuario.getOperacoesPermitidas(null);
        String operacao = getOperacao();
        
        if(operacao != null && operacoes.get(operacao) == null)
        {
            request.setAttribute(Constantes.MENSAGEM, "Você não tem permissão para visualizar essa página");
            return actionMapping.findForward("error");  
        }
        
        if (operacoes.size() == 0)
        {
            request.setAttribute(Constantes.MENSAGEM, "O usuário '" + usuario.getMatricula() + "' não possui perfil associado no Portal");
            return actionMapping.findForward("error");  
        }
        
        // EXECUTA A AÇÃO DO ACTION (implementada nas classes derivadas)
        
        try
        {
            result = performPortal(actionMapping, actionForm, request, response, db);
            if(getOperacao() != null && !getOperacao().startsWith("MENU"))
            {
                logOperacao(actionMapping, actionForm, request, response, db);
            }
        } 
        catch(Exception e) 
        {
            log.error("Erro ao executar operacao no portal", e);
            request.setAttribute(Constantes.MENSAGEM, e.getMessage());
            return actionMapping.findForward("error");
        } 
        finally 
        {
            try 
            {
                if(db.isActive())   db.commit();    
                if(!db.isClosed())  db.close(); 
            }
            catch(Exception e) 
            {
                    log.error("Erro ao fechar banco", e);
                    request.setAttribute(Constantes.MENSAGEM, e.getMessage());
                    return actionMapping.findForward("error");
            }
        }
        
        return result;
    }
    
    /**
     * Método abstrato que representa a finalidade do método perform do Struts com a conexão do banco de dados.
     * 
     * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
     * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
     * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
     * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
     * @param db parâmetro do tipo org.exolab.castor.jdo.Database.
     * @throws java.lang.Exception, 
     */
    
    public abstract ActionForward performPortal(
        ActionMapping actionMapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response, Database db) throws Exception;
        
    /**
     * Método responsável pela gravação dos logs das operações do sistema.
     * 
     * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
     * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
     * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
     * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
     * @param db parâmetro do tipo org.exolab.castor.jdo.Database.
     * 
     * @throws java.lang.Exception, 
     */ 
    public void logOperacao(ActionMapping actionMapping,
           ActionForm actionForm,
           HttpServletRequest request,
           HttpServletResponse response, Database db) throws Exception 
    {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute(Constantes.USUARIO);
        
        if(!db.isActive())
            db.begin();
        
        if(usuario != null)
        {
            LogFacade.log(db, usuario.getMatricula(),getOperacao(),
                (String) request.getAttribute(Constantes.MENSAGEM), request.getRemoteAddr());
        }
        else
        {
            LogFacade.log(db, null,getOperacao(),
                (String) request.getAttribute(Constantes.MENSAGEM), request.getRemoteAddr());
        }       
    }
    
    /**
     * Verifica se o usuário está logado através da informações passadas pelo IChain
     */
    private Usuario validaUsuario(HttpServletRequest request, Database db) throws PersistenceException 
    {
        String loginHeader = request.getHeader(Constantes.LOGIN_HEADER);    // Header passado pelo ICHAIN
        String grupoHeader = request.getHeader(Constantes.GRUPO_HEADER);    // Header passado pelo ICHAIN
        
        ServletContext servletContext   = servlet.getServletContext();
        Usuario usuario = (Usuario) request.getSession(true).getAttribute(Constantes.USUARIO); 
        
        // Ignora autenticacao pelo iChain se a flag 'integracaoiChain' valer N (ou o header for null)
        if("N".equalsIgnoreCase((String)servletContext.getAttribute(Constantes.INTEGRACAO_ICHAIN)) || loginHeader == null)
            return usuario;
        
        // Confere se o usuario logado eh diferente do informado pelo iChain
        if(usuario != null && !usuario.getMatricula().equalsIgnoreCase(loginHeader))    
            usuario = null;     
                
        /*
         * Se o usuario for null, realiza o logon, caso contrario
         * apenas sincroniza os grupos (se necessario)
         */
        if (usuario == null || deveSincronizarGrupo(usuario, grupoHeader))
        {
            try
            {
                db.begin();
                usuario = UsuarioHome.findByID(db, loginHeader);    
                if (grupoHeader != null && usuario != null)
                {
                    usuario.setGrupos(new Vector());
                    usuario.getGrupos().add(GrupoHome.findByNome(db, grupoHeader));
                }
                db.commit();
                db.close(); 
            }
            finally
            {
                if(db != null &&  db.isActive()) db.commit();   
                if(db != null && !db.isClosed()) db.close();
            }
        }
        
        return usuario;
    }
    
    private boolean deveSincronizarGrupo(Usuario usuario, String nomeGrupo)
    {
        if (nomeGrupo == null)
            return false;        
        
        boolean sincronizar = true;
        Collection grupos = usuario.getGrupos();
        
        if (grupos != null && grupos.size() == 1)
        {
            Grupo grupo = (Grupo)grupos.iterator().next();
            
            if (grupo != null && nomeGrupo.equalsIgnoreCase(grupo.getNome()))
                sincronizar = false;
        }
        
        return sincronizar;
    }
    
    /**
     * Método abstrato que pega a operação realizada pela classe que extende a classe ActionPortal.
     * @return Um objeto do tipo String, que representa a operação realizada.
     */
    public abstract String getOperacao();

}