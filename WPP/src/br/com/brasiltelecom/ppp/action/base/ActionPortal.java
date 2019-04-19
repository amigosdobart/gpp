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
 * Classe abstrata que deve ser extendida por todas as classes de a��o.
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
     * M�todo principal do Struts, � o corpo da Classe.<br>
     * 
     * Possui regra de neg�cio necess�ria para controle de conex�o com o banco de dados,sess�o do usu�rio e gera��o de Log.
     * 
     * @param actionMapping par�metro do tipo org.apache.struts.action.ActionMapping.
     * @param actionForm par�metro do tipo org.apache.struts.action.ActionForm.
     * @param request  par�metro do tipo javax.servlet.http.HttpServletRequest.
     * @param response par�metro do tipo javax.servlet.http.HttpServletResponse.
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
        // Tenta capturar o usu�rio da sess�o ou do ICHAIN

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
        
        // SALVA USU�RIO NA SESSAO
        
        if(usuario == null)
        {
            request.setAttribute(Constantes.MENSAGEM, "N�o existe usu�rio logado ou as informa��es de autentica��o s�o inv�lidas");
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
            request.setAttribute(Constantes.MENSAGEM, "Voc� n�o tem permiss�o para visualizar essa p�gina");
            return actionMapping.findForward("error");  
        }
        
        if (operacoes.size() == 0)
        {
            request.setAttribute(Constantes.MENSAGEM, "O usu�rio '" + usuario.getMatricula() + "' n�o possui perfil associado no Portal");
            return actionMapping.findForward("error");  
        }
        
        // EXECUTA A A��O DO ACTION (implementada nas classes derivadas)
        
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
     * M�todo abstrato que representa a finalidade do m�todo perform do Struts com a conex�o do banco de dados.
     * 
     * @param actionMapping par�metro do tipo org.apache.struts.action.ActionMapping.
     * @param actionForm par�metro do tipo org.apache.struts.action.ActionForm.
     * @param request  par�metro do tipo javax.servlet.http.HttpServletRequest.
     * @param response par�metro do tipo javax.servlet.http.HttpServletResponse.
     * @param db par�metro do tipo org.exolab.castor.jdo.Database.
     * @throws java.lang.Exception, 
     */
    
    public abstract ActionForward performPortal(
        ActionMapping actionMapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response, Database db) throws Exception;
        
    /**
     * M�todo respons�vel pela grava��o dos logs das opera��es do sistema.
     * 
     * @param actionMapping par�metro do tipo org.apache.struts.action.ActionMapping.
     * @param actionForm par�metro do tipo org.apache.struts.action.ActionForm.
     * @param request  par�metro do tipo javax.servlet.http.HttpServletRequest.
     * @param response par�metro do tipo javax.servlet.http.HttpServletResponse.
     * @param db par�metro do tipo org.exolab.castor.jdo.Database.
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
     * Verifica se o usu�rio est� logado atrav�s da informa��es passadas pelo IChain
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
     * M�todo abstrato que pega a opera��o realizada pela classe que extende a classe ActionPortal.
     * @return Um objeto do tipo String, que representa a opera��o realizada.
     */
    public abstract String getOperacao();

}