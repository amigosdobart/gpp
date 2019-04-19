package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.util.HttpRequestParser;

import com.brt.gpp.comum.mapeamentos.entidade.ItemContestacao;

/**
 * Sincroniza o atributo 'checked' dos itens de contestacao de acordo com a VM
 * 
 * @author Bernardo Vergne Dias
 * Data: 25/02/2008
 */
public class SelecaoItensBSAction extends ActionPortalHibernate
{
    Logger logger = Logger.getLogger(this.getClass());

    public ActionForward performPortal(ActionMapping actionMapping, 
            ActionForm actionForm, HttpServletRequest request, 
            HttpServletResponse response, SessionFactory sessionFactory) throws Exception
    {
        response.setContentType("text/plain");
        response.setHeader( "Pragma", "no-cache" );
        response.addHeader( "Cache-Control", "must-revalidate" );
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );
        response.setDateHeader("Expires", 0);
        
        HttpSession httpSession = request.getSession();
        Properties params       = HttpRequestParser.getParametros(request);
        Session session = null;
        
        try 
        {   
            // CAPTURA OS PARAMETROS
            
            String itensMarcadosAtuais     = params.getProperty("itensMarcadosAtuais");
            String itensDesmarcadosAtuais  = params.getProperty("itensDesmarcadosAtuais");            
            String itensMarcadosNovos     = params.getProperty("itensMarcadosNovos");
            String itensDesmarcadosNovos  = params.getProperty("itensDesmarcadosNovos");   
            
            List itensContestacaoAtuais     = (List)httpSession.getAttribute(ShowConsultaContestacaoAction.ID_SESSAO_ATUAIS);
            List itensContestacaoNovos      = (List)httpSession.getAttribute(ShowConsultaContestacaoAction.ID_SESSAO_NOVOS);
            
            atualizarStatusSelecao(itensContestacaoAtuais, itensMarcadosAtuais, true);
            atualizarStatusSelecao(itensContestacaoAtuais, itensDesmarcadosAtuais, false);
            atualizarStatusSelecao(itensContestacaoNovos, itensMarcadosNovos, true);
            atualizarStatusSelecao(itensContestacaoNovos, itensDesmarcadosNovos, false);
            
            response.getWriter().print("sucesso");
        }
        catch (Exception e)
        {
            response.getWriter().print("Erro ao sincronizar itens de BS. " + e);
            logger.error("Erro ao sincronizar itens de BS. " + e);  
            if (session != null && session.isOpen()) 
                session.getTransaction().rollback();
        }
        
        return null;
    }
    
    /**
     * Atualiza o valor do atributo 'checked' dos itens de constestacao de acordo
     * com a lista de índices informada.
     */
    public static void atualizarStatusSelecao(List resultadoTotal, String listaItens, boolean checked)
    {
        if (listaItens == null) return;
        String[] ids = listaItens.split(";");
        
        for (int i = 0; i < ids.length; i++)
        {
            Object item = resultadoTotal.get(Integer.parseInt(ids[i]));
            ((ItemContestacao)item).setChecked(checked);
        }
    }

    /**
     * @return Nome da operação (permissão) associada a essa ação.
     */
    public String getOperacao() 
    {
        return null;
    }
}