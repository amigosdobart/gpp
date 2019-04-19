package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.HttpRequestParser;

import com.brt.gpp.comum.mapeamentos.entidade.Contestacao;

/**
 * Mostra a tela de historico de BS (detalhamento de itens)
 * 
 * @author Bernardo Vergne Dias
 * Data: 14-02-2008
 */
public class ShowBSItemContestacaoAction extends ShowActionHibernate 
{
    public  final static String ID_SESSAO     = "ConsultaHistoricoContestacao_Resultado";    
    private final static String codOperacao   = Constantes.MENU_CONTESTACAO_HIS;
    private final static Logger logger        = Logger.getLogger(ShowBSItemContestacaoAction.class);
    private final static DecimalFormat formatoDecimal  = new DecimalFormat("#,##0.00",
            new DecimalFormatSymbols(
                    new Locale("pt", "BR")));
           
    public String getTela() 
    {
        return "consultaBSItemContestacao.vm";
    }

    /**
     * Implementa a lógica de negócio dessa ação.
     * 
     * @param context           Contexto do Velocity.
     * @param request           Requisição HTTP que originou a execução dessa ação.
     * @param sessionFactory    Factory de sessões para acesso ao banco de dados (Hibernate).
     */
    public void updateVelocityContext(VelocityContext context,
           HttpServletRequest request, SessionFactory sessionFactory) throws Exception
    {
        HttpSession httpSession = request.getSession();
        Properties params       = HttpRequestParser.getParametros(request);
        Session session         = null;
        
        try 
        {
            // CAPTURA OS PARAMETROS
                       
            String numBS             = params.getProperty("numBS");               
            List resultadoTotal      = (List)httpSession.getAttribute(ID_SESSAO);
            
            // BUSCA O BS
           
            Contestacao contestacao = null;
            
            for (Iterator it = resultadoTotal.iterator(); it.hasNext(); )
            {
                contestacao = (Contestacao)it.next();
                if (contestacao.getNumeroBS().equals(numBS))
                    break;
            }
            context.put("contestacao",      contestacao);            
            context.put("decFormat",        formatoDecimal);
            context.put("sdfData",          new SimpleDateFormat("dd/MM/yyyy"));
            context.put("sdfHora",          new SimpleDateFormat("HH:mm:ss"));

        }
        catch (Exception e)
        {
            context.put("erro", "Erro ao consultar histórido de BS. " + e);
            logger.error("Erro ao consultar histórido de BS. " + e);  
        }    
    }
    
    public String getOperacao() 
    {
        return codOperacao;
    }
}