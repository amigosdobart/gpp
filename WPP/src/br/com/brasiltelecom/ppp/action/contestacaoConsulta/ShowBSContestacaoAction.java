package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import br.com.brasiltelecom.ppp.dao.ContestacaoDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.HttpRequestParser;

import com.brt.gpp.comum.mapeamentos.entidade.Contestacao;

/**
 * Mostra a tela de historico de BS
 * 
 * @author Bernardo Vergne Dias
 * Data: 14-02-2008
 */
public class ShowBSContestacaoAction extends ShowActionHibernate 
{
    public  final static String ID_SESSAO     = "ConsultaHistoricoContestacao_Resultado";    
    private final static String codOperacao   = Constantes.MENU_CONTESTACAO_HIS;
    private final static Logger logger        = Logger.getLogger(ShowBSContestacaoAction.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final static DecimalFormat formatoDecimal  = new DecimalFormat("#,##0.00",
            new DecimalFormatSymbols(
                    new Locale("pt", "BR")));
        
    private final int PAGESIZE          = 50;
    
    public String getTela() 
    {
        return "consultaBSContestacao.vm";
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
            
            String tmpDataInicial    = params.getProperty("dataInicial");
            String tmpDataFinal      = params.getProperty("dataFinal");
            String tmpPeriodo        = params.getProperty("periodo");
                 
            String msisdn            = params.getProperty("msisdn");            
            String numBS             = params.getProperty("numBS");            
            String tipoPeriodo       = params.getProperty("tipoPeriodo");
            int pagina               = params.containsKey("pagina") ? Integer.parseInt(params.getProperty("pagina")) : 0;
            int periodo              = (tmpPeriodo != null) ? Integer.parseInt(tmpPeriodo) : 0;
            Date dataInicial         = getDataInicial(tmpDataInicial, tipoPeriodo, periodo);
            Date dataFinal           = getDataFinal(tmpDataFinal, tipoPeriodo, periodo);      
            List resultadoTotal      = (List)httpSession.getAttribute(ID_SESSAO);
            
            // REALIZA A CONSULTA
            
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            
            // Verifica se eh necessário realizar efetivamente a consulta
            if (!params.containsKey("pagina") || httpSession.getAttribute(ID_SESSAO) == null)
            {
                resultadoTotal = ContestacaoDAO.findByCaracteristicas(session, msisdn, numBS, dataInicial, dataFinal);
                for (Iterator it = resultadoTotal.iterator(); it.hasNext(); )
                    ((Contestacao)it.next()).getItensContestacao().iterator().hasNext(); // lazy init
                
                httpSession.setAttribute(ID_SESSAO, resultadoTotal);
            }
            
            // Faz a paginacao
            int fromIndex = pagina * PAGESIZE;
            int toIndex = Math.min((pagina + 1) * PAGESIZE, resultadoTotal.size());
            List resultadoPagina = (toIndex > 0 && fromIndex < toIndex) ? resultadoTotal.subList(fromIndex, toIndex) : null;

            // Captura o msisdn (em caso de busca por BS)
            if (resultadoPagina != null)
                msisdn = ((Contestacao)resultadoPagina.get(0)).getIdtMsisdn();     
            
            // PERSISTE OS RESULTADOS
            
            context.put("msisdn",           msisdn);
            context.put("numBS",            numBS);
            context.put("tipoPeriodo",      tipoPeriodo);         
            context.put("pagina",           new Integer(pagina)); 
            context.put("periodo",          tmpPeriodo);
            context.put("dataInicial",      tmpDataInicial);
            context.put("dataFinal",        tmpDataFinal);
            
            context.put("decFormat",        formatoDecimal);
            context.put("pagesize",         new Integer(PAGESIZE));
            context.put("resultado",        resultadoPagina);      
            context.put("paginas",          new Integer((int)Math.ceil(resultadoTotal.size() * 1. / PAGESIZE)));
            context.put("totalItens",       new Integer(resultadoTotal.size()));

            session.getTransaction().rollback(); 
        }
        catch (Exception e)
        {
            context.put("erro", "Erro ao consultar histórido de BS. " + e);
            logger.error("Erro ao consultar histórido de BS. " + e);  
            
            if (session != null && session.getTransaction() != null) 
                session.getTransaction().rollback();
        }    
    }
    
    /**
     * Configura a data inicial de acordo com os parametros informados.
     * @throws ParseException 
     */
    private Date getDataInicial(String strDataInicial, String tipoPeriodo, int periodo) throws ParseException
    {
        if (tipoPeriodo == null) // Nao ha restricao de data
            return null;
        
        if (tipoPeriodo.equals("P") && periodo > 0) // Limitamos a data inicial da consulta
        {  
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, - periodo);
            return cal.getTime(); 
        }
        else if (strDataInicial != null) 
        {
            return sdf.parse(strDataInicial);
        }
        
        return null;
    }
  
    /**
     * Configura a data final de acordo com os parametros informados.
     * @throws ParseException 
     */
    private Date getDataFinal(String strDataFinal, String tipoPeriodo, int periodo) throws ParseException
    {
        if (tipoPeriodo == null) // Nao ha restricao de data
            return null;
        
        if (tipoPeriodo.equals("D") && strDataFinal != null) // Corrige a data final em +1 dia
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strDataFinal));
            cal.add(Calendar.DAY_OF_YEAR, 1);
            return cal.getTime();
        }
        
        return null;
    }
    
    public String getOperacao() 
    {
        return codOperacao;
    }
}