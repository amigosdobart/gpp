package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.util.HttpRequestParser;

import com.brt.gpp.comum.mapeamentos.entidade.ItemContestacao;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 * Adiciona um item de contestacao na sessao 
 * 
 * @author Bernardo Vergne Dias
 * Data: 25/02/2008
 */
public class InclusaoItemBSAction extends ActionPortalHibernate
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
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DecimalFormat df     = new DecimalFormat("#,##0.00",  new DecimalFormatSymbols(new Locale("pt","BR")));

        HttpSession httpSession = request.getSession();
        Properties params       = HttpRequestParser.getParametros(request);
        Session session = null;
        
        try 
        {               
            // CAPTURA OS PARAMETROS
            
            String tmpData              = params.getProperty("data");
            String tmpHora              = params.getProperty("hora");
            String tmpSaldoPrincipal    = params.getProperty("saldoPrincipal");
            String tmpSaldoBonus        = params.getProperty("saldoBonus");
            String tmpSaldoSms          = params.getProperty("saldoSms");
            String tmpSaldoDados        = params.getProperty("saldoDados");
            String tmpSaldoPeriodico    = params.getProperty("saldoPeriodico");            
            String tmpOrigem            = params.getProperty("origem");

            double saldoPrincipal       = (tmpSaldoPrincipal != null) ? df.parse(tmpSaldoPrincipal).doubleValue() : 0.0;
            double saldoBonus           = (tmpSaldoBonus != null) ? df.parse(tmpSaldoBonus).doubleValue() : 0.0;
            double saldoSms             = (tmpSaldoSms != null) ? df.parse(tmpSaldoSms).doubleValue() : 0.0;
            double saldoDados           = (tmpSaldoDados != null) ? df.parse(tmpSaldoDados).doubleValue() : 0.0;
            double saldoPeriodico       = (tmpSaldoPeriodico != null) ? df.parse(tmpSaldoPeriodico).doubleValue() : 0.0;
            
            Date   data                 = sdf.parse(tmpData + " " + ((tmpHora != null) ? tmpHora : "00:00:00"));
            String idCanal              = tmpOrigem.substring(0,2);
            String idOrigem             = tmpOrigem.substring(2);
            List itensContestacaoNovos  = (List)httpSession.getAttribute(ShowConsultaContestacaoAction.ID_SESSAO_NOVOS);
            
            // CRIA O NOVO ITEM DE CONTESTACAO
            
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            
            OrigemRecarga origem = OrigemRecargaDAO.findById(session, idCanal, idOrigem);
            
            if (origem == null)
                throw new RuntimeException("Erro ao consultar o tipo de transação informado.");
            
            ItemContestacao item = new ItemContestacao();
            item.setTimestamp(data);
            item.setDesOperacao(origem.getDesOrigem());
            item.setChecked(true);
            item.setVlrPrincipal(saldoPrincipal);
            item.setVlrBonus(saldoBonus);
            item.setVlrSms(saldoSms);
            item.setVlrDados(saldoDados);
            item.setVlrPeriodico(saldoPeriodico);
            item.setOrigem(origem);
            
            // TODO falta setar o SUB_ID do item!!
            
            // ADICIONA 'A SESSAO
            
            if (itensContestacaoNovos == null)
            {
                itensContestacaoNovos = new ArrayList();
                httpSession.setAttribute(ShowConsultaContestacaoAction.ID_SESSAO_NOVOS, itensContestacaoNovos);
            }
            
            itensContestacaoNovos.add(item);
            
            
            response.getWriter().print("sucesso");
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            response.getWriter().print("Erro ao inserir item de BS. " + (e.getMessage() != null ? e.getMessage() : e.toString()));
            logger.error("Erro ao inserir item de BS. " + e);  
            if (session != null && session.isOpen()) 
                session.getTransaction().rollback();
        }
        
        return null;
    }

    /**
     * @return Nome da operação (permissão) associada a essa ação.
     */
    public String getOperacao() 
    {
        return null;
    }
}